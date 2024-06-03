#ifndef TOKEN_H
#define TOKEN_H
#include <iostream>

enum class TokenType {
    COLON,
    COLON_DASH,
    UNDEFINED,
    COMMA,
    PERIOD,
    Q_MARK,
    LEFT_PAREN,
    RIGHT_PAREN,
    MULTIPLY,
    ADD,
    SCHEMES,
    FACTS,
    RULES,
    QUERIES,
    ID,
    STRING,
    COMMENT,
    E_O_F,
};

class Token
{
private:
    TokenType tokenType;
    std::string stringDescription;
    int lineNumber;

public:
    Token(TokenType type, std::string description, int line);
    void PrintToken();
    std::string GetType ();
    std::string GetDescription ();
    int GetLine ();
};

#endif // TOKEN_H

