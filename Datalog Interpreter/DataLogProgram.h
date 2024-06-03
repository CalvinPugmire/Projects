#ifndef DATALOGPROGRAM_H
#define DATALOGPROGRAM_H
#include <set>
#include <vector>
#include "Parser.h"
#include "Predicate.h"
#include "Rule.h"


class DataLogProgram
{
private:
    std::vector<Rule*> rules;
    std::vector<Predicate*> schemes;
    std::vector<Predicate*> facts;
    std::vector<Predicate*> queries;
    std::set <std::string> domains;

public:
    DataLogProgram(Parser* inputParser);
    ~DataLogProgram();

    std::vector<Predicate*> GetSchemes();
    std::vector<Predicate*> GetFacts();
    std::vector<Rule*> GetRules();
    std::vector<Predicate*> GetQueries();
    void ToString();
};


#endif // DATALOGPROGRAM_H
