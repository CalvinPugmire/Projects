#include "ColonDashAutomaton.h"

void ColonDashAutomaton::S0(const std::string& input) {
    if (input.at(index) == ':') {
        inputRead++;
        index++;
        S1(input);
    }
    else {
        Serr();
    }
}

void ColonDashAutomaton::S1(const std::string& input) {
    if (input.at(index) == '-') {
        inputRead++;
    }
    else {
        Serr();
    }
}