package com.example.tasklist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;

public class MainActivity extends AppCompatActivity {

    List<String> items;
    EditText task;
    RecyclerView taskList;
    Button addTask;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addTask = findViewById(R.id.addTaskButton);
        task = findViewById(R.id.taskEditText);
        taskList = findViewById(R.id.taskList);
        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);

                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_LONG).show();
                saveItems();
            }
        };
        itemsAdapter = new ItemsAdapter(items,onLongClickListener);
        taskList.setAdapter(itemsAdapter);
        taskList.setLayoutManager(new LinearLayoutManager(this));

        addTask.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String todoItem = task.getText().toString();
                //add item and notify adapter
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size()-1);
                task.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_LONG).show();
                saveItems();
            }
            });

    }
    private File getDataFile(){
        return  new File(getFilesDir(), "data.txt");
    }
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }catch(IOException e) {
            Log.e("Main activity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    private void saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), items);
        }catch (IOException e){
            Log.e("Main Activity", "Error writing items", e);
        }
    }
}


