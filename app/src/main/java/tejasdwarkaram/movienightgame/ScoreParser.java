/*
*   File Name:      ScoreParser.java
*   Author:         Tejas Dwarkaram
*   Description:    Creating the class that will define the
*                   process required to choose an avatar
*                   for the user, as well as check if a user
*                   name was entered or no
*/

package tejasdwarkaram.movienightgame;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScoreParser {

    //creating the variables that will be used to store
    //the information regarding the details of the people
    //that have made it onto the leaderboard

    private String player = "";
    private String score = "";
    private String urlString = null;
    private List<String> players;
    private List<String> scores;

    public volatile boolean parsingComplete = true;

    public ScoreParser(String url) {
        this.urlString = url;
        players = new ArrayList<String>();
        scores = new ArrayList<String>();
    }

    public void addPlayer(String s) {
        players.add(s);
    }

    public void addScore(String s) {
        scores.add(s);
    }

    public String[] getScores() {
        return scores.toArray(new String[1]);
    }

    public String[] getPlayers() {
        return players.toArray(new String[1]);
    }


    @SuppressLint("NewApi")
    public void readAndParseJSON(String in) {
        try {

            //opening a reader object to use to read in the JSON
            //attributes
            JSONObject reader = new JSONObject(in);

            //creating the JSON array object that will get the
            //information pertaining to the Leaderboard object
            JSONArray sys = reader.getJSONArray("leaderboard");

            for (int y = 0; y < 5; y++) {
                //getting the player names and scores
                //for each of the 5 members that are
                //on the leaderboard
                JSONObject main = sys.getJSONObject(y);

                score = main.getString("score");
                player = main.getString("player");

                //adding the information to the relevant String
                //arrays
                addScore(score);
                addPlayer(player);
            }
            parsingComplete = false;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //creating the method that will access the remote JSON file for parsing
    //purposes
    public void fetchJSON() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    String data = convertStreamToString(stream);

                    readAndParseJSON(data);
                    stream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
