package com.unipi.nikolastyl.mypois;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Processing extends AppCompatActivity {

    EditText title,description;
    Spinner category;
    SQLiteDatabase db;
    String location;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);
        //get old values
        title=findViewById(R.id.editTextTitle);
        title.setText(getIntent().getStringExtra("TITLE"));
        description=findViewById(R.id.editTextDescription);
        description.setText(getIntent().getStringExtra("DESCRIPTION"));
        location=getIntent().getStringExtra("LOCATION");

        category=findViewById(R.id.spinnerCategory);
        // spinner default list
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("HOME");
        arrayList.add("COURT");
        arrayList.add("SHOPPING");
        arrayList.add("ENTERTAINMENT");
        arrayList.add("WORKPLACE");

        @SuppressLint("ResourceType") ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_custom, arrayList);
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

    public void save(View view){ //save button onClick
        db = openOrCreateDatabase("POIs.db", MODE_PRIVATE, null);//db connection
        String newTitle,newDescription,newCategory;
        newTitle=title.getText().toString();
        newDescription=description.getText().toString();
        newCategory=category.getSelectedItem().toString();
        //save changes in the database
        db.execSQL("UPDATE POIS " +
                "SET title= '"+newTitle+"', " +
                "description= '"+newDescription+"', "+
                "category= '"+newCategory+"'"+
                " WHERE" +
                " location='"+location+"'");

        showMessage("Perfect","The saving was successful!!");

    }

    public void showMessage(String title, String text) {
        new android.app.AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(title)
                .setMessage(text)
                .show();
    }
}