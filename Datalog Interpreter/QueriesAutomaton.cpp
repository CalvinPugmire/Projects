#include "QueriesAutomaton.h"

void QueriesAutomaton::S0(const std::string& input) {
    if (input.at(index) == 'Q') {
        inputRead = 1;
        index++;
        S1(input);
    }
    else {
        Serr();
    }
}

void QueriesAutomaton::S1(const std::string& input) {
    if (input.at(index) == 'u') {
        inputRead++;
        index++;
        S2(input);
    }
    else {
        Serr();
    }
}

void QueriesAutomaton::S2(const std::string& input) {
    if (input.at(index) == 'e') {
        inputRead++;
        index++;
        S3(input);
    }
    else {
        Serr();
    }
}

void QueriesAutomaton::S3(const std::string& input) {
    if (input.at(index) == 'r') {
        inputRead++;
        index++;
        S4(input);
    }
    else {
        Serr();
    }
}

void QueriesAutomaton::S4(const std::string& input) {
    if (input.at(index) == 'i') {
        inputRead++;
        index++;
        S5(input);
    }
    else {
        Serr();
    }
}

void QueriesAutomaton::S5(const std::string& input) {
    if (input.at(index) == 'e') {
        inputRead++;
        index++;
        S6(input);
    }
    else {
        Serr();
    }
}

void QueriesAutomaton::S6(const std::string& input) {
    if (input.at(index) == 's') {
        inputRead++;
        index++;
    }
    else {
        Serr();
    }
}