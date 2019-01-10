package com.example.aleksej.budget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import java.lang.String;
import java.lang.reflect.Type;
import java.util.*;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class secondScreen extends AppCompatActivity {
    final Context c = this;
    String s;
    double d;
    ArrayList<budget> list = new ArrayList<budget>();
    RecyclerView recyclerView;
    adapter mAdapter;
    RecyclerView.LayoutManager manager = new LinearLayoutManager(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);
        loadData();
        Button addButton = findViewById(R.id.addButton);

        Button buttonSave = findViewById(R.id.saveButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    public void add(){
        final LayoutInflater layoutInflaterAndriod = LayoutInflater.from(c);
        View v = layoutInflaterAndriod.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(v);

        final EditText inputName = (EditText) v.findViewById(R.id.name);
        final EditText inputNumber = (EditText) v.findViewById(R.id.amount);
        buildRecycleView();
        alertDialogBuilderUserInput.setCancelable(false);
        alertDialogBuilderUserInput.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    s = inputName.getText().toString();
                    d = Double.parseDouble(inputNumber.getText().toString());
                    budget b = new budget(s, d);
                    list.add(b);
                    System.out.println(list.toString());

                } catch (NumberFormatException q) {
                    Toast.makeText(secondScreen.this, "Please Enter A Number in the Enter Amount", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialogBuilderUserInput.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    private void saveData(){
        System.out.println("in save data");
        SharedPreferences  sharedPreferences = getSharedPreferences("Shared Preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("Task List", json);
        editor.apply();
    }

    private void loadData(){
        System.out.println("in load data");
        SharedPreferences  sharedPreferences = getSharedPreferences("Shared Preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<budget>>() {}.getType();
        list = gson.fromJson(json , type);
        if(list == null){
            list = new ArrayList<>();
        }
    }


    public void removeItem(int position){
        list.remove(position);
        mAdapter.notifyItemChanged(position);
    }

    public void buildRecycleView(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mAdapter = new adapter(list);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            Intent intent = new Intent(secondScreen.this, budget_screen.class);
            intent.putExtra("Budget", list.get(position));

            startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });

    }

}
