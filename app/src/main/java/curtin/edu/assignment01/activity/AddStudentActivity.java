/**
 * PURPOSE: add new students activity.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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

import curtin.edu.assignment01.R;
import curtin.edu.assignment01.database.StudentDatabase;
import curtin.edu.assignment01.fragment.CountrySelectorFragment;
import curtin.edu.assignment01.model.Country;
import curtin.edu.assignment01.model.Student;
import curtin.edu.assignment01.model.User;
import curtin.edu.assignment01.model.UserList;

public class AddStudentActivity extends AppCompatActivity
{
    //Declare variables:
    private TextView title;
    private CountrySelectorFragment fragC;
    private Button add, back;
    private LinearLayout changeLayout, layout7;
    private EditText fullName, email, userName,
            digitOne, digitTwo, digitThree, digitFour,
            reDigitOne, reDigitTwo, reDigitThree, reDigitFour;

    private Country selectedCountry;
    private String pinDigits, rePinDigits, userType;
    private StudentDatabase studentDb;
    private List<User> userList;

    //Return intent access to this activity.
    public static Intent getIntent(Context c, String userType)
    {
        Intent intent = new Intent(c, AddStudentActivity.class);
        intent.putExtra("user_type", userType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_it);

        //get the user type from log-in activity.
        userType = getIntent().getStringExtra("user_type");

        //load the list of all user accounts.
        userList = UserList.getInstance(AddStudentActivity.this).getUserList();

        //load the student database.
        studentDb = new StudentDatabase(AddStudentActivity.this);
        studentDb.load();

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
        title.setText("ADD STUDENT(S)");

        //link the country selector fragment to this activity.
        FragmentManager fm = getSupportFragmentManager();
        fragC = (CountrySelectorFragment) fm.findFragmentById(R.id.countrySelector);
        if (fragC == null)
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
                for (User currUser : userList) {
                    if (userName.getText().toString().equals(currUser.getUserName())) {
                        unique = false;
                        //early terminate loop to save resources.
                        break;
                    }
                }

                //Check if there's any empty field.
                if (!(fullName.getText().toString().equals(""))
                        && !(email.getText().toString().equals(""))
                        && !(userName.getText().toString().equals(""))
                        && (pinDigits.length() == 4)
                        && (rePinDigits.length() == 4)) {
                    if (unique) {
                        //Check if the 2 input PIN matched.
                        if (pinDigits.equals(rePinDigits)) {
                            //get the selected instructor's country of origin.
                            selectedCountry = fragC.getSelectedCountry();

                            //add the newly created student to the database.
                            Student student = new Student(userName.getText().toString(),
                                    pinDigits,
                                    fullName.getText().toString(),
                                    email.getText().toString(),
                                    selectedCountry.getLabel(),
                                    userType);
                            studentDb.add(student);
                            //add to the existing list of user accounts.
                            userList.add(student);
                            Toast.makeText(AddStudentActivity.this, "Successfully add a new student!", Toast.LENGTH_LONG).show();

                            //clear all the view fields for the next student creation.
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
                            Toast.makeText(AddStudentActivity.this, "PIN input mismatched. Please check your password again!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(AddStudentActivity.this, "The input username is identical to another user! Please choose a different username.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AddStudentActivity.this, "There is at least one empty field or an entry doesn't match the input requirement! Please check again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}