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

public class GestorePrendi implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput){
        StringBuilder msg = new StringBuilder();
        
        // verifica se il comando è PRENDI
        if(parserOutput.getComando().getTipo() == TipoComandi.PRENDI){
             // Verifica se ci sono oggetti nella stanza corrente
            if (descrizione.getStanzaCorrente().getOggetti().isEmpty()) {
                return "Non ci sono oggetti in questa stanza.";
            }
            // verifica se l'oggetto è diverso da null
            if(parserOutput.getOggetto() != null){
                // verifica se l'oggetto può essere raccolto
                if(parserOutput.getOggetto().isPrendibile()){
                    // aggiunge l'oggetto all'inventario e lo rimuove dalla stanza corrente
                    descrizione.getInventario().add(parserOutput.getOggetto());
                    descrizione.getStanzaCorrente().getOggetti().remove(parserOutput.getOggetto());
                    msg.append("Hai appena raccolto: ").append(parserOutput.getOggetto().getDescrizione());
                    
                    if (haTuttiGliOggetti(descrizione)) {
                        mostraDialogoDirettore(descrizione);
                    }
                } else {
                    msg.append("Non puoi prendere questo oggetto");
                }           
            } else {
                msg.append("Non capisco cosa vuoi prendere");
            }
        }
        return msg.toString();
    }
    
    private boolean haTuttiGliOggetti(GestioneGioco descrizione) {
        boolean haDocumentiRicatto = descrizione.getInventario().stream().anyMatch(o -> "documenti ricatto".equalsIgnoreCase(o.getNome()));
        boolean haSoldiGioielli = descrizione.getInventario().stream().anyMatch(o -> "soldi e gioielli".equalsIgnoreCase(o.getNome()));
        return haDocumentiRicatto && haSoldiGioielli;
    }

    private void mostraDialogoDirettore(GestioneGioco descrizione) {
        System.out.println("Direttore: Che cosa stai facendo qui? Come hai ottenuto quei documenti?");
        System.out.println("Giocatore: \"Direttore, ho trovato questi documenti nel caveau. Potrebbero rovinarti la carriera.\"");
        System.out.println("Direttore: \"Cosa vuoi in cambio del tuo silenzio?\"");
        System.out.println("Giocatore: \"Una via di fuga sicura e tutte le risorse nascoste.\"");
        System.out.println("Direttore: \"D'accordo, hai vinto. Ecco il passaggio sicuro e le informazioni sulla cassaforte nascosta.\"");
        System.out.println("Adesso puoi ricattare il direttore o ignorarlo");
    }
}
