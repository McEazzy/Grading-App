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

import java.util.ArrayList;
import java.util.List;

import curtin.edu.assignment01.database.CourseSchemas.InstructorTable;
import curtin.edu.assignment01.model.Instructor;

public class InstructorDatabase extends SQLiteOpenHelper
{
    //Constants:
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Instructor.db";

    //Class-fields:
    private SQLiteDatabase db;
    private List<Instructor> instructorList;

    //Return access to the instructor list.
    public List<Instructor> getInstructorList()
    {
        return instructorList;
    }

    public InstructorDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
        instructorList = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //create a table in database.
        sqLiteDatabase.execSQL("CREATE TABLE "
                + InstructorTable.NAME + "("
                + InstructorTable.Cols.USER + " TEXT, "
                + InstructorTable.Cols.PIN + " TEXT, "
                + InstructorTable.Cols.NAME + " TEXT, "
                + InstructorTable.Cols.EMAIL + " TEXT, "
                + InstructorTable.Cols.COUNTRY + " TEXT)");
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
        Cursor cursor = db.query(InstructorTable.NAME, null, null, null, null, null, null);

        //Iterate over each row and retrieve the entire database content.
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //add to the database.
                instructorList.add(new Instructor(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    //add a new Instructor user.
    public void add(Instructor instructor)
    {
        //add to the Instructor list.
        instructorList.add(instructor);
        //add to the database then.
        ContentValues cv = new ContentValues();
        cv.put(InstructorTable.Cols.USER, instructor.getUserName());
        cv.put(InstructorTable.Cols.PIN, instructor.getPinNo());
        cv.put(InstructorTable.Cols.NAME, instructor.getName());
        cv.put(InstructorTable.Cols.EMAIL, instructor.getEmail());
        cv.put(InstructorTable.Cols.COUNTRY, instructor.getCountry());
        db.insert(InstructorTable.NAME, null, cv);
    }

    //delete an Instructor.
    public void remove(String insName)
    {
        Instructor delInstructor = null;
        //remove an instructor from the list.
        for(Instructor currIns: instructorList)
        {
            if(currIns.getUserName().equals(insName))
            {
                delInstructor = currIns;
            }
        }

        //if the targeted instructor is found.
        if(delInstructor != null)
        {
            instructorList.remove(delInstructor);
            //remove from database then.
            String[] whereValue = {insName};
            db.delete(InstructorTable.NAME, InstructorTable.Cols.USER + " = ?", whereValue);
        }
    }

    //edit an Instructor.
    public void edit(Instructor newInstructor, String where)
    {
        Instructor delInstructor = null;
        //remove an instructor from the list.
        for(Instructor currIns: instructorList) {
            if (currIns.getUserName().equals(where))
            {
                delInstructor = currIns;
            }
        }
        //if the targeted instructor is found.
        if(delInstructor != null)
        {
            //replace the old entry with the new entry.
            instructorList.set(instructorList.indexOf(delInstructor), newInstructor);
            //update in the database then.
            ContentValues cv = new ContentValues();
            cv.put(InstructorTable.Cols.USER, newInstructor.getUserName());
            cv.put(InstructorTable.Cols.PIN, newInstructor.getPinNo());
            cv.put(InstructorTable.Cols.NAME, newInstructor.getName());
            cv.put(InstructorTable.Cols.EMAIL, newInstructor.getEmail());
            cv.put(InstructorTable.Cols.COUNTRY, newInstructor.getCountry());
            String[] whereValue = {where};
            db.update(InstructorTable.NAME, cv, InstructorTable.Cols.USER + " = ?", whereValue );
        }
    }
}
