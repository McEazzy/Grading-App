/**
 * PURPOSE: fragment indicating a scrollable list of practical marks.
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
import android.widget.TextView;

import java.util.HashMap;

import curtin.edu.assignment01.R;

public class MarkBreakdownFragment extends Fragment {
    //Declare variables:
    private HashMap<String, String> pracMap = null;

    private RecyclerView rv;
    private TextView emptyText;
    private MarkAdapter adapter;

    //Default Constructor:
    public MarkBreakdownFragment() {
        // Required empty public constructor
    }

    //Alternative Constructor:
    public MarkBreakdownFragment(HashMap<String, String> pracMap)
    {
        this.pracMap = pracMap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mark_breakdown, container, false);

        rv = view.findViewById(R.id.rvMarks);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        //when the practical map is empty, a text indicating the student hasn't received any marks yet.
        if(pracMap.isEmpty())
        {
            emptyText = view.findViewById(R.id.empty);
            emptyText.setVisibility(View.VISIBLE);
        }

        adapter = new MarkAdapter();
        rv.setAdapter(adapter);
        return view;
    }

    //indicates the current viewed entry in the recycler view.
    private class MarkViewHolder extends RecyclerView.ViewHolder
    {
        //Define variables:
        private TextView pracInfo;

        public MarkViewHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.mark_entry, parent, false));
            pracInfo = itemView.findViewById(R.id.pracInfo);
        }

        public void bind(String pracTitle, Double pracGrade)
        {
            if(pracGrade <= 50)
            {
                pracInfo.setBackgroundColor(Color.parseColor("#FF4040"));
            }
            else if(pracGrade <= 80)
            {
                pracInfo.setBackgroundColor(Color.parseColor("#FFFF00"));
            }
            else
            {
                pracInfo.setBackgroundColor(Color.parseColor("#00FF00"));
            }
            String pracInfoStr = pracTitle + " - Grade: " + pracGrade + "%";
            pracInfo.setText(pracInfoStr);
        }
    }

    //adapter class to find the source of practical map to consecutively bind to the recycled view of the view-holder.
    private class MarkAdapter extends RecyclerView.Adapter<MarkViewHolder>
    {
        @NonNull
        @Override
        public MarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new MarkViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MarkViewHolder holder, int position)
        {
            String pracTitle = null;
            Double pracGrade = null;
            Object key = pracMap.keySet().toArray()[position];
            if(key != null)
            {
                pracTitle = key.toString();
                pracGrade = Double.parseDouble(pracMap.get(pracTitle));
            }
            holder.bind(pracTitle, pracGrade);
        }

        @Override
        public int getItemCount() {
            return pracMap.size();
        }
    }
}