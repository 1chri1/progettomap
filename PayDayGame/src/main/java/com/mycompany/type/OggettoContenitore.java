package com.mycompany.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Classe che rappresenta un oggetto contenitore nel gioco.
 * Un oggetto contenitore può contenere altri oggetti.
 * 
 * @autore Alessandro
 */
public class OggettoContenitore extends Oggetto {

    private List<Oggetto> contenutoOggettoContenitore; 
   
    /**
     * Costruttore che crea un oggetto contenitore con nome, descrizione e alias.
     * 
     * @param nome il nome dell'oggetto contenitore
     * @param descrizione la descrizione dell'oggetto contenitore
     * @param alias gli alias dell'oggetto contenitore
     */
    public OggettoContenitore(String nome, String descrizione, Set<String> alias) {
        super(nome, descrizione, alias);
        this.contenutoOggettoContenitore = new ArrayList<>();
    }

    /**
     * Restituisce la lista degli oggetti contenuti.
     * 
     * @return la lista degli oggetti contenuti
     */
    public List<Oggetto> getContenutoOggettoContenitore() {
        return contenutoOggettoContenitore;
    }

    /**
     * Aggiunge un oggetto al contenitore.
     * 
     * @param o l'oggetto da aggiungere
     */
    public void aggiungi(Oggetto o) {
        contenutoOggettoContenitore.add(o);
    }

    /**
     * Rimuove un oggetto dal contenitore.
     * 
     * @param o l'oggetto da rimuovere
     */
    public void rimuovi(Oggetto o) {
        contenutoOggettoContenitore.remove(o);
    }
}
