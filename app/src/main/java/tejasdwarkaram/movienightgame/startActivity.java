/*
*   File Name:      startActivity.java
*   Author:         Tejas Dwarkaram
*   Description:    Creating the class that will define the
*                   process required to choose an avatar
*                   for the user, as well as check if a user
*                   name was entered or not
*
*
*/

package tejasdwarkaram.movienightgame;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


public class startActivity extends ActionBarActivity {

    //creating a message string that will contain the user name to be used
    //in the following activity when choosing categories
    public final static String EXTRA_MESSAGE = "tejasdwarkaram.movienightgame.MESSAGE";
    private static int RESULT_LOAD_IMAGE = 1;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        builder = new AlertDialog.Builder(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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


    //method that will be used to initiate the category activity
    public void startGame(View view) {
        //initializing the Intent class in order to change activities
        //when required
        Intent intent = new Intent(this, categoryActivity.class);
        EditText editText = (EditText) findViewById(R.id.nameTxtBox);
        String message = editText.getText().toString();
        //checking that the user did indeed enter a message
        Boolean checked = checkMessage(message);
        //when the user has entered a name, the name is stored in the Message
        //and is taken to the Category Activity
        if(checked){
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }

    //method to check whether the text is empty or not
    //the user is alerted of this, and will be asked to
    //re-enter their name
    public Boolean checkMessage(String message){
        if(message.equals("")){
            builder.setMessage("Please enter your name to continue")
                    .setCancelable(false)
                    .setPositiveButton("Continue", null);
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }else {
            return true;
        }
    }


    //creating the method to access the external storage media
    //in order for the user to use an image as their avatar
    public void changeImage(View view){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    //executing the method to fetch the image and display it on the ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            //placing the image in the ImageView that was created on the
            //activity
            ImageView imageView = (ImageView) findViewById(R.id.avatarImgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }


}
