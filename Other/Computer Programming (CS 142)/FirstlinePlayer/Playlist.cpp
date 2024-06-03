//
// Created by troop on 11/20/2020.
//

#include "Playlist.h"

#include <iostream>
#include <string>

Playlist::Playlist () {
    listName = "none";
}

Playlist::Playlist (std::string entryName) {
    listName = entryName;
}

void Playlist::AddSong(Song* entrySong) {
    storedSongs.push_back(entrySong);
}

void Playlist::RemSong(int entryIndex) {
    storedSongs.erase(storedSongs.begin()+entryIndex);
}

int Playlist::Size() {
    return storedSongs.size();
}

void Playlist::Print () {
    std::cout << listName << std::endl;
}

Song* Playlist::SongRet(int entryIndex) {
    return storedSongs.at(entryIndex);
}
