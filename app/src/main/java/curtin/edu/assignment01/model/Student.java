/**
 * PURPOSE: define and create a concrete Student object.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.model;

import java.util.HashMap;

public class Student extends NonIT
{
    //Class-field:
    private String by;
    private HashMap<String, String> markedPracs;

    //Constructor:
    public Student(String userName, String pinNo, String name, String email, String country, String by)
    {
        super(userName, pinNo, name, email, country);
        this.by = by;
        markedPracs = new HashMap<>();
    }

    @Override
    public boolean verifyInstructor() {
        return false;
    }

    //Accessor:
    public HashMap<String, String> getPracMap() {
        return markedPracs;
    }

    public String getBy()
    {
        return by;
    }

    //return the total average percentage mark from the student record.
    public Double getAvePercentMark()
    {
        Double totalMark;
        if(markedPracs.isEmpty())
        {
            totalMark = null;
        }
        else {
            totalMark = 0.0;
            for(String mark: markedPracs.values())
            {
                totalMark += Double.parseDouble(mark);
            }
            return (Math.round(totalMark*10/markedPracs.size()))/10.0;
        }
        return totalMark;
    }

    //Mutator:
    public String addMark(String pracName, double mark, double maxMark)
    {
        String result = null;
        if(maxMark >= mark)
        {
            //round up the grade percentage.
            double roundUp = (Math.round(mark*100*10/maxMark))/10.0;
            if(markedPracs.put(pracName, String.valueOf(roundUp)) != null)
            {
                result = "A mark has already given to this student for this practical.";
            }
        }
        else
        {
            result = "Given grade exceeds the maximum allowable mark for this practical";
        }
        return result;
    }

    public void setMarkedPrac(HashMap<String, String> inputMap)
    {
        markedPracs = inputMap;
    }
}
