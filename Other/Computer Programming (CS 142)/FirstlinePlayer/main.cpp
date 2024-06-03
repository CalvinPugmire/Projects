//A Note regarding 'using namespace std':
// The author of this code prefers to not have the line 'using namespace std;'
// Instead, you will see 'std::' in front of string, cout, and other identifiers
// You could put 'using namespace std;' at the top of your code and not need 'std::' in front of these words,
// but many people consider it better practice to not have 'using namespace std;'


#include <iostream>
#include <string>
#include <vector>

#include "Song.h"
#include "Playlist.h"

std::string GetUserString(const std::string& prompt);
int GetUserInt(const std::string& prompt);
void OptionsMenuOption();

int main() {
    std::cout << "Welcome to the Firstline Player!  Enter options to see menu options." << std::endl << std::endl;

    std::vector <Song*> allSongs;
    std::vector <Playlist*> allLists;

    std::string userMenuChoice = "none";
    bool continueMenuLoop = true;

    while (continueMenuLoop) {
        userMenuChoice = GetUserString("Enter your selection now: ");

        if (userMenuChoice == "add") {
            const std::string DONE_KEYWORD = "DONE";
            std::cout << "Enter songs' names and first lines"
                    << " (type \"" << DONE_KEYWORD << "\" when done):" << std::endl;

            std::string songName = "none";
            std::string firstLine = "none";

            songName = GetUserString("Song Name: ");
            while (songName != DONE_KEYWORD) {
                firstLine = GetUserString("Song's first line: ");

                Song* newSong = new Song(songName, firstLine, 0);
                allSongs.push_back(newSong);

                songName = GetUserString("Song Name: ");
            }
        }

        else if (userMenuChoice == "list") {
            for (unsigned int i = 0; i < allSongs.size(); i++) {
                allSongs.at(i)->Print();
            }
        }

        else if (userMenuChoice == "addp") {
            std::string listName = "none";

            listName = GetUserString("Playlist Name: ");

            Playlist* newList = new Playlist(listName);
            allLists.push_back(newList);
        }

        else if (userMenuChoice == "addsp") {
            for (unsigned int i = 0; i < allLists.size(); i++) {
                std::cout << "  " << i << ": ";
                allLists.at(i)->Print();
            }
            int indexNum;
            indexNum = GetUserInt("Pick a playlist index number: ");
            for (unsigned int i = 0; i < allSongs.size(); i++) {
                std::cout << "  " << i << ": " << allSongs.at(i)->Name() << std::endl;
            }
            int songNum;
            songNum = GetUserInt("Pick a song index number: ");
            allLists.at(indexNum)->AddSong(allSongs.at(songNum));
        }

        else if (userMenuChoice == "listp") {
            for (unsigned int i = 0; i < allLists.size(); i++) {
                std::cout << i << ": ";
                allLists.at(i)->Print();
            }
        }

        else if (userMenuChoice == "play") {
            for (unsigned int i = 0; i < allLists.size(); i++) {
                std::cout << "  " << i << ": ";
                allLists.at(i)->Print();
            }
            int indexNum;
            indexNum = GetUserInt("Pick a playlist index number: ");
            std::cout << std::endl;
            std::cout << "Playing songs from playlist: ";
            allLists.at(indexNum)->Print();
            for (int i = 0; i < allLists.at(indexNum)->Size(); i++) {
                std::cout << allLists.at(indexNum)->SongRet(i)->Play() << std::endl;
            }
        }

        else if (userMenuChoice == "remp") {
            for (unsigned int i = 0; i < allLists.size(); i++) {
                std::cout << "  " << i << ": ";
                allLists.at(i)->Print();
            }

            int listNum;
            listNum = GetUserInt("Pick a playlist index number to remove: ");

            //FREEING MEMORYvvv
            delete allLists.at(listNum);
            //FREEING MEMORY^^^
            allLists.erase(allLists.begin()+listNum);
        }

        else if (userMenuChoice == "remsp") {
            for (unsigned int i = 0; i < allLists.size(); i++) {
                std::cout << "  " << i << ": ";
                allLists.at(i)->Print();
            }
            int indexNum;
            indexNum = GetUserInt("Pick a playlist index number: ");
            for (int i = 0; i < allLists.at(indexNum)->Size(); i++) {
                std::cout << "  " << i << ": " << allLists.at(indexNum)->SongRet(i)->Name() << std::endl;
            }
            int songNum;
            songNum = GetUserInt("Pick a song index number to remove: ");
            allLists.at(indexNum)->RemSong(songNum);
        }

        else if (userMenuChoice == "remsl") {
            for (unsigned int i = 0; i < allSongs.size(); i++) {
                std::cout << "  " << i << ": " << allSongs.at(i)->Name() << std::endl;
            }
            int songNum;
            songNum = GetUserInt("Pick a song index number to remove: ");
            for (unsigned int i = 0; i < allLists.size(); i++) {
                for (int j = 0; j < allLists.at(i)->Size(); j++) {
                    if (allLists.at(i)->SongRet(j)->Name() == allSongs.at(songNum)->Name()) {
                        allLists.at(i)->RemSong(j);
                    }
                }
            }
            //FREEING MEMORYvvv
            delete allSongs.at(songNum);
            //FREEING MEMORY^^^
            allSongs.erase(allSongs.begin()+songNum);
        }

        else if (userMenuChoice == "options") {
            OptionsMenuOption();
        }

        else if (userMenuChoice == "quit") {
            //FREEING MEMORYvvv
            for (unsigned int i = 0; i < allSongs.size(); i++) {
                delete allSongs.at(i);
            }
            for (unsigned int i = 0; i < allLists.size(); i++) {
                delete allLists.at(i);
            }
            //FREEING MEMORY^^^
            std::cout << "Goodbye!" << std::endl;
            continueMenuLoop = false;
        }

        else {
            OptionsMenuOption();
        }
    }

    return 0;
}


std::string GetUserString(const std::string& prompt) {
    std::string userAnswer = "none";

    std::cout << prompt;
    std::getline(std::cin, userAnswer);
    std::cout << std::endl;
    return userAnswer;
}
int GetUserInt(const std::string& prompt) {
    int userAnswer = 0;

    std::cout << prompt;
    std::cin >> userAnswer;
    std::cin.ignore();
    std::cout << std::endl;
    return userAnswer;
}

void OptionsMenuOption() {
    std::cout << "add      Adds a list of songs to the library" << std::endl
        << "list     Lists all the songs in the library" << std::endl
        << "addp     Adds a new playlist" << std::endl
        << "addsp    Adds a song to a playlist" << std::endl
        << "listp    Lists all the playlists" << std::endl
        << "play     Plays a playlist" << std::endl
        << "remp     Removes a playlist" << std::endl
        << "remsp    Removes a song from a playlist" << std::endl
        << "remsl    Removes a song from the library (and all playlists)" << std::endl
        << "options  Prints this options menu" << std::endl
        << "quit     Quits the program" << std::endl << std::endl;
}
