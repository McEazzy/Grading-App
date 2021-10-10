/**
 * PURPOSE: initial sign-up account for the app, create Admin account.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import curtin.edu.assignment01.model.Admin;
import curtin.edu.assignment01.R;
import curtin.edu.assignment01.database.AdminDatabase;
import curtin.edu.assignment01.model.User;
import curtin.edu.assignment01.model.UserList;

public class SignUpActivity extends AppCompatActivity {
    //Declare variables.
    private EditText userName, digitOne, digitTwo, digitThree, digitFour, reDigitOne, reDigitTwo, reDigitThree, reDigitFour;
    private Button signUp;
    private LinearLayout pinLayout;

    private AdminDatabase adminDb;
    private List<User> userList;
    private Admin addedAdmin = null;

    //give access to this class activity intent.
    public static Intent getIntent(Context c)
    {
        return new Intent(c, SignUpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initialize or load the admin database.
        adminDb = new AdminDatabase(SignUpActivity.this);
        adminDb.load();

        //load the list of all user accounts.
        userList = UserList.getInstance(SignUpActivity.this).getUserList();

        userName = (EditText) findViewById(R.id.inputName);
        pinLayout = (LinearLayout) findViewById(R.id.part2);
        digitOne = (EditText) findViewById(R.id.firstDigit);
        digitTwo= (EditText) findViewById(R.id.secondDigit);
        digitThree = (EditText) findViewById(R.id.thirdDigit);
        digitFour = (EditText) findViewById(R.id.fourthDigit);
        reDigitOne = (EditText) findViewById(R.id.reFirstDigit);
        reDigitTwo= (EditText) findViewById(R.id.reSecondDigit);
        reDigitThree = (EditText) findViewById(R.id.reThirdDigit);
        reDigitFour = (EditText) findViewById(R.id.reFourthDigit);
        signUp = (Button) findViewById(R.id.signUp);

        //when SIGN UP button is pressed, respond accordingly.
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when the user've just only entered the username.
                if(pinLayout.getVisibility() == View.GONE) {
                    //username for admin setup is guaranteed to be unique.
                    pinLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(SignUpActivity.this, "The input admin username is unique. Proceed to password creation!", Toast.LENGTH_LONG).show();
                }
                //when the user've entered 2-factor PIN password setup.
                else
                {
                    String pinDigits = digitOne.getText().toString()
                            + digitTwo.getText().toString()
                            + digitThree.getText().toString()
                            + digitFour.getText().toString();
                    String rePinDigits = reDigitOne.getText().toString()
                            + reDigitTwo.getText().toString()
                            + reDigitThree.getText().toString()
                            + reDigitFour.getText().toString();
                    if((pinDigits.length() == 4) && (rePinDigits.length() == 4))
                    {
                        //check if the 2 input PIN matched.
                        if(pinDigits.equals(rePinDigits))
                        {
                            Admin admin = new Admin(userName.getText().toString(), pinDigits);
                            adminDb.add(admin);
                            //add to the existing list of user accounts.
                            userList.add(admin);
                            addedAdmin = admin;
                            Toast.makeText(SignUpActivity.this, "A new admin is successfully added to the application database.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, "PIN mismatched! Please reenter the passwords.", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, "PIN password doesn't meet the requirements! Please re-enter.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(addedAdmin == null)
        {
            Toast.makeText(SignUpActivity.this, "The admin wasn't successfully created! System won't advance until the admin is created!", Toast.LENGTH_LONG).show();
            recreate();
        }
        else
        {
            super.onBackPressed();
        }
    }
}