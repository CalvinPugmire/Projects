#ifndef DATABASE_H
#define DATABASE_H
#include <vector>
#include "Relation.h"


class Database {
private:
    std::vector<Relation> relations;

public:
    Database();
    void AddRelation (Relation inputRelation);
    std::vector<Relation>* GetRelations ();
};


#endif //DATABASE_H
