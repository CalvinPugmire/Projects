#include "RulesAutomaton.h"

void RulesAutomaton::S0(const std::string& input) {
    if (input.at(index) == 'R') {
        inputRead = 1;
        index++;
        S1(input);
    }
    else {
        Serr();
    }
}

void RulesAutomaton::S1(const std::string& input) {
    if (input.at(index) == 'u') {
        inputRead++;
        index++;
        S2(input);
    }
    else {
        Serr();
    }
}

void RulesAutomaton::S2(const std::string& input) {
    if (input.at(index) == 'l') {
        inputRead++;
        index++;
        S3(input);
    }
    else {
        Serr();
    }
}

void RulesAutomaton::S3(const std::string& input) {
    if (input.at(index) == 'e') {
        inputRead++;
        index++;
        S4(input);
    }
    else {
        Serr();
    }
}

void RulesAutomaton::S4(const std::string& input) {
    if (input.at(index) == 's') {
        inputRead++;
        index++;
    }
    else {
        Serr();
    }
}