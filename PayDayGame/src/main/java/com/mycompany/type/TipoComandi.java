package com.mycompany.type;

import java.io.Serializable;

/**
 * Enumerazione che rappresenta i vari tipi di comandi che possono essere utilizzati nel gioco.
 * Ogni costante dell'enumerazione rappresenta un comando specifico.
 * 
 * @autore Alessandro
 */
public enum TipoComandi implements Serializable {
    /**
     * Comando per uscire dal gioco.
     */
    ESCI,

    /**
     * Comando per visualizzare l'inventario.
     */
    INVENTARIO,

    /**
     * Comando per muoversi verso nord.
     */
    NORD,

    /**
     * Comando per muoversi verso sud.
     */
    SUD,

    /**
     * Comando per muoversi verso est.
     */
    EST,

    /**
     * Comando per muoversi verso ovest.
     */
    OVEST,

    /**
     * Comando per aprire un oggetto.
     */
    APRI,

    /**
     * Comando per entrare in una stanza.
     */
    ENTRA,

    /**
     * Comando per prendere un oggetto.
     */
    PRENDI,
    
    /**
     * Comando per guardare o osservare l'ambiente.
     */
    GUARDA,
    
    /**
     * Comando per salire.
     */
    SALI,
    
    /**
     * Comando per scendere.
     */
    SCENDI,
    
    /**
     * Comando per disattivare un oggetto.
     */
    DISATTIVA,
    
    /**
     * Comando per attivare un oggetto.
     */
    ATTIVA,
    
    /**
     * Comando per ascoltare.
     */
    ASCOLTA,
    
    /**
     * Comando per ricattare.
     */
    RICATTA
}
