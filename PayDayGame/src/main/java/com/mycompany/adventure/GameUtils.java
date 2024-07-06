package com.mycompany.adventure;

import com.mycompany.type.Oggetto;
import java.util.List;

/**
 * Classe di utilità per operazioni comuni del gioco.
 */
public class GameUtils {

    /**
     * Ottiene un oggetto dall'inventario dato il suo ID.
     *
     * @param inventory la lista degli oggetti nell'inventario
     * @param id l'ID dell'oggetto da cercare
     * @return l'oggetto con l'ID specificato, oppure null se non trovato
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
