//
// Created by troop on 11/20/2020.
//

#ifndef PLAYLIST_H
#define PLAYLIST_H

#include <iostream>
#include <string>
#include <vector>
#include "Song.h"

class Playlist {
public:
    Playlist ();
    Playlist (std::string entryName);
    void AddSong (Song* entrySong);
    void RemSong (int entryIndex);
    int Size ();
    Song* SongRet (int entryIndex);
    void Print ();
private:
    std::string listName;
    std::vector <Song*> storedSongs;
};


#endif //PLAYLIST_H
