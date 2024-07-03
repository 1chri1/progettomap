/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.adventure;
import com.mycompany.implComandi.Modifica;

/**
 *
 * @author Tommaso
 */
public interface GestoreComandi {
    
    /**
     *
     * @param o
     */
    public void assegna(Modifica o);
    
    /**
     *
     * @param o
     */
    public void rimuovi(Modifica o);
    
    /**
     *
     */
    public void notificaGestori();
    
}
