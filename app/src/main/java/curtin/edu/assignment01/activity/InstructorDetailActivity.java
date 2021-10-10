/**
 * PURPOSE: edit or delete instructor info activity.
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import curtin.edu.assignment01.R;
import curtin.edu.assignment01.database.InstructorDatabase;
import curtin.edu.assignment01.fragment.CountrySelectorFragment;
import curtin.edu.assignment01.model.Country;
import curtin.edu.assignment01.model.CountryData;
import curtin.edu.assignment01.model.Instructor;
import curtin.edu.assignment01.model.User;
import curtin.edu.assignment01.model.UserList;

public class InstructorDetailActivity extends AppCompatActivity {
    //Declare variables
    private FragmentManager fm = getSupportFragmentManager();
    private CountrySelectorFragment fragC;
    private TextView title;
    private Button edit, delete, add, back;
    private EditText fullName, email, userName,
            digitOne, digitTwo, digitThree, digitFour,
            reDigitOne, reDigitTwo, reDigitThree, reDigitFour;

    private CountryData countryList = CountryData.getInstance();
    private List<User> userList;
    private Country selectedCountry;
    private InstructorDatabase insDb;
    private Instructor selectedInstructor;
    private String pinDigits, rePinDigits;

    //return access to this activity's intent
    public static Intent getIntent(Context c, String selectedInsName)
    {
        Intent intent = new Intent(c, InstructorDetailActivity.class);
        intent.putExtra("instructor_name", selectedInsName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_it);

        //load the list of all user accounts.
        userList = UserList.getInstance(InstructorDetailActivity.this).getUserList();

        //load instructor database.
        insDb = new InstructorDatabase(InstructorDetailActivity.this);
        insDb.load();

        //get the selected instructor.
        for(Instructor currIns: insDb.getInstructorList())
        {
            if (currIns.getUserName().equals(getIntent().getStringExtra("instructor_name"))) {
                selectedInstructor = currIns;
                //early terminate loop to save resources.
                break;
            }
        }

        //attach view to control variables.
        title = (TextView) findViewById(R.id.title);
        add = (Button) findViewById(R.id.add);
        back = (Button) findViewById(R.id.back);
        edit = (Button) findViewById(R.id.edit);
        delete = (Button) findViewById(R.id.delete);
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

        //set the UI view to the data of the selected instructor.
        add.setVisibility(View.GONE);
        title.setText("INSTRUCTOR DETAIL");
        fullName.setText(selectedInstructor.getName());
        email.setText(selectedInstructor.getEmail());
        userName.setText(selectedInstructor.getUserName());
        digitOne.setText(selectedInstructor.getPinNo().substring(0, 1));
        digitTwo.setText(selectedInstructor.getPinNo().substring(1, 2));
        digitThree.setText(selectedInstructor.getPinNo().substring(2, 3));
        digitFour.setText(selectedInstructor.getPinNo().substring(3, 4));

        //link the country list fragment.
        fragC = (CountrySelectorFragment) fm.findFragmentById(R.id.countrySelector);
        if(fragC == null)
        {
            fragC = new CountrySelectorFragment(countryList.getPosition(selectedInstructor.getCountry()));
            fm.beginTransaction().add(R.id.countrySelector, fragC).commit();
        }

        //to return to previous activity.
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean unique = true;

                //check if the username is unique.
                for (User currUser : userList) {
                    if (userName.getText().toString().equals(currUser.getUserName())) {
                        if (!userName.getText().toString().equals(selectedInstructor.getUserName())) {
                            unique = false;
                            //early terminate loop to save resources.
                            break;
                        }
                    }
                }

                //create 2 input PIN.
                pinDigits = digitOne.getText().toString()
                        + digitTwo.getText().toString()
                        + digitThree.getText().toString()
                        + digitFour.getText().toString();
                rePinDigits = reDigitOne.getText().toString()
                        + reDigitTwo.getText().toString()
                        + reDigitThree.getText().toString()
                        + reDigitFour.getText().toString();

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

                            //Edit the instructor in the database.
                            Instructor instructor = new Instructor(userName.getText().toString(),
                                    pinDigits,
                                    fullName.getText().toString(),
                                    email.getText().toString(),
                                    selectedCountry.getLabel());
                            insDb.edit(instructor, selectedInstructor.getUserName());

                            //Edit the instructor in list of users.
                            int editIndex = -1;
                            for (User currIns : userList) {
                                if (selectedInstructor.getUserName().equals(currIns.getUserName())) {
                                    editIndex = userList.indexOf(currIns);
                                    //terminate loop early, save resources.
                                    break;
                                }
                            }
                            userList.set(editIndex, instructor);
                            Toast.makeText(InstructorDetailActivity.this, "Successfully edit an instructor!", Toast.LENGTH_LONG).show();

                            //point the reference of the currently selected practical to the newly edited instructor.
                            selectedInstructor = instructor;
                        } else {
                            Toast.makeText(InstructorDetailActivity.this, "PIN input mismatched. Please check your password again!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(InstructorDetailActivity.this, "The input username is identical to another user! Please choose a different username.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(InstructorDetailActivity.this, "There is at least one empty field or an entry doesn't match the input requirement! Please check again!", Toast.LENGTH_LONG).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove the selected instructor from the database and the user account list.
                insDb.remove(selectedInstructor.getUserName());
                userList.remove(selectedInstructor);
                Toast.makeText(InstructorDetailActivity.this, "Successfully remove the instructor from the database", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}