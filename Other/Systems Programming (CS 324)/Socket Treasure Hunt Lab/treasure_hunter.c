// Replace PUT_USERID_HERE with your actual BYU CS user id, which you can find
// by running `id -u` on a CS lab machine.
#define USERID 1823700636

#include <stdio.h>
#include <string.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdlib.h>
#include <unistd.h>

int SERVER = 1;
int PORT = 2;
int LEVEL = 3;
int SEED = 4;

int RECVBUF = 64;
int SENDBUF = 4;
int FIRSTBUF = 8;
int TREASUREBUF = 1024;

int verbose = 0;

void print_bytes(unsigned char *bytes, int byteslen);

int main(int argc, char *argv[]) {
        unsigned char treasure[TREASUREBUF];
        unsigned int index = 0;
        unsigned char first[FIRSTBUF];
        unsigned char recv[RECVBUF];
        unsigned char snd[SENDBUF];

        unsigned int id = htonl(USERID);
        unsigned short seed = htons(*argv[SEED]);

        int opcode;
        unsigned int nonce;

        struct addrinfo hints;
        struct addrinfo *result;
        struct addrinfo *rp;
        int sfd;
        int s;
        int af = AF_INET;
        int chunk = 0;

        int nread;

        unsigned short port = 1;
        char * portstr = malloc(24);

        first[0] = 0;
        first[1] = atoi(argv[LEVEL]);
        memcpy(&first[2], &id, 4);
        memcpy(&first[6], &seed, 2);

        memset(&hints, 0, sizeof(struct addrinfo));
        hints.ai_family = af;
        hints.ai_socktype = SOCK_DGRAM;
        hints.ai_flags = 0;
        hints.ai_protocol = 0;

        s = getaddrinfo(argv[SERVER], argv[PORT], &hints, &result);
        if (s != 0) {
                fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
                exit(1);
        }

        for (rp = result; rp != NULL; rp = rp->ai_next) {
                sfd = socket(rp->ai_family, rp->ai_socktype, rp->ai_protocol);
                if (sfd == -1) {
                        continue;
                }

                if (connect(sfd, rp->ai_addr, rp->ai_addrlen) != -1) {
                        break;  /* Success */
                }

                close(sfd);
        }

        if (rp == NULL) {   /* No address succeeded */
                fprintf(stderr, "Could not connect\n");
                exit(1);
        }

        freeaddrinfo(result);   /* No longer needed */

        send(sfd, first, FIRSTBUF, 0);
        nread = read(sfd, &recv, RECVBUF);

        if (nread == -1) {
                fprintf(stderr, "read");
                exit(1);
        }

        chunk = recv[0];
        memcpy(&treasure[index], &recv[1], chunk);
        index += chunk;

        while (1) {
                nonce = 0;

                if (chunk == 0) {
                        break;
                }

                if (chunk > 127) {
                        fprintf(stderr, "error: %d\n", recv[0]);
                        exit(1);
                }

                opcode = recv[chunk + 1];

                if (opcode == 1) {
                        memcpy(&port, &recv[chunk + 2], 2);
                        port = ntohs(port);

                        snprintf(portstr, 24, "%u", port);

                        s = getaddrinfo(argv[SERVER], portstr, &hints, &result);
                        if (s != 0) {
                                fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
                                exit(1);
                        }

                        for (rp = result; rp != NULL; rp = rp->ai_next) {
                                if (connect(sfd, rp->ai_addr, rp->ai_addrlen) != -1) {
                                        break;  /* Success */
                                }
                        }
                }
                else if (opcode == 2) {
                        close(sfd);

                        if (port != 1) {
                                s = getaddrinfo(argv[SERVER], portstr, &hints, &result);
                        } else {
                                s = getaddrinfo(argv[SERVER], argv[PORT], &hints, &result);
                        }

                        unsigned short localport;
                        memcpy(&localport, &recv[chunk + 2], 2);

                        if (af == AF_INET6) {
                                struct sockaddr_in6 address;
                                address.sin6_family = af;
                                address.sin6_addr = in6addr_any;
                                address.sin6_port = localport;

                                if (s != 0) {
                                        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
                                        exit(1);
                                }

                                for (rp = result; rp != NULL; rp = rp->ai_next) {
                                        sfd = socket(rp->ai_family, rp->ai_socktype, rp->ai_protocol);
                                        if (sfd == -1) {
                                                continue;
                                        }

                                        bind(sfd, (struct sockaddr *)&address, sizeof(address));

                                        if (connect(sfd, rp->ai_addr, rp->ai_addrlen) != -1) {
                                                break;  /* Success */
                                        }

                                        close(sfd);
                                }
                        } else {
                                struct sockaddr_in address;
                                address.sin_family = af;
                                address.sin_addr.s_addr = INADDR_ANY;
                                address.sin_port = localport;

                                if (s != 0) {
                                        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
                                        exit(1);
                                }

                                for (rp = result; rp != NULL; rp = rp->ai_next) {
                                        sfd = socket(rp->ai_family, rp->ai_socktype, rp->ai_protocol);
                                        if (sfd == -1) {
                                                continue;
                                        }

                                        bind(sfd, (struct sockaddr *)&address, sizeof(address));

                                        if (connect(sfd, rp->ai_addr, rp->ai_addrlen) != -1) {
                                                break;  /* Success */
                                        }

                                        close(sfd);
                                }
                        }

                        if (rp == NULL) {   /* No address succeeded */
                                fprintf(stderr, "Could not connect\n");
                                exit(1);
                        }

                        freeaddrinfo(result);   /* No longer needed */
                }
                else if (opcode == 3) {
                        if (af == AF_INET6) {
                                struct sockaddr_in6 local;
                                socklen_t len = sizeof(local);

                                if (getsockname(sfd, (struct sockaddr *)&local, &len) == -1) {
                                        fprintf(stderr, "error getting port number\n");
                                        exit(1);
                                }

                                close(sfd);

                                if (port != 1) {
                                        s = getaddrinfo(argv[SERVER], portstr, &hints, &result);
                                } else {
                                        s = getaddrinfo(argv[SERVER], argv[PORT], &hints, &result);
                                }

                                for (rp = result; rp != NULL; rp = rp->ai_next) {
                                        sfd = socket(rp->ai_family, rp->ai_socktype, rp->ai_protocol);
                                        if (sfd == -1) {
                                                continue;
                                        }

                                        bind(sfd, (struct sockaddr *)&local, sizeof(local));

                                        break;
                                }
                        } else {
                                struct sockaddr_in local;
                                socklen_t len = sizeof(local);

                                if (getsockname(sfd, (struct sockaddr *)&local, &len) == -1) {
                                        fprintf(stderr, "error getting port number\n");
                                        exit(1);
                                }

                                close(sfd);

                                if (port != 1) {
                                        s = getaddrinfo(argv[SERVER], portstr, &hints, &result);
                                } else {
                                        s = getaddrinfo(argv[SERVER], argv[PORT], &hints, &result);
                                }

                                for (rp = result; rp != NULL; rp = rp->ai_next) {
                                        sfd = socket(rp->ai_family, rp->ai_socktype, rp->ai_protocol);
                                        if (sfd == -1) {
                                                continue;
                                        }

                                        bind(sfd, (struct sockaddr *)&local, sizeof(local));

                                        break;
                                }
                        }

                        unsigned short m;
                        memcpy(&m, &recv[chunk + 2], 2);
                        m = ntohs(m);

                        for (unsigned short i = 0; i < m; ++i) {
                                struct sockaddr_in remote;
                                socklen_t remoteLen = sizeof(remote);

                                if(recvfrom(sfd, NULL, 0, 0, (struct sockaddr *) &remote, &remoteLen) == -1) {
                                        fprintf(stderr, "error with recvfrom\n");
                                        exit(1);
                                }

                                nonce += ntohs(remote.sin_port);
                        }

                        nonce += 1;
                        nonce = htonl(nonce);

                        if (connect(sfd, rp->ai_addr, rp->ai_addrlen) == -1) {
                                fprintf(stderr, "error connecting\n");
                                exit(1);
                        }

                        freeaddrinfo(result);   /* No longer needed */
                }
                else if (opcode == 4) {
                        if (af == AF_INET) {
                                af = AF_INET6;
                        } else {
                                af = AF_INET;
                        }

                        hints.ai_family = af;

                        memcpy(&port, &recv[chunk + 2], 2);
                        port = ntohs(port);

                        snprintf(portstr, 24, "%u", port);

                        s = getaddrinfo(argv[SERVER], portstr, &hints, &result);
                        if (s != 0) {
                                fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
                                exit(1);
                        }

                        close(sfd);

                        for (rp = result; rp != NULL; rp = rp->ai_next) {
                                sfd = socket(rp->ai_family, rp->ai_socktype, rp->ai_protocol);
                                if (sfd == -1) {
                                        continue;
                                }

                                if (connect(sfd, rp->ai_addr, rp->ai_addrlen) != -1) {
                                        break;  /* Success */
                                }

                                close(sfd);
                        }

                        if (rp == NULL) {   /* No address succeeded */
                                fprintf(stderr, "Could not connect\n");
                                exit(1);
                        }

                        freeaddrinfo(result);   /* No longer needed */
                }

                if (opcode != 3) {
                        memcpy(&nonce, &recv[recv[0] + 4], 4);
                        nonce = ntohl(nonce) + 1;
                        nonce = htonl(nonce);
                }

                memcpy(&snd, &nonce, 4);
                send(sfd, snd, SENDBUF, 0);
                nread = read(sfd, &recv, RECVBUF);

                if (nread == -1) {
                        fprintf(stderr, "read\n");
                        exit(1);
                }

                chunk = recv[0];
                memcpy(&treasure[index], &recv[1], chunk);
                index += chunk;
        }

        treasure[index] = '\0';
        printf("%s\n", treasure);
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