#ifndef RELATION_H
#define RELATION_H
#include <set>
#include <iostream>
#include "Header.h"
#include "Predicate.h"
#include "Tuple.h"


class Relation {
private:
    std::string name;
    Header header;
    std::set<Tuple> tuples;

public:
    Relation();
    Relation(std::string inputName, Header inputHeader);
    Relation(std::string inputName, Header inputHeader, std::set<Tuple> inputTuples);

    void AddName(std::string inputName);
    void AddTuple(Tuple inputTuple);
    std::string GetName();
    Header GetHeader();
    std::set<Tuple> GetTuples();

    Relation Select(int index, std::string value);
    Relation Select(int index1, int index2);
    Relation Project(std::vector<int> indices);
    Relation Rename(std::vector<std::string> values);
    Relation Join(Relation* inputRelation);
    void Onion(Relation* inputRelation);
};


#endif //RELATION_H
