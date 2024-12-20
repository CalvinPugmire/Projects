#include "FactsAutomaton.h"

void FactsAutomaton::S0(const std::string& input) {
    if (input.at(index) == 'F') {
        inputRead = 1;
        index++;
        S1(input);
    }
    else {
        Serr();
    }
}

void FactsAutomaton::S1(const std::string& input) {
    if (input.at(index) == 'a') {
        inputRead++;
        index++;
        S2(input);
    }
    else {
        Serr();
    }
}

void FactsAutomaton::S2(const std::string& input) {
    if (input.at(index) == 'c') {
        inputRead++;
        index++;
        S3(input);
    }
    else {
        Serr();
    }
}

void FactsAutomaton::S3(const std::string& input) {
    if (input.at(index) == 't') {
        inputRead++;
        index++;
        S4(input);
    }
    else {
        Serr();
    }
}

void FactsAutomaton::S4(const std::string& input) {
    if (input.at(index) == 's') {
        inputRead++;
        index++;
    }
    else {
        Serr();
    }
}