package com.mycompany.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Questa classe rappresenta un oggetto all'interno del gioco.
 */
public class Oggetto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private int id; 
    private String nome;
    private String descrizione;
    private Set<String> alias;
    private boolean apribile = false;
    private boolean prendibile = false;
    private boolean disattivabile = false;
    private boolean spingibile = false;
    private boolean ascoltabile = false;
    private boolean aperto = false;
    private boolean spinto = false;
    private boolean disattivo = false;

    /**
     * Costruttore per un oggetto con identificatore.
     * 
     * @param id l'identificatore dell'oggetto
     */
    public Oggetto(int id) {
        this.id = id;
    }
    
    /**
     * Costruttore per un oggetto con identificatore e nome.
     * 
     * @param id l'identificatore dell'oggetto
     * @param nome il nome dell'oggetto
     */
    public Oggetto(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    /**
     * Costruttore per un oggetto con identificatore, nome e descrizione.
     * 
     * @param id l'identificatore dell'oggetto
     * @param nome il nome dell'oggetto
     * @param descrizione la descrizione dell'oggetto
     */
    public Oggetto(int id, String nome, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
    }
    
    /**
     * Costruttore per un oggetto con nome, descrizione e alias.
     * 
     * @param nome il nome dell'oggetto
     * @param descrizione la descrizione dell'oggetto
     * @param alias gli alias associati all'oggetto
     */
    public Oggetto(String nome, String descrizione, Set<String> alias) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.alias = alias;
    }

    /**
     * Restituisce il nome dell'oggetto.
     * 
     * @return il nome dell'oggetto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta gli alias dell'oggetto.
     * 
     * @param alias array di alias dell'oggetto
     */
    public void setAlias(String[] alias) {
        this.alias = new HashSet<>(Arrays.asList(alias));
    }

    /**
     * Restituisce gli alias dell'oggetto.
     * 
     * @return gli alias dell'oggetto
     */
    public Set<String> getAlias() {
        return alias != null ? alias : Collections.emptySet();
    }

    /**
     * Imposta il nome dell'oggetto.
     * 
     * @param nome il nuovo nome dell'oggetto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce la descrizione dell'oggetto.
     * 
     * @return la descrizione dell'oggetto
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione dell'oggetto.
     * 
     * @param descrizione la nuova descrizione dell'oggetto
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce l'identificatore dell'oggetto.
     * 
     * @return l'identificatore dell'oggetto
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta se l'oggetto è apribile.
     * 
     * @param apribile il nuovo stato di apribilità dell'oggetto
     */
    public void setApribile(boolean apribile) {
        this.apribile = apribile;
    }

    /**
     * Imposta se l'oggetto è prendibile.
     * 
     * @param prendibile il nuovo stato di prendibilità dell'oggetto
     */
    public void setPrendibile(boolean prendibile) {
        this.prendibile = prendibile;
    }

    /**
     * Imposta se l'oggetto è spingibile.
     * 
     * @param spingibile il nuovo stato di spingibilità dell'oggetto
     */
    public void setSpingibile(boolean spingibile) {
        this.spingibile = spingibile;
    }

    /**
     * Imposta se l'oggetto è aperto.
     * 
     * @param aperto il nuovo stato di apertura dell'oggetto
     */
    public void setAperto(boolean aperto) {
        this.aperto = aperto;
    }

    /**
     * Imposta se l'oggetto è spinto.
     * 
     * @param spinto il nuovo stato di spinta dell'oggetto
     */
    public void setSpinto(boolean spinto) {
        this.spinto = spinto;
    }

    /**
     * Restituisce se l'oggetto è apribile.
     * 
     * @return true se l'oggetto è apribile, altrimenti false
     */
    public boolean isApribile() {
        return apribile;
    }

    /**
     * Restituisce se l'oggetto è prendibile.
     * 
     * @return true se l'oggetto è prendibile, altrimenti false
     */
    public boolean isPrendibile() {
        return prendibile;
    }

    /**
     * Restituisce se l'oggetto è spingibile.
     * 
     * @return true se l'oggetto è spingibile, altrimenti false
     */
    public boolean isSpingibile() {
        return spingibile;
    }

    /**
     * Restituisce se l'oggetto è aperto.
     * 
     * @return true se l'oggetto è aperto, altrimenti false
     */
    public boolean isAperto() {
        return aperto;
    }

    /**
     * Restituisce se l'oggetto è spinto.
     * 
     * @return true se l'oggetto è spinto, altrimenti false
     */
    public boolean isSpinto() {
        return spinto;
    }

    /**
     * Restituisce se l'oggetto è ascoltabile.
     * 
     * @return true se l'oggetto è ascoltabile, altrimenti false
     */
    public boolean isAscoltabile() {
        return ascoltabile;
    }

    /**
     * Imposta se l'oggetto è ascoltabile.
     * 
     * @param ascoltabile il nuovo stato di ascoltabilità dell'oggetto
     */
    public void setAscoltabile(boolean ascoltabile) {
        this.ascoltabile = ascoltabile;
    }

    /**
     * Restituisce se l'oggetto è disattivabile.
     * 
     * @return true se l'oggetto è disattivabile, altrimenti false
     */
    public boolean isDisattivabile() {
        return disattivabile;
    }

    /**
     * Imposta se l'oggetto è disattivabile.
     * 
     * @param disattivabile il nuovo stato di disattivabilità dell'oggetto
     */
    public void setDisattivabile(boolean disattivabile) {
        this.disattivabile = disattivabile;
    }

    /**
     * Restituisce se l'oggetto è disattivo.
     * 
     * @return true se l'oggetto è disattivo, altrimenti false
     */
    public boolean isDisattivo() {
        return disattivo;
    }

    /**
     * Imposta se l'oggetto è disattivo.
     * 
     * @param disattivo il nuovo stato di disattività dell'oggetto
     */
    public void setDisattivo(boolean disattivo) {
        this.disattivo = disattivo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id;
        return hash;
    }

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
        final Oggetto other = (Oggetto) obj;
        return this.id == other.id;
    }
}
