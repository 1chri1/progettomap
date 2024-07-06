package com.mycompany.type;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta l'inventario del giocatore.
 * 
 * @autore Alessandro
 */
public class Inventario {
    
    private List<Oggetto> list = new ArrayList<>();

    /**
     * Restituisce la lista degli oggetti nell'inventario.
     *
     * @return la lista degli oggetti
     */
    public List<Oggetto> getList() {
        return list;
    }

    /**
     * Imposta la lista degli oggetti nell'inventario.
     *
     * @param list la lista degli oggetti da impostare
     */
    public void setList(List<Oggetto> list) {
        this.list = list;
    }

    /**
     * Aggiunge un oggetto all'inventario.
     *
     * @param o l'oggetto da aggiungere
     * @return true se l'oggetto è stato aggiunto con successo, false altrimenti
     */
    public boolean aggiungi(Oggetto o) {
        return list.add(o);
    }

    /**
     * Rimuove un oggetto dall'inventario.
     *
     * @param o l'oggetto da rimuovere
     * @return true se l'oggetto è stato rimosso con successo, false altrimenti
     */
    public boolean rimuovi(Object o) {
        return list.remove(o);
    }
}
