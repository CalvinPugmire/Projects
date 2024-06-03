/*
Question 1:
Executable programs: 1
System calls: 2
Library calls: 3

Question 2:
1, 2.

Question 3:
3.

Question 4:
2.

Question 5:
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

Question 6:
2, 7.

Question 7:
SO_ACCEPTCONN.

Question 8:
3.

Question 9:
Null-terminated.

Question 10:
An integer greater than zero.
*/

//I completed the TMUX exercise from Part 2.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <unistd.h>

int main(int argc, char* argv[]) {
        int pid = getpid();

        fprintf(stderr, "%d\n\n", pid);

        FILE* filename = fopen(argv[1], "r");
        char fileline[512];

        while (fgets(fileline, sizeof(fileline), filename)) {
                char* catpat = getenv("CATMATCH_PATTERN");
                if (catpat != NULL) {
                        if (strstr(fileline, catpat) != NULL) {
                                fprintf(stdout, "1 %s\n", fileline);
                        } else {
                                fprintf(stdout, "0 %s\n", fileline);
                        }
                } else {
                        fprintf(stdout, "0 %s\n", fileline);
                }
        }
}
