#ifndef PARSER_H
#define PARSER_H
#include <set>
#include <vector>
#include "Token.h"
#include "Predicate.h"
#include "Rule.h"


class Parser
{
private:
    int tokensPlace = 0;
    std::vector<Token*> tokens;
    std::vector<Rule*> rules;
    std::vector<Predicate*> schemes;
    std::vector<Predicate*> facts;
    std::vector<Predicate*> queries;
    std::set <std::string> domains;

public:
    Parser(std::vector<Token*> inputTokens);
    ~Parser();
    std::vector<Rule*> GetRules();
    std::vector<Predicate*> GetSchemes();
    std::vector<Predicate*> GetFacts();
    std::vector<Predicate*> GetQueries();
    std::set <std::string> GetDomains();
    void schemeList();
    void factList();
    void ruleList();
    void queryList();
    void scheme();
    void fact();
    void rule();
    void query();
    Predicate* headPredicate(std::string inputInputType);
    Predicate* predicate(std::string inputInputType);
    void predicateList(Rule* inputRule);
    void parameterList(Predicate* inputPredicate);
    void stringList(Predicate* inputPredicate);
    void idList(Predicate* inputPredicate);
    std::string parameter();
    void COLON();
    void COLON_DASH();
    void COMMA();
    void PERIOD();
    void Q_MARK();
    void LEFT_PAREN();
    void RIGHT_PAREN();
    void SCHEMES();
    void FACTS();
    void RULES();
    void QUERIES();
    std::string ID();
    std::string STRING(std::string inputPredicateType);
    void E_O_F();
};


#endif // PARSER_H
