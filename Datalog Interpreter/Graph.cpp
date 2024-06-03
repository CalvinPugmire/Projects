#include "Graph.h"

Graph::Graph() {

}

void Graph::BuildDG(std::vector<Rule*> inputRules) {
    for (unsigned int i = 0; i < inputRules.size(); i++) {
        std::set<int> dependencies;
        for (unsigned int j = 0; j < inputRules.at(i)->GetPredicates().size(); j++) {
            for (unsigned int k = 0; k < inputRules.size(); k++) {
                if (inputRules.at(i)->GetPredicates().at(j)->GetID() == inputRules.at(k)->GetHeadPredicate()->GetID()) {
                    dependencies.insert(k);
                }
            }
        }
        dependencyGraph.insert({i,dependencies});
    }
}

void Graph::BuildRDG(std::vector<Rule*> inputRules) {
    for (unsigned int i = 0; i < inputRules.size(); i++) {
        std::set<int> dependencies;
        for (unsigned int j = 0; j < inputRules.size(); j++) {
            for (unsigned int k = 0; k < inputRules.at(j)->GetPredicates().size(); k++) {
                if (inputRules.at(j)->GetPredicates().at(k)->GetID() == inputRules.at(i)->GetHeadPredicate()->GetID()) {
                    dependencies.insert(j);
                }
            }
        }
        reverseDependencyGraph.insert({i,dependencies});
    }
    /*for (int i = 0; i < dependencyGraph.size(); i++) {
        std::set<int> dependencies;
        for (int j = 0; j < dependencyGraph.size(); j++) {
            for (int setit : dependencyGraph.at(j)) {
                if (setit == i) {
                    dependencies.insert(j);
                }
            }
        }
        reverseDependencyGraph.insert({i,dependencies});
    }*/
}

void Graph::DFSForestPO() {
    visitedNodes.clear();
    for (unsigned int i = 0; i < reverseDependencyGraph.size(); i++) {
        visitedNodes.insert({i,false});
    }
    for (unsigned int i = 0; i < reverseDependencyGraph.size(); i++) {
        if (!visitedNodes.at(i)) {
            visitedNodes.at(i) = true;
            DepthFirstSearchPO(i);
        }
    }
}

void Graph::DepthFirstSearchPO(int inputNode) {
    for (int childit : reverseDependencyGraph.at(inputNode)) {
        if (!visitedNodes.at(childit)) {
            visitedNodes.at(childit) = true;
            DepthFirstSearchPO(childit);
        }
    }
    postOrder.push_back(inputNode);
}

void Graph::DFSForestSCC() {
    visitedNodes.clear();
    for (int i = postOrder.size()-1; i >= 0; i--) {
        visitedNodes.insert({postOrder.at(i),false});
    }
    for (int i = postOrder.size()-1; i >= 0; i--) {
        SCC.clear();
        if (!visitedNodes.at(postOrder.at(i))) {
            visitedNodes.at(postOrder.at(i)) = true;
            SCC.insert(postOrder.at(i));
            DepthFirstSearchSCC(postOrder.at(i),i);
            SCCs.push_back(SCC);
        }
    }
}

void Graph::DepthFirstSearchSCC(int inputNode, int inputPos) {
    for (int childit : dependencyGraph.at(inputNode)) {
        if (!visitedNodes.at(childit)) {
            visitedNodes.at(childit) = true;
            SCC.insert(childit);
            DepthFirstSearchSCC(childit,inputPos-1);
        }
    }
}

std::vector<std::set<int>> Graph::GetSCCs() {
    return SCCs;
}

void Graph::PrintDG() {
    std::cout << "Dependency Graph" << std::endl;
    for (auto& i : dependencyGraph) {
        std::cout << "R" << i.first << ":";
        unsigned int count = 0;
        for (auto& j : i.second) {
            count += 1;
            std::cout << "R" << j;
            if (count < i.second.size()) {
                std::cout << ",";
            }
        }
        std::cout << std::endl;
    }
}

void Graph::PrintRDG() {
    std::cout << "Reverse Dependency Graph" << std::endl;
    for (auto& i : reverseDependencyGraph) {
        std::cout << "R" << i.first << ":";
        unsigned int count = 0;
        for (auto& j : i.second) {
            count += 1;
            std::cout << "R" << j;
            if (count < i.second.size()) {
                std::cout << ",";
            }
        }
        std::cout << std::endl;
    }
}

void Graph::PrintPO() {
    std::cout << "Post Order" << std::endl;
    for (unsigned int i = 0; i < postOrder.size(); i++) {
        std::cout << "R" << postOrder.at(i);
        if (i < postOrder.size()-1) {
            std::cout << ",";
        }
    }
    std::cout << std::endl;
}

void Graph::PrintSCCs() {
    std::cout << "SCCs" << std::endl;
    for (unsigned int i = 0; i < SCCs.size(); i++) {
        unsigned int count = 0;
        for (auto j : SCCs.at(i)) {
            count += 1;
            std::cout << "R" << j;
            if (count < SCCs.at(i).size()) {
                std::cout << ",";
            }
        }
        std::cout << std::endl;
    }
    std::cout << std::endl;
}

void Graph::PrintSCC(int inputSCC) {
    unsigned int count = 0;
    for (auto j : SCCs.at(inputSCC)) {
        count += 1;
        std::cout << "R" << j;
        if (count < SCCs.at(inputSCC).size()) {
            std::cout << ",";
        }
    }
    std::cout << std::endl;
}
