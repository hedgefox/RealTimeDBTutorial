package com.gamoffice.simplerealtimedb;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Belal on 2/26/2017.
 */

public class ArmiList extends ArrayAdapter<Arma> {
    private Activity context;
    List<Arma> armi;

    public ArmiList(Activity context, List<Arma> armi) {
        super(context, R.layout.layout_personaggio_list, armi);
        this.context = context;
        this.armi = armi;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_personaggio_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewRating = (TextView) listViewItem.findViewById(R.id.textViewGenre);

        Arma arma = armi.get(position);
        textViewName.setText(arma.getArmaName());
        textViewRating.setText(String.valueOf(arma.getDanno()));

        return listViewItem;
    }
}