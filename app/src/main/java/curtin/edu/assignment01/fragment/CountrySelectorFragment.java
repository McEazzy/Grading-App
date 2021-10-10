/**
 * PURPOSE: fragment indicating a scrollable list of countries.
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

import curtin.edu.assignment01.R;
import curtin.edu.assignment01.model.Country;
import curtin.edu.assignment01.model.CountryData;

public class CountrySelectorFragment extends Fragment {
    //Declare variables:
    private CountryData countryList;
    private Country selectedCountry;
    private int position = 0;

    private RecyclerView rv;
    private CountryAdapter adapter;

    //Default Constructor:
    public CountrySelectorFragment() {
        // Required empty public constructor
    }

    //Alternative Constructor:
    public CountrySelectorFragment(int position)
    {
        this.position = position;
    }

    //return the selected country.
    public Country getSelectedCountry()
    {
        return selectedCountry;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countryList = CountryData.getInstance();
        //set a default selected country when none is selected.
        selectedCountry = countryList.getCountry(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_country_selector, container, false);

        rv = (RecyclerView) view.findViewById(R.id.rvList);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //scroll the recycler-view to a specific entry position.
        rv.scrollToPosition(position);

        adapter = new CountryAdapter();
        rv.setAdapter(adapter);

        return view;
    }

    //indicates the current viewed entry in the recycler view.
    private class CountryViewHolder extends RecyclerView.ViewHolder
    {
        //Declare variables:
        private ImageView image;
        private TextView label;
        private Country country;

        public CountryViewHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.country_entry, parent, false));

            image = itemView.findViewById(R.id.flagImage);
            label = itemView.findViewById(R.id.flagDes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    selectedCountry = country;
                }
            });
        }

        //attach the source of each view's entry element.
        public void bind(Country country)
        {
            this.country = country;
            //update the flag's UI display.
            image.setImageResource(country.getDrawableId());
            label.setText(country.getLabel());
        }
    }

    //adapter class to find the source of country list to consecutively recycle the view of the view-holder.
    private class CountryAdapter extends RecyclerView.Adapter<CountryViewHolder>
    {
        @NonNull
        @Override
        public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new CountryViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
            holder.bind(countryList.getCountry(position));
        }

        @Override
        public int getItemCount() {
            return countryList.getSize();
        }
    }
}