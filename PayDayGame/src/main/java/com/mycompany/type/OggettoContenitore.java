/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt per cambiare questa licenza
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java per modificare questo template
 */
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

    private List<Oggetto> list; 

    /**
     * Costruttore che crea un oggetto contenitore con nome, descrizione e alias.
     * 
     * @param nome il nome dell'oggetto contenitore
     * @param descrizione la descrizione dell'oggetto contenitore
     * @param alias gli alias dell'oggetto contenitore
     */
    public OggettoContenitore(String nome, String descrizione, Set<String> alias) {
        super(nome, descrizione, alias);
        this.list = new ArrayList<>();
    }

    /**
     * Costruttore che crea un oggetto contenitore con una lista di oggetti, identificatore e nome.
     * 
     * @param list la lista di oggetti contenuti
     * @param id l'identificatore dell'oggetto contenitore
     * @param nome il nome dell'oggetto contenitore
     */
    public OggettoContenitore(List<Oggetto> list, int id, String nome) {
        super(id, nome);
        this.list = list;
    }

    /**
     * Costruttore che crea un oggetto contenitore con una lista di oggetti, nome, descrizione e alias.
     * 
     * @param list la lista di oggetti contenuti
     * @param nome il nome dell'oggetto contenitore
     * @param descrizione la descrizione dell'oggetto contenitore
     * @param alias gli alias dell'oggetto contenitore
     */
    public OggettoContenitore(List<Oggetto> list, String nome, String descrizione, Set<String> alias) {
        super(nome, descrizione, alias);
        this.list = list;
    }

    /**
     * Restituisce la lista degli oggetti contenuti.
     * 
     * @return la lista degli oggetti contenuti
     */
    public List<Oggetto> getList() {
        return list;
    }

    /**
     * Imposta la lista degli oggetti contenuti.
     * 
     * @param list la nuova lista degli oggetti contenuti
     */
    public void setList(List<Oggetto> list) {
        this.list = list;
    }

    /**
     * Aggiunge un oggetto al contenitore.
     * 
     * @param o l'oggetto da aggiungere
     */
    public void aggiungi(Oggetto o) {
        list.add(o);
    }

    /**
     * Rimuove un oggetto dal contenitore.
     * 
     * @param o l'oggetto da rimuovere
     */
    public void rimuovi(Oggetto o) {
        list.remove(o);
    }
}
