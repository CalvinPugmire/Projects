#ifndef LEXER_H
#define LEXER_H
#include <vector>
#include "Automaton.h"
#include "Token.h"

class Lexer
{
private:
    std::vector<Automaton*> automata;
    std::vector<Token*> tokens;

public:
    Lexer(std::string& input);
    ~Lexer();
    std::vector<Token*> GetTokens();
};

#endif // LEXER_H

