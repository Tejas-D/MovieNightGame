/*
*   File Name:      scoresActivity.java
*   Author:         Tejas Dwarkaram
*   Description:    Creating the class that will use the JSON data
*                   of the leaderboard information and display it
*                   to the user
*/


package tejasdwarkaram.movienightgame;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class scoresActivity extends ActionBarActivity {

    //creating new instances of all of the entities
    //that will need to be updated to display the entire leaderboard
    private ScoreParser obj;
    private String leaderURL;
    private String[] players;
    private String[] scores;
    private TextView firstPos;
    private TextView firstPlayer;
    private TextView firstScore;
    private TextView secPos;
    private TextView secPlayer;
    private TextView secScore;
    private TextView thirdPos;
    private TextView thirdPlayer;
    private TextView thirdScore;
    private TextView fourthPos;
    private TextView fourthPlayer;
    private TextView fourthScore;
    private TextView fifthPos;
    private TextView fifthPlayer;
    private TextView fifthScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        //assigning the variables to their existant entities on
        //the actual activity
        firstPos = (TextView) findViewById(R.id.firstPos);
        firstPlayer= (TextView) findViewById(R.id.firstPlayer);
        firstScore = (TextView) findViewById(R.id.firstScore);
        secPos = (TextView) findViewById(R.id.secondPos);
        secPlayer = (TextView) findViewById(R.id.secondPlayer);
        secScore = (TextView) findViewById(R.id.secondScore);
        thirdPos = (TextView) findViewById(R.id.thirdPos);
        thirdPlayer = (TextView) findViewById(R.id.thirdPlayer);
        thirdScore = (TextView) findViewById(R.id.thirdScore);
        fourthPos = (TextView) findViewById(R.id.fourthPos);
        fourthPlayer = (TextView) findViewById(R.id.fourthPlayer);
        fourthScore = (TextView) findViewById(R.id.fourthScore);
        fifthPos = (TextView) findViewById(R.id.fifthPos);
        fifthPlayer = (TextView) findViewById(R.id.fifthPlayer);
        fifthScore = (TextView) findViewById(R.id.fifthScore);

        leaderURL = "http://mobileclients.installprogram.eu/quiz/leaderboard.json";

        //sending the remote JSON URL to the Parser to fetch the string
        //array records that will have the information that was stored
        //on the JSON file
        getLeaderBoard();

        //invoking the method that will set all of the information from
        //the string arrays onto their respective entities on the activity
        setLeaderBoard();

    }

    //creating the method that will send the JSON url to be parsed
    private void getLeaderBoard() {
        obj = new ScoreParser(leaderURL);
        obj.fetchJSON();

        //getting the parsd information that was stored,
        //and storing it in new arrays
        while(obj.parsingComplete){
            players = obj.getPlayers();
            scores = obj.getScores();
        }
    }

    //creating the method that will set the values to
    //the entities on the activity
    public void setLeaderBoard(){

        //initializing arrays for all of the entities in order
        //place the values on each of them consistently

        TextView[] positions = new TextView[5];
        TextView[] scoring = new TextView[5];
        TextView[] play = new TextView[5];

        positions[0] = firstPos;
        positions[1] = secPos;
        positions[2] = thirdPos;
        positions[3] = fourthPos;
        positions[4] = fifthPos;

        scoring[0] = firstScore;
        scoring[1] = secScore;
        scoring[2] = thirdScore;
        scoring[3] = fourthScore;
        scoring[4] = fifthScore;

        play[0] = firstPlayer;
        play[1] = secPlayer;
        play[2] = thirdPlayer;
        play[3] = fourthPlayer;
        play[4] = fifthPlayer;

        for(int x = 0; x < 5; x++){
            positions[x].setText(String.valueOf(x + 1));
        }

        int playCount = 0;
        int scoreCount = 0;

        for(String playName : players){
            play[playCount].setText(playName);
            playCount++;
        }

        for(String scoreVal : scores){
            scoring[scoreCount].setText(scoreVal);
            scoreCount++;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scores, menu);
        return true;
    }

    public void restartGame(View view){
        Intent intent = new Intent(this, startActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
