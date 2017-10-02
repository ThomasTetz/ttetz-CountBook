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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        System.out.println("Creating main");

//        fixButton1 = (Button) findViewById(R.id.fixButton);

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
//                  Toast.makeText(MainActivity.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
                  editCounter(position);
              }
          }

        );

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) { // editing existing counter
//
////            int position = extras.getInt("position");
////            name = extras.getString("name");
////            date = new Date();
////            dateLong = extras.getLong("date");
////            date.setTime(dateLong);
////            initialCount = extras.getInt("initialCount");
////            currentCount = extras.getInt("currentCount");
////            comment = extras.getString("comment");
////
////            counters.add(new Counter(name, initialCount, currentCount, comment));
//        }

//    myUpdate();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {

            if (resultCode == MainActivity.RESULT_OK) {
                String countersString = data.getStringExtra("counters");
                Type typeOfListOfCounter = new TypeToken<ArrayList<Counter>>(){}.getType();

//                System.out.println("Giving to edit\n"+adapter.getCounters());
//                counters.clear();
                ArrayList<Counter> tmpCounts = gson.fromJson(countersString, typeOfListOfCounter);
//                adapter.setCounters(tmpCounts);
                counters.clear();
                counters.addAll(tmpCounts);
                adapter.notifyDataSetChanged();

//                adapter.

//                counters.addAll(tmpCounts);
//                counters = gson.fromJson(countersString, typeOfListOfCounter);
//                counterList
                System.out.println("Received from edit\n"+counters.size()+"\n"+counters);
//                adapter = new MyListAdapter(this, R.layout.counter_item, counters);
//                counterList.setAdapter(adapter);
                myUpdate();

//                counters = new Gson().fromJson(countersString,  ArrayList.class);
                System.out.println("Saved\n"+counters);
//                int result = data.getIntExtra("pos");
                // do something with the result

            } else if (resultCode == MainActivity.RESULT_CANCELED) {
                System.out.println("Cancelled");
                myUpdate();
                // some stuff that will happen if there's no result
            }
            else if(resultCode == MainActivity.DELETE_CODE){
                // delete from list
                System.out.println("Deleting");
                String countersString = data.getStringExtra("counters");
                Type typeOfListOfCounter = new TypeToken<ArrayList<Counter>>(){}.getType();
//                counters = gson.fromJson(countersString, typeOfListOfCounter);
//                counters.clear();

                ArrayList<Counter> tmpCount = gson.fromJson(countersString, typeOfListOfCounter);
//                adapter.setCounters(tmpCount);
                counters.clear();
                counters.addAll(tmpCount);
                adapter.notifyDataSetChanged();
//                counters.addAll(tmpCount);
                myUpdate();

            }
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
//		String[] tweets = loadFromFile();
        fixClick = false;

        System.out.println("Starting main");
//        generateListContent();
        adapter = new MyListAdapter(this, R.layout.counter_item, counters);
        counterList.setAdapter(adapter);
        loadFromFile();
//        myUpdate();
    }

    private void generateListContent(){
//        for (int i = 0; i<5; i++){
//            counters.add(new Counter("Candy", 0, 12, "Comment"));
//        }
        counters.add(new Counter("Apple", 0, 12, "Comment A", new Date()));
//        counters.add(new Counter("Banana", 1, 13, "Comment B"));
//        counters.add(new Counter("Carrot", 2, 14, "Comment C"));
//        counters.add(new Counter("Dino", 3, 15, "Comment D"));

    }

    public void addCounter(){
//        counters.add(new Counter("Test", 0, 1, "comment", new Date()));
//        counters.add(new Counter("Test", 0, 1, "comment"));
//        adapter.add(new Counter("Test", 3, 4, "comment", new Date()));

        Intent intent  = new Intent(getApplicationContext(), EditCounter.class);

        String countersString = (new Gson().toJson(counters));
        intent.putExtra("counters", countersString);

        startActivityForResult(intent, REQUEST_CODE);


//        myUpdate();
//        adapter.notifyDataSetChanged();
//        tweets.add(new NormalTweet(text));
//        adapter.notifyDataSetChanged();
    }


    private class MyListAdapter extends ArrayAdapter<Counter> {
        private int layout;
        private ArrayList<Counter>  items = new ArrayList<Counter>();
        public MyListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Counter> objects) {
            super(context, resource, objects);
            layout = resource;
//            this.items = objects;
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
//        public void myRefresh(ArrayList<Counter> counters2){
////            this.objects = counters2;
//            notifyDataSetChanged();
//        }

        public void notify(ArrayList<Counter> list){
            this.items= list;
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
//                int counterCount = viewHolder.counterCount.get


//                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_thumbnail);
//                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
//                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);


                viewHolder.resetCountButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
//                        Toast.makeText(getContext(), "Increase clicked for list item " + position, Toast.LENGTH_SHORT).show();
                        resetCount(position);
                        notifyDataSetChanged();
                        myUpdate();
                    }
                });


                viewHolder.increaseCountButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
//                        Toast.makeText(getContext(), "Increase clicked for list item " + position, Toast.LENGTH_SHORT).show();
                        if (fixClick == false){
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
//                        Toast.makeText(getContext(), "Decrease clicked for list item " + position, Toast.LENGTH_SHORT).show();
                        decreaseCount(position);
                        notifyDataSetChanged();
                        myUpdate();
//                        mainViewHolder.counterCount.setText(Integer.toString(counters.get(position).getCurrentCount()));
//                        Toast.makeText(getContext(), "Decrease clicked for list item " + position
//                                + ": " + counterCount.get, Toast.LENGTH_SHORT).show();
                    }
                });
//                viewHolder.decreaseCountButton.getOn
//                fixButton1 = viewHolder.increaseCountButton;
//                fixButton2 = viewHolder.decreaseCountButton;

//               fixButton1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v){
////                        Toast.makeText(getContext(), "Decrease clicked for list item " + position, Toast.LENGTH_SHORT).show();
//                        increaseCount(position);
//                        notifyDataSetChanged();
//                        decreaseCount(position);
//                        notifyDataSetChanged();
//                        myUpdate();
////                        mainViewHolder.counterCount.setText(Integer.toString(counters.get(position).getCurrentCount()));
////                        Toast.makeText(getContext(), "Decrease clicked for list item " + position
////                                + ": " + counterCount.get, Toast.LENGTH_SHORT).show();
//                    }
//                });



                convertView.setTag(viewHolder);
            }
            else{
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.counterName.setText(counters.get(position).getName());
                System.out.println("updating name stuff");
                System.out.println("view ListView "+ counterList);
                System.out.println("view counters " + counters);
                mainViewHolder.counterDate.setText(sdf.format(counters.get(position).getDate()));
                mainViewHolder.counterCount.setText(Integer.toString(counters.get(position).getCurrentCount()));
//                System.out.println(counters.toString());
//                System.out.println(counters.get(position).toString());

            }

            viewList.add(convertView);
            return convertView;

        }

    }

    public void editCounter(int position){
        String countersString = (new Gson().toJson(counters));

        Intent intent  = new Intent(getApplicationContext(), EditCounter.class);
        // could make Counter parcelable or serializable and pass the custom object in a bundle
        intent.putExtra("counters", countersString);
        intent.putExtra("position", position);
//        intent.putExtra("name", counters.get(position).getName());
//        intent.putExtra("date", counters.get(position).getDate().getTime());
//        intent.putExtra("initialCount", counters.get(position).getInitialCount());
//        intent.putExtra("currentCount", counters.get(position).getCurrentCount());
//        intent.putExtra("comment", counters.get(position).getComment());
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

//        ImageView thumbnail;
//        TextView title;
//        Button button;
    }

    public void myUpdate(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

//        ImageButton fixButton1 = (ImageButton) viewList.get(0).findViewById(R.id.increase_counter_button);
//        fixButton1.callOnClick();
//        ImageButton fixButton2 = (ImageButton) viewList.get(0).findViewById(R.id.decrease_counter_button);
//        fixButton2.callOnClick();


//        View view = counterList.getFirstVisiblePosition();
//˜

//        int pos = 0;
//        counterList.setItemChecked(pos, true);
//        View wantedView = adapter.getView(pos, null, counterList);
//        wantedView.counter_name

//        counterList.setAdapter(adapter);

//        counters.clear();
//        counters.addAll();
//        adapter.getView();
//        if (fixButton1 != null){
//            fixButton1.performClick();
//        }

//        public View getView(final int position, View convertView, ViewGroup parent);

//
//        ArrayList<Counter> t2 = adapter.getCounters();
//        if (t2.size()> 0){
//            Counter c2 = t2.get(0);
//            c2.incrementCount();
//            c2.decrementCount();
//
//        }

//        counter_item a = counterList.getFirstVisiblePosition().getOnclick;
//        getListView.getFirstVisiblePosition

//        adapter = new MyListAdapter(getActivity(), 0, counters);
        System.out.println("my ListView "+ counterList);
        System.out.println("my counters " + counters);

//        for (int i=0;i<counters.size();i++){
//            View view = counterList.getChildAt(i);
//            System.out.println(".\nmy view: \n"+view+"\n.");
//
//            TextView tvName = (TextView) view.findViewById(R.id.counter_name);
//            System.out.println("name: "+tvName.getText().toString());
////            HexCode he = vitaminsList.get(i);
////            he.setName(e.getText().toString());
//
//        }
        counterList.invalidateViews();
        adapter.notifyDataSetChanged();
//        counterList.setAdapter(adapter);
//        adapter.setCounters(counters);
        adapter.notifyDataSetChanged();
//        counterList.destroyDrawingCache();
//        counterList.setVisibility(ListView.INVISIBLE);
//        counterList.setVisibility(ListView.VISIBLE);
//        counterList.invalidateViews();
//        counterList.setAdapter(adapter);
        clearFile();
        saveInFile();
        // for each item, set values manually

//        adapter.myRefresh();
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
//		ArrayList<String> tweets = new ArrayList<String>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            // add dependency: File > Project Structure > app < Dependencies < + < dependency
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Counter>>() {}.getType();
//            counters = gson.fromJson(in, listType);


            counters.clear();
            ArrayList<Counter> tmpCounts = gson.fromJson(in, listType);
//            if (tmpCounts != null) {
////                adapter.setCounters(tmpCounts);
////                counters.addAll(tmpCounts);
//            }
            counters.addAll(tmpCounts);
            adapter.notifyDataSetChanged();
//            String line = in.readLine();
//			while (line != null) {
//				tweets.add(line);
//				line = in.readLine();
//			}

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
//		return tweets.toArray(new String[tweets.size()]);
    }



    /**
     *
     * Saves the current list of tweets into a file.
     *
     */
    private void saveInFile() {
        try {
//			FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
//
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(counters, writer);
            writer.flush();
// fos.write(new String(date.toString() + " | " + text+"\n")
//					.getBytes());
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

