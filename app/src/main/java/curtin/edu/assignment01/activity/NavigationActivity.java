/**
 * PURPOSE: activity to navigates which action to perform as a current user logged in.
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

import curtin.edu.assignment01.R;
import curtin.edu.assignment01.fragment.InstructorSelectorFragment;
import curtin.edu.assignment01.fragment.PracticalSelectorFragment;
import curtin.edu.assignment01.fragment.StudentSelectorFragment;

public class NavigationActivity extends AppCompatActivity {
    //Declare variables:
    private Button addIns, addPrac, addStudent, searchIns, searchStudent, marking, logOut;
    private LinearLayout insLayout, studentLayout, pracLayout;
    private InstructorSelectorFragment fragI;
    private StudentSelectorFragment fragS;
    private PracticalSelectorFragment fragP;
    private EditText inputSearchIns, inputSearchStudent;
    private FragmentManager fm = getSupportFragmentManager();

    private String userType;

    //return Intent object access.
    public static Intent getIntent(Context c, String userType)
    {
        Intent intent = new Intent(c, NavigationActivity.class);
        intent.putExtra("user_type", userType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //get the user type from log-in activity.
        userType = getIntent().getStringExtra("user_type");

        //attach view to control variables.
        insLayout = (LinearLayout) findViewById(R.id.instructorLayout);
        studentLayout = (LinearLayout) findViewById(R.id.studentLayout);
        pracLayout = (LinearLayout) findViewById(R.id.practicalLayout);
        marking = (Button) findViewById(R.id.marking);
        addIns = (Button) findViewById(R.id.addInstructor);
        searchIns = (Button) findViewById(R.id.searchInstructor);
        addStudent = (Button) findViewById(R.id.addStudent);
        searchStudent = (Button) findViewById(R.id.searchStudent);
        addPrac = (Button) findViewById(R.id.addPractical);
        logOut = (Button) findViewById(R.id.logOut);
        inputSearchIns = (EditText) findViewById(R.id.inputSearchInstructor);
        inputSearchStudent = (EditText) findViewById(R.id.inputSearchStudent);

        //admin log-in panel.
        if(userType.equals("admin"))
        {
            //link the instructor selector fragment to this activity.
            fragI = (InstructorSelectorFragment) fm.findFragmentById(R.id.instructorSelector);
            if(fragI == null)
            {
                fragI = new InstructorSelectorFragment();
                fm.beginTransaction().add(R.id.instructorSelector, fragI).commit();
            }

            searchIns.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //replace by the fragment selector view with filtered instructor list.
                    fragI = new InstructorSelectorFragment(inputSearchIns.getText().toString());
                    fm.beginTransaction().replace(R.id.instructorSelector, fragI).commit();
                }
            });

            //link the practical selector fragment to this activity.
            fragP = (PracticalSelectorFragment) fm.findFragmentById(R.id.practicalSelector);
            if(fragP == null)
            {
                fragP = new PracticalSelectorFragment();
                fm.beginTransaction().add(R.id.practicalSelector, fragP).commit();
            }
        }
        //instructor log-in panel.
        else
        {
            insLayout.setVisibility(View.GONE);
            pracLayout.setVisibility(View.GONE);
        }

        //link the initial student selector fragment to this activity.
        fragS = (StudentSelectorFragment) fm.findFragmentById(R.id.studentSelector);
        if(fragS == null)
        {
            fragS = new StudentSelectorFragment("", true, userType);
            fm.beginTransaction().add(R.id.studentSelector, fragS).commit();
        }

        //log out of the current session.
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //replace by the fragment selector view with filtered student list.
                fragS = new StudentSelectorFragment(inputSearchStudent.getText().toString(), true, userType);
                fm.beginTransaction().replace(R.id.studentSelector, fragS).commit();
            }
        });

        addIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddInstructorActivity.getIntent(NavigationActivity.this));
            }
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddStudentActivity.getIntent(NavigationActivity.this, userType));
            }
        });

        addPrac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddPracActivity.getIntent(NavigationActivity.this));
            }
        });

        marking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MarkingActivity.getIntent(NavigationActivity.this, userType));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //recreate the navigation panel with updated info and reset UI input.
        inputSearchIns.setText("");
        inputSearchStudent.setText("");

        //load updated database.
        //update instructor selector.
        fragI = new InstructorSelectorFragment();
        fm.beginTransaction().replace(R.id.instructorSelector, fragI).commit();

        //update student selector.
        fragS = new StudentSelectorFragment("", true, userType);
        fm.beginTransaction().replace(R.id.studentSelector, fragS).commit();

        //update practical selector.
        fragP = new PracticalSelectorFragment();
        fm.beginTransaction().replace(R.id.practicalSelector, fragP ).commit();
    }
}