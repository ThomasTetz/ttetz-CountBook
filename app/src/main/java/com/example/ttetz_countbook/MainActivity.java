package com.example.ttetz_countbook;
// reference https://www.youtube.com/watch?v=ZEEYYvVwJGY
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MainActivity extends AppCompatActivity {
    private static final String FILENAME = "file.sav";

    private ListView counterList;
    private ArrayList<Counter> counters = new ArrayList<Counter>();
    private MyListAdapter adapter;

    private ArrayList<View> viewList = new ArrayList<View>();
    public static final int REQUEST_CODE = 1;
    public static final int DELETE_CODE = -3;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public boolean fixClick = false;

    Button fixButton1;
//    View.OnClickListener clickFix1;
//    View.OnClickListener clickFix2

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton addCounterButton = (ImageButton) findViewById(R.id.addCounterButton);
        counterList = (ListView) findViewById(R.id.counterList);
//        adapter = new MyListAdapter(this, R.layout.counter_item, counters);
//        counterList.setAdapter(adapter);

        addCounterButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);

                addCounter();

            }
        });

//        counterList = (ListView) findViewById(R.id.counterList);

        counterList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                  editCounter(position);
              }
          }

        );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {

            if (resultCode == MainActivity.RESULT_OK) {
                String countersString = data.getStringExtra("counters");
                Type typeOfListOfCounter = new TypeToken<ArrayList<Counter>>(){}.getType();

                ArrayList<Counter> tmpCounts = gson.fromJson(countersString, typeOfListOfCounter);
                counters.clear();
                counters.addAll(tmpCounts);
                adapter.notifyDataSetChanged();

                myUpdate();


            } else if (resultCode == MainActivity.RESULT_CANCELED) {
                myUpdate();
            }
            else if(resultCode == MainActivity.DELETE_CODE){
                // delete from list
                String countersString = data.getStringExtra("counters");
                Type typeOfListOfCounter = new TypeToken<ArrayList<Counter>>(){}.getType();

                ArrayList<Counter> tmpCount = gson.fromJson(countersString, typeOfListOfCounter);
                counters.clear();
                counters.addAll(tmpCount);
                adapter.notifyDataSetChanged();
                myUpdate();

            }
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        fixClick = false;

        adapter = new MyListAdapter(this, R.layout.counter_item, counters);
        counterList.setAdapter(adapter);
        loadFromFile();
    }

//

    public void addCounter(){

        Intent intent  = new Intent(getApplicationContext(), EditCounter.class);

        String countersString = (new Gson().toJson(counters));
        intent.putExtra("counters", countersString);

        startActivityForResult(intent, REQUEST_CODE);

    }


    private class MyListAdapter extends ArrayAdapter<Counter> {
        private int layout;
        private ArrayList<Counter>  items = new ArrayList<Counter>();
        public MyListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Counter> objects) {
            super(context, resource, objects);
            layout = resource;
            this.items.clear();
            this.items.addAll(objects);
        }

        public ArrayList<Counter> getCounters(){
            return items;
        }

        public void setCounters(ArrayList<Counter> counters){
            this.items.clear();
            this.items.addAll(counters);
            notifyDataSetChanged();
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder = null;
            if (convertView == null){
                LayoutInflater inflator = LayoutInflater.from(getContext());
                convertView = inflator.inflate(layout, parent, false);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.resetCountButton = (ImageButton) convertView.findViewById(R.id.reset_counter_button);
                viewHolder.counterName = (TextView) convertView.findViewById(R.id.counter_name);
                viewHolder.counterDate = (TextView) convertView.findViewById(R.id.counterDate);
                viewHolder.counterCount = (TextView) convertView.findViewById(R.id.counter_value);
                viewHolder.increaseCountButton = (ImageButton) convertView.findViewById(R.id.increase_counter_button);
                viewHolder.decreaseCountButton = (ImageButton) convertView.findViewById(R.id.decrease_counter_button);


                viewHolder.resetCountButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        resetCount(position);
                        notifyDataSetChanged();
                        myUpdate();
                    }
                });


                viewHolder.increaseCountButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){

                        if (fixClick == false){ // fix discussed in README
                            fixClick = true;
                            increaseCount(position);
                            decreaseCount(position);
                            notifyDataSetChanged();
                        }
                        else{
                            increaseCount(position);
                            notifyDataSetChanged();
                            myUpdate();
                        }

                    }
                });

                viewHolder.decreaseCountButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        decreaseCount(position);
                        notifyDataSetChanged();
                        myUpdate();
                    }
                });

                convertView.setTag(viewHolder);
            }
            else{
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.counterName.setText(counters.get(position).getName());
                mainViewHolder.counterDate.setText(sdf.format(counters.get(position).getDate()));
                mainViewHolder.counterCount.setText(Integer.toString(counters.get(position).getCurrentCount()));
            }

            viewList.add(convertView);
            return convertView;

        }

    }

    public void editCounter(int position){
        String countersString = (new Gson().toJson(counters));

        Intent intent  = new Intent(getApplicationContext(), EditCounter.class);
        intent.putExtra("counters", countersString);
        intent.putExtra("position", position);

        startActivityForResult(intent, REQUEST_CODE);
    }

    public void increaseCount(int position){
        counters.get(position).incrementCount();
    }

    public void decreaseCount(int position){
        counters.get(position).decrementCount();
    }

    public void resetCount(int position){
        counters.get(position).resetCount();
    }

    public class ViewHolder{
        ImageButton resetCountButton;
        TextView counterName;
        TextView counterDate;
        TextView counterCount;
        ImageButton increaseCountButton;
        ImageButton decreaseCountButton;

    }

    public void myUpdate(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });



        counterList.invalidateViews();
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();

        clearFile();
        saveInFile();

        int i = adapter.getCount();
        TextView counterCount = (TextView) findViewById(R.id.counterCountTextView);
        counterCount.setText("Counters: " + Integer.toString(i));
    }


    /**
     *
     * Loads a list of tweets from a file.
     *
     */
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            // add dependency: File > Project Structure > app < Dependencies < + < dependency
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Counter>>() {}.getType();


            counters.clear();
            ArrayList<Counter> tmpCounts = gson.fromJson(in, listType);

            counters.addAll(tmpCounts);
            adapter.notifyDataSetChanged();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
//            counters.clear();
            counters = new ArrayList<Counter>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    /**
     *
     * Saves the current list of tweets into a file.
     *
     */
    private void saveInFile() {
        try {
//
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(counters, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     *
     * Clears data in a file.
     *
     */
    private void clearFile(){
        deleteFile(FILENAME);
    }


}

