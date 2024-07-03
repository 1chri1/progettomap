package com.mycompany.parser;

import com.mycompany.type.Oggetto;
import com.mycompany.type.Comandi;
import java.io.Serializable;

public class ParserOutput implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Comandi comando;
    private Oggetto oggetto;
    private Oggetto oggettoInventario;
    private String destinazione; 

    public ParserOutput(Comandi comando, Oggetto oggetto) {
        this.comando = comando;
        this.oggetto = oggetto;
    }

    public ParserOutput(Comandi comando, Oggetto oggetto, Oggetto oggettoInventario) {
        this.comando = comando;
        this.oggetto = oggetto;
        this.oggettoInventario = oggettoInventario;
    }

    public ParserOutput(Comandi comando, Oggetto oggetto, Oggetto oggettoInventario, String destinazione) {
        this.comando = comando;
        this.oggetto = oggetto;
        this.oggettoInventario = oggettoInventario;
        this.destinazione = destinazione;
    }

    public Comandi getComando() {
        return comando;
    }

    public void setComando(Comandi comando) {
        this.comando = comando;
    }

    public Oggetto getOggetto() {
        return oggetto;
    }

    public void setOggetto(Oggetto oggetto) {
        this.oggetto = oggetto;
    }

    public Oggetto getOggettoInventario() {
        return oggettoInventario;
    }

    public void setOggettoInventario(Oggetto oggettoInventario) {
        this.oggettoInventario = oggettoInventario;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }
}
