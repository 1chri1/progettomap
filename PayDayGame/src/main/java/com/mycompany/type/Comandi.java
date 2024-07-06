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

    private final TipoComandi tipo;
    private final String nome;
    private Set<String> alias;

    /**
     * Costruttore per creare un comando con il tipo e il nome specificati.
     *
     * @param tipo il tipo di comando
     * @param nome il nome del comando
     */
    public Comandi(TipoComandi tipo, String nome) {
        this.tipo = tipo;
        this.nome = nome;
    }

    /**
     * Costruttore per creare un comando con il tipo, nome e alias specificati.
     *
     * @param tipo il tipo di comando
     * @param nome il nome del comando
     * @param alias gli alias del comando
     */
    public Comandi(TipoComandi tipo, String nome, Set<String> alias) {
        this.tipo = tipo;
        this.nome = nome;
        this.alias = alias;
    }

    /**
     * Restituisce il nome del comando.
     *
     * @return il nome del comando
     */
    public String getNome() {
        return nome;
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
     * Restituisce il tipo del comando.
     *
     * @return il tipo del comando
     */
    public TipoComandi getTipo() {
        return tipo;
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
        hash = 97 * hash + Objects.hashCode(this.tipo);
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
        if (this.tipo != other.tipo) {
            return false;
        }
        return true;
    }
}
