/**
 * PURPOSE: define and create a concrete Instructor object.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.model;

public class Instructor extends NonIT
{
    //Constructor:
    public Instructor(String userName, String pinNo, String name, String email, String country) {
        super(userName, pinNo, name, email, country);
    }

    @Override
    public boolean verifyInstructor() {
        return true;
    }
}
