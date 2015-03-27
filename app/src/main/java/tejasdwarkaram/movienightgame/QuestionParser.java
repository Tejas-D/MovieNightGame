/*
*   File Name:      QuestionParser.java
*   Author:         Tejas Dwarkaram
*   Description:    Creating the class that will do the
*                   parsing of the JSON data from the web
*                   server and send it to the questions
*                   activity
*
*
*/

package tejasdwarkaram.movienightgame;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Tejas on 2015/03/26.
 */
public class QuestionParser {

    //initializing the variables that will store the information
    //from the JSON file
    private String question = "Question";
    private String imageURL = "Image URL";
    private String answerIdx = "Answer IDX";
    private Vector<MCQuestion> answerVec;
    private String urlString = null;
    private int catID = 0;

    public volatile boolean parsingComplete = true;

    public QuestionParser(String url, int cat){
        this.urlString = url;
        this.catID = cat;
    }

    public String getQuestion(){
        return question;
    }

    public String getImageURL(){
        return imageURL;
    }

    public String getAnswerIdx(){
        return answerIdx;
    }

    public Vector getQuestions(){
        return answerVec;
    }

    @SuppressLint("NewApi")
    public void readAndParseJSON(String in) {
        try {

            //creating a new vector to store the questions with
            //the relevant answers
            answerVec = new Vector<MCQuestion>();

            //getting the JSON object
            JSONObject reader = new JSONObject(in);

            JSONArray sys = reader.getJSONArray("categories");

            //getting the Object that will choose which category to use
            //from within the JSON
            JSONObject main  = sys.getJSONObject(catID);

            //getting the array set of questions and their relevant
            //information based on the category that was chosen
            JSONArray ques = main.getJSONArray("questions");

            if(catID == 6) {
                for (int y = 0; y < 5; y++) {
                    //getting the object for each of the 5 question sets
                    //which contains the information regarding the questions
                    JSONObject all = ques.getJSONObject(y);

                    JSONArray ans = all.getJSONArray("answers");

                    imageURL = all.getString("imageURL");

                    if(y == 0){
                        answerIdx = "0";
                    }else {
                        answerIdx = all.getString("answerIdx");
                    }

                    //storing the question along with the answers within
                    //a Vector that will house all of the questions for that
                    //category
                    MCQuestion q1 = new MCQuestion(all.getString("question"), imageURL);
                    for (int z = 0; z < 3; z++) {
                        if (answerIdx == Integer.toString(z)) {
                            q1.addAnswer(ans.getString(z), true);
                        } else {
                            q1.addAnswer(ans.getString(z), false);
                        }
                    }
                    answerVec.add(q1);

                }
            }else{
                    for (int y = 0; y < 5; y++) {
                        //getting the object for each of the 5 question sets
                        //which contains the information regarding the questions
                        JSONObject all = ques.getJSONObject(y);

                        JSONArray ans = all.getJSONArray("answers");

                        imageURL = all.getString("imageURL");
                        answerIdx = all.getString("answerIdx");

                        //storing the question along with the answers within
                        //a Vector that will house all of the questions for that
                        //category
                        MCQuestion q1 = new MCQuestion(all.getString("question"), imageURL);
                        for (int z = 0; z < 3; z++) {
                            if (answerIdx == Integer.toString(z)) {
                                q1.addAnswer(ans.getString(z), true);
                            } else {
                                q1.addAnswer(ans.getString(z), false);
                            }
                        }
                        answerVec.add(q1);
                    }

            /*//getting the array set of questions and their relevant
            //information based on the category that was chosen
            JSONArray ques = main.getJSONArray("questions");

            for(int y = 0; y < 5; y++) {
                //getting the object for each of the 5 question sets
                //which contains the information regarding the questions
                JSONObject all = ques.getJSONObject(y);

                JSONArray ans = all.getJSONArray("answers");

                imageURL = all.getString("imageURL");
                answerIdx = all.getString("answerIdx");

                //storing the question along with the answers within
                //a Vector that will house all of the questions for that
                //category
                MCQuestion q1 = new MCQuestion(all.getString("question"), imageURL);
                for (int z = 0; z < 3; z++) {
                    if (answerIdx == Integer.toString(z)) {
                        q1.addAnswer(ans.getString(z), true);
                    } else {
                        q1.addAnswer(ans.getString(z), false);
                    }
                }
                answerVec.add(q1);*/

                }
                parsingComplete = false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //creating the method that will access the remote JSON file for parsing
    //purposes
    public void fetchJSON(){
        Thread thread = new Thread(new Runnable(){
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
