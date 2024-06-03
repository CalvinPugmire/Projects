#ifndef INTERPRETER_H
#define INTERPRETER_H
#include <iostream>
#include <vector>
#include "Database.h"
#include "DataLogProgram.h"
#include "Relation.h"
#include "Graph.h"


class Interpreter {
public:
    Interpreter(DataLogProgram* inputDatalog, Database inputDatabase);
    Relation EvaluatePredicate (Predicate* inputPredicate, Database inputDatabase);
};


#endif //INTERPRETER_H
