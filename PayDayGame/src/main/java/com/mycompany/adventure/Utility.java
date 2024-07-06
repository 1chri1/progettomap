package com.mycompany.adventure;

import com.mycompany.type.Oggetto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe di utilità per operazioni comuni nel gioco.
 * Fornisce metodi per la gestione di file e inventario.
 */
public class Utility {

    /**
     * Carica un file e restituisce un set di stringhe.
     * 
     * @param file il file da caricare
     * @return un set di stringhe contenenti le righe del file
     * @throws IOException in caso di errore durante la lettura del file
     */
    public static Set<String> caricaFile(File file) throws IOException {
        Set<String> set = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (reader.ready()) {
            set.add(reader.readLine().trim().toLowerCase());
        }
        reader.close();
        return set;
    }

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
}
