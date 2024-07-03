/*
 * Clicca nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt per cambiare questa licenza
 * Clicca nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java per modificare questo template
 */
package com.mycompany.adventure;

/**
 *
 * @autore Alessandro
 */
import com.mycompany.type.Oggetto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utility {

    /**
     *
     * @param file
     * @return
     * @throws IOException
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
     *
     * @param inventario
     * @param id
     * @return
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

