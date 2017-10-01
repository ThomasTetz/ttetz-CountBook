package com.example.ttetz_countbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

public class EditCounter extends AppCompatActivity {

    public boolean edit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_counter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) { // editing existing counter
            String name = extras.getString("name");
            Date date = new Date();
            long dateLong = extras.getLong("date");
            date.setTime(dateLong);
            int initialCount = extras.getInt("initialCount");
            int currentCount = extras.getInt("currentCount");


        }
        else{ // creating new counter
            // button says cancel instead of delete
            // what if return? replace cancel button with return

            // do edit/save and delete/cancel
        }
    }

    public void saveChanges(){

    }

    public void deleteCounter(){

    }




}
