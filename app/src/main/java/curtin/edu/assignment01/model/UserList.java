/**
 * PURPOSE: stores the entire list of Users registered to this app.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import curtin.edu.assignment01.database.AdminDatabase;
import curtin.edu.assignment01.database.InstructorDatabase;
import curtin.edu.assignment01.database.StudentDatabase;

public class UserList
{
    //Class-field:
    private List<User> list;
    private AdminDatabase adminDb;
    private InstructorDatabase insDb;
    private StudentDatabase studentDb;

    //Constructor:
    public UserList(Context c)
    {
        //load all the user databases to the list of users.
        adminDb = new AdminDatabase(c);
        insDb = new InstructorDatabase(c);
        studentDb = new StudentDatabase(c);
        adminDb.load();
        insDb.load();
        studentDb.load();

        //import to the userList.
        list = new ArrayList<>();
        //if the database doesn't store any values, the user list won't add anything.
        if(adminDb.getAdmin() != null)
        {
            list.add(adminDb.getAdmin());
        }
        if(!insDb.getInstructorList().isEmpty()) {
            list.addAll(insDb.getInstructorList());
        }
        if(!studentDb.getStudentList().isEmpty()) {
            list.addAll(studentDb.getStudentList());
        }
    }

    //Singleton creation of the UserList class.
    private static UserList instance = null;
    public static UserList getInstance(Context c)
    {
        if(instance == null)
        {
            instance = new UserList(c);
        }
        return instance;
    }
    public static void setInstance(UserList inputList)
    {
        instance = inputList;
    }

    //Accessor:
    public List<User> getUserList()
    {
        return list;
    }
}