#include "Parser.h"

Parser::Parser(std::vector<Token*> inputTokens) {
    tokens = inputTokens;
    this->SCHEMES();
    this->COLON();
    this->scheme();
    this->schemeList();
    this->FACTS();
    this->COLON();
    this->factList();
    this->RULES();
    this->COLON();
    this->ruleList();
    this->QUERIES();
    this->COLON();
    this->query();
    this->queryList();
    this->E_O_F();
};

Parser::~Parser() {
    /*for (unsigned int i = 0; i < tokens.size(); i++) {
        delete tokens.at(i);
    }
    tokens.clear();*/
    for (unsigned int i = 0; i < rules.size(); i++) {
        delete rules.at(i);
    }
    rules.clear();
    for (unsigned int i = 0; i < schemes.size(); i++) {
        delete schemes.at(i);
    }
    schemes.clear();
    for (unsigned int i = 0; i < facts.size(); i++) {
        delete facts.at(i);
    }
    facts.clear();
    for (unsigned int i = 0; i < queries.size(); i++) {
        delete queries.at(i);
    }
    queries.clear();
}

std::vector<Rule*> Parser::GetRules() {return rules;}
std::vector<Predicate*> Parser::GetSchemes() {return schemes;}
std::vector<Predicate*> Parser::GetFacts() {return facts;}
std::vector<Predicate*> Parser::GetQueries() {return queries;}
std::set <std::string> Parser::GetDomains() {return domains;}

void Parser::schemeList() {
    if (tokens.at(tokensPlace)->GetType() == "ID") {
        this->scheme();
        this->schemeList();
    }
    else {}
};
void Parser::factList() {
    if (tokens.at(tokensPlace)->GetType() == "ID") {
        this->fact();
        this->factList();
    }
    else {}
};
void Parser::ruleList() {
    if (tokens.at(tokensPlace)->GetType() == "ID") {
        this->rule();
        this->ruleList();
    }
    else {}
};
void Parser::queryList() {
    if (tokens.at(tokensPlace)->GetType() == "ID") {
        this->query();
        this->queryList();
    }
    else {}
};

void Parser::scheme() {
    Predicate* scheme = new Predicate("SCHEME", this->ID());
    this->LEFT_PAREN();
    scheme->AddParameter(this->ID());
    this->idList(scheme);
    this->RIGHT_PAREN();
    schemes.push_back(scheme);
};
void Parser::fact() {
    Predicate* fact = new Predicate("FACT", this->ID());
    this->LEFT_PAREN();
    fact->AddParameter(this->STRING("FACT"));
    this->stringList(fact);
    this->RIGHT_PAREN();
    this->PERIOD();
    facts.push_back(fact);
};
void Parser::rule() {
    Rule* rule = new Rule(this->headPredicate("RULE"));
    this->COLON_DASH();
    rule->AddPredicate(this->predicate("RULE"));
    this->predicateList(rule);
    this->PERIOD();
    rules.push_back(rule);
};
void Parser::query() {
    Predicate* query = this->predicate("QUERY");
    this->Q_MARK();
    queries.push_back(query);
};

Predicate* Parser::headPredicate(std::string inputInputType) {
    Predicate* predicate = new Predicate(inputInputType, this->ID());
    this->LEFT_PAREN();
    predicate->AddParameter(this->ID());
    this->idList(predicate);
    this->RIGHT_PAREN();
    return predicate;
};
Predicate* Parser::predicate(std::string inputInputType) {
    Predicate* predicate = new Predicate(inputInputType, this->ID());
    this->LEFT_PAREN();
    predicate->AddParameter(this->parameter());
    this->parameterList(predicate);
    this->RIGHT_PAREN();
    return predicate;
};

void Parser::predicateList(Rule* inputRule) {
    if (tokens.at(tokensPlace)->GetType() == "COMMA") {
        this->COMMA();
        inputRule->AddPredicate(this->predicate("RULE"));
        this->predicateList(inputRule);
    }
    else {}
};
void Parser::parameterList(Predicate* inputPredicate) {
    if (tokens.at(tokensPlace)->GetType() == "COMMA") {
        this->COMMA();
        inputPredicate->AddParameter(this->parameter());
        this->parameterList(inputPredicate);
    }
    else {}
};
void Parser::stringList(Predicate* inputPredicate) {
    if (tokens.at(tokensPlace)->GetType() == "COMMA") {
        this->COMMA();
        inputPredicate->AddParameter(this->STRING(inputPredicate->GetType()));
        this->stringList(inputPredicate);
    }
    else {}
};
void Parser::idList(Predicate* inputPredicate) {
    if (tokens.at(tokensPlace)->GetType() == "COMMA") {
        this->COMMA();
        inputPredicate->AddParameter(this->ID());
        this->idList(inputPredicate);
    }
    else {}
};
std::string Parser::parameter() {
    if (tokens.at(tokensPlace)->GetType() == "STRING") {
        return this->STRING("");
    }
    else if (tokens.at(tokensPlace)->GetType() == "ID") {
        return this->ID();
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};

void Parser::COLON() {
    if (tokens.at(tokensPlace)->GetType() == "COLON") {
        tokensPlace++;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};
void Parser::COLON_DASH() {
    if (tokens.at(tokensPlace)->GetType() == "COLON_DASH") {
        tokensPlace++;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};
void Parser::COMMA() {
    if (tokens.at(tokensPlace)->GetType() == "COMMA") {
        tokensPlace++;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};
void Parser::PERIOD() {
    if (tokens.at(tokensPlace)->GetType() == "PERIOD") {
        tokensPlace++;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};
void Parser::Q_MARK() {
    if (tokens.at(tokensPlace)->GetType() == "Q_MARK") {
        tokensPlace++;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};
void Parser::LEFT_PAREN() {
    if (tokens.at(tokensPlace)->GetType() == "LEFT_PAREN") {
        tokensPlace++;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};
void Parser::RIGHT_PAREN() {
    if (tokens.at(tokensPlace)->GetType() == "RIGHT_PAREN") {
        tokensPlace++;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};
void Parser::SCHEMES() {
    if (tokens.at(tokensPlace)->GetType() == "SCHEMES") {
        tokensPlace++;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};
void Parser::FACTS() {
    if (tokens.at(tokensPlace)->GetType() == "FACTS") {
        tokensPlace++;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};
void Parser::RULES() {
    if (tokens.at(tokensPlace)->GetType() == "RULES") {
        tokensPlace++;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};
void Parser::QUERIES() {
    if (tokens.at(tokensPlace)->GetType() == "QUERIES") {
        tokensPlace++;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};
std::string Parser::ID() {
    if (tokens.at(tokensPlace)->GetType() == "ID") {
        std::string returnID = tokens.at(tokensPlace)->GetDescription();
        tokensPlace++;
        return returnID;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};
std::string Parser::STRING(std::string inputPredicateType) {
    if (tokens.at(tokensPlace)->GetType() == "STRING") {
        std::string returnSTRING = tokens.at(tokensPlace)->GetDescription();
        if (inputPredicateType == "FACT") {
            domains.insert(returnSTRING);
        }
        tokensPlace++;
        return returnSTRING;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};
void Parser::E_O_F() {
    if (tokens.at(tokensPlace)->GetType() == "EOF") {
        tokensPlace++;
    }
    else {
        std::cout << "Failure!" << std::endl;
        std::cout << "  ";
        tokens.at(tokensPlace)->PrintToken();
        throw 1;
    }
};