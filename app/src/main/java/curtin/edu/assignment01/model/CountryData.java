/**
 * PURPOSE: stores a constant list of Countries available to be assigned to users.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.model;

import java.util.Arrays;
import java.util.List;

import curtin.edu.assignment01.R;

public class CountryData
{
    //list of possible standard countries with label.
    private static final List<Country> countryList = Arrays.asList(new Country(R.drawable.flag__unknown, "unrecognised country (UNSELECTED DEFAULT)"),
            new Country(R.drawable.flag_ad, "Andorra"),
            new Country(R.drawable.flag_ae, "UAE"),
            new Country(R.drawable.flag_af, "Afghanistan"),
            new Country(R.drawable.flag_ag, "Antigua and Barbuda"),
            new Country(R.drawable.flag_ai, "Anguilla"),
            new Country(R.drawable.flag_al, "Albania"),
            new Country(R.drawable.flag_am, "Armenia"),
            new Country(R.drawable.flag_ar, "Argentina"),
            new Country(R.drawable.flag_at, "Austria"),
            new Country(R.drawable.flag_au, "Australia"),
            new Country(R.drawable.flag_az, "Azerbaijan"),
            new Country(R.drawable.flag_ba, "Bosnia and Herzegovina"),
            new Country(R.drawable.flag_bd, "Belgium"),
            new Country(R.drawable.flag_bf, "Burkina Faso"),
            new Country(R.drawable.flag_bg, "Bulgaria"),
            new Country(R.drawable.flag_br, "Brazil"),
            new Country(R.drawable.flag_ca, "Canada"),
            new Country(R.drawable.flag_ch, "Switzerland"),
            new Country(R.drawable.flag_cn, "China"),
            new Country(R.drawable.flag_cz, "Czeck Republic"),
            new Country(R.drawable.flag_de, "Germany"),
            new Country(R.drawable.flag_dk, "Denmark"),
            new Country(R.drawable.flag_es, "Spain"),
            new Country(R.drawable.flag_fr, "France"),
            new Country(R.drawable.flag_gb, "Great Britain"),
            new Country(R.drawable.flag_ge, "Georgia"),
            new Country(R.drawable.flag_gr, "Greece"),
            new Country(R.drawable.flag_hk, "Hong Kong"),
            new Country(R.drawable.flag_it, "Italy"),
            new Country(R.drawable.flag_jp, "Japan"),
            new Country(R.drawable.flag_lt, "Lithuania"),
            new Country(R.drawable.flag_mx, "Mexico"),
            new Country(R.drawable.flag_my, "Malaysia"),
            new Country(R.drawable.flag_nl, "Neetherlands"),
            new Country(R.drawable.flag_pl, "Polland"),
            new Country(R.drawable.flag_qa, "Qatar"),
            new Country(R.drawable.flag_ru, "Rusia"),
            new Country(R.drawable.flag_uk, "United Kingdom"),
            new Country(R.drawable.flag_us, "United States of America"),
            new Country(R.drawable.flag_vn, "Viet Nam"));

    //return a singleton to access the country data.
    private static CountryData instance = null;
    public static CountryData getInstance()
    {
        if(instance == null)
        {
            instance = new CountryData();
        }
        return instance;
    }

    //Default Constructor:
    public CountryData() {}

    //Accessor:
    public Country getCountry(int i)
    {
        return countryList.get(i);
    }

    public Country getCountry(String label)
    {
        Country result = null;
        for(Country currCountry: countryList)
        {
            if(currCountry.getLabel().equals(label))
            {
                result = currCountry;
            }
        }
        return result;
    }

    public int getSize()
    {
        return countryList.size();
    }

    public int getPosition(String label)
    {
        int result = -1;
        for(Country currCountry: countryList)
        {
            if(currCountry.getLabel().equals(label))
            {
                result = countryList.indexOf(currCountry);
            }
        }
        return result;
    }
}

