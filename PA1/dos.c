#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(void) {
  int exitflag = 0;
  printf("\nWelcome to the DOS command interpreter!\n");
  printf("Type Ctrl-C to exit\n");
  while (!exitflag) {
    

    char* inputString;
    char* convertString;
    char* command;
    char* arg1;
    char* arg2;
    char* systemString;

    
    inputString = calloc(1024, sizeof(char));
    convertString = calloc(strlen(inputString), sizeof(char));
    command = calloc(strlen(inputString), sizeof(char));
    arg1 = calloc(strlen(inputString), sizeof(char));
    arg2 = calloc(strlen(inputString), sizeof(char));
    systemString = calloc(strlen(inputString), sizeof(char));

    printf("\nEnter command: ");
    fgets(inputString, 1024, stdin);

    memcpy(convertString, inputString, strlen(inputString));

    char *token = strtok(convertString, " \t\n\r");
    command = token;
    token = strtok(NULL, " \t\n\r");
    arg1 = token;
    token = strtok(NULL, " \t\n\r");
    arg2 = token;
    
    if (strcmp(command, "Ctrl-C") == 0) {
      exitflag = 1;
    }
    else if (strcmp(command, "cd") == 0) {
      chdir(arg1);
    }
    else if (strcmp(command, "dir") == 0) {
      strcat(systemString, "ls");
      if (arg1 != NULL) {
        strcat(systemString, " ");
        strcat(systemString, arg1);
      }
      system(systemString);
    }
    else if (strcmp(command, "type") == 0) {
      strcat(systemString, "cat");
      if (arg1 != NULL) {
        strcat(systemString, " ");
        strcat(systemString, arg1);
      }
      system(systemString);
    }
    else if (strcmp(command, "del") == 0) {
      strcat(systemString, "rm");
      if (arg1 != NULL) {
        strcat(systemString, " ");
        strcat(systemString, arg1);
      }
      system(systemString);
    }
    else if (strcmp(command, "ren") == 0) {
      strcat(systemString, "mv");
      if (arg1 != NULL) {
        strcat(systemString, " ");
        strcat(systemString, arg1);
      }
      if (arg2 != NULL) {
        strcat(systemString, " ");
        strcat(systemString, arg2);
      }
      system(systemString);
    }
    else if (strcmp(command, "copy") == 0) {
      strcat(systemString, "cp");
      if (arg1 != NULL) {
        strcat(systemString, " ");
        strcat(systemString, arg1);
      }
      if (arg2 != NULL) {
        strcat(systemString, " ");
        strcat(systemString, arg2);
      }
      system(systemString);
    }
    
  }
  return 0;
}
