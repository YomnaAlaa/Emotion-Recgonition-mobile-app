package com.affectiva.imagedetectordemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signinActivity extends Activity {


    EditText nameVal;
    EditText passwordVal;
    Button login;
    DatabaseHelper db;
    Button signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        nameVal = (EditText)findViewById(R.id.edtUserName);
        passwordVal = (EditText)findViewById(R.id.edtPassword);
        login=(Button)findViewById(R.id.button);
        signUpBtn=(Button)findViewById(R.id.signUpbtn);
        db = new DatabaseHelper(this);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=nameVal.getText().toString().trim();
                String password = passwordVal.getText().toString();
                int userId=getId(name);

                Cursor cursor = db.getRowT1(userId);

                String databasename="";
                String databasepass ="";


                databasename = cursor.getString(DatabaseHelper.COL_USERUSERNAMEE);
                databasepass = cursor.getString(DatabaseHelper.COL_USERPASSWORD);

                String dbN=databasename.toLowerCase();
                String dbP = databasepass.toLowerCase();


                String nL=name.toLowerCase();
                String pL = password.toLowerCase();


                if(dbN.equals(nL)&&dbP.equals(pL))
                {
                    SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edt = pref.edit();
                    edt.putString("userName", name);
                    edt.putBoolean("activity_executed", true);
                    edt.commit();

                    Intent i = new Intent(signinActivity.this,StartActivity.class);
                    startActivity(i);
                }

                else
                {
                    Toast.makeText(signinActivity.this, "Wrong User Name or password, Try again", Toast.LENGTH_SHORT).show();

                }




            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(signinActivity.this,signupActivity.class);
                startActivity(i);
            }
        });
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
}
