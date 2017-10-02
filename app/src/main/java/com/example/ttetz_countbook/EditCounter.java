package com.example.ttetz_countbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditCounter extends AppCompatActivity {

    boolean edit; // if editing an existing counter

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    Counter counter;
    String name;
    String countersString;
    Date date;
    int initialCount;
    int currentCount;
    String comment;
    int position;

    EditText nameEditText;
    EditText initialCountEditText;
    EditText currentCountEditText;
    EditText commentEditText;
    TextView dateText;

    Gson gson = new Gson();


    private ArrayList<Counter> editCounters = new ArrayList<Counter>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_counter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        Button deleteButton = (Button) findViewById(R.id.deleteButton);

        nameEditText = (EditText) findViewById(R.id.editName);
        initialCountEditText = (EditText) findViewById(R.id.editInitialCount);
        currentCountEditText = (EditText) findViewById(R.id.editCurrentCount);
        commentEditText = (EditText) findViewById(R.id.editComment);
        dateText = (TextView) findViewById(R.id.dateText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) { // editing existing counter
            edit = false;

            countersString = extras.getString("counters");

            Type typeOfListOfCounter = new TypeToken<ArrayList<Counter>>() {
            }.getType();
            editCounters = gson.fromJson(countersString, typeOfListOfCounter);

            if (extras.containsKey("position")) {
                edit = true;
                position = extras.getInt("position");
                counter = editCounters.get(position);
                name = counter.getName();
                date = counter.getDate();
                initialCount = counter.getInitialCount();
                currentCount = counter.getCurrentCount();
                comment = counter.getComment();

                nameEditText.setText(name);
                initialCountEditText.setText(Integer.toString(initialCount));
                currentCountEditText.setText(Integer.toString(currentCount));
                commentEditText.setText(comment);
                dateText.setText(sdf.format(date));

            }

        } else { // creating new counter
            System.out.println("Should've gotten counters");
        }

        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);

                if (nameEditText.getText().toString().equals("") || initialCountEditText.getText().toString().equals("") ){
                    Toast.makeText(EditCounter.this, "Error: name and initial value required", Toast.LENGTH_SHORT).show();

                }
                else{
                    saveChanges();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                cancelChanges();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                deleteCounter();
            }
        });


    }

    public void saveChanges(){
        // get values from stuff
        String newName = nameEditText.getText().toString();
        int newInititalCount  = Integer.parseInt(initialCountEditText.getText().toString());
        int newCurrentCount;

        if (currentCountEditText.getText().toString().equals("")){
            newCurrentCount = newInititalCount;
        }
        else{
            newCurrentCount = Integer.parseInt(currentCountEditText.getText().toString());
        }

        String newComment = commentEditText.getText().toString();


        if (edit == false){ // new counter

            Counter newCounter = new Counter(newName, newInititalCount, newCurrentCount, newComment, new Date());

            editCounters.add(newCounter);
        }
        else{ // editing counter
            if (newCurrentCount != currentCount){ // check if date should be updated
                editCounters.set(position, new Counter(newName, newInititalCount, newCurrentCount, newComment, new Date()));

            }
            else{ // date shouldn't change
                editCounters.set(position, new Counter(newName, newInititalCount, newCurrentCount, newComment, date));
            }

        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        countersString = (new Gson().toJson(editCounters));
        intent.putExtra("counters", countersString);

        setResult(MainActivity.RESULT_OK, intent);
        finish();

    }
    public void cancelChanges(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        setResult(MainActivity.RESULT_CANCELED, intent);
        finish();
    }
    public void deleteCounter(){

        editCounters.remove(position);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        countersString = (new Gson().toJson(editCounters));
        intent.putExtra("counters", countersString);
        setResult(MainActivity.DELETE_CODE, intent);
        finish();
    }
}
