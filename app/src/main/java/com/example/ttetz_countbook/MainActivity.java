package com.example.ttetz_countbook;
// reference https://www.youtube.com/watch?v=ZEEYYvVwJGY
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String FILENAME = "file.sav";

    private ListView counterList;
    private ArrayList<Counter> counters = new ArrayList<Counter>();
    private MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton addCounterButton = (ImageButton) findViewById(R.id.addCounterButton);
        counterList = (ListView) findViewById(R.id.counterList);

        addCounterButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);

                openCounterEdit();

            }
        });

        counterList = (ListView) findViewById(R.id.counterList);

        counterList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                  Toast.makeText(MainActivity.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
              }
          }

        );

//    myUpdate();

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
//		String[] tweets = loadFromFile();
//        loadFromFile();
        generateListContent();
        adapter = new MyListAdapter(this, R.layout.counter_item, counters);
        counterList.setAdapter(adapter);
    }

    private void generateListContent(){
//        for (int i = 0; i<5; i++){
//            counters.add(new Counter("Candy", 0, 12, "Comment"));
//        }
        counters.add(new Counter("Apple", 0, 12, "Comment A"));
        counters.add(new Counter("Banana", 1, 13, "Comment B"));
        counters.add(new Counter("Carrot", 2, 14, "Comment C"));
        counters.add(new Counter("Dino", 3, 15, "Comment D"));

    }

    public void openCounterEdit(){
        counters.add(new Counter("Test", 0, 1));
        counters.add(new Counter("Test", 0, 1, "comment"));
        myUpdate();
//        adapter.notifyDataSetChanged();
//        tweets.add(new NormalTweet(text));
//        adapter.notifyDataSetChanged();
    }


    private class MyListAdapter extends ArrayAdapter<Counter> {
        private int layout;
        public MyListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Counter> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder = null;
            if (convertView == null){
                LayoutInflater inflator = LayoutInflater.from(getContext());
                convertView = inflator.inflate(layout, parent, false);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.editCounterButton = (ImageButton) convertView.findViewById(R.id.edit_counter_button);
                viewHolder.counterName = (TextView) convertView.findViewById(R.id.counter_name);
                viewHolder.counterCount = (TextView) convertView.findViewById(R.id.counter_value);
                viewHolder.increaseCountButton = (ImageButton) convertView.findViewById(R.id.increase_counter_button);
                viewHolder.decreaseCountButton = (ImageButton) convertView.findViewById(R.id.decrease_counter_button);


//                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_thumbnail);
//                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
//                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);
                viewHolder.editCounterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Edit clicked for list item " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolder.increaseCountButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        Toast.makeText(getContext(), "Increase clicked for list item " + position, Toast.LENGTH_SHORT).show();
                    }
                });

                viewHolder.decreaseCountButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        Toast.makeText(getContext(), "Decrease clicked for list item " + position, Toast.LENGTH_SHORT).show();
                    }
                });


                convertView.setTag(viewHolder);
            }
            else{
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.counterName.setText(counters.get(position).getName());
                mainViewHolder.counterCount.setText(Integer.toString(counters.get(position).getCurrentCount()));
//                System.out.println(counters.toString());
//                System.out.println(counters.get(position).toString());

            }


            return convertView;
        }

    }

    public class ViewHolder{
        ImageButton editCounterButton;
        TextView counterName;
        TextView counterCount;
        ImageButton increaseCountButton;
        ImageButton decreaseCountButton;

//        ImageView thumbnail;
//        TextView title;
//        Button button;
    }

    public void myUpdate(){
        adapter.notifyDataSetChanged();
        int i = adapter.getCount();
        TextView counterCount = (TextView) findViewById(R.id.counterCountTextView);
        counterCount.setText("Counters: " + Integer.toString(i));
    }
}