/**
 * PURPOSE: define and create an abstract NonIT object.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.model;

public abstract class NonIT extends User
{
    //Class-fields:
    protected String email;
    protected String country;
    protected String name;

    //Constructor:
    public NonIT(String userName, String pinNo, String name,  String email, String country)
    {
        super(userName, pinNo);
        this.name = name;
        this.email = email;
        this.country = country;
    }

    //Accessors:
    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getCountry()
    {
        return country;
    }

    @Override
    public boolean verifyAdmin() {
        return false;
    }

    //verify if the current non-IT instance is an instructor or a student.
    public abstract boolean verifyInstructor();
}
