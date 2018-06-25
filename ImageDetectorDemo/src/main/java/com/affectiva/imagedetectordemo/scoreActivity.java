package com.affectiva.imagedetectordemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class scoreActivity extends Activity {

    Button btn;


    ArrayList names;
    ArrayList scores;

    ArrayList history;

    String username;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_acitivity);

        names = new ArrayList();
        scores = new ArrayList();
        history = new ArrayList();
        db=new DatabaseHelper(this);



        btn=(Button)findViewById(R.id.btn);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i =new Intent(scoreActivity.this, StartActivity.class);
                        startActivity(i);
                    }

                });
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
         username=pref.getString("userName","");
        int userId=getId(username);
        Cursor c = db.getRowT2(userId);
//        displayRecordSet(c);
//        viewAll();
        displayRecordSet2(c);
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
                String name = cursor.getString(DatabaseHelper.COL_USERUSERNAMEE);
                String score = cursor.getString(DatabaseHelper.COL_USERSCORE );


                // Append data to the message:
                message = "id=" + id
                        + ", name=" + name
                        + ",score=" + score
                        + "\n";

                // historyList.add(message);
                Toast.makeText(this, "this is db----->"+message, Toast.LENGTH_SHORT).show();
                Log.i("This is ---->",message);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void viewAll() {

        int userid=getId(username);

        Cursor res = db.getRowT2(userid);
        if(res.getCount() == 0) {
            // show message

            return;
        }
        while (res.moveToNext()) {
            int id = res.getInt(0);
            names.add(res.getString(2));

           Cursor res2= db.getRowT2(id);
            scores.add( res2.getString(2));
        }


        String[] all = new String[names.size()];

        if(names.size()==scores.size())

        for (int i = 0; i < all.length; i++) {
            all[i] = names.get(i) + "   " + scores.get(i);
        }





    }



    private void displayRecordSet2(Cursor cursor) {
        String message = "";
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                message="";
                // Process the data:
                int id = cursor.getInt(DatabaseHelper.COL_USERIDD);
                String time = cursor.getString(DatabaseHelper.COL_USERTIME );
                int score = cursor.getInt(DatabaseHelper.COL_USERSCORE);

                // Append data to the message:
                message = "time =" + time
                        + ", id=" + id
                        + ",score=" + score
                        + "\n";

                history.add(message);
                Toast.makeText(this, "this is db----->"+message, Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());
        }
        cursor.close();
        String[] historyList= new String[history.size()];
        for(int i =0 ; i<historyList.length;i++)
        {
            historyList[i]=history.get(i).toString();
        }


        ListAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, historyList
        ){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };
        ListView myList = (ListView) findViewById(R.id.list);
        myList.setAdapter(myAdapter);
    }


}






