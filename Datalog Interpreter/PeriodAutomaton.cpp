#include "PeriodAutomaton.h"

void PeriodAutomaton::S0(const std::string& input) {
    if (input.at(index) == '.') {
        inputRead = 1;
    }
    else {
        Serr();
    }
}