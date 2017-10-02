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

    // detailed date format when viewing a counter
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    // instantiate counter attributes
    Counter counter;
    String name;
    String countersString;
    Date date;
    int initialCount;
    int currentCount;
    String comment;
    int position;

    // instantiate counter_item attributes
    EditText nameEditText;
    EditText initialCountEditText;
    EditText currentCountEditText;
    EditText commentEditText;
    TextView dateText;

    // for data formatting to pass/save/load
    Gson gson = new Gson();

    // ArrayList of Counters
    private ArrayList<Counter> editCounters = new ArrayList<Counter>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_counter);

        // disable the back button in top left in favour of custom cancelButton
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // initialize the buttons for user actions
        Button saveButton = (Button) findViewById(R.id.saveButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        Button deleteButton = (Button) findViewById(R.id.deleteButton);

        // initialize the fields for user inputs and their lables
        nameEditText = (EditText) findViewById(R.id.editName);
        initialCountEditText = (EditText) findViewById(R.id.editInitialCount);
        currentCountEditText = (EditText) findViewById(R.id.editCurrentCount);
        commentEditText = (EditText) findViewById(R.id.editComment);
        dateText = (TextView) findViewById(R.id.dateText);

        // get the data that was passed to this activity via intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            edit = false;

            countersString = extras.getString("counters");

            Type typeOfListOfCounter = new TypeToken<ArrayList<Counter>>() {
            }.getType();
            editCounters = gson.fromJson(countersString, typeOfListOfCounter);

            if (extras.containsKey("position")) { // editing existing counter, get the values
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

        // action to save values
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                if (nameEditText.getText().toString().equals("") || initialCountEditText.getText().toString().equals("") ){
                    // require name and initial counter
                    Toast.makeText(EditCounter.this, "Error: name and initial value required", Toast.LENGTH_SHORT).show();

                }
                else{
                    saveChanges();
                }
            }
        });

        // action to cancel changes
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                cancelChanges();
            }
        });

        // action to delete the selected Counter
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                deleteCounter();
            }
        });


    }


    public void saveChanges(){
        // get the values from input widgets and convert to a Counter
        String newName = nameEditText.getText().toString();
        int newInititalCount  = Integer.parseInt(initialCountEditText.getText().toString());
        int newCurrentCount;


        if (currentCountEditText.getText().toString().equals("")){
            // if no currentCount: make the same as initialCount
            newCurrentCount = newInititalCount;
        }
        else{
            // currentCount given: use
            newCurrentCount = Integer.parseInt(currentCountEditText.getText().toString());
        }

        String newComment = commentEditText.getText().toString();


        if (edit == false){ // creating new counter, not editing existing one

            Counter newCounter = new Counter(newName, newInititalCount, newCurrentCount, newComment, new Date());
            editCounters.add(newCounter);
        }
        else{ // editing an existing counter
            if (newCurrentCount != currentCount){
                // if the date should be updated
                editCounters.set(position, new Counter(newName, newInititalCount, newCurrentCount, newComment, new Date()));

            }
            else{
                // date shouldn't change because currentCount is the same
                editCounters.set(position, new Counter(newName, newInititalCount, newCurrentCount, newComment, date));
            }

        }

        // pass the data back to MainActivity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        countersString = (new Gson().toJson(editCounters));
        intent.putExtra("counters", countersString);
        setResult(MainActivity.RESULT_OK, intent);
        finish(); // end the activity

    }

    // forget the changes and return to MainActivity
    public void cancelChanges(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        setResult(MainActivity.RESULT_CANCELED, intent);
        finish();
    }

    // delete the selected counter and return the reduced list to MainActivity
    public void deleteCounter(){
        editCounters.remove(position);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        countersString = (new Gson().toJson(editCounters));
        intent.putExtra("counters", countersString);
        setResult(MainActivity.DELETE_CODE, intent);
        finish();
    }
}
