/**
 * PURPOSE: define and create an abstract User object.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.model;

public abstract class User
{
    //Class-field:
    protected String userName;
    protected String pinNo;

    //Constructor:
    public User(String userName, String pinNo)
    {
        this.userName = userName;
        this.pinNo = pinNo;
    }

    //Accessors:
    public String getUserName()
    {
        return userName;
    }

    public String getPinNo()
    {
        return pinNo;
    }

    //check if the current user an admin or not.
    public abstract boolean verifyAdmin();
}
