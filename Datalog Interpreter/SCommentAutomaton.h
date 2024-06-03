#ifndef SCOMMENTAUTOMATON_H
#define SCOMMENTAUTOMATON_H

#include "Automaton.h"

class SCommentAutomaton : public Automaton
{
public:
    SCommentAutomaton() : Automaton(TokenType::COMMENT) {}  // Call the base constructor

    void S0(const std::string& input);
    void S1(const std::string& input);
};

#endif // SCOMMENTAUTOMATON_H