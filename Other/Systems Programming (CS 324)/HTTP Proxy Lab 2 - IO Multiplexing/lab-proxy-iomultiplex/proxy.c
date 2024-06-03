#include <errno.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/epoll.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <signal.h>

/* Recommended max cache and object sizes */
#define MAX_CACHE_SIZE 1049000
#define MAX_OBJECT_SIZE 102400
#define MAXEVENTS 64
#define MAXLINE 2048

static const char *user_agent_hdr = "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:97.0) Gecko/20100101 Firefox/97.0";

struct request_info {
	int cfd;
	int sfd;
	int state;
	char readBuf[MAX_CACHE_SIZE];
	char writeBuf[MAX_CACHE_SIZE];
	int readC;
	int writeAmount;
	int writtenS;
	int readS;
	int writtenC;
};

struct request_info *newRequest;
struct request_info *activeRequest;

struct epoll_event event;
struct epoll_event *events;

int signalFlag = 0;
void sig_handler(int signum) {
	signalFlag = 1;
}

int all_headers_received(char *);
int parse_request(char *, char *, char *, char *, char *, char *);
void test_parser();
void print_bytes(unsigned char *, int);

int open_sfd (int argc, char *argv[]) {
	struct addrinfo hints;
        struct addrinfo *result, *rp;
        int sfd, s;

        memset(&hints, 0, sizeof(struct addrinfo));
        hints.ai_family = AF_INET;
        hints.ai_socktype = SOCK_STREAM;
        hints.ai_flags = 0;
        hints.ai_protocol = 0;

        if (argc < 1) {
                fprintf(stderr, "Missing command line argument.");
                exit(EXIT_FAILURE);
        }

        s = getaddrinfo(NULL, argv[1], &hints, &result);
        if (s != 0) {
                fprintf(stderr, "Getaddrinfo: %s.\n", gai_strerror(s));
                exit(EXIT_FAILURE);
        }

        for (rp = result; rp != NULL; rp = rp->ai_next) {
                sfd = socket(rp->ai_family, rp->ai_socktype, rp->ai_protocol);
                if (sfd == -1) {
                        continue;
                }
                break;
        }

        int optval = 1;
        setsockopt(sfd, SOL_SOCKET, SO_REUSEPORT, &optval, sizeof(optval));

	if (fcntl(sfd, F_SETFL, fcntl(sfd, F_GETFL, 0) | O_NONBLOCK) < 0) {
		fprintf(stderr, "Error setting socket option.\n");
		exit(EXIT_FAILURE);
	}

        if (bind(sfd, result->ai_addr, result->ai_addrlen) < 0) {
                fprintf(stderr, "Could not bind.\n");
                exit(EXIT_FAILURE);
        }

        listen(sfd, 100);

	if (fcntl(sfd, F_SETFL, fcntl(sfd, F_GETFL, 0) | O_NONBLOCK) < 0) {
		fprintf(stderr, "Error setting socket option.\n");
		exit(EXIT_FAILURE);
	}

        return sfd;
}

void read_request (struct request_info *request, int efd) {
	while (1) {
		int len = recv(request->cfd, &request->readBuf[request->readC], MAXLINE, 0);

		if (len < 0) {
			if (errno == EWOULDBLOCK || errno == EAGAIN) {
			} else {
				fprintf(stderr, "Client recv, cfd: %d.\n", request->cfd);
				close(request->cfd);
				free(request);
			}
			return;
		}

		request->readC = request->readC + len;
		if (all_headers_received(request->readBuf) == 1) {
			break;
		}
	}

	char method[16], hostname[64], port[8], path[64], headers[1024];
        if (parse_request(request->readBuf, method, hostname, port, path, headers)) {
                if (strcmp(port, "80")) {
         	       sprintf(request->writeBuf, "%s %s HTTP/1.0\r\nHost: %s:%s\r\nUser-Agent: %s\r\nConnection: close\r\nProxy-Connection: close\r\n\r\n",
         	       method, path, hostname, port, user_agent_hdr);
        	} else {
                	sprintf(request->writeBuf, "%s %s HTTP/1.0\r\nHost: %s\r\nUser-Agent: %s\r\nConnection: close\r\nProxy-Connection: close\r\n\r\n",
                	method, path, hostname, user_agent_hdr);
        	}
		request->writeAmount = strlen(request->writeBuf);
		printf("%s", request->writeBuf);
        } else {
                printf("REQUEST INCOMPLETE\n");
                close(request->cfd);
		free(request);
                return;
        }

	struct addrinfo hints;
        struct addrinfo *result, *rp;
        int sfd, s;

        memset(&hints, 0, sizeof(struct addrinfo));
        hints.ai_family = AF_INET;
        hints.ai_socktype = SOCK_STREAM;
        hints.ai_flags = 0;
        hints.ai_protocol = 0;

	s = getaddrinfo(hostname, port, &hints, &result);
        if (s != 0) {
                fprintf(stderr, "Getaddrinfo: %s.\n", gai_strerror(s));
		close(request->cfd);
		free(request);
                return;
        }

        for (rp = result; rp != NULL; rp = rp->ai_next) {
                sfd = socket(rp->ai_family, rp->ai_socktype, rp->ai_protocol);
                if (sfd == -1) {
                        continue;
                }
                if (connect(sfd, rp->ai_addr, rp->ai_addrlen) != -1) {
                        break;
                }
		close(sfd);
        }

	if (fcntl(sfd, F_SETFL, fcntl(sfd, F_GETFL, 0) | O_NONBLOCK) < 0) {
                fprintf(stderr, "Error setting socket option.\n");
                close(request->cfd);
                free(request);
                return;
        }

	request->sfd = sfd;
        event.data.ptr = request;
        event.events = EPOLLIN | EPOLLET;
        if (epoll_ctl(efd, EPOLL_CTL_ADD, sfd, &event) < 0) {
                fprintf(stderr, "Error adding event.\n");
                close(request->cfd);
		close(request->sfd);
                free(request);
                return;
        }
	memset(request->readBuf, '\0', request->readC);

	request->state = 1;
}

void send_request (struct request_info *request, int efd) {
	while (1) {
		int len = write(request->sfd, &request->writeBuf[request->writtenS], request->writeAmount);

		if (len < 0) {
                        if (errno == EWOULDBLOCK || errno == EAGAIN) {
                        } else {
                                fprintf(stderr, "Server write, sfd: %d.\n", request->sfd);
				close(request->cfd);
				close(request->sfd);
				free(request);
                        }
                        return;
                }

                request->writtenS += len;
                if (request->writtenS >= request->writeAmount) {
                        break;
                }
	}

	event.data.ptr = request;
        event.events = EPOLLIN | EPOLLET;
        if (epoll_ctl(efd, EPOLL_CTL_ADD, request->sfd, &event) < 0) {
                fprintf(stderr, "Error adding event.\n");
                exit(EXIT_FAILURE);
        }
        memset(request->writeBuf, '\0', request->writtenS);

        request->state = 2;
}

void read_response (struct request_info *request, int efd) {
        while (1) {
		int len = recv(request->sfd, &request->readBuf[request->readS], MAXLINE, 0);

		if (len < 0) {
			if (errno == EWOULDBLOCK || errno == EAGAIN) {
                       	} else {
                               	fprintf(stderr, "Server recv, sfd: %d.\n", request->sfd);
                               	close(request->cfd);
                               	free(request);
                       	}
                       	return;
               	}

               	if (len == 0) {
                       	break;
               	}
		request->readS += len;
	}

	event.data.ptr = request;
        event.events = EPOLLIN | EPOLLET;
        if (epoll_ctl(efd, EPOLL_CTL_ADD, request->cfd, &event) < 0) {
                fprintf(stderr, "Error adding event.\n");
                exit(EXIT_FAILURE);
        }
        request->writeAmount = request->readS;

        request->state = 3;
}

void send_response (struct request_info *request) {
        while (1) {
		int len = write(request->cfd, &request->readBuf[request->writtenC], request->writeAmount);

                if (len < 0) {
                        if (errno == EWOULDBLOCK || errno == EAGAIN) {
                        } else {
                                fprintf(stderr, "Client write, cfd: %d.\n", request->cfd);
				close(request->cfd);
                                close(request->sfd);
                                free(request);
                        }
                        return;
                }

                request->writtenC += len;
                if (request->writtenC >= request->writeAmount) {
                        break;
                }
	}

	close(request->cfd);
	close(request->sfd);
	request->state = 4;
}

void handle_client (struct request_info *request, int rfd, int wfd) {
	if (request->state == 0) {
		read_request(request, wfd);
	}
	if (request->state == 1) {
		send_request(request, rfd);
	}
	if (request->state == 2) {
		read_response(request, wfd);
	}
	if (request->state == 3) {
		send_response(request);
	}
}

void handle_new_clients (int rfd, int lfd) {
	while (1) {
		socklen_t clientlen = sizeof(struct sockaddr_storage);
		struct sockaddr_storage clientaddr;
		int sfd = accept(lfd, (struct sockaddr *)&clientaddr, &clientlen);

		if (sfd < 0) {
			if (errno == EWOULDBLOCK || errno == EAGAIN) {
				break;
			} else {
				fprintf(stderr, "Error.\n");
				exit(EXIT_FAILURE);
			}
		}

		if (fcntl(sfd, F_SETFL, fcntl(sfd, F_GETFL, 0) | O_NONBLOCK) < 0) {
                        fprintf(stderr, "Error setting socket option.\n");
                        exit(EXIT_FAILURE);
                }

		newRequest = (struct request_info *)malloc(sizeof(struct request_info));
		newRequest->cfd = sfd;
		fprintf(stderr, "New fd: %d.\n", newRequest->cfd);

		event.data.ptr = newRequest;
		event.events = EPOLLIN | EPOLLET;
		if (epoll_ctl(rfd, EPOLL_CTL_ADD, sfd, &event) < 0) {
			fprintf(stderr, "Error adding event.\n");
			exit(EXIT_FAILURE);
		}
	}
}

int main (int argc, char *argv[]) {
	//test_parser();
	//printf("%s\n", user_agent_hdr);

	struct sigaction sigact;
	sigact.sa_flags = SA_RESTART;
	sigact.sa_handler = sig_handler;
	sigaction(SIGINT, &sigact, NULL);

	int rfd;
	if ((rfd = epoll_create1(0)) < 0) {
                fprintf(stderr, "Error creating epoll fd.\n");
                exit(EXIT_FAILURE);
        }

	int wfd;
        if ((wfd = epoll_create1(0)) < 0) {
                fprintf(stderr, "Error creating epoll fd.\n");
                exit(EXIT_FAILURE);
        }

	int sfd = open_sfd(argc, argv);
	newRequest = malloc(sizeof(struct request_info));
	newRequest->cfd = sfd;

	event.data.ptr = newRequest;
	event.events = EPOLLIN | EPOLLET;
	if (epoll_ctl(rfd, EPOLL_CTL_ADD, sfd, &event) < 0) {
		fprintf(stderr, "Error adding event.\n");
		exit(EXIT_FAILURE);
	}

	events = calloc(MAXEVENTS, sizeof(struct epoll_event));

	while (1) {
		int result = epoll_wait(rfd, events, MAXEVENTS, 1000);

		if (result == 0) {
			if (signalFlag) {
				break;
			}
		}

		if (result < 0) {
			if (errno == EBADF) {
				fprintf(stderr, "Epoll_wait() error: EBADF.\n");
				break;
			}
			else if (errno == EFAULT) {
				fprintf(stderr, "Epoll_wait() error: EFAULT.\n");
				break;
			}
			else if (errno == EINTR) {
				fprintf(stderr, "Epoll_wait() error: EINTR.\n");
				break;
			}
			else if (errno == EINVAL) {
				fprintf(stderr, "Epoll_wait() error: EINVAL.\n");
				break;
			}
		}

		for (int i = 0; i < result; ++i) {
			activeRequest = (struct request_info *)(events[i].data.ptr);

			if ((events[i].events & EPOLLERR) || (events[i].events & EPOLLHUP) || (events[i].events & EPOLLRDHUP)) {
				fprintf(stderr, "Epoll error on %d.\n", activeRequest->cfd);
				close(activeRequest->cfd);
				close(activeRequest->sfd);
				free(activeRequest);
				continue;
			}

			if (activeRequest->cfd == sfd) {
				handle_new_clients(rfd, sfd);
			} else {
				handle_client(activeRequest, rfd, wfd);
			}
		}
	}
	free(events);
	close(rfd);
	close(wfd);
	close(sfd);
	return 0;
}

int all_headers_received (char *request) {
	if (strstr(request, "\r\n\r\n") != NULL) {
                return 1;
        } else {
                return 0;
        }
}

int parse_request (char *request, char *method, char *hostname, char *port, char *path, char *headers) {
	if (all_headers_received(request) == 0) {
                return 0;
        }

        int i = 0;
        while (request[i] != ' ') {
                method[i] = request[i];
                i++;
        }
        method[i] = '\0';

        i = strstr(request, "http:") - request + 7;
        int j = 0;
        while (request[i] != '/' && request[i] != ':') {
                hostname[j] = request[i];
                i++;
                j++;
        }
        hostname[j] = '\0';

        if (request[i] == ':') {
                i++;
                j = 0;
                while (request[i] != '/') {
                        port[j] = request[i];
                        i++;
                        j++;
                }
                port[j] = '\0';
        } else {
                port[0] = '8';
                port[1] = '0';
                port[2] = '\0';
        }

        j = 0;
        while (request[i] != ' ') {
                path[j] = request[i];
                i++;
                j++;
        }
        path[j] = '\0';

        i = strstr(request, "\r\n") - request + 2;
        j = 0;
        while (request[i] != '\0') {
                headers[j] = request[i];
                i++;
                j++;
        }
        headers[j] = '\0';

        return 1;
}

void test_parser() {
	int i;
	char method[16], hostname[64], port[8], path[64], headers[1024];

       	char *reqs[] = {
		"GET http://www.example.com/index.html HTTP/1.0\r\n"
		"Host: www.example.com\r\n"
		"User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0\r\n"
		"Accept-Language: en-US,en;q=0.5\r\n\r\n",

		"GET http://www.example.com:8080/index.html?foo=1&bar=2 HTTP/1.0\r\n"
		"Host: www.example.com:8080\r\n"
		"User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0\r\n"
		"Accept-Language: en-US,en;q=0.5\r\n\r\n",

		"GET http://localhost:1234/home.html HTTP/1.0\r\n"
		"Host: localhost:1234\r\n"
		"User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0\r\n"
		"Accept-Language: en-US,en;q=0.5\r\n\r\n",

		"GET http://www.example.com:8080/index.html HTTP/1.0\r\n",

		NULL
	};
	
	for (i = 0; reqs[i] != NULL; i++) {
		printf("Testing %s\n", reqs[i]);
		if (parse_request(reqs[i], method, hostname, port, path, headers)) {
			printf("METHOD: %s\n", method);
			printf("HOSTNAME: %s\n", hostname);
			printf("PORT: %s\n", port);
			printf("HEADERS: %s\n", headers);
		} else {
			printf("REQUEST INCOMPLETE\n");
		}
	}
}

void print_bytes(unsigned char *bytes, int byteslen) {
	int i, j, byteslen_adjusted;

	if (byteslen % 8) {
		byteslen_adjusted = ((byteslen / 8) + 1) * 8;
	} else {
		byteslen_adjusted = byteslen;
	}
	for (i = 0; i < byteslen_adjusted + 1; i++) {
		if (!(i % 8)) {
			if (i > 0) {
				for (j = i - 8; j < i; j++) {
					if (j >= byteslen_adjusted) {
						printf("  ");
					} else if (j >= byteslen) {
						printf("  ");
					} else if (bytes[j] >= '!' && bytes[j] <= '~') {
						printf(" %c", bytes[j]);
					} else {
						printf(" .");
					}
				}
			}
			if (i < byteslen_adjusted) {
				printf("\n%02X: ", i);
			}
		} else if (!(i % 4)) {
			printf(" ");
		}
		if (i >= byteslen_adjusted) {
			continue;
		} else if (i >= byteslen) {
			printf("   ");
		} else {
			printf("%02X ", bytes[i]);
		}
	}
	printf("\n");
}
