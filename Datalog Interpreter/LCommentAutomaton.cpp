#include "LCommentAutomaton.h"


void LCommentAutomaton::S0(const std::string& input) {
    if (input.at(index) == '#') {
        inputRead = 1;
        index++;
        S1(input);
    }
    else {
        Serr();
    }
}

void LCommentAutomaton::S1(const std::string& input) {
    if (input.at(index) == '|') {
        inputRead++;
        index++;
        S2(input);
    }
    else {
        Serr();
    }
}

void LCommentAutomaton::S2(const std::string& input) {
    if (input.at(index) != '|') {
        if (index >= input.size()) {
            endoffile = 1;
            return;
        }
        if (input.at(index) == '\n') {
            newLines++;
        }
        inputRead++;
        index++;
        S2(input);
    }
    else if (input.at(index) == '|') {
        inputRead++;
        index++;
        S3(input);
    }
    else {
        Serr();
    }
}

void LCommentAutomaton::S3(const std::string& input) {
    if (input.at(index) != '#') {
        if (input.at(index) == '\n') {
            newLines++;
        }
        inputRead++;
        index++;
        S2(input);
    }
    else if (input.at(index) == '#') {
        inputRead++;
        index++;
    }
    else {
        Serr();
    }
}