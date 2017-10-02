package com.example.ttetz_countbook;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    long dateLong;
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


    private ArrayList<Counter> counters = new ArrayList<Counter>();
//    counters.add(new Counter("Apple", 0, 12, "Comment A"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("\n\nCreated edit\n\n");
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
            counters = gson.fromJson(countersString, typeOfListOfCounter);

            if (extras.containsKey("position")) {
                edit = true;
                position = extras.getInt("position");
                counter = counters.get(position);
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


//            counters = new Gson().fromJson(countersString, ArrayList.class);


            System.out.println("Counters given to edit\n" + counters);

//            name = extras.getString("name");
//            date = new Date();
//            dateLong = extras.getLong("date");
//            date.setTime(dateLong);
//            initialCount = extras.getInt("initialCount");
//            currentCount = extras.getInt("currentCount");
//            comment = extras.getString("comment");


        } else { // creating new counter
            // button says cancel instead of delete
            // what if return? replace cancel button with return
            System.out.println("Should've gotten counters");
//            edit = false;
//            name = "";
//            // set date on save
//            initialCount = 0;
//            currentCount = 0;
//            position = -1; // new value flag
            // do edit/save and delete/cancel
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
        System.out.println("Trying to save");
        Log.d("MOO", "Trying to save");
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

//            date = new Date(); // if new or anything is changed
            System.out.println("Before add in edit\n"+counters);
            System.out.println("Adding in edit");
            counters.add(newCounter);
        }
        else{ // editing counter
            System.out.println("Edited existin in edit");
            if (newCurrentCount != currentCount){ // check if date should be updated
                counters.set(position, new Counter(newName, newInititalCount, newCurrentCount, newComment, new Date()));
//                counter.setName(newName);
//                counter.setInitialCount(newInititalCount);
//                counter.setCurrentCount(newCurrentCount);
//                counter.setComment(newComment);
//                counter.setDate(new Date());
            }
            else{ // date shouldn't change
                counters.set(position, new Counter(newName, newInititalCount, newCurrentCount, newComment, date));
//                counter
            }

        }



        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        System.out.println("Counters leaving edit\n"+counters);
        countersString = (new Gson().toJson(counters));
        intent.putExtra("counters", countersString);
//        intent.putExtra("position", position);
//        intent.putExtra("name", newName);
//        intent.putExtra("date", date);
//        intent.putExtra("initialCount", newInititalCount);
//        intent.putExtra("currentCount", newCurrentCount);
//        intent.putExtra("comment", newComment);


//        intent.putExtra("pos", position);

        setResult(MainActivity.RESULT_OK, intent);
        finish();
//        startActivity(intent);



    }
    public void cancelChanges(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        setResult(MainActivity.RESULT_CANCELED, intent);
        finish();
    }
    public void deleteCounter(){
        // ?
        // should just give the counterlist
        counters.remove(position);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        countersString = (new Gson().toJson(counters));
        intent.putExtra("counters", countersString);
        setResult(MainActivity.DELETE_CODE, intent);
        finish();
    }




}
