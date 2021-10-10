/**
 * PURPOSE: fragment indicating a scrollable list of students.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import curtin.edu.assignment01.R;
import curtin.edu.assignment01.activity.StudentDetailActivity;
import curtin.edu.assignment01.database.StudentDatabase;
import curtin.edu.assignment01.model.CountryData;
import curtin.edu.assignment01.model.Student;

public class StudentSelectorFragment extends Fragment
{
    //Declare variables:
    private Student selectedStudent;
    private List<Student> studentList;
    private StudentDatabase studentDb;
    private CountryData countryList = CountryData.getInstance();
    private String filterName = "";
    private boolean showDetail = true;
    private String userType = "admin";

    private RecyclerView rv;
    private StudentAdapter adapter;
    private TextView emptyText;

    //Default Constructor:
    public StudentSelectorFragment()
    {
        // Required empty public constructor
    }

    //Alternative Constructor:
    public StudentSelectorFragment(String filterName, boolean showDetail, String userType)
    {
        this.filterName = filterName;
        this.showDetail = showDetail;
        this.userType = userType;
    }

    //return the selected instructor.
    public Student getSelectedStudent()
    {
        return selectedStudent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectedStudent = null;

        //load the database and the entire list of students.
        studentDb = new StudentDatabase(getActivity());
        studentDb.load();
        studentList = studentDb.getStudentList();

        //create a temporary list to store students accessible according to the log-in user.
        List<Student> tempList = new ArrayList<>();
        if(!userType.equals("admin"))
        {
            for(Student currStudent: studentList)
            {
                if(currStudent.getBy().equals(userType))
                {
                    tempList.add(currStudent);
                }
            }
            studentList = tempList;
        }

        //create a temporary array list of students to store students having name matched the filter search.
        tempList = new ArrayList<>();
        for(Student currStudent: studentList)
        {
            if(currStudent.getName().contains(filterName))
            {
                tempList.add(currStudent);
            }
        }
        studentList = tempList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_academic_selector, container, false);

        //when the list is empty, a text indicating none student found will appear instead.
        if(studentList.isEmpty())
        {
            emptyText = (TextView) view.findViewById(R.id.empty);
            emptyText.setVisibility(View.VISIBLE);
        }

        rv = view.findViewById(R.id.rvList);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        adapter = new StudentAdapter();
        rv.setAdapter(adapter);
        return view;
    }

    //indicates the current viewed entry in the recycler view.
    private class StudentViewHolder extends RecyclerView.ViewHolder
    {
        //Declare variables:
        private Student student;
        private TextView studentInfo;
        private ImageView countryImg;

        public StudentViewHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.academic_entry, parent, false));

            studentInfo = itemView.findViewById(R.id.academicText);
            countryImg = itemView.findViewById(R.id.countryImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedStudent = student;
                    //if showDetail is true then on-click entry will open student detail information.
                    if(showDetail)
                    {
                        startActivity(StudentDetailActivity.getIntent(getActivity(), selectedStudent.getUserName()));
                    }
                }
            });
        }

        //attach the source of each view's entry element.
        public void bind(Student student)
        {
            this.student = student;
            //update the UI.
            //check if the user has received any mark to show on the view-holder entry.
            Double aveMark = student.getAvePercentMark();
            if(aveMark != null) {
                //set the background color based on the average mark percentage.
                if(aveMark <= 50) {
                    itemView.setBackgroundColor(Color.parseColor("#FF4040"));
                }
                else if(aveMark <= 80)
                {
                    itemView.setBackgroundColor(Color.parseColor("#FFFF00"));
                }
                else
                {
                    itemView.setBackgroundColor(Color.parseColor("#00FF00"));
                }
                //set the name of current viewed entry with the average mark percentage.
                String studentInfoStr = student.getName() + " - CWA: " + aveMark.toString() + "%";
                studentInfo.setText(studentInfoStr);
            }
            else
            {
                studentInfo.setText(student.getName());
            }
            //set the country image for the viewed entry.
            countryImg.setImageResource(countryList.getCountry(student.getCountry()).getDrawableId());
        }
    }

    //adapter class to find the source of instructorList to consecutively bind to the recycled view of the view-holder.
    private class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder>
    {
        @NonNull
        @Override
        public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new StudentViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
            holder.bind(studentList.get(position));
        }

        @Override
        public int getItemCount() {
            return studentList.size();
        }
    }
}