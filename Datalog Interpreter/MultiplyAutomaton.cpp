#include "MultiplyAutomaton.h"

void MultiplyAutomaton::S0(const std::string& input) {
    if (input.at(index) == '*') {
        inputRead = 1;
    }
    else {
        Serr();
    }
}