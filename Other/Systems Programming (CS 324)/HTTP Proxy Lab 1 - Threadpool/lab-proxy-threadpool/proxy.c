#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <netdb.h>
#include <pthread.h>
#include <semaphore.h>

/* Recommended max cache and object sizes */
#define MAX_CACHE_SIZE 1049000
#define MAX_OBJECT_SIZE 102400
#define HOST_PREFIX 2
#define PORT_PREFIX 1
#define NTHREADS 8
#define SBUFSIZE 5

typedef struct {
    int *buf;
    int n;
    int front;
    int rear;
    sem_t mutex;
    sem_t slots;
    sem_t items;
} sbuf_t;

void sbuf_init(sbuf_t *sp, int n) {
    sp->buf = calloc(n, sizeof(int));
    sp->n = n;
    sp->front = sp->rear = 0;
    sem_init(&sp->mutex, 0, 1);
    sem_init(&sp->slots, 0, n);
    sem_init(&sp->items, 0, 0);
}

void sbuf_deinit(sbuf_t *sp) {
    free(sp->buf);
}

void sbuf_insert(sbuf_t *sp, int item) {
    printf("before wait(slots)\n"); fflush(stdout);
    sem_wait(&sp->slots);
    printf("after wait(slots)\n"); fflush(stdout);
    sem_wait(&sp->mutex);
    sp->buf[(++sp->rear)%(sp->n)] = item;
    sem_post(&sp->mutex);
    printf("before post(items)\n"); fflush(stdout);
    sem_post(&sp->items);
    printf("after post(items)\n"); fflush(stdout);

}

int sbuf_remove(sbuf_t *sp) {
    int item;
    printf("before wait(items)\n"); fflush(stdout);
    sem_wait(&sp->items);
    printf("after wait(items)\n"); fflush(stdout);
    sem_wait(&sp->mutex);
    item = sp->buf[(++sp->front)%(sp->n)];
    sem_post(&sp->mutex);
    printf("before post(slots)\n"); fflush(stdout);
    sem_post(&sp->slots);
    printf("after post(slots)\n"); fflush(stdout);
    return item;
}

sbuf_t sbuf;
static const char *user_agent_hdr = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:97.0) Gecko/20100101 Firefox/97.0";

int all_headers_received(char *);
int parse_request(char *, char *, char *, char *, char *, char *);
void test_parser();
void print_bytes(unsigned char *, int);

int open_sfd(int argc, char *argv[]) {
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
                fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
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

	if (bind(sfd, result->ai_addr, result->ai_addrlen) < 0) {
		fprintf(stderr, "Could not bind.");
		exit(EXIT_FAILURE);
	}

	listen(sfd, 100);

	return sfd;
}

void handle_client(int sfd) {
	char request[MAX_CACHE_SIZE];

	int nread = 0;
	for (;;) {
		int cread = 0;
		cread = read(sfd, &request[nread], MAX_CACHE_SIZE);
		nread += cread;

		if (all_headers_received(request) == 1) {
			break;
		}
	}

	char method[16], hostname[64], port[8], path[64], headers[1024];

	if (parse_request(request, method, hostname, port, path, headers)) {
                printf("METHOD: %s\n", method);
                printf("HOSTNAME: %s\n", hostname);
                printf("PORT: %s\n", port);
                printf("HEADERS: %s\n", headers);
        } else {
                printf("REQUEST INCOMPLETE\n");
		close(sfd);
		return;
        }

	char modrequest[MAX_CACHE_SIZE];

	if (strcmp(port, "80")) {
		sprintf(modrequest, "%s %s HTTP/1.0\r\nHost: %s:%s\r\nUser-Agent: %s\r\nConnection: close\r\nProxy-Connection: close\r\n\r\n", 
		method, path, hostname, port, user_agent_hdr);
	}
	else {
		sprintf(modrequest, "%s %s HTTP/1.0\r\nHost: %s\r\nUser-Agent: %s\r\nConnection: close\r\nProxy-Connection: close\r\n\r\n", 
		method, path, hostname, user_agent_hdr);
	}

	printf("%s", modrequest);

	struct addrinfo hints;
        struct addrinfo *result, *rp;
        int ssfd, s;

        memset(&hints, 0, sizeof(struct addrinfo));
        hints.ai_family = AF_INET;
        hints.ai_socktype = SOCK_STREAM;
        hints.ai_flags = 0;
        hints.ai_protocol = 0;

	s = getaddrinfo(hostname, port, &hints, &result);
        if (s != 0) {
                fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
                exit(EXIT_FAILURE);
        }

        for (rp = result; rp != NULL; rp = rp->ai_next) {
                ssfd = socket(rp->ai_family, rp->ai_socktype, rp->ai_protocol);
                if (ssfd == -1) {
                        continue;
                }

		if (connect(ssfd, rp->ai_addr, rp->ai_addrlen) != -1) {
                	break;
		}

		close(ssfd);
        }

	write(ssfd, modrequest, MAX_CACHE_SIZE);
	char srequest[MAX_CACHE_SIZE];
	nread = 0;
	for (;;) {
		int cread = 0;
		cread = read(ssfd, &srequest[nread], MAX_CACHE_SIZE);
		nread += cread;

		if (cread == 0) {
			break;
		}
	}

	write(sfd, srequest, nread + 1);
	close(sfd);
	close(ssfd);
}

void *run_thread(void *vargp) {
        pthread_detach(pthread_self());
        while (1) {
                int nsfd = sbuf_remove(&sbuf);
                handle_client(nsfd);
        }
}


int main(int argc, char *argv[]) {
	//test_parser();
	printf("%s\n", user_agent_hdr);

	int sfd = open_sfd(argc, argv);
	int nsfd;

	struct sockaddr_storage peer_addr;
	socklen_t peer_addr_len = sizeof(struct sockaddr_storage);
	pthread_t tid;

	sbuf_init(&sbuf, SBUFSIZE);
        for (int i = 0; i < NTHREADS; i++) {
                pthread_create(&tid, NULL, run_thread, NULL);
	}

        while (1) {
		peer_addr_len = sizeof(struct sockaddr_storage);
		nsfd = accept(sfd, (struct sockaddr *) &peer_addr, &peer_addr_len);
		sbuf_insert(&sbuf, nsfd);
        }

	return 0;
}

int all_headers_received(char *request) {
	if (strstr(request, "\r\n\r\n") != NULL) {
		return 1;
	} else {
		return 0;
	}
}

int parse_request(char *request, char *method, char *hostname, char *port, char *path, char *headers) {
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
