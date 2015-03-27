/*
*   File Name:      questionActivity.java
*   Author:         Tejas Dwarkaram
*   Description:    Creating the activity that will display all
*                   of the questions that are retrieved from the
*                   JSON file
*/


package tejasdwarkaram.movienightgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;


public class questionActivity extends ActionBarActivity {

    //initializing variables that will be used to create
    //the instances in order to display the required
    //values on the activity
    ImageView thisView;
    public static String TOTAL_SCORE = "tejasdwarkaram.movienightgame.MESSAGE";
    private TextView newQuestion;
    private TextView correct;
    private Vector<MCQuestion> questions;
    private Button but1;
    private Button but2;
    private Button but3;
    private MCQuestion currentQuestion;
    private int noCorrect;
    private int noAnswered;
    private int noQuestions = 5;
    private String imageURL = "";
    private int category = 0;
    private String questionURL = "";
    private TextView yourAnswer;
    private CountDownTimer countDownTimer;
    private boolean timerStarted = false;
    private final long interval = 1 * 1000;
    private final long startTime = 10 * 1000;
    private TextView time;
    private AlertDialog.Builder builder;
    private QuestionParser obj;
    private TextView keepScore;
    int id = 0;
    String correctAnswer = "";
    private double totalScore = 0.0;
    private TextView round;
    private int roundNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //initializing a new intent to get values from the previous
        //activity
        Intent intent = getIntent();
        category = intent.getIntExtra(categoryActivity.CAT_MESSAGE, 0);
        //totalScore = intent.getStringExtra(questionActivity.TOTAL_SCORE);
        //creating a new Alert class that will be used to inform the user
        //of whether their answers are correct or incorrect
        builder = new AlertDialog.Builder(this);
        questionURL = "http://mobileclients.installprogram.eu/quiz/trivia.json";
        newQuestion = (TextView) findViewById(R.id.questionLbl);
        correct = (TextView) findViewById(R.id.correctTxt);
        yourAnswer = (TextView) findViewById(R.id.answerTxt);
        but1 = (Button) findViewById(R.id.zero);
        but2 = (Button) findViewById(R.id.one);
        but3 = (Button) findViewById(R.id.two);
        time = (TextView) findViewById(R.id.timeTxt);
        thisView = (ImageView) findViewById(R.id.questionImgView);
        countDownTimer = new Timer(startTime, interval);
        time.setText(time.getText() + String.valueOf(startTime / 1000));
        keepScore = (TextView) findViewById(R.id.scoreKeep);
        round = (TextView) findViewById(R.id.roundLbl);
        noCorrect = 0;
        noAnswered = 0;
        //executing the method to get the questions and store them in a new
        //vector
        getQuestions();

        //creating the first question of the Vector to display
        createQuestion(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
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


    //creating the method that will send the url to the Parser class
    //and retrieve the vector that contains the questions and their answers
    private void getQuestions() {
        obj = new QuestionParser(questionURL, category);
        obj.fetchJSON();

        while(obj.parsingComplete){
            questions = obj.getQuestions();
        }
    }

    //creating a method to create each question each time time the previous
    //one is completed
    private void createQuestion(int noQ) {
        yourAnswer.setText("Your Answer");
        currentQuestion = questions.get(noQ);
        newQuestion.setText(currentQuestion.getQuestion());
        imageURL = currentQuestion.getImage();
        int i = 0;

        //creating an array of the answer buttons that will be used
        //to systematically place assign the answers to each one
        Button[] buts = new Button[3];
        buts[0] = but1;
        buts[1] = but2;
        buts[2] = but3;
        for( String ans: currentQuestion.getAnswers()) {
            buts[i].setText(ans);
            i++;
        }

        //executing the method to get the Image from the specified
        //URL and assign it to the Image View on the Activity
        GetXMLTask task = new GetXMLTask();
        // Execute the task
        task.execute(new String[] { imageURL });

        round.setText("Round " + (noQ + 1));

        //start the Count Down Timer
        countDownTimer.start();
    }

    /*public void nextQuestion(View arg0){


            int id = 0;
            String correctAnswer = "";
            switch(arg0.getId()){
                case R.id.zero:
                    id = 0;
                    yourAnswer.setText("A");
                    correctAnswer = but1.getText().toString();
                case R.id.one:
                    id = 1;
                    yourAnswer.setText("B");
                    correctAnswer = but2.getText().toString();
                case R.id.two:
                    id = 2;
                    yourAnswer.setText("C");
                    correctAnswer = but3.getText().toString();
            }
            noAnswered++;

            isCorrect(id);

            //execucted when all the questions are completed, the activity is redirected to the Quiz
            //Activity
            /*if (noAnswered == noQuestions)
            {
                Intent intent = new Intent(this, ScoresActivity.class);
                startActivity(intent);
            }
            else {
                createQuestion(noAnswered);
            }
    }*/

    //creating the method for Answer A
    public void clickA(View view){
        id = 0;
        yourAnswer.setText("A");
        correctAnswer = but1.getText().toString();
        noAnswered++;
        //checking if this answer is correct
        isCorrectAnswer(id, correctAnswer);
    }

    //creating the method for Answer B
    public void clickB(View view){
        id = 1;
        yourAnswer.setText("B");
        correctAnswer = but2.getText().toString();
        noAnswered++;
        //creating the method for Answer A
        isCorrectAnswer(id, correctAnswer);
    }

    //creating the method for Answer C
    public void clickC(View view){
        id = 2;
        yourAnswer.setText("C");
        correctAnswer = but3.getText().toString();
        noAnswered++;
        //creating the method for Answer A
        isCorrectAnswer(id, correctAnswer);
    }

    //creating the method to check if the answer that was provided is correct or not
    public void isCorrectAnswer(int id, String correctAnswer){
        if(id == 10){
            //increasing the number answered so that the next question is created
            noAnswered++;
            correct.setText("Times Up!");
            //displaying an alert to inform the user that their time has run out
            builder.setMessage("Sorry your time is up!")
                    .setCancelable(false)
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startQuestion();
                        }
                    });
        }else if(currentQuestion.isCorrect(id)) {
            noCorrect++;
            correct.setText("Correct");
            builder.setMessage("That's Correct! Well Done :)")
                    .setCancelable(false)
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startQuestion();
                        }
                    });
            calculateScore(Integer.valueOf(time.getText().toString()));
        }else{
            correct.setText("Incorrect");
            builder.setMessage("That's incorrect!")
                    .setCancelable(false)
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startQuestion();
                        }
                    });
        }
        //creating a new Alert instance
        AlertDialog alert = builder.create();
        //displaying the alert to the user
        alert.show();
    }

    //creating a method to check if the a new question must be
    //created or if all of the questions have been answered
    public void startQuestion() {
        if (noAnswered == noQuestions) {
            Intent intent = new Intent(this, scoresActivity.class);
            startActivity(intent);
        } else {
            createQuestion(noAnswered);
            keepScore.setText(String.valueOf(totalScore));
        }
    }

    //creating a method to check the time taken to answer the question
    //and increasing the score along with an assigned multiplier
    public void calculateScore(int timeTaken){
        if(timeTaken > 9){
            totalScore = totalScore + (100 * 1.5);
        }else if(timeTaken > 8){
            totalScore = totalScore + (100 * 1.4);
        }else if(timeTaken > 7){
            totalScore = totalScore + (100 * 1.3);
        }else if(timeTaken > 6){
            totalScore = totalScore + (100 * 1.2);
        }else if(timeTaken > 5){
            totalScore = totalScore + (100 * 1.0);
        }else if(timeTaken > 2){
            totalScore = totalScore + (100 * 0.5);
        }else{
            totalScore = totalScore + (100 * 0.25);
        }
    }



    //creating the class that will be used to fetch the Image from the URL
    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground and setting it
        //on the ImageView
        @Override
        protected void onPostExecute(Bitmap result) {
            thisView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            thisView.setScaleType(ImageView.ScaleType.FIT_XY);
            //thisView.setMaxHeight(317);
            //thisView.setMaxWidth(214);
            thisView.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }

    //creates the Countdown timer class that will be used to time the
    //time that the user has to answer each question
    public class Timer extends CountDownTimer {

        public Timer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            time.setText("Time's up!");
            isCorrectAnswer(10,"T");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            time.setText("" + millisUntilFinished / 1000);
        }

    }
}
