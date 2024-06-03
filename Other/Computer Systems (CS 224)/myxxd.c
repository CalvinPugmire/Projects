#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define TRUE 1
#define FALSE 0
#define BAD_NUMBER_ARGS 1

FILE *parseCommandLine(int argc, char **argv, int *bits) {
  if (argc > 2) {
    printf("Usage: %s [-b|-bits]\n", argv[0]);
    exit(BAD_NUMBER_ARGS);
  }

  if (argc == 2 &&
      (strcmp(argv[1], "-b") == 0 || strcmp(argv[1], "-bits") == 0)) {
    *bits = TRUE;
  } else {
    *bits = FALSE;
  }

  return stdin;
}

void printDataAsHex(unsigned char *data, size_t size) {
  for (unsigned int i = 0; i < size-1; i++) {
    printf("%02x" , data[i]);
    if (i % 2 != 0) {
      printf(" ");
    }
  }
  printf("%02x" , data[size-1]);
  for (unsigned int i = 16-size; i > 0; i--) {
    printf("  ");
    if (i % 2 == 0) {
      printf(" ");
    }
  }
}

void printDataAsBinary(unsigned char *data, size_t size) {
  for (unsigned int i = 0; i < size; i++) {
    unsigned char x = data[i];
    unsigned char binary[8];
    for (int j = 0; j < 8; j++) {
      if (x % 2 == 1) {
        binary[7-j] = '1';
      } else {
        binary[7-j] = '0';
      }
      x = x / 2;
    }
    if (i % 8 != 0) {
      printf(" ");
    }
    printf("%s" , binary);
  }
  for (unsigned int i = 6-size; i > 0; i--) {
    printf("         ");
    if (i % 8 == 0) {
      printf(" ");
    }
  }
}

void printDataAsChars(unsigned char *data, size_t size) {
  for (unsigned int i = 0; i < size; i++) {
    if (data[i] < 127 && data[i] > 31) {
      printf("%c" , data[i]);
    }
    else {
      printf(".");
    }
  }
}

void readAndPrintInputAsHex(FILE *input) {
  unsigned char data[16];
  int numBytesRead = fread(data, 1, 16, input);
  unsigned int offset = 0;
  while (numBytesRead != 0) {
    printf("%08x: ", offset);
    offset += numBytesRead;
    printDataAsHex(data, numBytesRead);
    printf("  ");
    printDataAsChars(data, numBytesRead);
    printf("\n");
    numBytesRead = fread(data, 1, 16, input);
  }
}

void readAndPrintInputAsBits(FILE *input) {
  unsigned char data[6];
  int numBytesRead = fread(data, 1, 6, input);
  unsigned int offset = 0;
  while (numBytesRead != 0) {
    printf("%08x: ", offset);
    offset += numBytesRead;
    printDataAsBinary(data, numBytesRead);
    printf("  ");
    printDataAsChars(data, numBytesRead);
    printf("\n");
    numBytesRead = fread(data, 1, 6, input);
  }
}

int main(int argc, char **argv) {
  int bits = FALSE;
  FILE *input = parseCommandLine(argc, argv, &bits);

  if (bits == FALSE) {
    readAndPrintInputAsHex(input);
  } else {
    readAndPrintInputAsBits(input);
  }
  return 0;
}
