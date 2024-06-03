#include "Rule.h"

Rule::Rule(Predicate* inputHeadPredicate) {
    headPredicate = inputHeadPredicate;
}

Rule::~Rule() {
    delete headPredicate;
    for (unsigned int i = 0; i < predicates.size(); i++) {
        delete predicates.at(i);
    }
    predicates.clear();
}

void Rule::AddPredicate(Predicate* inputPredicate) {
    predicates.push_back(inputPredicate);
}

Predicate *Rule::GetHeadPredicate() {
    return headPredicate;
}

std::vector<Predicate *> Rule::GetPredicates() {
    return predicates;
}

void Rule::ToString() {
    headPredicate->ToString();
    std::cout << " :- ";
    for (unsigned int i = 0; i < predicates.size(); i++) {
        predicates.at(i)->ToString();
        if (i < predicates.size()-1) {
            std::cout << ",";
        }
    }
    std::cout << ".";
}
