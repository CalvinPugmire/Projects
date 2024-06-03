#include <iostream>
#include <string>
#include <vector>

using namespace std;

//Document.h
class Document{
private:
    string name;
    string extension;
    string contents;
public:
    Document(string nameIn, string extensionIn, string contentsIn);
    string GetName();
    void Rename(string newDocumentName);
    string GetExtension();
    string GetContents();
    void Print();

};

//Document.cpp
Document::Document(string nameIn, string extensionIn, string contentsIn){
    name = nameIn;
    extension = extensionIn;
    contents = contentsIn;
}
string Document::GetName(){
    return name;
}
void Document::Rename(string newDocumentName){
    name = newDocumentName;
}
string Document::GetExtension(){
    return extension;
}
string Document::GetContents(){
    return contents;
}
void Document::Print(){
    cout << "Name: " << name << "." << extension << endl;
    cout << "Contents: " << contents << endl;
}

//main.cpp----------
void DisplayMenu(){
    cout << "Choose an option below: "<< endl;
    cout << "1. Add a document to your list"<< endl;
    cout << "2. Rename one of your documents"<< endl;
    cout << "3. Share one of your documents with your friend"<< endl;
    cout << "4. Display all of the documents"<< endl;
    cout << "5. Quit"<< endl << endl;
}

// Modify the main function to handle each menu option
// Consider creating a new function (above main) per menu option
int main(){
    const int ADD_DOCUMENT_OPTION = 1;
    const int RENAME_DOCUMENT_OPTION = 2;
    const int SHARE_YOUR_DOCUMENT_OPTION = 3;
    const int DISPLAY_ALL_DOCUMENTS_OPTION = 4;
    const int QUIT_OPTION = 5;

    vector <Document*> documents;
    vector <Document*> sharedDocuments;

    cout << "Choose an option below: "<< endl;
    cout << "1. Add a document to your list"<< endl;
    cout << "2. Rename one of your documents"<< endl;
    cout << "3. Share one of your documents with your friend"<< endl;
    cout << "4. Display all of the documents"<< endl;
    cout << "5. Quit"<< endl << endl;
    int option;
    cin >> option;

    while (option != QUIT_OPTION){
        if (option == ADD_DOCUMENT_OPTION){
            cin.ignore();
            string entryName = "none";
            cout << "Enter the document name: " << endl;
            getline(cin, entryName);

            string entryExt = "none";
            cout << "Enter the document extension: " << endl;
            getline(cin, entryExt);

            string entryCont = "none";
            cout << "Enter the document content: " << endl;
            getline(cin, entryCont);

            Document* newDocument = new Document(entryName, entryExt, entryCont);
            documents.push_back(newDocument);
        }
        else if (option == RENAME_DOCUMENT_OPTION){
            cout << "Enter the index of your document you want to rename: " << endl;
            int entryIndex;
            cin >> entryIndex;
            cout << "Enter the updated name of the document: " << endl;
            string entryName;
            getline(cin, entryName);

            documents.at(entryIndex)->Rename(entryName);
        }
        else if (option == SHARE_YOUR_DOCUMENT_OPTION){
            cout << "Enter the index of your document you want to share: " << endl;
            int entryIndex;
            cin >> entryIndex;

            sharedDocuments.push_back(documents.at(entryIndex));
        }
        else if (option == DISPLAY_ALL_DOCUMENTS_OPTION){
            cout << "-------------------Your List------------------" << endl;
            for (unsigned int i = 0; i < documents.size(); i++) {
                cout << "Name: " << documents.at(i)->GetName() << endl;
                cout << "Contents: " << documents.at(i)->GetContents() << endl;
            }
            cout << "-------------------Your Friend's List------------------" << endl;
            for (unsigned int i = 0; i < sharedDocuments.size(); i++) {
                cout << "Name: " << sharedDocuments.at(i)->GetName() << endl;
                cout << "Contents: " << sharedDocuments.at(i)->GetContents() << endl;
            }
        }

        cout << endl << "Enter option: " << endl;
        cin >> option;
    }
    for (unsigned int i = 0; i < documents.size(); i++) {
        documents.erase(documents.begin()+i);
    }
    for (unsigned int i = 0; i < sharedDocuments.size(); i++) {
        delete sharedDocuments.at(i);
        sharedDocuments.erase(sharedDocuments.begin()+i);
    }
    return 0;
}