package com.example.mobimech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobimech.R;
import com.example.mobimech.models.Mechanics_here;

import java.util.ArrayList;

public class CustomListAdapter
/**********************************CUSTOM ADAPTER START************************/
class CustomAdapter extends BaseAdapter {
    Context c;
    ArrayList<Mechanics_here> teachers;

    public CustomAdapter(Context c, ArrayList<Mechanics_here> teachers) {
        this.c = c;
        this.teachers = teachers;
    }

    @Override
    public int getCount() {
        return teachers.size();
    }

    @Override
    public Object getItem(int position) {
        return teachers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.model, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView quoteTextView = convertView.findViewById(R.id.quoteTextView);
        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);

        final Mechanics_here s = (Mechanics_here) this.getItem(position);

        nameTextView.setText(s.getName());
        quoteTextView.setText(s.getSpecialty());
        descriptionTextView.setText(s.getDescription());

        //ONITECLICK
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, s.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}

