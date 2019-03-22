package com.company.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//In Music SQLite Database 2, we practice using indicies
//One thing to note about indicies is that when working with result
//sets, the index we pass to the ResultSet's getter method
//is the index of the column in the ResultSet, NOT IN THE TABLE

public class Datasource {
    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING
            = "jdbc:sqlite:C:\\Users\\14cjeong\\" + DB_NAME;
    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";
    //Indicies
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    //Indicies
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";
    //Indicies
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public static final String QUERY_ALBUMS_BY_ARTIST_START =
            "SELECT " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME +
                    " FROM " + TABLE_ALBUMS + " INNER JOIN " +
                    TABLE_ARTISTS + " ON " + TABLE_ALBUMS +
                    "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS +
                    "." + COLUMN_ARTIST_ID + " WHERE " +
                    TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME +
                    " = \"";

    public static final String QUERY_ALBUMS_BY_ARTIST_SORT =
            " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
                    " COLLATE NOCASE ";

    public static final String QUERY_ARTIST_FOR_SONG_START =
            "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " +
                    TABLE_SONGS + "." + COLUMN_SONG_TRACK + " FROM " + TABLE_SONGS +
                    " INNER JOIN " + TABLE_ALBUMS + " ON " +
                    TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
                    " WHERE " + TABLE_SONGS + "." + COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_ARTIST_FOR_SONG_SORT =
            " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";


  //Creating constants for creating a VIEW

    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";

  public static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " +
          TABLE_ARTIST_SONG_VIEW + " AS SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
          TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " AS " + COLUMN_SONG_ALBUM + ", " +
          TABLE_SONGS + "." + COLUMN_SONG_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE +
          " FROM " + TABLE_SONGS +
          " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS +
          "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
          " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
          " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
          " ORDER BY " +
          TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
          TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " +
          TABLE_SONGS + "." + COLUMN_SONG_TRACK;


   //Creating Constants for Querying a VIEW
    public static final String QUERY_VIEW_SONG_INFO =  "SELECT " + COLUMN_ARTIST_NAME + ", " +
            COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
            " WHERE " + COLUMN_SONG_TITLE + " = \"";


    //Creating Constants to create Prepared Statements
    // SELECT name, album, track FROM artist_list
    // WHERE title = ?
    public static final String QUERY_VIEW_SONG_INFO_PREP =
            "SELECT " + COLUMN_ARTIST_NAME + ", " +
                    COLUMN_SONG_ALBUM + ", " +
                    COLUMN_SONG_TRACK + " FROM " +
                    TABLE_ARTIST_SONG_VIEW +
                    " WHERE " + COLUMN_SONG_TITLE +
                    " = ?";
        //Notice the question mark
        //That's the placeholder that we use for
        //Prepared Statements
        //When we preform the query, we'll replace
        //the placeholder with the actual title
        //we want to query
    //also notice in line 101 that there are no quotation
    //marks around the question mark.
    //the database already understands that it's going to
    //be replaced with a value

    //Creating Constants for Transactions
    //Inserting Records with JDBC
    public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS +
            '(' + COLUMN_ARTIST_NAME + ") VALUES(?)";
    public static final String INSERT_ALBUMS = "INSERT INTO " + TABLE_ALBUMS +
            '(' + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST + ") VALUES(?, ?)";

    public static final String INSERT_SONGS = "INSERT INTO " + TABLE_SONGS +
            '(' + COLUMN_SONG_TRACK + ", " + COLUMN_SONG_TITLE + ", " + COLUMN_SONG_ALBUM +
            ") VALUES(?, ?, ?)";

    //Adding the constants for the statements we're going to
    //use to check whether an artist or an album already
    //exists in a given table (Still tied to our transactions)
    public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTIST_ID + " FROM " +
            TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";
        //SELECT is going to return the ID
        //But the WHERE clause is doing a search by name
        //So we get back the ID that we can use for the insertion,
        //if that already exists
        //if it doesn't exist in the method, then we're going to return
        // the new id AFTER the insert
        //that way, whether the records on file or not,
        //we're going to get the right ID
    public static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID + " FROM " +
            TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_NAME + " = ?";


    private Connection conn;
    //Creating the private instance variable for
    //Prepared Statement. We want to create a new
    //instance ONCE
    private PreparedStatement querySongInfoView;
    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertIntoSongs;

   //Created after we created constants in line 134 and line 144
    private PreparedStatement queryArtist;
    private PreparedStatement queryAlbum;


    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
           //adding Prepared Statement instance
            querySongInfoView =
                    conn.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
            insertIntoArtists = conn.prepareStatement(
                    INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS
            );
            insertIntoAlbums = conn.prepareStatement(INSERT_ALBUMS, Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs = conn.prepareStatement(INSERT_SONGS);

            queryArtist = conn.prepareStatement(QUERY_ARTIST);
            queryAlbum = conn.prepareStatement(QUERY_ALBUM);

            return true;
        } catch(SQLException e) {
            System.out.println("Couldn't connect to " +
                    "database: " + e.getMessage());
                    return false;
        }
    }

    public void close() {
        try {

            //Added the following for prepared statement
            //The order is important. We have to close
            //the prepared statement before we close the connection.
            if(querySongInfoView != null) {
                querySongInfoView.close();
            }

            if(insertIntoArtists != null) {
                insertIntoArtists.close();
            }

            if(insertIntoAlbums != null) {
                insertIntoAlbums.close();
            }

            if(insertIntoSongs !=  null) {
                insertIntoSongs.close();
            }

            if(queryArtist != null) {
                queryArtist.close();
            }

            if(queryAlbum != null) {
                queryAlbum.close();
            }

            //for more info on closing querySongInfoView go to line 450
            if(conn !=null) {
                conn.close();
            }
        } catch(SQLException e) {
            System.out.println("Couldn't close " +
                    "connection: " + e.getMessage());
        }
    }

    //Now, that we've created classes for artist, album,
    //and song. We can now create the a query method
    // for artist
   //UPDATED: for method to use column indicies
   //RETRIEVES LIST OF ARTISTS
    public List<Artist> queryArtists(int sortOrder) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);
        if(sortOrder !=ORDER_BY_NONE) {
            sb.append(" Order By ");
            sb.append(COLUMN_ALBUM_NAME);
            sb.append(" COLLATE NOCASE ");
            if(sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

       //Whether an exception generated or not
        //using try with resources automatically
        //calls a close method after the try block is finished.
        try (Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sb.toString())) {


            List<Artist> artists = new ArrayList<>();
            while(results.next()) {
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID));
                artist.setName(results.getString(INDEX_ARTIST_NAME)); //Overloaded method that accepts int
                artists.add(artist);
            }

            return artists;

        } catch(SQLException e) {
            System.out.println("Query failed: " +
                    e. getMessage());
                    return null;
        } /*finally {
            try {
                if(results !=null) {
                    results.close();
                }
            } catch(SQLException e) {
                System.out.println("Error closing ResultSet: " +
                        e.getMessage());
            }
            try {
                if(statement !=null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing Statement: " +
                        e.getMessage());
            }
        }*/
    }

    //RETRIEVES ALL THE ALBUMS OF A PARTICULAR ARTIST
    public List<String> queryAlbumsForArtist(String artistName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
        sb.append(artistName);
        sb.append("\"");

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
            if(sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        System.out.println("SQL statement = " +
                sb.toString());


        try (Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sb.toString())) {
            List<String> albums = new ArrayList<>();
            while(results.next()) {
                albums.add(results.getString(1));
            }
            return albums;
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<SongArtist> queryArtistsForSong(String songName, int sortOrder) {

        StringBuilder sb = new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
        sb.append(songName);
        sb.append("\"");

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ARTIST_FOR_SONG_SORT);
            if(sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        System.out.println("SQL Statement: " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<SongArtist> songArtists = new ArrayList<>();

            while(results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtists.add(songArtist);
            }

            return songArtists;
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //unfortunately, sqlite doesn't have good support for querying the database schema
    //now the connection class has a get metadata method that is supposed
    //to return information about the database tables
    //but when using this method with a sqlite dabase, unfortunately,
    //the returned object doesn't have any of its fieldsets. It's all null
    //
    //but we can query meta data using line 243
    public void querySongsMetadata() {
        String sql = "SELECT * FROM " + TABLE_SONGS;

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            ResultSetMetaData meta = results.getMetaData();
            int numColumns = meta.getColumnCount();
           //starts at one because columns start at 1, not 0
            for(int i=1; i<= numColumns; i++) {
                //notice the regular expressions here
                System.out.format("Column %d in the songs table is names %s\n",
                        i, meta.getColumnName(i));
            }
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }


    //Counts
    public int getCount(String table) {
        String sql = "SELECT COUNT(*) AS COUNT FROM " + table;
        try(Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sql)) {

            int count = results.getInt(1);

            System.out.format("Count = %d\n", count);
            return count;
        } catch(SQLException e) {
            System.out.println("Query failed: " +
                    e.getMessage());
                    return -1;
        }
    }

    //
    public boolean createViewForSongArtists() {

        try(Statement statement = conn.createStatement()) {
                //Notice the lack of the semi-colon in the resources statement.
                //Semi colons only for multiple statements
            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
            return true;

        } catch(SQLException e) {
            System.out.println("Create View failed: " + e.getMessage());
            return false;
        }
    }

    //HOW TO QUERY A VIEW.
    //(We can query a view just as we can query a table)
    //SELECT name, album, tack FROM artist_list WHERE title
    // = "title"
    //Notice how it's accepting String title as its parameter
    //It's replacing "title" in the query...obviously...
    public List<SongArtist> querySongInfoView(String title) {
       //Lines 351-355 commented out after making prepared statement
        /* StringBuilder sb = new StringBuilder(QUERY_VIEW_SONG_INFO);
        sb.append(title);
        sb.append("\"");

        System.out.println(sb.toString());*/

       //Updating the method for Prepared statement
       try {
           //setString is used because the method parameter is "String title"
          //also note that the prepared statement below is actually
           //a subclass of statement. So it has all the other methods
           //that statement has. Which is why we can use the executeQuery
           //in line 369.
           querySongInfoView.setString(1, title);
           //The 1 in the parameter index. (It will be replaced by title)
           //It is specifying the position of the placeholder we want to replace
           //In other words, in line 108, we're looking at the FIRST
           //occurrence of the question mark in the SQL code
           ResultSet results = querySongInfoView.executeQuery();
           List<SongArtist> songArtists = new ArrayList<>();
           while(results.next()) {
               SongArtist songArtist = new SongArtist();
               songArtist.setArtistName(results.getString(1));
               songArtist.setAlbumName(results.getString(2));
               songArtist.setTrack(results.getInt(3));
               songArtists.add(songArtist);
           }
           return songArtists;

       } //Catch statement applied to this below

       /* try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<SongArtist> songArtists = new ArrayList<>();
            while(results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtists.add(songArtist);
            }

            return songArtists;

        }*/ catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
        //adding the finally clause because we stopped using resource with try
       //after using the prepared statement. We have to close our database.

    }


    //Method for our transaction: InsertArtist
    private int insertArtist(String name) throws SQLException {

        queryArtist.setString(1, name);
        ResultSet results = queryArtist.executeQuery();
        if(results.next()) {
            return results.getInt(1);
        } else {
            // Insert the artist
            insertIntoArtists.setString(1, name);

            //We're not using an execute method here because the execute method returns a boolean
            //indicating what type of result the executed SQL statement returned
            //Here we used executeUpdate because that returns
            //the number of affectedRows
            //In this case, we're only changing (inserting) one row
            //which is why we are doing !=1 in line 496
            int affectedRows = insertIntoArtists.executeUpdate();

            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert artist!");
            }
            //This returns our ID
            ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
                //Key at position 1 because we only expect one key
                //to be returned
            } else {
                throw new SQLException("Couldn't get _id for artist");
            }
        }
    }

    //The second parameter here int artistID comes from
    //the previous method, which returns the artist ID
    private int insertAlbum(String name, int artistId) throws SQLException {

        queryAlbum.setString(1, name);
        ResultSet results = queryAlbum.executeQuery();
        if(results.next()) {
            return results.getInt(1);
            //means album is already on file
            //otherwise goes into else statement
            //amd will return the ID to the calling method
        } else {
            //Insert the album
            insertIntoAlbums.setString(1, name);
            insertIntoAlbums.setInt(2, artistId);
            int affectedRows = insertIntoAlbums.executeUpdate();

            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert album!");
            }

            ResultSet generatedKeys = insertIntoAlbums.getGeneratedKeys();
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for album");
            }
        }
    }

    //THE TRANSACTION CODE
    //Where the transaction comes into play.
    //The previous two methods and this third method
    //are conducted as a single unit.
    //The transaction part is in this method
    public void insertSong(String title, String artist, String album, int track) {

        try {
            //We must set this to false
            //Because IntelliJ automatically runs inserts, updates,
            //or deletes as transactions
            conn.setAutoCommit(false);
            //remember insertArtist is literally the method
            //we just wrote in line 478
            int artistId = insertArtist(artist);
           //Same thing as previous comment.

            int albumId = insertAlbum(album, artistId);
            insertIntoSongs.setInt(1, track);
            insertIntoSongs.setString(2, title);
            insertIntoSongs.setInt(3, albumId);
            int affectedRows = insertIntoSongs.executeUpdate();
            if(affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("The song insert failed");
            }
        //Important to catch all exceptions
        //Otherwise, it will bypass the SQLException
        //Run the finally block
        // set autoCommit to true
        //and insert whatever can be inserted
        //even if one of them has an error
        } catch(Exception e) {
            System.out.println("Insert song exception: " + e.getMessage());
            //ROLLBACK code
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch(SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
                //This is if the rollback itself doesn't work
                //THat's bad.
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
    }
}

//Note that in large enterprise applications
//a class might very well be created in the model package
//for each table, and the connections might be coming
//from the connection pool.
//But for small files like this, we're putting everything here.

//Note:
//we don't want to return a result set
//because we don't want classes that use methods in the
//mode package to have to understand the implementation
//details of the model. The reason is that we want
//to be able to switch to different types of DBs as
//smoothly as possible
//AS a result, we created separate classes for
//album, artist, and song

//Why it's more efficient to use a column index;
//when we use the column name instead, the method
//has to match the column name against the columns in the
//ResultSet. When you have thousands and thousands of records,
//having to match strings versus integers, makes
//a large time difference


//Also, about queries in a real world application
//we don't have to make eery possible query
//Rather, we should focus our queries based on what is present
//on the UI side. Whatever buttons are there, that's how we should
//perform our queries.
//now, unfortunately, there isn't a way to build elegant query strings
//when using JDBC. It would be nice to be able to create a query
//string by chaining methods together. Something like a
//.joined or .order by etc. and those things could possibly come from
//third party libraries.


//If we use more than one statement to do a query, which is common,
//the program will focus on the latest statement, updating from the
//previous ResultSet (by closing the previous one and creating the new one)
//so we don't have to worry about closing the result set when doing the
//prepared statement.
//When we close a prepared statemtent (in the main by doing datasource.close())
// , whichever ResultSet is active will
//also be closed


//Why the injection attack doesn't work with a prepared statement
//The values being substitued into the question mark are treated as literal
//values. In other words, none of the values are treated as SQL.
//You're basically only substituing a PREPARED SQL statement, whose SQL itself
//cannot be changed. I'm over-explaining at this point.
//Here's a demonstration of the difference
// NO Prepared Statement. Just using String Builder
// SELECT name, album, track From artist_list WHERE title = "Go Your Own Way" or 1=1 or ""
//The above changes the SQL (injects SQL)

//Using Prepared Statement.
//SELECT name, album, track FROM artist_list WHERE title = "Go Your Own Way or 1=1 or""
//There is no song with the name above.


//Other things for Prepared Statements
//We can only replace values, so no column names and such
//And of course, we can use them for insert, update, and delete

//RECAP
//HOW TO USE A PREPARED STATEMENT
//1) Declare a constant for th eSQL statement that contains the placeholders
//2) Create a PreparedStatement instance using Connection.prepareStatement(sqlStmtString)
//3) When we're ready to perform the query (or the insert, update, delete), we
//call the appropriate setter methods to set the placeholders to the values we
//want to use in the statement
//4) We run the statement using PrepareStatement.execute() or PreparedStatement.executeQuery()
//5) We process the results the same way we do when using a regular old statement

//ON TRANSACTIONS
//Transaction is a SEQUENCE of SQL statements that are treated as a single logical unit
//if any of them fail, the whole thing will roll back or not save
//Database Transactions must also be ACID compliant.
//They must meet the following characteristics:
//1) Atomicity - either all changes are committed or not
//2) Consistency - Before and after a transaction, the database
//must be in a valid state
//3) Isolation - Transactions cannot depend on each other
//Changes committed by a transaction are completed but won't
//be visible to other connections.
//4) Durability - Once transaction changes are applied. It is permanent.
//Bottom-line: Transactions ensure integrity of data within a database
//Now, transactions are only used for CHANGING THE DATA. Not for querying
//Also, remember that the JDBC Connection Class auto-commits changes by default (and can be turned off).

//TRANSACTIONS COMMANDS IN SQLite
//BEGIN TRANSACTION - manually start transaction
//END TRANSACTION or COMMIT - ends transaction...obv
//ROLLBACK - goes back to previos commit
//We call methods in the Connection class to execute
//transaction-related commands with the following steps:
//1) Turn off the default auto-commit behavior by calling Connection.setAutoCommit(false)
//2) Perform the SQL operations that form the transaction
//3) If there are no errors, call Connection.commit() to commit the changes.
//If there are errors, call Connection.rollback() to rollback any changes made since the transaction began.
//Turn the default auto-commit behavior back on by calling setAutoCommit(true)


//Sidenote: When speaking about databases, we use the word "Commit" not save











