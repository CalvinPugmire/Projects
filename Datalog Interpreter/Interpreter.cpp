#include "Interpreter.h"

Interpreter::Interpreter(DataLogProgram *inputDatalog, Database inputDatabase) {
    for (unsigned int i = 0; i < inputDatalog->GetSchemes().size(); i++) {
        Header header = Header(inputDatalog->GetSchemes().at(i)->GetParameters());
        Relation relation = Relation(inputDatalog->GetSchemes().at(i)->GetID(), header);
        inputDatabase.AddRelation(relation);
    }

    for (unsigned int i = 0; i < inputDatalog->GetFacts().size(); i++) {
        Tuple tuple = Tuple(inputDatalog->GetFacts().at(i)->GetParameters());
        for (unsigned int j = 0; j < inputDatabase.GetRelations()->size(); j++) {
            if (inputDatabase.GetRelations()->at(j).GetName() == inputDatalog->GetFacts().at(i)->GetID()) {
                inputDatabase.GetRelations()->at(j).AddTuple(tuple);
            }
        }
    }

    Graph sccProduction = Graph();
    sccProduction.BuildDG(inputDatalog->GetRules());
    sccProduction.PrintDG();
    std::cout << std::endl;
    sccProduction.BuildRDG(inputDatalog->GetRules());
    sccProduction.DFSForestPO();
    sccProduction.DFSForestSCC();
    std::vector<std::set<int>> SCCs = sccProduction.GetSCCs();

    std::cout << "Rule Evaluation" << std::endl;
    for (unsigned int h = 0; h < SCCs.size(); h++) {
        std::cout << "SCC: ";
        sccProduction.PrintSCC(h);
        bool change = true;
        int passes = 0;
        while (change) {
            change = false;
            passes++;
            for (auto i : SCCs.at(h)) {
                inputDatalog->GetRules().at(i)->ToString();
                std::cout << std::endl;
                std::vector <Relation> dummyRelations;
                for (unsigned int j = 0; j < inputDatalog->GetRules().at(i)->GetPredicates().size(); j++) {
                    dummyRelations.push_back(EvaluatePredicate(inputDatalog->GetRules().at(i)->GetPredicates().at(j), inputDatabase));
                }
                Relation masterDummyRelation;
                if (!dummyRelations.empty()) {
                    masterDummyRelation = dummyRelations.at(0);
                    for (unsigned int j = 1; j < dummyRelations.size(); j++) {
                        masterDummyRelation = masterDummyRelation.Join(&dummyRelations.at(j));
                    }
                }
                std::vector <int> masterDummyHeader;
                for (unsigned int j = 0; j < inputDatalog->GetRules().at(i)->GetHeadPredicate()->GetParameters().size(); j++) {
                    for (unsigned int k = 0; k < masterDummyRelation.GetHeader().GetAttributeNames().size(); k++) {
                        if (masterDummyRelation.GetHeader().GetAttributeNames().at(k) == inputDatalog->GetRules().at(i)->GetHeadPredicate()->GetParameters().at(j)) {
                            masterDummyHeader.push_back(k);
                            continue;
                        }
                    }
                }
                masterDummyRelation = masterDummyRelation.Project(masterDummyHeader);
                masterDummyRelation.AddName(inputDatalog->GetRules().at(i)->GetHeadPredicate()->GetID());
                for (unsigned int j = 0; j < inputDatabase.GetRelations()->size(); j++) {
                    Relation beforeRelation = inputDatabase.GetRelations()->at(j);
                    if (inputDatabase.GetRelations()->at(j).GetName() == masterDummyRelation.GetName() && inputDatabase.GetRelations()->at(j).GetHeader().GetAttributeNames().size() == masterDummyRelation.GetHeader().GetAttributeNames().size()) {
                        inputDatabase.GetRelations()->at(j).Onion(&masterDummyRelation);
                        if (beforeRelation.GetTuples().size() != inputDatabase.GetRelations()->at(j).GetTuples().size()) {
                            change = true;
                        }
                    }
                }
            }
            if (SCCs.at(h).size() == 1) {
                for (auto i : SCCs.at(h)) {
                    bool setchange = false;
                    for (unsigned int j = 0; j < inputDatalog->GetRules().at(i)->GetPredicates().size(); j++) {
                        if (inputDatalog->GetRules().at(i)->GetPredicates().at(j)->GetID() == inputDatalog->GetRules().at(i)->GetHeadPredicate()->GetID()) {
                            setchange = true;
                        }
                    }
                    if (!setchange) {
                        change = false;
                    }
                }
            }
        }
        std::cout << passes << " passes: ";
        sccProduction.PrintSCC(h);
    }
    std::cout << std::endl;

    std::cout << "Query Evaluation" << std::endl;
    for (unsigned int i = 0; i < inputDatalog->GetQueries().size(); i++) {
        EvaluatePredicate(inputDatalog->GetQueries().at(i), inputDatabase);
    }
}

Relation Interpreter::EvaluatePredicate(Predicate* inputPredicate, Database inputDatabase) {
    Relation returnRelation = Relation();
    for (unsigned int j = 0; j < inputDatabase.GetRelations()->size(); j++) {
        if (inputDatabase.GetRelations()->at(j).GetName() == inputPredicate->GetID()) {
            Relation relation = inputDatabase.GetRelations()->at(j);
            for (unsigned int k = 0; k < inputPredicate->GetParameters().size(); k++) {
                if (inputPredicate->GetParameters().at(k).substr(0,1) == "'") {
                    relation = relation.Select(k, inputPredicate->GetParameters().at(k));
                } else {
                    for (unsigned int l = 0; l < inputPredicate->GetParameters().size(); l++) {
                        if (inputPredicate->GetParameters().at(k) == inputPredicate->GetParameters().at(l) && k != l) {
                            relation = relation.Select(k, l);
                        }
                    }
                }
            }
            relation = relation.Rename(inputPredicate->GetParameters());
            if (!relation.GetHeader().GetAttributeNames().empty()) {
                std::vector<int> indices;
                for (unsigned int k = 0; k < inputPredicate->GetParameters().size(); k++) {
                    if (inputPredicate->GetParameters().at(k).substr(0, 1) != "'") {
                        indices.push_back(k);
                    }
                }
                relation = relation.Project(indices);
            }
            if (!relation.GetHeader().GetAttributeNames().empty()) {
                std::vector<int> indices;
                indices.push_back(0);
                for (unsigned int k = 1; k < relation.GetHeader().GetAttributeNames().size(); k++) {
                    bool add1 = true;
                    bool add2 = false;
                    for (unsigned int l = 0; l < k; l++) {
                        if (relation.GetHeader().GetAttributeNames().at(k) == relation.GetHeader().GetAttributeNames().at(l)) {
                            add1 = false;
                        }
                        for (Tuple tuple : relation.GetTuples()) {
                            if (tuple.GetAttributeValues().at(k) != tuple.GetAttributeValues().at(l)) {
                                add2 = true;
                            }
                        }

                    }
                    if (add1 || add2) {
                        indices.push_back(k);
                    }
                }
                relation = relation.Project(indices);
            }
            returnRelation = relation;

            if (inputPredicate->GetType() == "QUERY") {
                inputPredicate->ToString();
                if (relation.GetTuples().size() != 0) {
                    std::cout << " Yes(" << relation.GetTuples().size() << ")" << std::endl;
                } else {
                    std::cout << " No" << std::endl;
                }
                for (auto tuple : relation.GetTuples()) {
                    if (!tuple.GetAttributeValues().empty()) {
                        std::cout << "  ";
                        for (unsigned int k = 0; k < tuple.GetAttributeValues().size(); k++) {
                            std::cout << relation.GetHeader().GetAttributeNames().at(k) << "=" << tuple.GetAttributeValues().at(k);
                            if (k < tuple.GetAttributeValues().size()-1) {
                                std::cout << ", ";
                            }
                        }
                        std::cout << std::endl;
                    }
                }
            }
        }
    }
    return returnRelation;
}
