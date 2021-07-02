package com.example.synradar_sectudo.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DBLHelper extends SQLiteOpenHelper  {

    public static final String DATABASE_NAME = "learn.db";
    public static final String TABLE_NAME = "lrnscore";
    public static final String TOPIC = "Topic";
    public static final String OVERALL_STATUS = "Overall_Status";
    public static final String EXP = "Exp";
    public static final String MIT = "Mit";


    public DBLHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
              "CREATE TABLE " + TABLE_NAME + "(" + TOPIC + " TEXT, " + OVERALL_STATUS + " TEXT, " + EXP + " TEXT, " + MIT + " TEXT)");




    }

   @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insertData () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);

        db.execSQL("INSERT INTO " + TABLE_NAME +"("+ TOPIC + ","+OVERALL_STATUS + ","+EXP + "," + MIT +" ) VALUES ('ia' , 'pending' , 'n' , 'n')");
        db.execSQL("INSERT INTO " + TABLE_NAME +"("+ TOPIC + ","+OVERALL_STATUS + ","+EXP + "," + MIT +" ) VALUES ('ism' , 'pending' , 'n' , 'n')");
        db.execSQL("INSERT INTO " + TABLE_NAME +"("+ TOPIC + ","+OVERALL_STATUS + ","+EXP + "," +MIT +" ) VALUES ('csua' , 'pending' , 'n' , 'n')");
        db.execSQL("INSERT INTO " + TABLE_NAME +"("+ TOPIC + ","+OVERALL_STATUS + ","+EXP + "," +MIT +" ) VALUES ('ids' , 'pending' , 'n' , 'n')");
        db.execSQL("INSERT INTO " + TABLE_NAME +"("+ TOPIC + ","+OVERALL_STATUS + ","+EXP + "," +MIT +" ) VALUES ('xss' , 'pending' , 'n' , 'n')");
        db.execSQL("INSERT INTO " + TABLE_NAME +"("+ TOPIC + ","+OVERALL_STATUS + ","+EXP + "," +MIT +" ) VALUES ('sqli' , 'pending' , 'n' , 'n')");
        db.execSQL("INSERT INTO " + TABLE_NAME +"("+ TOPIC + ","+OVERALL_STATUS + ","+EXP + "," +MIT +" ) VALUES ('ida' , 'pending' , 'n' , 'n')");
        db.execSQL("INSERT INTO " + TABLE_NAME +"("+ TOPIC + ","+OVERALL_STATUS + ","+EXP + "," +MIT +" ) VALUES ('dl' , 'pending' , 'n' , 'n')");
        db.execSQL("INSERT INTO " + TABLE_NAME +"("+ TOPIC + ","+OVERALL_STATUS + ","+EXP + "," +MIT +" ) VALUES ('icp' , 'pending' , 'n' , 'n')");
        db.execSQL("INSERT INTO " + TABLE_NAME +"("+ TOPIC + ","+OVERALL_STATUS + ","+EXP + "," +MIT +" ) VALUES ('sm' , 'pending' , 'n' , 'n')");
    }

    public void addExp(String Exp, String Topic){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXP, Exp);
        contentValues.put(TOPIC, Topic);
        db.update(TABLE_NAME,contentValues,"TOPIC = ?", new String[]{ Topic});
    }

    public void addMit(String Mit, String Overall_Status, String Topic ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MIT, Mit);
        contentValues.put(OVERALL_STATUS, Overall_Status);
        contentValues.put(TOPIC, Topic);
        db.update(TABLE_NAME,contentValues, "TOPIC = ?", new String[]{ Topic});
    }

    public int getCount(){
          SQLiteDatabase db = this.getReadableDatabase();
         String query = "SELECT COUNT(*) FROM "+ TABLE_NAME +" WHERE "+ OVERALL_STATUS + " = "+ "'completed'";
         Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        } else {
            return 0;
        }
    }

    public String getStatus(String Topic){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = " SELECT " + OVERALL_STATUS+ " FROM "+ TABLE_NAME +" WHERE "+ TOPIC + " = " +"?" ;
        Cursor cursor = db.rawQuery(query, new String[] {Topic});

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(0);
        } else {
            return "";
        }
    }

    public String getMit(String Topic){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = " SELECT " + MIT+ " FROM "+ TABLE_NAME +" WHERE "+ TOPIC + " = " +"?" ;
        Cursor cursor = db.rawQuery(query, new String[] {Topic});

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(0);
        } else {
            return "";
        }
    }

    public HashMap loadData() {
        HashMap arr = new HashMap();
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from "+ TABLE_NAME;

        Cursor cr = db.rawQuery(sql, null);
        String status = "0";

        if(cr.moveToFirst()){
            status = "1";
        }
        arr.put("Status", status);

        return arr;
    }





}
