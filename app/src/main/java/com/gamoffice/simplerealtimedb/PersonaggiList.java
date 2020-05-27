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

public class PersonaggiList extends ArrayAdapter<Personaggio> {
    private Activity context;
    List<Personaggio> personaggi;

    public PersonaggiList(Activity context, List<Personaggio> personaggi) {
        super(context, R.layout.layout_personaggio_list, personaggi);
        this.context = context;
        this.personaggi = personaggi;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_personaggio_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewGenre);

        Personaggio personaggio = personaggi.get(position);
        textViewName.setText(personaggio.getPersonaggioNome());
        textViewGenre.setText(personaggio.getPersonaggioClasse());

        return listViewItem;
    }
}