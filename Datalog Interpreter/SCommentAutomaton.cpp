#include "SCommentAutomaton.h"


void SCommentAutomaton::S0(const std::string& input) {
    if (input.at(index) == '#') {
        if (input[index+1] == '|') {
            Serr();
        }
        else {
            inputRead = 1;
            index++;
            S1(input);
        }
    }
    else {
        Serr();
    }
}

void SCommentAutomaton::S1(const std::string& input) {
    if (input.at(index) != EOF && input.at(index) != '\n') {
        inputRead++;
        index++;
        S1(input);
    }
    else if (input.at(index) == EOF || input.at(index) == '\n') {
        //return;
    }
    else {
        Serr();
    }
}