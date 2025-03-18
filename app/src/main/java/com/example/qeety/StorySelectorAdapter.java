package com.example.qeety;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StorySelectorAdapter extends BaseAdapter {
    private final Context context;
    private final int[] storyName;

    public StorySelectorAdapter(Context context, int[] storyName) {
        this.context = context;
        this.storyName = storyName;
    }

    @Override
    public int getCount() {
        return storyName.length;
    }

    @Override
    public Object getItem(int position) {
        return storyName[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.story_selector, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.text_selection);
        textView.setText(storyName[position]);
        //ImageView imageView = convertView.findViewById(R.id.image_view);
        //imageView.setImageResource(storyName[position]);
        return convertView;
    }
}
