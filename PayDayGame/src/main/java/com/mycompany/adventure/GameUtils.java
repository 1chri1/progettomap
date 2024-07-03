/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.adventure;

/**
 *
 * @author Tommaso
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import com.mycompany.type.Oggetto;
import java.util.List;

import java.util.List;

/**
 *
 * @author Tommaso
 */
public class GameUtils {

    /**
     *
     * @param inventory
     * @param id
     * @return
     */
    public static Oggetto getObjectFromInventory(List<Oggetto> inventory, int id) {
        for (Oggetto o : inventory) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

}
