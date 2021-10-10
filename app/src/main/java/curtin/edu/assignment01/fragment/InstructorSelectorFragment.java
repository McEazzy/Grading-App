/**
 * PURPOSE: fragment indicating a scrollable list of instructors.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.fragment;

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
import curtin.edu.assignment01.activity.InstructorDetailActivity;
import curtin.edu.assignment01.database.InstructorDatabase;
import curtin.edu.assignment01.model.CountryData;
import curtin.edu.assignment01.model.Instructor;

public class InstructorSelectorFragment extends Fragment {
    //Declare variables:
    private Instructor selectedInstructor;
    private List<Instructor> insList;
    private InstructorDatabase insDb;
    private CountryData countryList = CountryData.getInstance();
    private String filterName;

    private RecyclerView rv;
    private InstructorAdapter adapter;
    private TextView emptyText;

    //Default Constructor:
    public InstructorSelectorFragment()
    {
        // Required empty public constructor
        filterName = "";
    }

    //Alternative Constructor:
    public InstructorSelectorFragment(String filterName)
    {
        this.filterName = filterName;
    }

    //return access to the selected instructor.
    public Instructor getSelectedInstructor()
    {
        return selectedInstructor;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        insDb = new InstructorDatabase(getActivity());
        insDb.load();
        insList = insDb.getInstructorList();

        //create a temporary array list of students to store instructors having name matched the filter search.
        List<Instructor> tempList = new ArrayList<>();
        for(Instructor currIns: insList)
        {
            if(currIns.getName().contains(filterName))
            {
                tempList.add(currIns);
            }
        }
        insList = tempList;
        selectedInstructor = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_academic_selector, container, false);

        //when the list is empty, there will be a text indicating no instructor found instead.
        if(insList.isEmpty())
        {
            emptyText = (TextView) view.findViewById(R.id.empty);
            emptyText.setVisibility(View.VISIBLE);
        }

        rv = (RecyclerView) view.findViewById(R.id.rvList);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        adapter = new InstructorAdapter();
        rv.setAdapter(adapter);
        return view;
    }

    //indicates the current viewed entry in the recycler view.
    private class InstructorViewHolder extends RecyclerView.ViewHolder
    {
        //Declare variables:
        private Instructor instructor;
        private TextView insName;
        private ImageView countryImg;

        public InstructorViewHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.academic_entry, parent, false));

            insName = itemView.findViewById(R.id.academicText);
            countryImg = itemView.findViewById(R.id.countryImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedInstructor = instructor;
                    //start the detail activity of the selected instructor.
                    startActivity(InstructorDetailActivity.getIntent(getActivity(), selectedInstructor.getUserName()));
                }
            });
        }

        //attach the source of each view's entry element.
        public void bind(Instructor instructor)
        {
            this.instructor = instructor;
            //update the UI label.
            insName.setText(instructor.getName());
            //set the country image for the viewed entry.
            countryImg.setImageResource(countryList.getCountry(instructor.getCountry()).getDrawableId());
        }
    }

    //adapter class to find the source of insList to consecutively recycle the view of the view-holder.
    private class InstructorAdapter extends RecyclerView.Adapter<InstructorViewHolder>
    {
        @NonNull
        @Override
        public InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new InstructorViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull InstructorViewHolder holder, int position) {
            holder.bind(insList.get(position));
        }

        @Override
        public int getItemCount() {
            return insList.size();
        }
    }
}