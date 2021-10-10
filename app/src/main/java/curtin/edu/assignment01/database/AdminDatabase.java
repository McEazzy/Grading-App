/**
 * PURPOSE: creates database that stores the admin.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import curtin.edu.assignment01.model.Admin;
import curtin.edu.assignment01.database.CourseSchemas.AdminTable;

public class AdminDatabase extends SQLiteOpenHelper
{
    //Constants:
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Admin.db";

    //Class-fields:
    private SQLiteDatabase db;
    private Admin admin;

    //Return the universal admin variable for the app.
    public Admin getAdmin()
    {
        return admin;
    }

    public AdminDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        admin = null;
        //create a table in database.
        sqLiteDatabase.execSQL("CREATE TABLE "
                + AdminTable.NAME + "("
                + AdminTable.Cols.NAME + " TEXT, "
                + AdminTable.Cols.PIN + " TEXT)");
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
        Cursor cursor = db.query(AdminTable.NAME, null, null, null, null, null, null);

        //Iterate over each row and retrieve the entire database content.
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //add to the database.
                admin = new Admin(cursor.getString(0), cursor.getString(1));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    //add a new admin user and return true if it's successfully added to the database.
    public boolean add(Admin admin)
    {
        //set the one and only necessary admin to the input admin.
        this.admin = admin;
        //add the admin data to the database then.
        ContentValues cv = new ContentValues();
        cv.put(AdminTable.Cols.NAME, admin.getUserName());
        cv.put(AdminTable.Cols.PIN, admin.getPinNo());
        return (db.insert(AdminTable.NAME, null, cv) != -1);
    }
}
