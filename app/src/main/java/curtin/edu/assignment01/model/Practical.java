/**
 * PURPOSE: define and create a concrete Practical object.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.model;

public class Practical
{
    //Class-fields:
    private String title;
    private String description;
    private Double maxMark;
    private boolean startMarking;

    //Constructor:
    public Practical(String title, String description, Double maxMark, boolean startMarking)
    {
        this.title = title;
        this.description = description;
        this.maxMark = maxMark;
        this.startMarking = startMarking;
    }

    //Accessors:
    public String getTitle()
    {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getMaxMark() {
        return maxMark;
    }

    public Boolean getStartMarking()
    {
        return startMarking;
    }

    //Mutators:
    public void setMaxMarks(Double maxMark) {
        this.maxMark = maxMark;
    }

    public void setStartMarking(boolean startMarking)
    {
        this.startMarking = startMarking;
    }
}
