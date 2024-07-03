/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.type;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alessandro
 */
public class Inventario {
    
    private List<Oggetto> list = new ArrayList<>();

    public List<Oggetto> getList() {
        return list;
    }

    public void setList(List<Oggetto> list) {
        this.list = list;
    }

    public boolean aggiungi(Oggetto o) {
        return list.add(o);
    }

    public boolean rimuovi(Object o) {
        return list.remove(o);
    }
    
    
    
}
