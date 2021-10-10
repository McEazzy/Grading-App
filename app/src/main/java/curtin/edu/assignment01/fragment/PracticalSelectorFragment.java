/**
 * PURPOSE: fragment indicating a scrollable list of practicals.
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
import android.widget.TextView;

import java.util.List;

import curtin.edu.assignment01.R;
import curtin.edu.assignment01.activity.PracDetailActivity;
import curtin.edu.assignment01.database.PracticalDatabase;
import curtin.edu.assignment01.model.Practical;

public class PracticalSelectorFragment extends Fragment {
    //Declare variables:
    private TextView emptyText;
    private RecyclerView rv;
    private PracticalAdapter adapter;

    private boolean showDetail = true;
    private Practical selectedPrac;
    private PracticalDatabase pracDb;
    private List<Practical> pracList;

    //Default Constructor:
    public PracticalSelectorFragment()
    {
        // Required empty public constructor
    }

    //Alternative Constructor:
    public PracticalSelectorFragment(boolean showDetail)
    {
        this.showDetail = showDetail;
    }

    //return access to the selected practical.
    public Practical getSelectedPrac()
    {
        return selectedPrac;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pracDb = new PracticalDatabase(getActivity());
        pracDb.load();
        pracList = pracDb.getPracList();
        selectedPrac = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_academic_selector, container, false);

        //when the list is empty, there will be a text indicating no practical found instead.
        if(pracList.isEmpty())
        {
            emptyText = (TextView) view.findViewById(R.id.empty);
            emptyText.setVisibility(View.VISIBLE);
        }

        rv = (RecyclerView) view.findViewById(R.id.rvList);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        adapter = new PracticalAdapter();
        rv.setAdapter(adapter);
        return view;
    }

    //indicates the current viewed entry in the recycler view.
    private class PracticalViewHolder extends RecyclerView.ViewHolder
    {
        //Declare variables:
        private Practical prac;
        private TextView pracName;

        public PracticalViewHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.academic_entry, parent, false));

            pracName = itemView.findViewById(R.id.academicText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPrac = prac;
                    //if showDetail is true then on-click entry will open practical detail information.
                    if(showDetail)
                    {
                        startActivity(PracDetailActivity.getIntent(getActivity(), selectedPrac.getTitle()));
                    }
                }
            });
        }

        //attach the source of each view's entry element.
        public void bind(Practical prac)
        {
            this.prac = prac;
            //update the UI name.
            pracName.setText(prac.getTitle());
        }
    }

    //adapter class to find the source of practical list to consecutively recycle the view for the view-holder.
    private class PracticalAdapter extends RecyclerView.Adapter<PracticalViewHolder>
    {
        @NonNull
        @Override
        public PracticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new PracticalViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PracticalViewHolder holder, int position) {
            holder.bind(pracList.get(position));
        }

        @Override
        public int getItemCount() {
            return pracList.size();
        }
    }
}