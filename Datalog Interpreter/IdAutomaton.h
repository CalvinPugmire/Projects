#ifndef IDAUTOMATON_H
#define IDAUTOMATON_H

#include "Automaton.h"

class IdAutomaton : public Automaton
{
public:
    IdAutomaton() : Automaton(TokenType::ID) {}  // Call the base constructor

    void S0(const std::string& input);
    void S1(const std::string& input);
};

#endif // IDAUTOMATON_H