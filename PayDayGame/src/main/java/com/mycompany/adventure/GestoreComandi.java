package com.mycompany.adventure;

import com.mycompany.implComandi.Modifica;

/**
 * Interfaccia per la gestione dei comandi.
 * Fornisce i metodi necessari per assegnare, rimuovere e notificare i gestori di comandi.
 */
public interface GestoreComandi {
    
    /**
     * Assegna un nuovo comando.
     *
     * @param o il comando da assegnare
     */
    public void assegna(Modifica o);
    
    /**
     * Rimuove un comando esistente.
     *
     * @param o il comando da rimuovere
     */
    public void rimuovi(Modifica o);
    
    /**
     * Notifica tutti i gestori dei comandi.
     */
    public void notificaGestori();
}
