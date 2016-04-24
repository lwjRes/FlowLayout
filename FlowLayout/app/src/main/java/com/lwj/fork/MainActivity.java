package com.lwj.fork;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lwj.fork.view.Adapter;
import com.lwj.fork.view.FlowLayout;

import java.util.ArrayList;

public class MainActivity extends Activity {

    FlowLayout flow;
    ArrayList<String> strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flow = (FlowLayout) findViewById(R.id.flow);
        strings = new ArrayList<>();
        strings.add("11111");
        strings.add("222");
        strings.add("333");
        strings.add("44444");
        flow.setAdapter(new Adapter(strings, this));
        flow.setOnItemClickListener(new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, FlowLayout flowLayout, int position) {
                Toast.makeText(MainActivity.this, strings.get(position), Toast.LENGTH_LONG).show();
            }
        });
    }


}
