package com.unipi.nikolastyl.mypois;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddNewPOIs extends AppCompatActivity {

    SQLiteDatabase db;
    EditText title,description;
    Spinner category;
    String location12,ts,title2,desc2,category2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pois);

        //database connection
        db = openOrCreateDatabase("POIs.db", MODE_PRIVATE, null);

        // data for database
        category = findViewById(R.id.spinner);
        title = findViewById(R.id.titleInput);
        description = findViewById(R.id.descriptionInput);
        location12 = getIntent().getStringExtra("CUR_LOC");
        Date ts1 = Calendar.getInstance().getTime();
        ts=ts1.toString();

        // spinner default list
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("HOME");
        arrayList.add("COURT");
        arrayList.add("SHOPPING");
        arrayList.add("ENTERTAINMENT");
        arrayList.add("WORKPLACE");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_custom,arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(arrayAdapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + selected, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


    public void addNew2(View view){ //add new button onClick
        //get values
        title2=title.getText().toString();
        category2=category.getSelectedItem().toString();
        desc2=description.getText().toString();

        if(title2.equals("") || desc2.equals("")){
            showMessage("Error","Please fill all the fields");
          }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("CONFIRMATION");
            builder.setMessage("Are you sure you want to add this POI??");
            builder.setPositiveButton("Yes",
                    (dialog, which) -> {
                if(location12!=null) {
                    db = openOrCreateDatabase("POIs.db", MODE_PRIVATE, null);
                    try {
                        db.execSQL("Insert into POIS(title,timestamp,description,category,location) Values(?,?,?,?,?)", new String[]{title2,ts,desc2,category2,location12});
                        showMessage("Perfect","The registration was successful!!");
                    }
                    catch (Exception e){
                        showMessage("Error","This location already exists");
                    }

                }
                else {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                }
            });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }
    public void showMessage(String title, String text) {
        new android.app.AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(title)
                .setMessage(text)
                .show();
    }
}