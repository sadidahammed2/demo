package com.nomann.practice_all_inyears;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
public class dataadd extends AppCompatActivity {

    SQLiteDatabase myy;
    RecyclerView recyclerView;
    EditText editTextText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataadd);

        // ১. ডাটাবেস এবং টেবিল সেটআপ
        myy = openOrCreateDatabase("BabsayeeDB", MODE_PRIVATE, null);
        myy.execSQL("CREATE TABLE IF NOT EXISTS my_table (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");

        // ২. ভিউ পরিচিতি
        editTextText = findViewById(R.id.editTextText);
        Button button4 = findViewById(R.id.button4);
        recyclerView = findViewById(R.id.reci);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ৩. শুরুতে ডাটা লোড করা
        refreshList();

        // ৪. বাটন ক্লিক লজিক
        button4.setOnClickListener(v -> {
            String name = editTextText.getText().toString();
           inset(name);
        });
    }

    // ডাটাবেস থেকে সব ডাটা নিয়ে RecyclerView-তে দেখানো
    public void refreshList() {
        Cursor cursor = myy.rawQuery("select * from my_table", null);
        String[] data = new String[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            data[i] = cursor.getString(1); // ১ মানে 'name' কলাম
            i++;
        }
        cursor.close();
        recyclerView.setAdapter(new CustomAdapter(data));
    }

    public void inset(String text) {
        ContentValues conval = new ContentValues();
        conval.put("name", text);
        myy.insert("my_table", null, conval);
        Toast.makeText(this, "সেভ হয়েছে!", Toast.LENGTH_SHORT).show();
    }

    // --- ছোট এবং সহজ অ্যাডাপ্টার ক্লাস ---
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private String[] localDataSet;

        public CustomAdapter(String[] dataSet) {
            localDataSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.dataadditem, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.textView3.setText(localDataSet[position]);
        }

        @Override
        public int getItemCount() {
            return localDataSet.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView3;
            public ViewHolder(View view) {
                super(view);
                textView3 = view.findViewById(R.id.textView3);
            }
        }
    }
}