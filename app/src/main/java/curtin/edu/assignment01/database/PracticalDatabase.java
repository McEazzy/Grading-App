/**
 * PURPOSE: creates database that stores all the practicals records.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import curtin.edu.assignment01.database.CourseSchemas.PracticalTable;
import curtin.edu.assignment01.model.Practical;

import java.util.ArrayList;
import java.util.List;

public class PracticalDatabase extends SQLiteOpenHelper
{
    //Constants:
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Practical.db";

    //Class-fields:
    private SQLiteDatabase db;
    private List<Practical> pracList;

    //give access to list of available practicals.
    public List<Practical> getPracList()
    {
        return pracList;
    }

    //Constructor:
    public PracticalDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
        pracList = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //create a table in database.
        sqLiteDatabase.execSQL("CREATE TABLE "
                + PracticalTable.NAME + "("
                + PracticalTable.Cols.TITLE + " TEXT, "
                + PracticalTable.Cols.DES + " TEXT, "
                + PracticalTable.Cols.MARK + " DOUBLE, "
                + PracticalTable.Cols.START + " INTEGER)");
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
        Cursor cursor = db.query(PracticalTable.NAME, null, null, null, null, null, null);

        //Iterate over each row and retrieve the entire database content.
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //add to the database.
                pracList.add(new Practical(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getInt(3) == 1));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    //add a new Practical.
    public void add(Practical practical)
    {
        //add to the Instructor list.
        pracList.add(practical);
        //add to the database then.
        ContentValues cv = new ContentValues();
        cv.put(PracticalTable.Cols.TITLE, practical.getTitle());
        cv.put(PracticalTable.Cols.DES, practical.getDescription());
        cv.put(PracticalTable.Cols.MARK, practical.getMaxMark());
        cv.put(PracticalTable.Cols.START, practical.getStartMarking() ? 1 : 0);
        db.insert(PracticalTable.NAME, null, cv);
    }

    //delete a Practical.
    public void remove(String pracName)
    {
        Practical rmPrac = null;
        //remove an instructor from the list.
        for(Practical currPrac: pracList)
        {
            if(currPrac.getTitle().equals(pracName))
            {
                rmPrac = currPrac;
            }
        }
        //if the targeted old practical is found.
        if(rmPrac != null)
        {
            pracList.remove(rmPrac);
            //remove from database then.
            String[] whereValue = {pracName};
            db.delete(PracticalTable.NAME, PracticalTable.Cols.TITLE + " = ?", whereValue);
        }
    }

    //edit a Practical.
    public void edit(Practical practical, String where)
    {
        Practical rmPrac = null;
        //remove a practical from the list.
        for(Practical currPrac: pracList)
        {
            if(currPrac.getTitle().equals(where))
            {
                rmPrac = currPrac;
            }
        }
        //if the targeted old practical is found.
        if(rmPrac != null)
        {
            //replace the old entry with the new entry.
            pracList.set(pracList.indexOf(rmPrac), practical);
            //update in the database then.
            ContentValues cv = new ContentValues();
            cv.put(PracticalTable.Cols.TITLE, practical.getTitle());
            cv.put(PracticalTable.Cols.DES, practical.getDescription());
            cv.put(PracticalTable.Cols.MARK, practical.getMaxMark());
            cv.put(PracticalTable.Cols.START, practical.getStartMarking() ? 1 : 0);
            String[] whereValue = {where};
            db.update(PracticalTable.NAME, cv, PracticalTable.Cols.TITLE + " = ?", whereValue );
        }
    }
}
