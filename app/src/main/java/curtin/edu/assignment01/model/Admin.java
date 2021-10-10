/**
 * PURPOSE: define and create a concrete Admin object.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.model;

public class Admin extends User
{
    //Constructor:
    public Admin(String userName, String pinNo)
    {
        super(userName, pinNo);
    }

    @Override
    public boolean verifyAdmin() {
        return true;
    }
}
