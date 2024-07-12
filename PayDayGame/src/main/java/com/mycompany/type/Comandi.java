package com.mycompany.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Classe che rappresenta un comando nel gioco.
 * 
 * @autore Alessandro
 */
public class Comandi implements Serializable {
    private static final long serialVersionUID = 1L;

    private final TipoComandi TIPO;
    private final String NOME;
    private Set<String> alias;

    /**
     * Costruttore per creare un comando con il TIPO e il NOME specificati.
     *
     * @param tipo il TIPO di comando
     * @param nome il NOME del comando
     */
    public Comandi(TipoComandi tipo, String nome) {
        this.TIPO = tipo;
        this.NOME = nome;
    }

    /**
     * Costruttore per creare un comando con il TIPO, NOME e alias specificati.
     *
     * @param tipo il TIPO di comando
     * @param nome il NOME del comando
     * @param alias gli alias del comando
     */
    public Comandi(TipoComandi tipo, String nome, Set<String> alias) {
        this.TIPO = tipo;
        this.NOME = nome;
        this.alias = alias;
    }

    /**
     * Restituisce il NOME del comando.
     *
     * @return il NOME del comando
     */
    public String getNome() {
        return NOME;
    }

    /**
     * Restituisce gli alias del comando.
     *
     * @return un insieme di alias
     */
    public Set<String> getAlias() {
        return alias;
    }

    /**
     * Imposta gli alias del comando.
     *
     * @param alias un insieme di alias
     */
    public void setAlias(Set<String> alias) {
        this.alias = alias;
    }

    /**
     * Restituisce il TIPO del comando.
     *
     * @return il TIPO del comando
     */
    public TipoComandi getTipo() {
        return TIPO;
    }

    /**
     * Imposta gli alias del comando usando un array di stringhe.
     *
     * @param alias un array di alias
     */
    public void setAlias(String[] alias) {
        this.alias = new HashSet<>(Arrays.asList(alias));
    }

    /**
     * Calcola il codice hash per l'oggetto Comandi.
     *
     * @return il codice hash
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.TIPO);
        return hash;
    }

    /**
     * Verifica l'uguaglianza tra questo comando e un altro oggetto.
     *
     * @param obj l'oggetto da confrontare
     * @return true se gli oggetti sono uguali, false altrimenti
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
        final Comandi other = (Comandi) obj;
        if (this.TIPO != other.TIPO) {
            return false;
        }
        return true;
    }
}
