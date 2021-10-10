/**
 * PURPOSE: create new instructors activity.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import curtin.edu.assignment01.R;
import curtin.edu.assignment01.database.InstructorDatabase;
import curtin.edu.assignment01.fragment.CountrySelectorFragment;
import curtin.edu.assignment01.model.Country;
import curtin.edu.assignment01.model.Instructor;
import curtin.edu.assignment01.model.User;
import curtin.edu.assignment01.model.UserList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AddInstructorActivity extends AppCompatActivity {
    //Declare variables:
    private TextView title;
    private CountrySelectorFragment fragC;
    private Button add, back;
    private LinearLayout changeLayout, layout7;
    private EditText fullName, email, userName,
            digitOne, digitTwo, digitThree, digitFour,
            reDigitOne, reDigitTwo, reDigitThree, reDigitFour;

    private Country selectedCountry;
    private String pinDigits, rePinDigits;
    private InstructorDatabase insDb;
    private List<User> userList;

    //Return intent access to this activity.
    public static Intent getIntent(Context c)
    {
        return new Intent(c, AddInstructorActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_it);

        //load instructor database.
        insDb = new InstructorDatabase(AddInstructorActivity.this);
        insDb.load();

        //load the list of all user accounts.
        userList = UserList.getInstance(AddInstructorActivity.this).getUserList();

        //attach view to control variables.
        title = (TextView) findViewById(R.id.title);
        add = (Button) findViewById(R.id.add);
        back = (Button) findViewById(R.id.back);
        changeLayout = (LinearLayout) findViewById(R.id.changeLayout);
        layout7 = (LinearLayout) findViewById(R.id.layout7);
        fullName = (EditText) findViewById(R.id.inputFullName);
        email = (EditText) findViewById(R.id.inputEmail);
        userName = (EditText) findViewById(R.id.inputUserName);
        digitOne = (EditText) findViewById(R.id.firstDigit);
        digitTwo= (EditText) findViewById(R.id.secondDigit);
        digitThree = (EditText) findViewById(R.id.thirdDigit);
        digitFour = (EditText) findViewById(R.id.fourthDigit);
        reDigitOne = (EditText) findViewById(R.id.reFirstDigit);
        reDigitTwo= (EditText) findViewById(R.id.reSecondDigit);
        reDigitThree = (EditText) findViewById(R.id.reThirdDigit);
        reDigitFour = (EditText) findViewById(R.id.reFourthDigit);

        //set start-up view:
        changeLayout.setVisibility(View.GONE);
        layout7.setVisibility(View.GONE);
        title.setText("ADD INSTRUCTOR(S)");

        //link the country selector fragment to this activity.
        FragmentManager fm = getSupportFragmentManager();
        fragC = (CountrySelectorFragment) fm.findFragmentById(R.id.countrySelector);
        if(fragC == null)
        {
            fragC = new CountrySelectorFragment();
            fm.beginTransaction().add(R.id.countrySelector, fragC).commit();
        }

        //to return to previous activity.
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean unique = true;
                //create 2 input PIN.
                pinDigits = digitOne.getText().toString()
                        + digitTwo.getText().toString()
                        + digitThree.getText().toString()
                        + digitFour.getText().toString();
                rePinDigits = reDigitOne.getText().toString()
                        + reDigitTwo.getText().toString()
                        + reDigitThree.getText().toString()
                        + reDigitFour.getText().toString();

                //check if the username is unique.
                for(User currUser: userList)
                {
                    if(userName.getText().toString().equals(currUser.getUserName()))
                    {
                        unique = false;
                        //early terminate loop to proceed.
                        break;
                    }
                }

                //Check if there's any empty field.
                if(!(fullName.getText().toString().equals(""))
                        && !(email.getText().toString().equals(""))
                        && !(userName.getText().toString().equals(""))
                        && (pinDigits.length() == 4)
                        && (rePinDigits.length() == 4))
                {
                    if (unique) {
                        //Check if the 2 input PIN matched.
                        if (pinDigits.equals(rePinDigits)) {
                            //get the selected instructor's country of origin.
                            selectedCountry = fragC.getSelectedCountry();

                            //add the newly created instructor to the database.
                            Instructor instructor = new Instructor(userName.getText().toString(),
                                    pinDigits,
                                    fullName.getText().toString(),
                                    email.getText().toString(),
                                    selectedCountry.getLabel());
                            insDb.add(instructor);
                            //add to the existing list of user accounts.
                            userList.add(instructor);
                            Toast.makeText(AddInstructorActivity.this, "Successfully add a new instructor!", Toast.LENGTH_LONG).show();

                            //clear all the view fields for the next instructor creation.
                            fullName.setText("");
                            email.setText("");
                            userName.setText("");
                            digitOne.setText("");
                            digitTwo.setText("");
                            digitThree.setText("");
                            digitFour.setText("");
                            reDigitOne.setText("");
                            reDigitTwo.setText("");
                            reDigitThree.setText("");
                            reDigitFour.setText("");
                        } else {
                            Toast.makeText(AddInstructorActivity.this, "PIN input mismatched. Please check your password again!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(AddInstructorActivity.this, "The input username is identical to another user! Please choose a different username.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(AddInstructorActivity.this, "There is at least one empty field or an entry doesn't match the input requirement! Please check again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}