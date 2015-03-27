/*
*   File Name:      MCQuestion.java
*   Author:         Tejas Dwarkaram
*   Description:    Creating the class that will be used as the JavaBean
*                   for getting and setting the Vector and other information
*                   pertaining to the questions of each category
*/

package tejasdwarkaram.movienightgame;

import java.util.ArrayList;
import java.util.List;

public class MCQuestion {

    private String question;
    private String image;
    private List<String> answers;
    private int correctAnswer = -1;

    public MCQuestion(String q, String i) {
        question = q;
        image = i;
        answers = new ArrayList<String>();
    }

    public void addAnswer(String s, boolean correct)
    {
        answers.add(s);
        if (correct)
            correctAnswer = answers.size();
    }

    public String getQuestion(){
        return question;
    }

    public String getImage(){
        return image;
    }

    public String getAnswer(int i)
    {
        return answers.get(i-1);
    }

    public String[] getAnswers()
    {
        return answers.toArray(new String[1]);
    }

    public boolean isCorrect( int i)
    {
        return i == correctAnswer;
    }

}
