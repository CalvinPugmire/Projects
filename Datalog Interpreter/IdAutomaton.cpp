#include "IdAutomaton.h"


void IdAutomaton::S0(const std::string& input) {
    if (isalpha(input.at(index))) {
        inputRead = 1;
        index++;
        S1(input);
    }
    else {
        Serr();
    }
}

void IdAutomaton::S1(const std::string& input) {
    if (isalpha(input.at(index)) || isdigit(input.at(index))) {
        inputRead++;
        index++;
        S1(input);
    }
    else if (!isalpha(input.at(index)) && !isdigit(input.at(index))) {
        //return;
    }
    else {
        Serr();
    }
}