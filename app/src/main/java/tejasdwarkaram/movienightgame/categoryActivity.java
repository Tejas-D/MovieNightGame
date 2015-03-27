/*
*   File Name:      categoryActivity.java
*   Author:         Tejas Dwarkaram
*   Description:    Creating the class that will be used to display
*                   the categories on an image that will be spun to
*                   choose a category and then redirected to the
*                   questions activity
*/


package tejasdwarkaram.movienightgame;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class categoryActivity extends ActionBarActivity {

    //creating a new instance of the Wheel class that will be used
    //to define the rotations of the image
    private categoryWheel wheelMenu;
    private TextView selectedPositionText;
    public final static String CAT_MESSAGE = "tejasdwarkaram.movienightgame.MESSAGE";
    int cat = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //creating a new intent instance in order to get
        //messages that were parsed through the activities
        Intent intent = getIntent();
        String userName = intent.getStringExtra(startActivity.EXTRA_MESSAGE);
        TextView nameTextView = (TextView) findViewById(R.id.nameTxt);
        nameTextView.setText(userName);

        wheelMenu = (categoryWheel) findViewById(R.id.categoryWheel);

        //set the no of divisions in the wheel, default is 1
        wheelMenu.setDivCount(6);

        //set the drawable to be used as the wheel image. If you
        //don't set this, you'll get a  NullPointerException.
        wheelMenu.setWheelImage(R.drawable.categories);

        wheelMenu.setSnapToCenterFlag(true);

        selectedPositionText = (TextView) findViewById(R.id.categoryName);
        selectedPositionText.setText("Spin the wheel!");

        //creating a listener for the Wheel so that the selected category
        //can be used to get the questions from the remote JSON file
        wheelMenu.setWheelChangeListener(new categoryWheel.WheelChangeListener() {
            @Override
            public void onSelectionChange(int selectedPosition) {
                int category = selectedPosition + 1;
                if(category == 1){
                    selectedPositionText.setText("Category: Comedy");
                    cat = selectedPosition;
                    startActivity();
                }else if(category == 2){
                    selectedPositionText.setText("Category: Action");
                    cat = selectedPosition;
                    startActivity();
                }else if(category == 3){
                    selectedPositionText.setText("Category: Horror");
                    cat = selectedPosition;
                    startActivity();
                }else if(category == 4){
                    selectedPositionText.setText("Category: Romance");
                    cat = selectedPosition;
                    startActivity();
                }else if(category == 5){
                    selectedPositionText.setText("Category: Sci-Fi");
                    cat = selectedPosition;
                    startActivity();
                }else if(category == 6){
                    selectedPositionText.setText("Category: Thriller");
                    cat = selectedPosition;
                    startActivity();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
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

    //once a category has been chose the user will be taken to the questions
    //for that category
    public void startActivity() {
        Intent intent = new Intent(this, questionActivity.class);
        intent.putExtra(CAT_MESSAGE, cat);
        startActivity(intent);
    }
}
