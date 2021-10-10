/**
 * PURPOSE: main activity to log in to an user account.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import curtin.edu.assignment01.R;
import curtin.edu.assignment01.model.NonIT;
import curtin.edu.assignment01.database.AdminDatabase;
import curtin.edu.assignment01.model.User;
import curtin.edu.assignment01.model.UserList;

public class LogInActivity extends AppCompatActivity
{
    //Declare variables:
    private AdminDatabase adminDb;
    private List<User> userList;
    private User logInUser = null;

    private Button signIn;
    private EditText inputUser;
    private EditText inputPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //load all the user databases for previous admin setup and stored data or initialize a fresh setup if none is stored.
        //load admin database.
        adminDb = new AdminDatabase(LogInActivity.this);
        adminDb.load();

        //if the there's no admin saved in database, fire sign-up activity.
        if(adminDb.getAdmin() == null)
        {
            startActivity(SignUpActivity.getIntent(LogInActivity.this));
        }

        //load the user list.
        userList = UserList.getInstance(LogInActivity.this).getUserList();

        //attach view to control variables.
        signIn = (Button) findViewById(R.id.signIn);
        inputUser = (EditText) findViewById(R.id.inputName);
        inputPIN = (EditText) findViewById(R.id.inputPIN);

        //When the SIGN IN button is pressed, logging in to user accordingly.
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputUserStr = inputUser.getText().toString();
                String inputPINStr = inputPIN.getText().toString();

                //find the user that has login credential matching the input username and PIN.
                if(!userList.isEmpty())
                {
                    for(User currUser: userList)
                    {
                        if(currUser.getUserName().equals(inputUserStr))
                        {
                            logInUser = currUser;
                            //early terminate loop to save resources.
                            break;
                        }
                    }
                }

                if(logInUser != null)
                {
                    if(inputPINStr.equals(logInUser.getPinNo()))
                    {
                        //check if the user log in is for an admin.
                        if(logInUser.verifyAdmin())
                        {
                            startActivity(NavigationActivity.getIntent(LogInActivity.this, "admin"));
                        }
                        //check if the user log in is for an instructor.
                        else if(((NonIT)logInUser).verifyInstructor())
                        {
                            startActivity(NavigationActivity.getIntent(LogInActivity.this, "instructor"));
                        }
                        //check if the user log in is for a student.
                        else
                        {
                            startActivity(ResultActivity.getIntent(LogInActivity.this, inputUserStr));
                        }

                        //clear the previous log-in user data.
                        inputUser.setText("");
                        inputPIN.setText("");

                        //recreate the activity to update the list of user accounts.
                        recreate();
                    }
                    else
                    {
                        Toast.makeText(LogInActivity.this, "Invalid username or password. Please retry!", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(LogInActivity.this, "Invalid username or password. Please retry!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}