package com.affectiva.imagedetectordemo;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;



public class DatabaseHelper extends SQLiteOpenHelper {



    private static final String DB_Name ="MyDb.db";
    private static final int DB_Version = 1;

    public static final String TAG = "DBHelper";


    public static final String KEY_ROWTIME ="usertime" ;
    public static final String KEY_ROWUSERID ="userid" ;
    public static final String KEY_ROWSCORE ="score" ;

    public static final int COL_USERTIME = 0;
    public static final int COL_USERIDD = 1;
    public static final int COL_USERSCORE = 2;

    public static final String KEY_ROWID ="id" ;
    public static final String KEY_ROWNAME ="username" ;
    public static final String KEY_ROWUSERNAME ="usernamee" ;
    public static final String KEY_ROWAGE ="age" ;
    public static final String KEY_ROWPASSWORD ="password" ;

    public static final int COL_USERID = 0;
    public static final int COL_USERNAME = 1;
    public static final int COL_USERUSERNAMEE = 2;
    public static final int COL_USERAGE= 3;
    public static final int COL_USERPASSWORD= 4;

    SQLiteDatabase db;

    public static final String[] ALL_KEYS1 = new String[] {KEY_ROWID, KEY_ROWNAME, KEY_ROWUSERNAME, KEY_ROWAGE, KEY_ROWPASSWORD};
    public static final String[] ALL_KEYS2 = new String[] {KEY_ROWTIME, KEY_ROWUSERID,  KEY_ROWSCORE};


    String where;

    public DatabaseHelper(Context context) {
        super(context, DB_Name, null, DB_Version);
        // SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlUser = "CREATE TABLE IF  NOT EXISTS User (id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR , usernamee VARCHAR , age INTEGER, password VARCHAR ) ";
        String sqlUserInfo = "CREATE TABLE UserInfo (usertime VARCHAR unique PRIMARY KEY , userid INTEGER , score INTEGER,  FOREIGN KEY(userid) REFERENCES User(id))";

        db.execSQL(sqlUser);
        db.execSQL(sqlUserInfo);
    }

    public void Insertuser(User user){
        db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",user.getUsername());
        values.put("usernamee",user.getUsernamee());
        values.put("age",user.getAge());
        values.put("password",user.getPassword());
        db.insert("User", null, values);
        db.close();
        String h="done";
        Log.i("tag-----", h);
        //Toast.makeText(,"done" ,Toast.LENGTH_SHORT).show();

    }
    public void InsertuserInfo(UserInfo user){
        db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("usertime",user.getUsertime());
        values.put("userid",user.getUserid());
        values.put("score",user.getScore());
        db.insert("UserInfo", null, values);
        db.close();
        String h="done";
        Log.i("tag-----", h);
        //Toast.makeText(,"done" ,Toast.LENGTH_SHORT).show();

    }


    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, "User", ALL_KEYS1,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public Cursor getRowT1(int rowid) {
        SQLiteDatabase db = getReadableDatabase();
        String where = "("+KEY_ROWID + "='" + rowid+"');";
        Cursor c = 	db.query(true, "User", ALL_KEYS1,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRowT2(int id) {
        db = getReadableDatabase();
        String where = KEY_ROWUSERID + "=" + id;
        Cursor c = 	db.query(true, "UserInfo", ALL_KEYS2,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getid (String username){
        db = getReadableDatabase();
        String where ="("+ KEY_ROWUSERNAME + "='" + username+"');";
        Cursor c = 	db.query(true, "User", ALL_KEYS1,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sqlUser = "DROP TABLE IF EXISTS User";
        String sqlUserInfo = "DROP TABLE IF EXISTS UserInfo";

        db.execSQL(sqlUser);
        db.execSQL(sqlUserInfo);

        onCreate(db);

    }



}
