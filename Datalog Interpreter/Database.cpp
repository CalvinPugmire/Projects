#include "Database.h"

Database::Database() {
}

void Database::AddRelation(Relation inputRelation) {
    relations.push_back(inputRelation);
}

std::vector<Relation>* Database::GetRelations() {
    return &relations;
}
