package com.example.synradar_sectudo.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import androidx.annotation.RequiresApi;


import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.lang.String;
import java.util.HashMap;



public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;

    public static final String DATABASE_NAME = "securekyc.db";
    public static final String TABLE_NAME = "kycdetail_table";
    public static final String FULL_NAME = "Full_name";
    public static final String PAN_NUMBER = "PAN_Number";
    public static final String ADDRESS = "Address";
    public static final String EMPLOYMENT_TYPE = "Employment_type";
    public static final String DESIGNATION_OCCUPATION = "DesignationOccupation";
    public static final String SALARY = "Salary";
    public static String pass = "";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    static public synchronized DBHelper getInstance(Context context) {
        if (instance == null)
            instance = new DBHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "(" + FULL_NAME + " TEXT, " + PAN_NUMBER + " TEXT, " + ADDRESS + " TEXT, " + EMPLOYMENT_TYPE + " TEXT," + DESIGNATION_OCCUPATION + " TEXT, " + SALARY + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean insertData(String Full_Name, String Pan_Number, String Address, String Employment_Type, String Designation_Occupation, String Salary) {
       if(DBHelper.pass.equals("")){
        DBHelper.pass = EncryptionUtil.getDBKey();
       }

        SQLiteDatabase db = instance.getWritableDatabase(pass);

        ContentValues contentValues = new ContentValues();
        contentValues.put(FULL_NAME, Full_Name);
        contentValues.put(PAN_NUMBER, Pan_Number);
        contentValues.put(ADDRESS, Address);
        contentValues.put(EMPLOYMENT_TYPE, Employment_Type);
        contentValues.put(DESIGNATION_OCCUPATION, Designation_Occupation);
        contentValues.put(SALARY, Salary);
        db.execSQL("delete from " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public HashMap loadData() {
        HashMap arr = new HashMap();
        if(DBHelper.pass.equals("")){
            DBHelper.pass = EncryptionUtil.getDBKey();
        }
        SQLiteDatabase db = instance.getWritableDatabase(pass);
        String sql = "select * from " + TABLE_NAME;

        Cursor cr = db.rawQuery(sql, null);
        String status = "0";

        if (cr.moveToFirst()) {
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
        cr.close();

        return arr;
    }
}
