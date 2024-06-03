//
// Created by troop on 11/20/2020.
//

#ifndef SONG_H
#define SONG_H

#include <iostream>
#include <string>


class Song {
public:
    Song ();
    Song (std::string entryName, std::string entryLine, int entryPlays);
    std::string Play ();
    std::string Name ();
    void Print ();
private:
    std::string songName;
    std::string firstLine;
    int timesPlayed;
};


#endif //SONG_H
