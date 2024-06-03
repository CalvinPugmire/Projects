#ifndef RULE_H
#define RULE_H
#include <vector>
#include "Predicate.h"


class Rule
{
private:
    Predicate* headPredicate;
    std::vector<Predicate*> predicates;

public:
    Rule(Predicate* inputHeadPredicate);
    ~Rule();
    void AddPredicate(Predicate* inputPredicate);
    Predicate* GetHeadPredicate ();
    std::vector <Predicate*> GetPredicates ();
    void ToString();
};


#endif //RULE_H
