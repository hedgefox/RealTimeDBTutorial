package com.gamoffice.simplerealtimedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PersonaggiActivity extends AppCompatActivity {

    Button buttonAddArma;
    EditText editTextArmaName;
    SeekBar seekBarRating;
    TextView textViewRating, textViewPersonaggio;
    ListView listViewArmi;

    DatabaseReference databaseArmi;

    List<Arma> armi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaggio);

        Intent intent = getIntent();

        /*
         * this line is important
         * this time we are not getting the reference of a direct node
         * but inside the node armi we are creating a new child with the personaggio id
         * and inside that node we will store all the armi with unique ids
         * */
        databaseArmi = FirebaseDatabase.getInstance().getReference("armi").child(intent.getStringExtra(MainActivity.PERSONAGGIO_ID));

        buttonAddArma = (Button) findViewById(R.id.buttonAddArma);
        editTextArmaName = (EditText) findViewById(R.id.editTextName);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);
        textViewRating = (TextView) findViewById(R.id.textViewRating);
        textViewPersonaggio = (TextView) findViewById(R.id.textViewPersonaggio);
        listViewArmi = (ListView) findViewById(R.id.listViewArmi);

        armi = new ArrayList<>();

        textViewPersonaggio.setText(intent.getStringExtra(MainActivity.PERSONAGGIO_NAME));

        seekBarRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewRating.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonAddArma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTrack();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseArmi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                armi.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Arma arma = postSnapshot.getValue(Arma.class);
                    armi.add(arma);
                }
                ArmiList trackListAdapter = new ArmiList(PersonaggiActivity.this, armi);
                listViewArmi.setAdapter(trackListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveTrack() {
        String armaNAme = editTextArmaName.getText().toString().trim();
        int rating = seekBarRating.getProgress();
        if (!TextUtils.isEmpty(armaNAme)) {
            String id  = databaseArmi.push().getKey();
            Arma arma = new Arma(id, armaNAme, rating);
            databaseArmi.child(id).setValue(arma);
            Toast.makeText(this, "Arma salvata", Toast.LENGTH_LONG).show();
            editTextArmaName.setText("");
        } else {
            Toast.makeText(this, "Inserisci un'arma", Toast.LENGTH_LONG).show();
        }
    }
}