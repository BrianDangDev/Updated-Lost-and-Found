package com.example.a71;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AdvertListAdapter extends ArrayAdapter<Advert> {

    public AdvertListAdapter(Context context, List<Advert> adverts) {
        super(context, 0, adverts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_advert, parent, false);
        }

        Advert advert = getItem(position);

        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
        TextView textViewDescription = convertView.findViewById(R.id.textViewDescription);

        String title = advert.getPostType() + ": " + advert.getDescription();
        textViewTitle.setText(title);
        textViewDescription.setText(advert.getDate());

        return convertView;
    }
    public void updateData(List<Advert> newData) {
        clear();
        addAll(newData);
        notifyDataSetChanged();
    }
}
