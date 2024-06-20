package com.example.restaurantproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.restaurantproject.bean.Role;

import java.util.List;

public class RoleSpinnerAdapter extends ArrayAdapter<Role> {
    private List<Role> roles;
    private LayoutInflater inflater;

    public RoleSpinnerAdapter(Context context, List<Role> roles) {
        super(context, android.R.layout.simple_spinner_item, roles);
        this.roles = roles;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(roles.get(position).getRoleName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(roles.get(position).getRoleName());

        return convertView;
    }
}
