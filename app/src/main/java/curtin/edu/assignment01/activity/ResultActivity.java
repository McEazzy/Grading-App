/**
 * PURPOSE: when a student logs in, a result activity will indicates his result.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import curtin.edu.assignment01.R;
import curtin.edu.assignment01.database.StudentDatabase;
import curtin.edu.assignment01.fragment.MarkBreakdownFragment;
import curtin.edu.assignment01.model.CountryData;
import curtin.edu.assignment01.model.Student;

public class ResultActivity extends AppCompatActivity {
    //Declare variables:
    private Button logOut;
    private TextView studentInfo;
    private ImageView studentCountry;
    private FragmentManager fm = getSupportFragmentManager();
    private MarkBreakdownFragment fragM;
    private LinearLayout layout0;

    private StudentDatabase studentDb;
    private Student selectedStudent;
    private CountryData countryList = CountryData.getInstance();

    //Return access to this activity's intent.
    public static Intent getIntent(Context c, String userName)
    {
        Intent intent = new Intent(c, ResultActivity.class);
        intent.putExtra("student_user", userName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //load the student database.
        studentDb = new StudentDatabase(ResultActivity.this);
        studentDb.load();

        //get the logged-in student.
        for(Student currStudent: studentDb.getStudentList())
        {
            if(currStudent.getUserName().equals(getIntent().getStringExtra("student_user")))
            {
                selectedStudent = currStudent;
                //early terminate loop to save resources.
                break;
            }
        }

        //attach UI view to control variables.
        logOut = (Button) findViewById(R.id.logOut);
        studentInfo = (TextView) findViewById(R.id.studentInfo);
        studentCountry = (ImageView) findViewById(R.id.studentCountry);
        layout0 = (LinearLayout) findViewById(R.id.layout0);

        //setup initial view:
        //check if the user has received any marks yet.
        String studentInfoStr;
        Double aveMark = selectedStudent.getAvePercentMark();
        if(aveMark != null)
        {
            //set the background color based on the average mark percentage.
            if(aveMark <= 50) {
                layout0.setBackgroundColor(Color.parseColor("#FF4040"));
            }
            else if(aveMark <= 80)
            {
                layout0.setBackgroundColor(Color.parseColor("#FFFF00"));
            }
            else
            {
                layout0.setBackgroundColor(Color.parseColor("#00FF00"));
            }
            studentInfoStr = "Student: " + selectedStudent.getName() + " - CWA: " + aveMark.toString() + "%";
        }
        else
        {
            studentInfoStr = "Student: " + selectedStudent.getName();
        }
        studentInfo.setText(studentInfoStr);
        studentCountry.setImageResource(countryList.getCountry(selectedStudent.getCountry()).getDrawableId());

        //link the mark breakdown fragment to this activity.
        fragM = (MarkBreakdownFragment) fm.findFragmentById(R.id.markList);
        if(fragM == null)
        {
            fragM = new MarkBreakdownFragment(selectedStudent.getPracMap());
            fm.beginTransaction().add(R.id.markList, fragM).commit();
        }

        //log out of the current user.
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }
}