/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

public class GestoreRicatto implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo() == TipoComandi.RICATTO) {
            Oggetto documentiRicatto = descrizione.getInventario().stream()
                    .filter(o -> "documenti ricatto".equalsIgnoreCase(o.getNome()))
                    .findFirst()
                    .orElse(null);

            if (documentiRicatto != null) {
                descrizione.setRicattoDirettore(true);
                return "Hai ricattato il direttore con i documenti! Ora il tuo bottino sarà più alto.";
            } else {
                return "Non hai i documenti per ricattare il direttore.";
            }
        }
        return "";
    }
}
