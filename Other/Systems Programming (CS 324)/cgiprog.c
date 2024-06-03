/*
1.
USER         PID    PPID NLWP     LWP S CMD
calvin74 2164855 2164519    1 2164855 S echoserveri

2. 1 process and 1 thread. This is because echoserveri does not use concurrency.

3. The second client's requests are answered and the third client's requests remain unanswered.
   Each time you Ctrl+C a handled client, the next client's requests get handled by the server.

4.
USER         PID    PPID NLWP     LWP S CMD
calvin74 2167884 2164519    1 2167884 S echoserverp
calvin74 2167936 2167884    1 2167936 S echoserverp
calvin74 2167981 2167884    1 2167981 S echoserverp
calvin74 2168014 2167884    1 2168014 S echoserverp

5. 4 processes and 4 threads. This is because echoserverp uses process-based concurrency.

6.
USER         PID    PPID NLWP     LWP S CMD
calvin74 2168824 2164519    4 2168824 S echoservert
calvin74 2168824 2164519    4 2168862 S echoservert
calvin74 2168824 2164519    4 2168886 S echoservert
calvin74 2168824 2164519    4 2168907 S echoservert

7. 1 process and 4 threads. This is because echoservert uses simple thread-based concurrency.

8.
USER         PID    PPID NLWP     LWP S CMD
calvin74 2169987 2164519    9 2169987 S echoservert_pre
calvin74 2169987 2164519    9 2169988 S echoservert_pre
calvin74 2169987 2164519    9 2169989 S echoservert_pre
calvin74 2169987 2164519    9 2169990 S echoservert_pre
calvin74 2169987 2164519    9 2169991 S echoservert_pre
calvin74 2169987 2164519    9 2169992 S echoservert_pre
calvin74 2169987 2164519    9 2169993 S echoservert_pre
calvin74 2169987 2164519    9 2169994 S echoservert_pre
calvin74 2169987 2164519    9 2169995 S echoservert_pre

9. 1 process and 9 threads. This is because echoservert_pre uses thread-based concurrency using a thread-pool.

10. 1.

11. 8.

12. Connection attempts made by clients.

13. The items semaphore becoming greater than 0.

14. Connecting to a client.

15. The post(items) call.

16. 1.

17. The post(items) call.

18. Connection attempts made by clients.

19. Process-based > thread-based > threadpool-based.

20. Threadpool-based.

21. Thread-based, threadpool-based.

22. Threadpool-based.
*/

#include "stdio.h"
#include "string.h"
#include "stdlib.h"

int main (void) {
        char* query;
        char content[500];

        query = getenv("QUERY_STRING");

        sprintf(content, "The query string is: %s", query);

        printf("Content-type: text/plain\r\n");
        printf("Content-length: %d\r\n\r\n", (int)strlen(content));
        printf("%s", content);

        fflush(stdout);

        return 0;
}