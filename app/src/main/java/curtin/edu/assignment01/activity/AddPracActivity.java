/**
 * PURPOSE: create new practicals activity.
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
import android.widget.TextView;
import android.widget.Toast;

import curtin.edu.assignment01.R;
import curtin.edu.assignment01.database.PracticalDatabase;
import curtin.edu.assignment01.model.Practical;

public class AddPracActivity extends AppCompatActivity {
    //Declare variables:
    private LinearLayout layout0;
    private TextView title;
    private Button add, back;
    private EditText pracTitle, description, maxMark;
    private LinearLayout changeLayout;

    private PracticalDatabase pracDb;

    //Return intent access to this activity.
    public static Intent getIntent(Context c)
    {
        return new Intent(c, AddPracActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical);

        //load practical database.
        pracDb = new PracticalDatabase(AddPracActivity.this);
        pracDb.load();

        //attach view to control variables.
        title = (TextView) findViewById(R.id.title);
        add = (Button) findViewById(R.id.add);
        back = (Button) findViewById(R.id.back);
        changeLayout = (LinearLayout) findViewById(R.id.changeLayout);
        pracTitle = (EditText) findViewById(R.id.inputTitle);
        description = (EditText) findViewById(R.id.inputDescription);
        maxMark = (EditText) findViewById(R.id.inputMark);

        //set start-up view:
        changeLayout.setVisibility(View.GONE);
        title.setText("ADD PRACTICAL(S)");

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

                //Check if there's any empty field.
                if((!pracTitle.getText().toString().equals(""))
                    && (!description.getText().toString().equals(""))
                    && (!maxMark.getText().toString().equals("")))
                {
                    if(unique)
                    {
                        //add the newly created practical to the database.
                        pracDb.add(new Practical(pracTitle.getText().toString(), description.getText().toString(), Double.parseDouble(maxMark.getText().toString()), false));
                        Toast.makeText(AddPracActivity.this, "Successfully add a new practical!", Toast.LENGTH_LONG).show();

                        //clear all the view fields for the next practical creation.
                        pracTitle.setText("");
                        description.setText("");
                        maxMark.setText("");
                    }
                    else
                    {
                        Toast.makeText(AddPracActivity.this, "The practical title is identical to another practical! Please choose a different name.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(AddPracActivity.this, "There is at least one empty field! Please check again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}