package com.mycompany.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Alessandro
 */
public class Comandi implements Serializable {
    private static final long serialVersionUID = 1L;

    private final TipoComandi tipo;
    
    private final String nome;
    
    private Set<String> alias;

    public Comandi(TipoComandi tipo, String nome) {
        this.tipo = tipo;
        this.nome = nome;
    }

    public Comandi(TipoComandi tipo, String nome, Set<String> alias) {
        this.tipo = tipo;
        this.nome = nome;
        this.alias = alias;
    }

    public String getNome() {
        return nome;
    }

    public Set<String> getAlias() {
        return alias;
    }

    public void setAlias(Set<String> alias) {
        this.alias = alias;
    }

    public TipoComandi getTipo() {
        return tipo;
    }
    
    public void setAlias(String[] alias) {
        this.alias = new HashSet<>(Arrays.asList(alias));
    }
    
    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.tipo);
        return hash;
    }
    
    /**
     *
     * @param obj
     * @return
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
