/**
 * PURPOSE: edit or delete student information activity.
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
import curtin.edu.assignment01.database.StudentDatabase;
import curtin.edu.assignment01.fragment.CountrySelectorFragment;
import curtin.edu.assignment01.fragment.MarkBreakdownFragment;
import curtin.edu.assignment01.model.Country;
import curtin.edu.assignment01.model.CountryData;
import curtin.edu.assignment01.model.Student;
import curtin.edu.assignment01.model.User;
import curtin.edu.assignment01.model.UserList;

public class StudentDetailActivity extends AppCompatActivity {
    //Declare variables
    private TextView title;
    private FragmentManager fm = getSupportFragmentManager();
    private CountrySelectorFragment fragC;
    private MarkBreakdownFragment fragM;
    private Button add, edit, delete, back;
    private EditText inputSearch, fullName, email, userName,
            digitOne, digitTwo, digitThree, digitFour,
            reDigitOne, reDigitTwo, reDigitThree, reDigitFour;

    private CountryData countryList = CountryData.getInstance();
    private List<User> userList;
    private Country selectedCountry;
    private StudentDatabase studentDb;
    private Student selectedStudent;
    private String pinDigits, rePinDigits;

    //return access to this activity's intent
    public static Intent getIntent(Context c, String studentName)
    {
        Intent intent = new Intent(c, StudentDetailActivity.class);
        intent.putExtra("student_name", studentName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_it);

        //load the list of all user accounts.
        userList = UserList.getInstance(StudentDetailActivity.this).getUserList();

        //load Student database.
        studentDb = new StudentDatabase(StudentDetailActivity.this);
        studentDb.load();

        //get the selected Student.
        for(Student currStudent: studentDb.getStudentList())
        {
            if(currStudent.getUserName().equals(getIntent().getStringExtra("student_name")))
            {
                selectedStudent = currStudent;
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

        //set the UI view to the data of the selected student.
        add.setVisibility(View.GONE);
        title.setText("STUDENT DETAIL");
        fullName.setText(selectedStudent.getName());
        email.setText(selectedStudent.getEmail());
        userName.setText(selectedStudent.getUserName());
        digitOne.setText(selectedStudent.getPinNo().substring(0, 1));
        digitTwo.setText(selectedStudent.getPinNo().substring(1, 2));
        digitThree.setText(selectedStudent.getPinNo().substring(2, 3));
        digitFour.setText(selectedStudent.getPinNo().substring(3, 4));

        //pass in the current country entry position set for the student to the fragment.
        fragC = (CountrySelectorFragment) fm.findFragmentById(R.id.countrySelector);
        if(fragC == null)
        {
            fragC = new CountrySelectorFragment(countryList.getPosition(selectedStudent.getCountry()));
            fm.beginTransaction().add(R.id.countrySelector, fragC).commit();
        }

        //link the list of mark breakdown of each practical graded for this student.
        fragM = (MarkBreakdownFragment) fm.findFragmentById(R.id.markList);
        if(fragM == null)
        {
            fragM = new MarkBreakdownFragment(selectedStudent.getPracMap());
            fm.beginTransaction().add(R.id.markList, fragM).commit();
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
                        if(!userName.getText().toString().equals(selectedStudent.getUserName()))
                        {
                            unique = false;
                            //early terminate loop to save resources.
                            break;
                        }
                    }
                }

                //Check if there's any empty field.
                if(!(fullName.getText().toString().equals(""))
                        && !(email.getText().toString().equals(""))
                        && !(userName.getText().toString().equals(""))
                        && (pinDigits.length() == 4)
                        && (rePinDigits.length() == 4))
                {
                    if(unique)
                    {
                        //Check if the 2 input PIN matched.
                        if (pinDigits.equals(rePinDigits))
                        {
                            //get the selected student's country of origin.
                            selectedCountry = fragC.getSelectedCountry();

                            //Edit the student in the database.
                            Student student = new Student(userName.getText().toString(),
                                    pinDigits,
                                    fullName.getText().toString(),
                                    email.getText().toString(),
                                    selectedCountry.getLabel(),
                                    selectedStudent.getBy());
                            student.setMarkedPrac(selectedStudent.getPracMap());
                            studentDb.edit(student, selectedStudent.getUserName());

                            //Edit the student in list of users.
                            int editIndex = -1;
                            for(User currIns: userList)
                            {
                                if(selectedStudent.getUserName().equals(currIns.getUserName()))
                                {
                                    editIndex = userList.indexOf(currIns);
                                    //terminate loop early, save resources.
                                    break;
                                }
                            }
                            userList.set(editIndex, student);
                            Toast.makeText(StudentDetailActivity.this, "Successfully edit a student!", Toast.LENGTH_LONG).show();

                            //point the reference of the currently selected practical to the newly edited student.
                            selectedStudent = student;
                        }
                        else
                        {
                            Toast.makeText(StudentDetailActivity.this, "PIN input mismatched. Please check your password again!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(StudentDetailActivity.this, "The input username is identical to another user! Please choose a different username.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(StudentDetailActivity.this, "There is at least one empty field or an entry doesn't match the input requirement! Please check again!", Toast.LENGTH_LONG).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove the student from the database and the user account list.
                studentDb.remove(selectedStudent.getUserName());
                userList.remove(selectedStudent);
                Toast.makeText(StudentDetailActivity.this, "Successfully remove the student from the database", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}