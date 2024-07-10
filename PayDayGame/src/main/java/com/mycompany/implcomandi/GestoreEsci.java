package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

public class GestoreEsci implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo() == TipoComandi.ESCI) {
            // Gestione dell'uscita spostata nel metodo ProssimoSpostamento di PayDayGame
        }
        return "";
    }
}
