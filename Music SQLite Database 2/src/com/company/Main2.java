package com.company;

import com.company.model.Artist;
import com.company.model.Datasource;
import com.company.model.SongArtist;

import java.util.List;
import java.util.Scanner;

//Created a main2 to focus on injection attacks
//and prepared statements
public class Main2 {

    public static void main(String[] args) {

        Datasource datasource = new Datasource();
        if(!datasource.open()) {
            System.out.println("Can't open datasource");

        }

        //Querying Artists
        List<Artist> artists = datasource.queryArtists(Datasource.ORDER_BY_DESC);
        if(artists == null) {
            System.out.println("No artists!");

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


       //Introducing Injection Attack Vulnerability
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a song title: ");
        String title = scanner.nextLine();
        //If someone typed in
        //Go Your Own Way" or 1=1 or "
        //It's going to output EVERYTHING (EVERY ROW) in the database.
        //A hacker could thus access everything...You don't want that.
        //This is known as a SQL injection attack
        //Because the user has injected SQL
        //that we didn't intend to run
        //note continued on line 96

        //Querying the View
        songArtists = datasource.querySongInfoView(title);
        if(songArtists.isEmpty()) {
            System.out.println("Couldn't find the artist for the song");
            return;
        }

        for(SongArtist artist : songArtists) {
            System.out.println("FROM VIEW - Artist name = " + artist.getArtistName() +
                    " Album name = " + artist.getAlbumName() +
                    " Track number = " + artist.getTrack());
        }

        datasource.insertSong("Bird Dog", "Everly Brothers", "All-Time Greatest Hits", 7);

        datasource.close();


    }
}

//Continuing on injection attacks
//We do have some protection against things
//like drop tables since we're using jdbc
//the jdbc sqlite driver, for example, the
//execute and execute query methods won't run
//more than a single sql statement so in other words
//a user can't take on something like a semicolon
//drop table songs
//But, if the user was using something like PHP
//and was running sql directly based on user input
//rather than going through an API
//then a malicious user would be able to
//execute whatever sql statements they wanted

//PREPARED STATEMENTS
//protect against SQL injection attacks