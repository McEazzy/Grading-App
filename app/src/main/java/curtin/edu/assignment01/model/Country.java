/**
 * PURPOSE: define and create a concrete Country object.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.model;

public class Country
{
    //Class-field:
    private final int drawableId;
    private final String label;

    //Constructor:
    public Country(int drawableId, String label)
    {
        this.drawableId = drawableId;
        this.label = label;
    }

    //Getters:
    public int getDrawableId()
    {
        return drawableId;
    }

    public String getLabel()
    {
        return label;
    }
}
