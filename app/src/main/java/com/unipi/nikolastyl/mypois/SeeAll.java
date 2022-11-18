package com.unipi.nikolastyl.mypois;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SeeAll extends AppCompatActivity  {
    SQLiteDatabase db;
    RecyclerView rv;
    ArrayAdapter<String> arrayAdapter;
    MyAdapter myAdapter;
    LinearLayout linearLayout;
    ArrayList<String> PoisList = new ArrayList<>();
    TextView txtV;




    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);
        db = openOrCreateDatabase("POIs.db", MODE_PRIVATE, null);
        Toast.makeText(this, "Swipe Left to Delete", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Tap to process", Toast.LENGTH_SHORT).show();

        //the list with the pois
        Cursor cursor = db.rawQuery("Select * from POIS", null);
        while (cursor.moveToNext()) {
            PoisList.add(cursor.getString(0));
        }
        //Recycle View
        rv=findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(this,PoisList);
        rv.setAdapter(myAdapter);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,PoisList);
        ItemTouchHelper helper =new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.searchView){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search,menu);
        MenuItem menuItem=menu.findItem(R.id.searchView);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);

                return false;
            }
        });
        return true;
    }





    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {//on swipe left delete the item from database
            int test=viewHolder.getAdapterPosition();
            showMessage("Delete!!","Item Deleted");
            String titleToDelete= PoisList.get(test);
            PoisList.remove(viewHolder.getAdapterPosition());
            myAdapter.notifyDataSetChanged();
            db.execSQL("DELETE FROM POIS WHERE title='"+titleToDelete+"'");
        }

    };







    public void textViewClick(View view){//click to process a POI
        txtV=findViewById(R.id.textView2);
        String title13=((TextView)view).getText().toString();
        ArrayList<String> data=new ArrayList<>();

        Cursor c = db.rawQuery("SELECT title,description,location FROM POIS WHERE TRIM(title)='"+title13.trim()+"'",null);

        while (c.moveToNext()) {
            data.add(c.getString(0));
            data.add(c.getString(1));
            data.add(c.getString(2));
        }

        String title123 = data.get(0);
        String desc123 =data.get(1);
        String loc123 = data.get(2);

        Intent intent = new Intent(this,Processing.class);
        intent.putExtra("TITLE",title123);
        intent.putExtra("DESCRIPTION",desc123);
        intent.putExtra("LOCATION",loc123);
        startActivity(intent);
    }

    public void showMessage(String title, String text) {
        new android.app.AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(title)
                .setMessage(text)
                .show();
    }

    
}