#include <iostream>
#include "Token.h"

Token::Token(TokenType type, std::string description, int line) {
    tokenType = type;
    stringDescription = description;
    lineNumber = line;
}

void Token::PrintToken() {
    std::string tokenTypeString;
    if (tokenType == TokenType::COLON) {tokenTypeString = "COLON";}
    else if (tokenType == TokenType::COLON_DASH) {tokenTypeString = "COLON_DASH";}
    else if (tokenType == TokenType::COMMA) {tokenTypeString = "COMMA";}
    else if (tokenType == TokenType::PERIOD) {tokenTypeString = "PERIOD";}
    else if (tokenType == TokenType::Q_MARK) {tokenTypeString = "Q_MARK";}
    else if (tokenType == TokenType::LEFT_PAREN) {tokenTypeString = "LEFT_PAREN";}
    else if (tokenType == TokenType::RIGHT_PAREN) {tokenTypeString = "RIGHT_PAREN";}
    else if (tokenType == TokenType::MULTIPLY) {tokenTypeString = "MULTIPLY";}
    else if (tokenType == TokenType::ADD) {tokenTypeString = "ADD";}
    else if (tokenType == TokenType::SCHEMES) {tokenTypeString = "SCHEMES";}
    else if (tokenType == TokenType::FACTS) {tokenTypeString = "FACTS";}
    else if (tokenType == TokenType::RULES) {tokenTypeString = "RULES";}
    else if (tokenType == TokenType::QUERIES) {tokenTypeString = "QUERIES";}
    else if (tokenType == TokenType::ID) {tokenTypeString = "ID";}
    else if (tokenType == TokenType::STRING) {tokenTypeString = "STRING";}
    else if (tokenType == TokenType::COMMENT) {tokenTypeString = "COMMENT";}
    else if (tokenType == TokenType::E_O_F) {tokenTypeString = "EOF";}
    else if (tokenType == TokenType::UNDEFINED) {tokenTypeString = "UNDEFINED";}
    std::cout << "(" << tokenTypeString << ",\"" << stringDescription << "\"," << lineNumber << ")" << std::endl;
}

std::string Token::GetType () {
    std::string tokenTypeString;
    if (tokenType == TokenType::COLON) {tokenTypeString = "COLON";}
    else if (tokenType == TokenType::COLON_DASH) {tokenTypeString = "COLON_DASH";}
    else if (tokenType == TokenType::COMMA) {tokenTypeString = "COMMA";}
    else if (tokenType == TokenType::PERIOD) {tokenTypeString = "PERIOD";}
    else if (tokenType == TokenType::Q_MARK) {tokenTypeString = "Q_MARK";}
    else if (tokenType == TokenType::LEFT_PAREN) {tokenTypeString = "LEFT_PAREN";}
    else if (tokenType == TokenType::RIGHT_PAREN) {tokenTypeString = "RIGHT_PAREN";}
    else if (tokenType == TokenType::MULTIPLY) {tokenTypeString = "MULTIPLY";}
    else if (tokenType == TokenType::ADD) {tokenTypeString = "ADD";}
    else if (tokenType == TokenType::SCHEMES) {tokenTypeString = "SCHEMES";}
    else if (tokenType == TokenType::FACTS) {tokenTypeString = "FACTS";}
    else if (tokenType == TokenType::RULES) {tokenTypeString = "RULES";}
    else if (tokenType == TokenType::QUERIES) {tokenTypeString = "QUERIES";}
    else if (tokenType == TokenType::ID) {tokenTypeString = "ID";}
    else if (tokenType == TokenType::STRING) {tokenTypeString = "STRING";}
    else if (tokenType == TokenType::COMMENT) {tokenTypeString = "COMMENT";}
    else if (tokenType == TokenType::E_O_F) {tokenTypeString = "EOF";}
    else if (tokenType == TokenType::UNDEFINED) {tokenTypeString = "UNDEFINED";}
    return tokenTypeString;
};

std::string Token::GetDescription () {
    return stringDescription;
};

int Token::GetLine () {
    return lineNumber;
};