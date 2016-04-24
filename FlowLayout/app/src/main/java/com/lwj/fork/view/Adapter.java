package com.lwj.fork.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lwj on 16/4/24.
 * Des:
 */
public class Adapter extends FlowLayout.BaseAdapter {
    ArrayList<String> strings;
    Context context;
    public Adapter(  ArrayList<String> strings,Context context) {
        super();
        this.strings = strings;
        this.context = context;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public View getItemView(int index, FlowLayout parent) {
        TextView textView = new TextView(context);
        textView.setText(strings.get(index));
        return textView;
    }
}