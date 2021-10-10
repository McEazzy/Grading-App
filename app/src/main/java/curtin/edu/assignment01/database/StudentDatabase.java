/**
 * PURPOSE: creates database that stores all the students records.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import curtin.edu.assignment01.database.CourseSchemas.StudentTable;
import curtin.edu.assignment01.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentDatabase extends SQLiteOpenHelper
{
    //Constants:
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Student.db";

    //Class-fields:
    private SQLiteDatabase db;
    private List<Student> studentList;

    //Return access to the student list.
    public List<Student> getStudentList()
    {
        return studentList;
    }

    public StudentDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
        studentList = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //create a table in database.
        sqLiteDatabase.execSQL("CREATE TABLE "
                + StudentTable.NAME + "("
                + StudentTable.Cols.USER + " TEXT, "
                + StudentTable.Cols.PIN + " INTEGER, "
                + StudentTable.Cols.NAME + " TEXT, "
                + StudentTable.Cols.EMAIL + " TEXT, "
                + StudentTable.Cols.COUNTRY + " TEXT, "
                + StudentTable.Cols.PRAC + " TEXT, "
                + StudentTable.Cols.BY + " TEXT)");
    }

    //Not implementing onUpgrade - not necessary for the scope of this unit.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        throw new UnsupportedOperationException("Sorry this method isn't implemented!");
    }

    //Implementing database query functions.
    public void load()
    {
        db = this.getWritableDatabase();
        Cursor cursor = db.query(StudentTable.NAME, null, null, null, null, null, null);

        //Iterate over each row and retrieve the entire database content.
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //add to the database.
                Student addedStudent = new Student(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(6));
                //create a Tree Map holding practical title and mark pairs and then added to each student from JSON string read by GSON.
                HashMap<String, String> pracMap = new Gson().fromJson(cursor.getString(5), new TypeToken<HashMap<String, String>>(){}.getType());
                addedStudent.getPracMap().putAll(pracMap);
                studentList.add(addedStudent);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    //add a new Student.
    public void add(Student student)
    {
        //add to the Student list.
        studentList.add(student);
        //add to the database then.
        ContentValues cv = new ContentValues();
        cv.put(StudentTable.Cols.USER, student.getUserName());
        cv.put(StudentTable.Cols.PIN, student.getPinNo());
        cv.put(StudentTable.Cols.NAME, student.getName());
        cv.put(StudentTable.Cols.EMAIL, student.getEmail());
        cv.put(StudentTable.Cols.COUNTRY, student.getCountry());
        cv.put(StudentTable.Cols.BY, student.getBy());
        //convert the marked practical list for the student to String.
        cv.put(StudentTable.Cols.PRAC, new Gson().toJson(student.getPracMap()));
        db.insert(StudentTable.NAME, null, cv);
    }

    //delete a Student.
    public void remove(String studentName)
    {
        Student rmStudent = null;
        //remove a student from the list.
        for(Student currStudent: studentList)
        {
            if(currStudent.equals(studentName))
            {
                rmStudent = currStudent;
            }
        }
        //if the targeted student is found.
        if(rmStudent != null)
        {
            studentList.remove(rmStudent);
            //remove from database then.
            String[] whereValue = {studentName};
            db.delete(StudentTable.NAME, StudentTable.Cols.USER + " = ?", whereValue);
        }
    }

    //edit a Student.
    public void edit(Student student, String where)
    {
        Student rmStudent = null;
        //remove a student from the list.
        for(Student currStudent: studentList)
        {
            if(currStudent.getUserName().equals(where))
            {
                rmStudent = currStudent;
            }
        }

        //if the targeted student is found.
        if(rmStudent != null)
        {
            studentList.set(studentList.indexOf(rmStudent), student);
            //update in the database then.
            ContentValues cv = new ContentValues();
            cv.put(StudentTable.Cols.USER, student.getUserName());
            cv.put(StudentTable.Cols.PIN, student.getPinNo());
            cv.put(StudentTable.Cols.NAME, student.getName());
            cv.put(StudentTable.Cols.EMAIL, student.getEmail());
            cv.put(StudentTable.Cols.COUNTRY, student.getCountry());
            //convert the marked practical list for the student to String.
            cv.put(StudentTable.Cols.PRAC, new Gson().toJson(student.getPracMap()));
            cv.put(StudentTable.Cols.BY, student.getBy());
            String[] whereValue = {where};
            db.update(StudentTable.NAME, cv, StudentTable.Cols.USER + " = ?", whereValue );
        }
    }
}
