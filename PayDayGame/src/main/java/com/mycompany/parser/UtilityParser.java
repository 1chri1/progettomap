package com.mycompany.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Classe di utilità per il parsing delle stringhe.
 * 
 * @autore Alessandro
 */
public class UtilityParser {

    /**
     * Metodo per analizzare una stringa e rimuovere le stopwords.
     * 
     * @param string la stringa da analizzare
     * @param stopwords l'insieme delle parole da ignorare
     * @return una lista di token senza stopwords
     */
    public static List<String> parseString(String string, Set<String> stopwords) {
        List<String> tokens = new ArrayList<>();
        String[] split = string.toLowerCase().split("\\s+");
        for (String t : split) {
            if (!stopwords.contains(t)) {
                tokens.add(t);
            }
        }
        return tokens;
    }
}
