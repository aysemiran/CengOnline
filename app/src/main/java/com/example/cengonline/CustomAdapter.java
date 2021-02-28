package com.example.cengonline;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resouce;
    private ArrayList<String> list;
    private Stack colors;
    private Stack recycle;

    public CustomAdapter(Context context, int resource, ArrayList<String> list){
        super(context, resource, list);
        this.context = context;
        this.list = list;
        this.resouce = resource;
        colors = new Stack(6);
        recycle = new Stack(6);
        recycle.push(0xff66febd);
        recycle.push(0xff75E4CA);
        recycle.push(0xff85CAD7);
        recycle.push(0xff94B1E4);
        recycle.push(0xffA497F1);
        recycle.push(0xffB37DFE);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String selectedItem = list.get(i);
        LayoutInflater li = LayoutInflater.from(context);
        view = li.inflate(resouce, viewGroup, false);
        TextView text = (TextView) view.findViewById(R.id.context);
        text.setText(selectedItem);
        text.setTextColor(Color.BLACK);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 150;
        view.setLayoutParams(params);
        view.setBackgroundColor(getColor());
        return view;
    }

    public int getColor() {
        if (colors.size() == 0) {
            while(!recycle.isEmpty()) {
                colors.push(recycle.pop());
            }
        }
        int c = (int) colors.pop();
        recycle.push(c);
        return c;
    }
}
