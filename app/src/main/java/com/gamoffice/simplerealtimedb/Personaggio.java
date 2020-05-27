package com.gamoffice.simplerealtimedb;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Belal on 2/26/2017.
 */
@IgnoreExtraProperties
public class Personaggio {
    private String personaggioId;
    private String personaggioNome;
    private String personaggioClasse;

    public Personaggio() {
        //this constructor is required
    }

    public Personaggio(String personaggioId, String personaggioNome, String personaggioClasse) {
        this.personaggioId = personaggioId;
        this.personaggioNome = personaggioNome;
        this.personaggioClasse = personaggioClasse;
    }

    public String getPersonaggioId() {
        return personaggioId;
    }

    public String getPersonaggioNome() {
        return personaggioNome;
    }

    public String getPersonaggioClasse() {
        return personaggioClasse;
    }
}