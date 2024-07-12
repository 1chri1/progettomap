package com.mycompany.adventure;

import com.mycompany.type.Oggetto;
import java.util.List;

/**
 * Classe di utilità per operazioni comuni del gioco.
 */
public class GameUtils {
    
     boolean torciaAccesa = false;
     
    /**
     * Ottiene un oggetto dall'inventario dato il suo ID.
     *
     * @param inventario la lista degli oggetti nell'inventario
     * @param id l'ID dell'oggetto da cercare
     * @return l'oggetto con l'ID specificato, oppure null se non trovato
     */
    public static Oggetto getOggettoInventario(List<Oggetto> inventario, int id) {
        for (Oggetto o : inventario) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    public static boolean hasOggetto(List<Oggetto> inventario, String nome) {
        return inventario.stream().anyMatch(o -> nome.equalsIgnoreCase(o.getNome()));
    }

    public static boolean isQuadroElettricoDisattivato(PayDayGame game) {
        return game.isQuadroElettricoDisattivato();
    }

    public static void setQuadroElettricoDisattivato(PayDayGame game, boolean quadroElettricoDisattivato) {
        game.setQuadroElettricoDisattivato(quadroElettricoDisattivato);
    }

    public static boolean isTorciaAccesa(PayDayGame game) {
        return game.isTorciaAccesa();
    }
    public static void setTorciaAccesa(PayDayGame game, boolean torciaAccesa) {
        game.setTorciaAccesa(torciaAccesa);
    }

    public static boolean isRicattoDirettore(PayDayGame game) {
        return game.isRicattoDirettore();
    }

    public static void setRicattoDirettore(PayDayGame game, boolean ricattoDirettore) {
        game.setRicattoDirettore(ricattoDirettore);
    }

    public static boolean hasSoldi(List<Oggetto> inventario) {
        return hasOggetto(inventario, "soldi");
    }

    public static boolean hasGioielli(List<Oggetto> inventario) {
        return hasOggetto(inventario, "gioielli");
    }
}
