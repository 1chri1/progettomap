package com.mycompany.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe che rappresenta una stanza all'interno del gioco.
 * Ogni stanza ha un identificatore univoco e può contenere oggetti.
 * È possibile spostarsi da una stanza all'altra tramite i punti cardinali nord, sud, est e ovest.
 * La visibilità della stanza può essere modificata.
 * La classe implementa i metodi hashCode e equals per garantire la corretta gestione delle stanze in una struttura dati.
 * 
 * Inoltre, utilizza una mappa per gestire le stanze in base al piano e al nome.
 * 
 * @autore Tommaso
 */
public class Stanza implements Serializable {
    private static final long serialVersionUID = 1L; // Versione della serializzazione

    private final int piano; // Identificatore del piano
    private final String nome; // Nome della stanza
    private String descrizione; // Descrizione della stanza
    private String guarda; // Informazioni aggiuntive visualizzabili quando si osserva la stanza
    private boolean visibile = true; // Flag che indica se la stanza è visibile o meno
    
    private Stanza nord = null; // Stanza adiacente a nord
    private Stanza sud = null; // Stanza adiacente a sud
    private Stanza est = null; // Stanza adiacente a est
    private Stanza ovest = null; // Stanza adiacente a ovest

    private final List<Oggetto> oggetti = new ArrayList<>(); // Lista degli oggetti presenti nella stanza
    private final Set<String> alias = new HashSet<>(); // Insieme degli alias per la stanza

    // Mappa delle stanze per identificare il piano e il nome
    private static final Map<String, Stanza> mappaStanze = new HashMap<>();

    /**
     * Costruttore della classe Stanza.
     * 
     * @param piano Identificatore del piano
     * @param nome Nome della stanza
     */
    public Stanza(int piano, String nome) {
        this.piano = piano;
        this.nome = nome;
        mappaStanze.put(generaChiave(piano, nome), this);
    }

    /**
     * Costruttore della classe Stanza.
     * 
     * @param piano Identificatore del piano
     * @param nome Nome della stanza
     * @param descrizione Descrizione della stanza
     */
    public Stanza(int piano, String nome, String descrizione) {
        this.piano = piano;
        this.nome = nome;
        this.descrizione = descrizione;
        mappaStanze.put(generaChiave(piano, nome), this);
    }
    
    /**
     * Costruttore della classe Stanza.
     * 
     * @param piano Identificatore del piano
     * @param nome Nome della stanza
     * @param descrizione Descrizione della stanza
     * @param guarda Per la descrizione degli ambienti nella stanza
     */
    public Stanza(int piano, String nome, String descrizione, String guarda) {
        this.piano = piano;
        this.nome = nome;
        this.descrizione = descrizione;
        this.guarda = guarda;
        mappaStanze.put(generaChiave(piano, nome), this);
    }

    /**
     * Genera una chiave univoca per la stanza basata sul piano e sul nome.
     * 
     * @param piano Identificatore del piano
     * @param nome Nome della stanza
     * @return Chiave univoca per la mappa delle stanze
     */
    private static String generaChiave(int piano, String nome) {
        return piano + "," + nome;
    }

    /**
     * Trova una stanza basata sul piano e sul nome.
     * 
     * @param piano Identificatore del piano
     * @param nome Nome della stanza
     * @return La stanza corrispondente, null se non trovata
     */
    public static Stanza trovaStanza(int piano, String nome) {
        return mappaStanze.get(generaChiave(piano, nome));
    }

    /**
     * Restituisce il nome della stanza.
     * 
     * @return Nome della stanza
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il piano della stanza.
     * 
     * @return Piano della stanza
     */
    public int getPiano() {
        return piano;
    }

    /**
     * Restituisce la descrizione della stanza.
     * 
     * @return Descrizione della stanza
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione della stanza.
     * 
     * @param descrizione Descrizione della stanza
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce true se la stanza è visibile, false altrimenti.
     * 
     * @return true se la stanza è visibile, false altrimenti
     */
    public boolean isVisibile() {
        return visibile;
    }

    /**
     * Imposta la visibilità della stanza.
     * 
     * @param visibile true se la stanza è visibile, false altrimenti
     */
    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }

    /**
     * Restituisce la stanza adiacente a nord.
     * 
     * @return Stanza adiacente a nord
     */
    public Stanza getNord() {
        return nord;
    }

    /**
     * Imposta la stanza adiacente a nord.
     * 
     * @param nord Stanza adiacente a nord
     */
    public void setNord(Stanza nord) {
        this.nord = nord;
    }

    /**
     * Restituisce la stanza adiacente a sud.
     * 
     * @return Stanza adiacente a sud
     */
    public Stanza getSud() {
        return sud;
    }

    /**
     * Imposta la stanza adiacente a sud.
     * 
     * @param sud Stanza adiacente a sud
     */
    public void setSud(Stanza sud) {
        this.sud = sud;
    }

    /**
     * Restituisce la stanza adiacente a est.
     * 
     * @return Stanza adiacente a est
     */
    public Stanza getEst() {
        return est;
    }

    /**
     * Imposta la stanza adiacente a est.
     * 
     * @param est Stanza adiacente a est
     */
    public void setEst(Stanza est) {
        this.est = est;
    }

    /**
     * Restituisce la stanza adiacente a ovest.
     * 
     * @return Stanza adiacente a ovest
     */
    public Stanza getOvest() {
        return ovest;
    }

    /**
     * Imposta la stanza adiacente a ovest.
     * 
     * @param ovest Stanza adiacente a ovest
     */
    public void setOvest(Stanza ovest) {
        this.ovest = ovest;
    }

    /**
     * Restituisce la lista degli oggetti presenti nella stanza.
     * 
     * @return Lista degli oggetti presenti nella stanza
     */
    public List<Oggetto> getOggetti() {
        return oggetti;
    }

    /**
     * Calcola e restituisce il codice hash per la stanza.
     * 
     * @return Codice hash della stanza
     */
    @Override
    public int hashCode() {
        return generaChiave(piano, nome).hashCode();
    }
    
    /**
     * Verifica se un oggetto con un determinato identificatore è presente nella stanza.
     * 
     * @param id Identificatore dell'oggetto da cercare
     * @return Oggetto con l'identificatore specificato, null se non trovato
     */
    public Oggetto getOggetto(int id) {
        for (Oggetto o : oggetti) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    /**
     * Restituisce le informazioni aggiuntive visualizzabili quando si osserva la stanza.
     * 
     * @return Informazioni aggiuntive visualizzabili quando si osserva la stanza
     */
    public String getGuarda() {
        return guarda;
    }

    /**
     * Imposta le informazioni aggiuntive visualizzabili quando si osserva la stanza.
    *
    * @param guarda Informazioni aggiuntive visualizzabili quando si osserva la stanza
    */
    public void setGuarda(String guarda) {
        this.guarda = guarda;
    }

    /**
     * Restituisce l'insieme degli alias della stanza.
     * 
     * @return Insieme degli alias della stanza
     */
    public Set<String> getAlias() {
        return alias;
    }

    /**
     * Imposta gli alias della stanza.
     * 
     * @param alias Insieme degli alias della stanza
     */
    public void setAlias(Set<String> alias) {
        this.alias.clear();
        this.alias.addAll(alias);
    }

    /**
     * Imposta gli alias della stanza usando un array.
     * 
     * @param alias Array di alias della stanza
     */
    public void setAlias(String[] alias) {
        this.alias.clear();
        this.alias.addAll(Arrays.asList(alias));
    }

    /**
     * Verifica se due stanze sono uguali confrontando i loro identificatori.
     * 
     * @param obj Oggetto da confrontare con la stanza corrente
     * @return true se le stanze sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Stanza other = (Stanza) obj;
        return this.piano == other.piano && this.nome.equals(other.nome);
    }
}
