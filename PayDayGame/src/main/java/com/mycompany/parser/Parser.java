package com.mycompany.parser;

import com.mycompany.type.Comandi;
import com.mycompany.type.Oggetto;
import com.mycompany.type.Stanza;
import com.mycompany.type.TipoComandi;

import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

public class Parser {

    private final Set<String> stopwords;

    public Parser(Set<String> stopwords) {
        this.stopwords = stopwords;
    }

    private <T> int ricercaIndice(String token, List<T> elementi, BiPredicate<T, String> matcher) {
        return IntStream.range(0, elementi.size())
                .filter(i -> matcher.test(elementi.get(i), token))
                .findFirst()
                .orElse(-1);
    }

    public ParserOutput parse(String comando, List<Comandi> comandi, List<Oggetto> oggetti, List<Oggetto> inventario, List<Stanza> stanze) {
        List<String> tokens = UtilityParser.parseString(comando, stopwords);

        if (tokens.isEmpty()) {
            return null;
        }

        int indiceComando = ricercaIndice(tokens.get(0), comandi,
            (comandoObj, token) -> comandoObj.getNome().equals(token) || comandoObj.getAlias().contains(token));

        if (indiceComando < 0) {
            return new ParserOutput(null, null);
        }

        Comandi comandoRiconosciuto = comandi.get(indiceComando);

        if (comandoRiconosciuto.getTipo() == TipoComandi.ENTRA) {
            if (tokens.size() > 1) {
                String destinazioneToken = String.join(" ", tokens.subList(1, tokens.size()));
                Stanza destinazione = trovaStanzaPerNomeOAlias(destinazioneToken, stanze);
                if (destinazione != null) {
                    return new ParserOutput(comandoRiconosciuto, null, null, destinazione.getNome());
                } else {
                    return new ParserOutput(comandoRiconosciuto, null, null, destinazioneToken);
                }
            } else {
                return new ParserOutput(comandoRiconosciuto, null, null, null);
            }
        }

        if (tokens.size() <= 1) {
            return new ParserOutput(comandi.get(indiceComando), null);
        }

        int indiceOggetto = ricercaIndice(tokens.get(1), oggetti,
            (oggetto, token) -> oggetto.getNome().equals(token) || oggetto.getAlias().contains(token));

        if (indiceOggetto < 0 && tokens.size() > 2) {
            indiceOggetto = ricercaIndice(tokens.get(2), oggetti,
                (oggetto, token) -> oggetto.getNome().equals(token) || oggetto.getAlias().contains(token));
        }

        int indiceOggettoInventario = -1;
        if (indiceOggetto < 0) {
            indiceOggettoInventario = ricercaIndice(tokens.get(1), inventario,
                (oggetto, token) -> oggetto.getNome().equals(token) || oggetto.getAlias().contains(token));
            if (indiceOggettoInventario < 0 && tokens.size() > 2) {
                indiceOggettoInventario = ricercaIndice(tokens.get(2), inventario,
                    (oggetto, token) -> oggetto.getNome().equals(token) || oggetto.getAlias().contains(token));
            }
        }

        if (indiceOggetto > -1) {
            return new ParserOutput(comandi.get(indiceComando), oggetti.get(indiceOggetto), null, null);
        }

        if (indiceOggettoInventario > -1) {
            return new ParserOutput(comandi.get(indiceComando), inventario.get(indiceOggettoInventario), inventario.get(indiceOggettoInventario), null);
        }

        return new ParserOutput(comandi.get(indiceComando), null, null, null);
    }

    private Stanza trovaStanzaPerNomeOAlias(String nomeOAlias, List<Stanza> stanze) {
        for (Stanza stanza : stanze) {
            if (stanza.getNome().equalsIgnoreCase(nomeOAlias) || stanza.getAlias().contains(nomeOAlias.toLowerCase())) {
                return stanza;
            }
        }
        return null;
    }
}
