#include "DataLogProgram.h"

DataLogProgram::DataLogProgram(Parser* inputParser) {
    rules = inputParser->GetRules();
    schemes = inputParser->GetSchemes();
    facts = inputParser->GetFacts();
    queries = inputParser->GetQueries();
    domains = inputParser->GetDomains();
};

DataLogProgram::~DataLogProgram() {
    /*for (unsigned int i = 0; i < rules.size(); i++) {
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
    queries.clear();*/
}

std::vector<Predicate*> DataLogProgram::GetSchemes() {
    return schemes;
}

std::vector<Predicate*> DataLogProgram::GetFacts() {
    return facts;
}

std::vector<Rule *> DataLogProgram::GetRules() {
    return rules;
}

std::vector<Predicate*> DataLogProgram::GetQueries() {
    return queries;
}

void DataLogProgram::ToString() {
    std::cout << "Success!" << std::endl;
    std::cout << "Schemes(" << schemes.size() << "):" << std::endl;
    for (unsigned int i = 0; i < schemes.size(); i++) {
        std::cout << "  ";
        schemes.at(i)->ToString();
        std::cout << std::endl;
    }
    std::cout << "Facts(" << facts.size() << "):" << std::endl;
    for (unsigned int i = 0; i < facts.size(); i++) {
        std::cout << "  ";
        facts.at(i)->ToString();
        std::cout << std::endl;
    }
    std::cout << "Rules(" << rules.size() << "):" << std::endl;
    for (unsigned int i = 0; i < rules.size(); i++) {
        std::cout << "  ";
        rules.at(i)->ToString();
        std::cout << std::endl;
    }
    std::cout << "Queries(" << queries.size() << "):" << std::endl;
    for (unsigned int i = 0; i < queries.size(); i++) {
        std::cout << "  ";
        queries.at(i)->ToString();
        std::cout << std::endl;
    }
    std::cout << "Domain(" << domains.size() << "):" << std::endl;
    for(auto it = domains.begin(); it != domains.end(); it++)
    {
        std::cout << "  " << *it << std::endl;
    }
}
