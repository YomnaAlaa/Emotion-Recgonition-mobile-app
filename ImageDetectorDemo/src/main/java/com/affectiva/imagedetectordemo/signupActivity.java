package com.affectiva.imagedetectordemo;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signupActivity extends Activity {
    EditText edtname, edtusername, edtpassword, edtage;
    boolean allIsOk = false;
    Button btnSignUp;
    String name, userName, password , ageTemp;
    TextView txtname, txtusername, txtpassword, txtage;
    String message = "";
    int age = 0;
    boolean flags []  = new boolean[4];
    User u;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        db = new DatabaseHelper(this);
        start();
        click();
    }


    void click(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                message = "";
                name = edtname.getText().toString().trim();
                userName =  edtusername.getText().toString().trim();
                password = edtpassword.getText().toString().trim();
                ageTemp = edtage.getText().toString();
                if (name.equals("")){
                    txtname.setTextColor(Color.RED);
                    message+="-fill name \n";
                    flags[0] = false;
                }else{
                    flags[0]  = true;
                }

                if(password.equals("")){
                    message+="-fill password \n";
                    txtpassword.setTextColor(Color.RED);
                    flags[1]  = false;
                }else{
                    flags[1]  = true;
                }

                if (userName.equals("")){
                    message+="-fill username \n";

                    txtusername.setTextColor(Color.RED);
                    flags[2]  = false;
                }else{
                    flags[2]  = true;
                }


                if(ageTemp.equals("")){
                    message+="-fill age \n";
                    txtage.setTextColor(Color.RED);
                    flags[3]  = false;
                }else {
                    age = Integer.parseInt(ageTemp);
                    flags[3]  = true;
                }

                for (int i = 0; i < flags.length; i++) {
                    if (flags[i]==false){
                        allIsOk=false;
                        break;
                    }
                    allIsOk = true;
                }
                if (allIsOk == false) {
//                    Toast.makeText(signupActivity.this, "fill all data", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(signupActivity.this);
                    builder1.setMessage(message);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else {
                    u = new User();
                    u.setAge(age);
                    u.setPassword(password);
                    u.setUsername(name);
                    u.setUsernamee(userName);
                    db.Insertuser(u);
                    int id = getId(userName);
                    Cursor c = db.getRowT1(id);
                    displayRecordSet(c);
                    // Cursor c = db.getid(userName);
                    Intent toSignIn = new Intent(signupActivity.this, signinActivity.class);
                    finish();
                    startActivity(toSignIn);
                }
            }
        });
    }

    void start(){
        edtname = (EditText)findViewById(R.id.edtname);
        edtusername = (EditText)findViewById(R.id.edtusername);
        edtpassword = (EditText)findViewById(R.id.edtpassword);
        edtage = (EditText)findViewById(R.id.edtage);
        btnSignUp = (Button)findViewById(R.id.signupBtn);
        txtname = (TextView) findViewById(R.id.txtname);
        txtusername = (TextView)findViewById(R.id.txxtusername);
        txtpassword = (TextView)findViewById(R.id.txtpassword);
        txtage = (TextView)findViewById(R.id.txtage);
        btnSignUp = (Button)findViewById(R.id.signupBtn);
    }

    private int getId(String username){
        Cursor c = db.getid(username);
        int id= 0;
        if (c.moveToFirst()) {
            do {
                // Process the data:
                id = c.getInt(DatabaseHelper.COL_USERID);
                // Append data to the message:
                Toast.makeText(this, "this is db in the getID----->"+id, Toast.LENGTH_SHORT).show();
                // [TO_DO_B6]
                // Create arraylist(s)? and use it(them) in the list view
            } while (c.moveToNext());
        }
        Log.i("id----===>",id+"");
        c.close();
        return  id;
    }

    private void displayRecordSet(Cursor cursor) {
        String message = "";
        if (cursor.moveToFirst()) {

            do {

//                message ="";
                // Process the data:
                int id = cursor.getInt(DatabaseHelper.COL_USERID);
                String email = cursor.getString(DatabaseHelper.COL_USERNAME );
                String name = cursor.getString(DatabaseHelper.COL_USERUSERNAMEE);
                int age = cursor.getInt(DatabaseHelper.COL_USERAGE);
                String pass = cursor.getString(DatabaseHelper.COL_USERPASSWORD);

                // Append data to the message:
                message = "id=" + id
                        + ", name=" + name
                        + ",Email=" + email
                        + ", Age=" + age
                        + ", Password =" + pass
                        + "\n";

                // historyList.add(message);
                Toast.makeText(this, "this is db----->"+message, Toast.LENGTH_SHORT).show();
                Log.i("This is ---->",message);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
