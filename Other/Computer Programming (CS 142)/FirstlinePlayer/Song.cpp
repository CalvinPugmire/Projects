//
// Created by troop on 11/20/2020.
//

#include "Song.h"

#include <iostream>
#include <string>

Song::Song () {
    songName = "none";
    firstLine = "none";
    timesPlayed = 0;
}

Song::Song (std::string entryName, std::string entryLine, int entryPlays) {
    songName = entryName;
    firstLine = entryLine;
    timesPlayed = entryPlays;
}

std::string Song::Play() {
    timesPlayed = timesPlayed + 1;
    return firstLine;
}

std::string Song::Name () {
    return songName;
}

void Song::Print () {
    std::cout << songName << ": \"" << firstLine << "\", " << timesPlayed << " play(s)." << std::endl;
}
