package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

/**
 *
 * @author Alessandro
 */
public class GestoreGuarda implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     * @param descrizione
     * @param parserOutput
     * @return 
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo() == TipoComandi.GUARDA) {
            return messaggioGuarda(descrizione);
        } else {
            return "";
        }
    }
    
    private String messaggioGuarda(GestioneGioco descrizione) {
        StringBuilder msg = new StringBuilder();

        // Verifica lo stato del quadro elettrico e della torcia
        if (descrizione.isQuadroElettricoDisattivato() && !descrizione.isTorciaAccesa()) {
            msg.append("Non riesci a vedere nulla perché è tutto buio.");
        } else {
            if (descrizione.getStanzaCorrente().getGuarda() != null) {
                msg.append(descrizione.getStanzaCorrente().getGuarda());
            } else {
                msg.append("Non c'è nulla di interessante qui.");
            }
        }

        return msg.toString();
    }
}
