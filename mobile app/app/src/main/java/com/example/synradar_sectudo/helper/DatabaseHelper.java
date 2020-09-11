package com.example.synradar_sectudo.helper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "insecurekyc.db";
    public static final String TABLE_NAME = "kycdetail_table";
    public static final String FULL_NAME = "Full_name";
    public static final String PAN_NUMBER = "PAN_Number";
    public static final String ADDRESS = "Address";
    public static final String EMPLOYMENT_TYPE = "Employment_type";
    public static final String DESIGNATION_OCCUPATION = "DesignationOccupation";
    public static final String SALARY = "Salary";


    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                //   "create table " + TABLE_NAME +
                //         "(" + FULL_NAME + " TEXT primary key, " + PAN_NUMBER + " TEXT," + ADDRESS + " TEXT,"+ EMPLOYMENT_TYPE + " TEXT , " + DESIGNATION_OCCUPATION + " TEXT," + SALARY + " INTEGER)"
                "CREATE TABLE " + TABLE_NAME + "(" + FULL_NAME + " TEXT, " + PAN_NUMBER + " TEXT, " + ADDRESS + " TEXT, " + EMPLOYMENT_TYPE + " TEXT," + DESIGNATION_OCCUPATION +  " TEXT, " + SALARY + " INTEGER)"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData (String Full_Name, String Pan_Number, String Address, String Employment_Type,String Designation_Occupation,String Salary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FULL_NAME, Full_Name);
        contentValues.put(PAN_NUMBER, Pan_Number);
        contentValues.put(ADDRESS, Address);
        contentValues.put(EMPLOYMENT_TYPE, Employment_Type);
        contentValues.put(DESIGNATION_OCCUPATION, Designation_Occupation);
        contentValues.put(SALARY, Salary);
        db.execSQL("delete from "+ TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public HashMap loadData() {
        HashMap arr = new HashMap();
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from "+ TABLE_NAME;

        Cursor cr = db.rawQuery(sql, null);
        String status = "0";

        if(cr.moveToFirst()){
            status = "1";
            String fullname = cr.getString(cr.getColumnIndex(FULL_NAME));
            String pannum = cr.getString(cr.getColumnIndex(PAN_NUMBER));
            String address = cr.getString(cr.getColumnIndex(ADDRESS));
            String emp = cr.getString(cr.getColumnIndex(EMPLOYMENT_TYPE));
            String desgn = cr.getString(cr.getColumnIndex(DESIGNATION_OCCUPATION));
            int salary = cr.getInt(cr.getColumnIndex(SALARY));

            arr.put(FULL_NAME, fullname);
            arr.put(ADDRESS, address);
            arr.put(PAN_NUMBER, pannum);
            arr.put(EMPLOYMENT_TYPE, emp);
            arr.put(DESIGNATION_OCCUPATION, desgn);
            arr.put(SALARY, salary);
        }
        arr.put("Status", status);

        return arr;
    }
}

