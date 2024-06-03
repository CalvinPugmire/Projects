#include "Lexer.h"
#include "ColonAutomaton.h"
#include "ColonDashAutomaton.h"
#include "CommaAutomaton.h"
#include "PeriodAutomaton.h"
#include "QMarkAutomaton.h"
#include "LeftParenAutomaton.h"
#include "RightParenAutomaton.h"
#include "MultiplyAutomaton.h"
#include "AddAutomaton.h"
#include "SchemesAutomaton.h"
#include "FactsAutomaton.h"
#include "RulesAutomaton.h"
#include "QueriesAutomaton.h"
#include "IdAutomaton.h"
#include "StringAutomaton.h"
#include "SCommentAutomaton.h"
#include "LCommentAutomaton.h"

Lexer::Lexer(std::string& input) {
    automata.push_back(new ColonAutomaton());
    automata.push_back(new ColonDashAutomaton());
    automata.push_back(new CommaAutomaton());
    automata.push_back(new PeriodAutomaton());
    automata.push_back(new QMarkAutomaton());
    automata.push_back(new LeftParenAutomaton());
    automata.push_back(new RightParenAutomaton());
    automata.push_back(new MultiplyAutomaton());
    automata.push_back(new AddAutomaton());
    automata.push_back(new SchemesAutomaton());
    automata.push_back(new FactsAutomaton());
    automata.push_back(new RulesAutomaton());
    automata.push_back(new QueriesAutomaton());
    automata.push_back(new IdAutomaton());
    automata.push_back(new StringAutomaton());
    automata.push_back(new SCommentAutomaton());
    automata.push_back(new LCommentAutomaton());

    int lineNumber = 1;
    while (input.size() > 0) {
        int maxRead = 0;
        Automaton* maxAutomaton = automata.at(0);
        for (unsigned int i = 0; i < automata.size(); i++) {
            int inputRead = automata.at(i)->Start(input);
            if (inputRead > maxRead) {
                maxRead = inputRead;
                maxAutomaton = automata.at(i);
            }
        }
        if (maxRead > 0) {
            if (maxAutomaton->ReturnEndOfFile()) {
                Token* newToken = new Token(TokenType::UNDEFINED,input.substr(0,maxRead-1),lineNumber);
                lineNumber = lineNumber + maxAutomaton->NewLinesRead();
                tokens.push_back(newToken);
            }
            else {
                Token* newToken = maxAutomaton->CreateToken(input.substr(0,maxRead), lineNumber);
                lineNumber = lineNumber + maxAutomaton->NewLinesRead();
                tokens.push_back(newToken);
            }
        }
        else {
            maxRead = 1;
            if (input.substr(0,1) == " " || input.substr(0,1) == "\t" || input.substr(0,1) == "\n") {
                if (input.substr(0,1) == "\n") {
                    lineNumber++;
                }
                /*do nothing*/
            }
            else {
                Token* newToken = new Token(TokenType::UNDEFINED,input.substr(0,1),lineNumber);
                tokens.push_back(newToken);
            }
        }
        input.erase(0, maxRead);
    }
    Token* newToken = new Token(TokenType::E_O_F, "", lineNumber-1);
    tokens.push_back(newToken);
}

Lexer::~Lexer() {
    for (unsigned int i = 0; i < automata.size(); i++) {
        delete automata.at(i);
    }
    automata.clear();
    for (unsigned int i = 0; i < tokens.size(); i++) {
        delete tokens.at(i);
    }
    tokens.clear();
}

std::vector<Token*> Lexer::GetTokens() {
    return tokens;
}