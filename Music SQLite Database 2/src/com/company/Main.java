package com.company;

import com.company.model.Artist;
import com.company.model.Datasource;
import com.company.model.SongArtist;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Datasource datasource = new Datasource();
        if(!datasource.open()) {
            System.out.println("Can't open datasource");
            //return; -- These returns are optional and mean
            //              "return nothing"
        }

        //Querying Artists
        List<Artist> artists = datasource.queryArtists(Datasource.ORDER_BY_DESC);
        if(artists == null) {
            System.out.println("No artists!");
           // return;
        }

        for(Artist artist: artists) {
            System.out.println("ID = " + artist.getId() + ", Name = "
            + artist.getName());
        }

       //Querying Albums
        List<String> albumsForArtists =
                datasource.queryAlbumsForArtist("Carole King", Datasource.ORDER_BY_ASC);
        for(String album : albumsForArtists) {
            System.out.println(album);
        }

       //Querying Artists for a song
        List<SongArtist> songArtists = datasource.queryArtistsForSong("Turkeys", Datasource.ORDER_BY_ASC);
        if(songArtists == null) {
            System.out.println("Couldn't find the artist for the song");
            return;
        }

        for(SongArtist artist : songArtists) {
            System.out.println("Artist name = " + artist.getArtistName() +
                    " Album name = " + artist.getAlbumName() +
                    " Track = " + artist.getTrack());
        }


        //Querying for metadata because we can't query for schema data from SQLite for some reason
        datasource.querySongsMetadata();

        int count = datasource.getCount(Datasource.TABLE_SONGS);
        System.out.println("Number of songs is: " + count);

        //Creating a View
        datasource.createViewForSongArtists();


        //Querying the View
        songArtists = datasource.querySongInfoView("Evil Woman");
        //It's actually a better practice to use the isEmpty() boolean,
        //consider that we've been creating lists (then looping through them)
        if(songArtists.isEmpty()) {
            System.out.println("Couldn't find the artist for the song");
            return;
        }

        for(SongArtist artist : songArtists) {
            System.out.println("FROM VIEW - Artist name = " + artist.getArtistName() +
                    " Album name = " + artist.getAlbumName() +
                    " Track number = " + artist.getTrack());
        }

        datasource.close();


    }
}

//Important Note:
//Notice that the main method code doesn't make any assumptions
//about how or where the data is stored
//the data could be coming from an xml file, a spreadsheet,
//mySQL database, or even a flat file
//if we were to change how and where the data was stored
//as long as we don't have to change any of the method
//signatures in the datasource class
//then we won't have to change any classes that use it
//and in this case, the main method will remain unchanged.
//Literally, the only thing that would change would be
//CONNECTION_STRING, where this time, we are using a diff DB