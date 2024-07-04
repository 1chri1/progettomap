package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.OggettoContenitore;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

import java.util.Scanner;

public class GestoreApri implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;
    private static final String CODICE_CORRETTO = "9872"; // Il codice corretto della cassetta

    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        StringBuilder messaggio = new StringBuilder();

        if (parserOutput.getComando().getTipo() == TipoComandi.APRI) {
              // Verifica se ci sono oggetti nella stanza corrente
            if (descrizione.getStanzaCorrente().getOggetti().isEmpty()) {
                return "Non ci sono oggetti da aprire in questa stanza.";
            }

            Oggetto oggetto = parserOutput.getOggetto();
            Oggetto oggettoInventario = parserOutput.getOggettoInventario();

            if (oggetto == null && oggettoInventario == null) {
                return "Non riesco a capire cosa vuoi aprire.";
            }

            if (oggetto != null) {
                messaggio.append(gestisciAperturaOggetto(descrizione, oggetto));
            }

            if (oggettoInventario != null) {
                messaggio.append(gestisciAperturaOggettoInventario(descrizione, oggettoInventario));
            }    
        } else {
            return "";
        }
        
        return messaggio.toString();
    }

    private String gestisciAperturaOggetto(GestioneGioco gioco, Oggetto oggetto) {
        if (!oggetto.isApribile()) {
            return "Non puoi aprire questo oggetto.";
        }

        if (oggetto.isAperto()) {
            return "L'oggetto è già aperto.";
        }

        if (oggetto instanceof OggettoContenitore && "cassetta".equalsIgnoreCase(oggetto.getNome())) {
            // Chiedi il codice all'utente
            Scanner scanner = new Scanner(System.in);
            System.out.print("Inserisci il codice di 4 cifre: ");
            String codiceInserito = scanner.nextLine();
            if (codiceInserito.equals(CODICE_CORRETTO)) {
                oggetto.setAperto(true);
                return "Codice corretto! " + apriContenitore(gioco, (OggettoContenitore) oggetto);
            } else {
                return "Codice errato. Non puoi aprire la cassetta.\n";
            }
        }

        oggetto.setAperto(true);

        StringBuilder messaggio = new StringBuilder("Hai aperto: ").append(oggetto.getNome());
        if (oggetto instanceof OggettoContenitore) {
            messaggio.append(apriContenitore(gioco, (OggettoContenitore) oggetto));
        }

        return messaggio.toString();
    }

    private String gestisciAperturaOggettoInventario(GestioneGioco gioco, Oggetto oggettoInventario) {
        if (!oggettoInventario.isApribile()) {
            return "Non puoi aprire questo oggetto.";
        }

        if (oggettoInventario.isAperto()) {
            return "L'oggetto è già aperto.";
        }

        oggettoInventario.setAperto(true);

        StringBuilder messaggio = new StringBuilder("Hai aperto nel tuo inventario: ").append(oggettoInventario.getNome());
        if (oggettoInventario instanceof OggettoContenitore) {
            OggettoContenitore contenitore = (OggettoContenitore) oggettoInventario;
            messaggio.append(apriContenitore(gioco, contenitore));
        }

        return messaggio.toString();
    }

    private String apriContenitore(GestioneGioco gioco, OggettoContenitore contenitore) {
        StringBuilder messaggio = new StringBuilder();
        if (contenitore.getList().isEmpty()) {
            return messaggio.toString();
        }

        messaggio.append(" ").append(contenitore.getNome()).append(" contiene:");
        for (Oggetto oggetto : contenitore.getList()) {
            gioco.getStanzaCorrente().getOggetti().add(oggetto);
            messaggio.append(" ").append(oggetto.getNome());
        }
        messaggio.append("\n");

        contenitore.getList().clear();

        return messaggio.toString();
    }
}
