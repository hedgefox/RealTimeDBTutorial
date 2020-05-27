package com.gamoffice.simplerealtimedb;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //we will use these constants later to pass the personaggio name and id to another activity
    public static final String PERSONAGGIO_NAME = "com.gamoffice.simplerealtimedb.personaggioName";
    public static final String PERSONAGGIO_ID = "com.gamoffice.simplerealtimedb..personaggioid";

    //view objects
    EditText editTextName;
    Spinner spinnerClasse;
    Button buttonAddPersonaggio;
    ListView listViewPersonaggi;

    //a list to store all the personaggi from firebase database
    List<Personaggio> personaggi;

    //our database reference object
    DatabaseReference databasePersonaggi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the reference of personaggi node
        databasePersonaggi = FirebaseDatabase.getInstance().getReference("personaggi");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        spinnerClasse = (Spinner) findViewById(R.id.spinnerClasse);
        listViewPersonaggi = (ListView) findViewById(R.id.listViewPersonaggi);

        buttonAddPersonaggio = (Button) findViewById(R.id.buttonAddPersonaggio);

        //list to store personaggi
        personaggi = new ArrayList<>();


        //adding an onclicklistener to button
        buttonAddPersonaggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addPersonaggio()
                //the method is defined below
                //this method is actually performing the write operation
                addPersonaggio();
            }
        });

        listViewPersonaggi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected personaggio
                Personaggio personaggio = personaggi.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), PersonaggiActivity.class);

                //putting personaggio name and id to intent
                intent.putExtra(PERSONAGGIO_NAME, personaggio.getPersonaggioNome());
                intent.putExtra(PERSONAGGIO_ID, personaggio.getPersonaggioId());

                //starting the activity with intent
                startActivity(intent);
            }
        });

        listViewPersonaggi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Personaggio personaggio = personaggi.get(i);
                showUpdateDeleteDialog(personaggio.getPersonaggioId(), personaggio.getPersonaggioNome());
                return true;
            }
        });
    }

    /*
     * This method is saving a new personaggio to the
     * Firebase Realtime Database
     * */
    private void addPersonaggio() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();
        String classe = spinnerClasse.getSelectedItem().toString();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our personaggio
            String id = databasePersonaggi.push().getKey();

            //creating a personaggio Object
            Personaggio personaggio = new Personaggio(id, name, classe);

            //Saving the personaggio
            databasePersonaggi.child(id).setValue(personaggio);

            //setting edittext to blank again
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "Personaggio aggiunto", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Scegli un nome", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databasePersonaggi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous personaggi list
                personaggi.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting personaggio
                    Personaggio personaggio = postSnapshot.getValue(Personaggio.class);
                    //adding personaggio to the list
                    personaggi.add(personaggio);
                }

                //creating adapter
                PersonaggiList personaggiAdapter = new PersonaggiList(MainActivity.this, personaggi);
                //attaching adapter to the listview
                listViewPersonaggi.setAdapter(personaggiAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDeleteDialog(final String personaggioId, String personaggioNome) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Spinner spinnerclasse = (Spinner) dialogView.findViewById(R.id.spinnerClasse);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdatePersonaggio);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeletePersonaggio);

        dialogBuilder.setTitle(personaggioNome);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String classe = spinnerclasse.getSelectedItem().toString();
                if (!TextUtils.isEmpty(name)) {
                    updatePersonaggio(personaggioId, name, classe);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deletePersonaggio(personaggioId);
                b.dismiss();
            }
        });
    }


    private boolean updatePersonaggio(String id, String name, String genre) {
        //getting the specified personaggio reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("personaggi").child(id);

        //updating personaggio
        Personaggio personaggio = new Personaggio(id, name, genre);
        dR.setValue(personaggio);
        Toast.makeText(getApplicationContext(), "Personaggio aggiornato", Toast.LENGTH_LONG).show();
        return true;
    }


    private boolean deletePersonaggio(String id) {
        //getting the specified personaggio reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("personaggi").child(id);

        //removing personaggio
        dR.removeValue();

        //getting the tracks reference for the specified personaggi
        DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);

        //removing all tracks
        drTracks.removeValue();
        Toast.makeText(getApplicationContext(), "Personaggio cancellato", Toast.LENGTH_LONG).show();

        return true;
    }
}