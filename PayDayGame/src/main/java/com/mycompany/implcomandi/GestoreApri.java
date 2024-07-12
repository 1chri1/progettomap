package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.OggettoContenitore;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;
import javax.swing.JOptionPane;

/**
 * Gestore del comando "apri". Implementa l'interfaccia Modifica.
 */
public class GestoreApri implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;
    private static final String CODICE_CORRETTO = "9872"; // Il codice corretto della cassetta

    /**
     * Aggiorna lo stato del gioco in base all'output del parser.
     *
     * @param descrizione L'oggetto GestioneGioco che rappresenta lo stato corrente del gioco.
     * @param parserOutput L'output del parser contenente il comando e gli oggetti associati.
     * @return Il messaggio risultante dall'aggiornamento dello stato del gioco.
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        StringBuilder messaggio = new StringBuilder();

        if (parserOutput.getComando().getTipo() == TipoComandi.APRI) {
            // Verifica se ci sono oggetti nella stanza corrente
            if (descrizione.getStanzaCorrente().getOggetti().isEmpty()) {
                return "\nNon ci sono oggetti da aprire in questa stanza.\n";
            }

            Oggetto oggetto = parserOutput.getOggetto();
            Oggetto oggettoInventario = parserOutput.getOggettoInventario();

            if (oggetto == null && oggettoInventario == null) {
                return "\nNon riesco a capire cosa vuoi aprire.\n";
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

    /**
     * Gestisce l'apertura di un oggetto nella stanza corrente.
     *
     * @param gioco L'oggetto GestioneGioco che rappresenta lo stato corrente del gioco.
     * @param oggetto L'oggetto da aprire.
     * @return Il messaggio risultante dall'apertura dell'oggetto.
     */
    private String gestisciAperturaOggetto(GestioneGioco gioco, Oggetto oggetto) {
        if (!oggetto.isApribile()) {
            return "\nNon puoi aprire questo oggetto.\n";
        }

        if (oggetto.isAperto()) {
            return "\nL'oggetto è già aperto.\n";
        }

        if (oggetto instanceof OggettoContenitore && "cassetta".equalsIgnoreCase(oggetto.getNome())) {
            // Chiedi il codice all'utente tramite JOptionPane
            String codiceInserito = JOptionPane.showInputDialog(null, "Inserisci il codice di 4 cifre:");
            
            if (codiceInserito != null && codiceInserito.length() == 4 && codiceInserito.equals(CODICE_CORRETTO)) {
                oggetto.setAperto(true);
                return "\nCodice corretto! " + apriContenitore(gioco, (OggettoContenitore) oggetto) + "\n";
            } else {
                return "\nCodice errato. Non puoi aprire la cassetta.\n";
            }
        }

        oggetto.setAperto(true);

        StringBuilder messaggio = new StringBuilder("\nHai aperto: ").append(oggetto.getNome());
        if (oggetto instanceof OggettoContenitore) {
            messaggio.append(apriContenitore(gioco, (OggettoContenitore) oggetto));
        }

        return messaggio.toString();
    }

    /**
     * Gestisce l'apertura di un oggetto nell'inventario del giocatore.
     *
     * @param gioco L'oggetto GestioneGioco che rappresenta lo stato corrente del gioco.
     * @param oggettoInventario L'oggetto dell'inventario da aprire.
     * @return Il messaggio risultante dall'apertura dell'oggetto dell'inventario.
     */
    private String gestisciAperturaOggettoInventario(GestioneGioco gioco, Oggetto oggettoInventario) {
        if (!oggettoInventario.isApribile()) {
            return "\nNon puoi aprire questo oggetto.\n";
        }

        if (oggettoInventario.isAperto()) {
            return "\nL'oggetto è già aperto.\n";
        }

        oggettoInventario.setAperto(true);

        StringBuilder messaggio = new StringBuilder("\nHai aperto nel tuo inventario: ").append(oggettoInventario.getNome() + "\n");
        if (oggettoInventario instanceof OggettoContenitore) {
            OggettoContenitore contenitore = (OggettoContenitore) oggettoInventario;
            messaggio.append(apriContenitore(gioco, contenitore));
        }

        return messaggio.toString();
    }

    /**
     * Apre un oggetto contenitore e trasferisce il contenuto nella stanza corrente.
     *
     * @param gioco L'oggetto GestioneGioco che rappresenta lo stato corrente del gioco.
     * @param contenitore L'oggetto contenitore da aprire.
     * @return Il messaggio risultante dall'apertura del contenitore.
     */
    private String apriContenitore(GestioneGioco gioco, OggettoContenitore contenitore) {
        StringBuilder messaggio = new StringBuilder();
        if (contenitore.getList().isEmpty()) {
            return messaggio.toString();
        }
        messaggio.append("\n");
        messaggio.append(contenitore.getNome()).append(" contiene:");
        for (Oggetto oggetto : contenitore.getList()) {
            gioco.getStanzaCorrente().getOggetti().add(oggetto);
            messaggio.append(" ").append(oggetto.getNome());
        }
        messaggio.append("\n");

        contenitore.getList().clear();

        return messaggio.toString();
    }
}
