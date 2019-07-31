package com.example.bingi.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


class customAdapter extends ArrayAdapter<String[]>{

    customAdapter(@NonNull Context context, String[][] data) {
        super(context,R.layout.custom_row, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater tempinflater = LayoutInflater.from(getContext());
        @SuppressLint("ViewHolder") View customview = tempinflater.inflate(R.layout.custom_row,parent,false);

        String[] single_data = getItem(position);
        TextView list_content = customview.findViewById(R.id.message_list_content);
        TextView list_user = customview.findViewById(R.id.message_list_user);

        assert single_data != null;
        list_content.setText(single_data[1]);
        String colon_text = single_data[0]+":";
        list_user.setText(colon_text);

        return customview;
    }

}
