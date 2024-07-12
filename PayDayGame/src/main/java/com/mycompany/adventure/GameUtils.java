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
     * @param inventario la lista degli oggetti nell'inventario
     * @param id l'ID dell'oggetto da cercare
     * @return l'oggetto con l'ID specificato, oppure null se non trovato
     */
    public static Oggetto getOggettiInventario(List<Oggetto> inventario, int id) {
        for (Oggetto o : inventario) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }
}
