#ifndef LCOMMENTAUTOMATON_H
#define LCOMMENTAUTOMATON_H

#include "Automaton.h"

class LCommentAutomaton : public Automaton
{
public:
    LCommentAutomaton() : Automaton(TokenType::COMMENT) {}  // Call the base constructor

    void S0(const std::string& input);
    void S1(const std::string& input);
    void S2(const std::string& input);
    void S3(const std::string& input);
};

#endif // LCOMMENTAUTOMATON_H