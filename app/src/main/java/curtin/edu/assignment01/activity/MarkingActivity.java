/**
 * PURPOSE: activity to mark students on practicals.
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

import curtin.edu.assignment01.R;
import curtin.edu.assignment01.database.PracticalDatabase;
import curtin.edu.assignment01.database.StudentDatabase;
import curtin.edu.assignment01.fragment.PracticalSelectorFragment;
import curtin.edu.assignment01.fragment.StudentSelectorFragment;
import curtin.edu.assignment01.model.Practical;
import curtin.edu.assignment01.model.Student;

public class MarkingActivity extends AppCompatActivity {
    //Declare variables:
    private TextView maxMark;
    private Button set, confirm, back;
    private FragmentManager fm;
    private StudentSelectorFragment fragS;
    private PracticalSelectorFragment fragP;
    private LinearLayout layout2, layout3;
    private EditText mark;

    private StudentDatabase studentDb;
    private PracticalDatabase pracDb;
    private Student selectedStudent;
    private Practical selectedPrac;
    private String userType;

    //Return intent access to this activity.
    public static Intent getIntent(Context c, String userType)
    {
        Intent intent = new Intent(c, MarkingActivity.class);
        intent.putExtra("user_type", userType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marking);

        //get the log-in user type.
        userType = getIntent().getStringExtra("user_type");

        //load the student database.
        studentDb = new StudentDatabase(MarkingActivity.this);
        studentDb.load();

        //load practical database.
        pracDb = new PracticalDatabase(MarkingActivity.this);
        pracDb.load();

        //attach view to control variables.
        set = (Button) findViewById(R.id.set);
        confirm = (Button) findViewById(R.id.confirm);
        back = (Button) findViewById(R.id.back);
        maxMark = (TextView) findViewById(R.id.maxMark);
        mark = (EditText) findViewById(R.id.mark);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);

        //set-up view.
        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);

        //link the student selector fragment to this activity.
        fm = getSupportFragmentManager();
        fragS = (StudentSelectorFragment) fm.findFragmentById(R.id.studentSelector);
        if(fragS == null)
        {
            fragS = new StudentSelectorFragment("", false, userType);
            fm.beginTransaction().add(R.id.studentSelector, fragS).commit();
        }

        //link the practical selector fragment to this activity.
        fragP = (PracticalSelectorFragment) fm.findFragmentById(R.id.pracSelector);
        if(fragP == null)
        {
            fragP = new PracticalSelectorFragment(false);
            fm.beginTransaction().add(R.id.pracSelector, fragP).commit();
        }

        //to return to previous activity.
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //get the selected entry from the 2 selectors.
                selectedStudent = fragS.getSelectedStudent();
                selectedPrac = fragP.getSelectedPrac();

                if(selectedStudent != null)
                {
                    if(selectedPrac != null)
                    {
                        //if none practical is marked before yet then set layouts visible.
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.VISIBLE);
                        confirm.setVisibility(View.VISIBLE);

                        //show the full mark of the given practical.
                        maxMark.setText(String.valueOf(selectedPrac.getMaxMark()));
                    }
                    else
                    {
                        Toast.makeText(MarkingActivity.this, "No practical has been selected for marking.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(MarkingActivity.this, "No student has been selected for marking.", Toast.LENGTH_LONG).show();
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //set the counter checking whether the practical has been graded for at least one student to true.
                selectedPrac.setStartMarking(true);
                //then set it into the database.
                pracDb.edit(selectedPrac, selectedPrac.getTitle());

                //add mark into the student database.
                String result = selectedStudent.addMark(selectedPrac.getTitle(), Double.parseDouble(mark.getText().toString()), Double.parseDouble(maxMark.getText().toString()));
                studentDb.edit(selectedStudent, selectedStudent.getUserName());

                if(result == null)
                {
                    Toast.makeText(MarkingActivity.this, "The student has been graded for the chosen practical!", Toast.LENGTH_LONG).show();

                    //clear the UI view after adding a student's grade.
                    maxMark.setText("");
                    mark.setText("");
                }
                else
                {
                    Toast.makeText(MarkingActivity.this, result, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}