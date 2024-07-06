package com.mycompany.parser;

import com.mycompany.type.Oggetto;
import com.mycompany.type.Comandi;
import java.io.Serializable;

/**
 * Classe che rappresenta l'output del parser.
 */
public class ParserOutput implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Comandi comando;
    private Oggetto oggetto;
    private Oggetto oggettoInventario;
    private String destinazione; 

    /**
     * Costruttore che crea un oggetto ParserOutput con un comando e un oggetto.
     *
     * @param comando il comando riconosciuto
     * @param oggetto l'oggetto riconosciuto
     */
    public ParserOutput(Comandi comando, Oggetto oggetto) {
        this.comando = comando;
        this.oggetto = oggetto;
    }

    /**
     * Costruttore che crea un oggetto ParserOutput con un comando, un oggetto e un oggetto nell'inventario.
     *
     * @param comando il comando riconosciuto
     * @param oggetto l'oggetto riconosciuto
     * @param oggettoInventario l'oggetto nell'inventario riconosciuto
     */
    public ParserOutput(Comandi comando, Oggetto oggetto, Oggetto oggettoInventario) {
        this.comando = comando;
        this.oggetto = oggetto;
        this.oggettoInventario = oggettoInventario;
    }

    /**
     * Costruttore che crea un oggetto ParserOutput con un comando, un oggetto, un oggetto nell'inventario e una destinazione.
     *
     * @param comando il comando riconosciuto
     * @param oggetto l'oggetto riconosciuto
     * @param oggettoInventario l'oggetto nell'inventario riconosciuto
     * @param destinazione la destinazione riconosciuta
     */
    public ParserOutput(Comandi comando, Oggetto oggetto, Oggetto oggettoInventario, String destinazione) {
        this.comando = comando;
        this.oggetto = oggetto;
        this.oggettoInventario = oggettoInventario;
        this.destinazione = destinazione;
    }

    /**
     * Restituisce il comando riconosciuto.
     *
     * @return il comando
     */
    public Comandi getComando() {
        return comando;
    }

    /**
     * Imposta il comando riconosciuto.
     *
     * @param comando il comando da impostare
     */
    public void setComando(Comandi comando) {
        this.comando = comando;
    }

    /**
     * Restituisce l'oggetto riconosciuto.
     *
     * @return l'oggetto
     */
    public Oggetto getOggetto() {
        return oggetto;
    }

    /**
     * Imposta l'oggetto riconosciuto.
     *
     * @param oggetto l'oggetto da impostare
     */
    public void setOggetto(Oggetto oggetto) {
        this.oggetto = oggetto;
    }

    /**
     * Restituisce l'oggetto dell'inventario riconosciuto.
     *
     * @return l'oggetto dell'inventario
     */
    public Oggetto getOggettoInventario() {
        return oggettoInventario;
    }

    /**
     * Imposta l'oggetto dell'inventario riconosciuto.
     *
     * @param oggettoInventario l'oggetto dell'inventario da impostare
     */
    public void setOggettoInventario(Oggetto oggettoInventario) {
        this.oggettoInventario = oggettoInventario;
    }

    /**
     * Restituisce la destinazione riconosciuta.
     *
     * @return la destinazione
     */
    public String getDestinazione() {
        return destinazione;
    }

    /**
     * Imposta la destinazione riconosciuta.
     *
     * @param destinazione la destinazione da impostare
     */
    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }
}
