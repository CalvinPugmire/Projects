#include "StringAutomaton.h"


void StringAutomaton::S0(const std::string& input) {
    if (input.at(index) == '\'') {
        inputRead = 1;
        index++;
        S1(input);
    }
    else {
        Serr();
    }
}

void StringAutomaton::S1(const std::string& input) {
    if (input.at(index) != '\'') {
        if (index >= input.size()) {
            endoffile = 1;
            return;
        }
        if (input.at(index) == '\n') {
            newLines++;
        }
        inputRead++;
        index++;
        S1(input);
    }
    else if (input.at(index) == '\'') {
        inputRead++;
        index++;
        S2(input);
    }
    else {
        Serr();
    }
}

void StringAutomaton::S2(const std::string& input) {
    if (input.at(index) == '\'') {
        inputRead++;
        index++;
        S1(input);
    }
    else if (input.at(index) != '\'') {
        //return;
    }
    else {
        Serr();
    }
}