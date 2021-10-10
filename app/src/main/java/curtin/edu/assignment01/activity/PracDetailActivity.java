/**
 * PURPOSE: edit or delete practical info activity.
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

import curtin.edu.assignment01.R;
import curtin.edu.assignment01.database.PracticalDatabase;
import curtin.edu.assignment01.fragment.PracticalSelectorFragment;
import curtin.edu.assignment01.model.Practical;

public class PracDetailActivity extends AppCompatActivity {
    //Declare variables
    private TextView title;
    private FragmentManager fm = getSupportFragmentManager();
    private Button add, edit, delete, back;
    private EditText pracTitle, description, maxMark;
    private PracticalSelectorFragment fragP;

    private PracticalDatabase pracDb;
    private Practical selectedPrac;

    //Return intent access to this activity.
    public static Intent getIntent(Context c, String selectedPracName)
    {
        Intent intent = new Intent(c, PracDetailActivity.class);
        intent.putExtra("prac_name", selectedPracName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical);

        //load practical database.
        pracDb = new PracticalDatabase(PracDetailActivity.this);
        pracDb.load();

        //get the selected practical.
        for(Practical currPrac: pracDb.getPracList())
        {
            if(currPrac.getTitle().equals(getIntent().getStringExtra("prac_name")))
            {
                selectedPrac = currPrac;
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
        pracTitle = (EditText) findViewById(R.id.inputTitle);
        description = (EditText) findViewById(R.id.inputDescription);
        maxMark = (EditText) findViewById(R.id.inputMark);

        //set start-up view:
        title.setText("PRACTICAL DETAIL");
        add.setVisibility(View.GONE);

        //set the UI view to the data of the selected practical.
        pracTitle.setText(selectedPrac.getTitle());
        description.setText(selectedPrac.getDescription());
        maxMark.setText(String.valueOf(selectedPrac.getMaxMark()));

        //to return to previous activity.
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                boolean unique = true;
                //check if the practical title is unique.
                for(Practical currPrac: pracDb.getPracList())
                {
                    if(currPrac.getTitle().equals(pracTitle.getText().toString()))
                    {
                        unique = false;
                        //early terminate loop to save resources.
                        break;
                    }
                }

                //check if the practical has already been given to students and at least one has received his mark.
                if(!selectedPrac.getStartMarking())
                {
                    //Check if there's any empty field.
                    if((!pracTitle.getText().toString().equals(""))
                            && (!description.getText().toString().equals(""))
                            && (!maxMark.getText().toString().equals("")))
                    {
                        if(unique)
                        {
                            //Edit the practical in the database.
                            Practical prac = new Practical(pracTitle.getText().toString(), description.getText().toString(), Double.parseDouble(maxMark.getText().toString()), false);
                            pracDb.edit(prac, selectedPrac.getTitle());
                            Toast.makeText(PracDetailActivity.this, "Successfully edit a practical!", Toast.LENGTH_LONG).show();

                            //point the reference of the currently selected practical to the newly edited practical.
                            selectedPrac = prac;
                        }
                        else
                        {
                            Toast.makeText(PracDetailActivity.this, "The input title is identical to another practical! Please choose a different name.", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(PracDetailActivity.this, "There is at least one empty field! Please check again!", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(PracDetailActivity.this, "Practical has already been done and marked for students! Can't edit from database.", Toast.LENGTH_LONG).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selectedPrac.getStartMarking()) {
                    //remove the selected practical from the database.
                    pracDb.remove(selectedPrac.getTitle());
                    Toast.makeText(PracDetailActivity.this, "Successfully remove the practical from the database", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    Toast.makeText(PracDetailActivity.this, "Practical has already been done and marked for students! Can't delete from database.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}