/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.type;

/**
 *
 * @author Alessandro
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class OggettoContenitore extends Oggetto {
    
    private List <Oggetto> list = new ArrayList<>();; 


    public OggettoContenitore(String nome, String descrizione, Set<String> alias) {
        super(nome, descrizione, alias);
        this.list = new ArrayList<>();
    }
    
    public OggettoContenitore(List<Oggetto> list, int id, String nome) {
        super(id, nome);
        this.list = list;
    }

    public OggettoContenitore(List<Oggetto> list, String nome, String descrizione, Set<String> alias) {
        super(nome, descrizione, alias);
        this.list = list;
    }

     /**
     *
     * @return
     */
    public List<Oggetto> getList() {
        return list;
    }
    
    /**
     *
     * @param list
     */
    public void setList(List<Oggetto> list) {
        this.list = list;
    }
    
    /**
     *
     * @param o
     */
    public void aggiungi(Oggetto o) {
        list.add(o);
    }
    
    /**
     *
     * @param o
     */
    public void rimuovi(Oggetto o) {
        list.remove(o);
    }


}
