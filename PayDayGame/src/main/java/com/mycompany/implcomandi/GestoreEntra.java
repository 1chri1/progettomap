package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.Oggetto;
import com.mycompany.type.Stanza;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;

public class GestoreEntra implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;
    private static final String CHIAVE_DIRETTORE = "chiavi direttore";
    private static final String TESSERINO_CAVEAU = "tesserino caveau";
    private static final String CHIAVE_UFFICI = "chiavi uffici";

    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo() != TipoComandi.ENTRA) {
            return "";
        }

        Stanza stanzaCorrente = descrizione.getStanzaCorrente();
        String nomeStanzaCorrente = stanzaCorrente.getNome();
        String nomeStanzaDestinazione = parserOutput.getDestinazione();
        Stanza nuovaStanza = null;
        StringBuilder msg = new StringBuilder();

        // Controllo se siamo in esterno lato destro o sinistro e il comando entra è senza destinazione
        if ((nomeStanzaCorrente.equalsIgnoreCase("Lato destro") || nomeStanzaCorrente.equalsIgnoreCase("Lato sinistro"))
                && (nomeStanzaDestinazione == null || nomeStanzaDestinazione.isEmpty())) {
            if (nomeStanzaCorrente.equalsIgnoreCase("Lato destro")) {
                nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Scale Piano di Sopra");
            } else if (nomeStanzaCorrente.equalsIgnoreCase("Lato sinistro")) {
                nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Bagno");
            }
            if (nuovaStanza != null) {
                descrizione.setStanzaCorrente(nuovaStanza);
                return "Sei entrato in: " + nuovaStanza.getNome();
            } else {
                return "Non riesci ad entrare in nessuna stanza da qui.";
            }
        } 

        // Controllo se siamo nel corridoio 1 e il comando entra è senza destinazione
        if (nomeStanzaCorrente.equalsIgnoreCase("Corridoio 1") && (nomeStanzaDestinazione == null || nomeStanzaDestinazione.isEmpty())) {
            if (hasRequiredItem(descrizione, CHIAVE_UFFICI)) {
                nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Ufficio Vicino Corridoio 1");
                if (nuovaStanza != null) {
                    descrizione.setStanzaCorrente(nuovaStanza);
                    return "Sei entrato in: " + nuovaStanza.getNome();
                } else {
                    return "Non riesci ad entrare in nessuna stanza da qui.";
                }
            } else {
                msg.append("Non puoi entrare nell'ufficio vicino al corridoio 1. Ti manca la chiave degli uffici.");
                return msg.toString();
            }
        }

        if (nomeStanzaDestinazione == null || nomeStanzaDestinazione.isEmpty()) {
            msg.append("Non capisco dove vuoi andare.");
        } else {
            switch (nomeStanzaCorrente.toLowerCase()) {
                case "lato destro":
                    if (matchesDestination(stanzaCorrente.getPiano(), nomeStanzaDestinazione, "Scale Piano di Sopra")) {
                        nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Scale Piano di Sopra");
                    } else {
                        msg.append("Non capisco dove vuoi andare.");
                    }
                    break;
                case "lato sinistro":
                    if (matchesDestination(stanzaCorrente.getPiano(), nomeStanzaDestinazione, "Bagno")) {
                        nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Bagno");
                    } else {
                        msg.append("Non capisco dove vuoi andare.");
                    }
                    break;
                case "scale piano di sotto":
                    if (matchesDestination(stanzaCorrente.getPiano(), nomeStanzaDestinazione, "Ufficio Direttore")) {
                        if (hasRequiredItem(descrizione, CHIAVE_DIRETTORE)) {
                            nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Ufficio Direttore");
                        } else {
                            msg.append("Non puoi entrare nell'ufficio del direttore. Ti manca la chiave.");
                        }
                    } else if (matchesDestination(stanzaCorrente.getPiano(), nomeStanzaDestinazione, "Caveau")) {
                        if (hasRequiredItem(descrizione, TESSERINO_CAVEAU)) {
                            nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Caveau");
                        } else {
                            msg.append("Non puoi entrare nel caveau. Ti manca il tesserino.");
                        }
                    } else {
                        msg.append("Non capisco dove vuoi andare.");
                    }
                    break;
                case "corridoio 1":
                    if (matchesDestination(stanzaCorrente.getPiano(), nomeStanzaDestinazione, "Ufficio Vicino Corridoio 1")) {
                        if (hasRequiredItem(descrizione, CHIAVE_UFFICI)) {
                            nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Ufficio Vicino Corridoio 1");
                        } else {
                            msg.append("Non puoi entrare nell'ufficio vicino al corridoio 1. Ti manca la chiave degli uffici.");
                        }
                    } else {
                        msg.append("Non capisco dove vuoi andare.");
                    }
                    break;
                case "scale piano di sopra":
                    if (matchesDestination(stanzaCorrente.getPiano(), nomeStanzaDestinazione, "Ufficio Vicino Scale")) {
                        if (hasRequiredItem(descrizione, CHIAVE_UFFICI)) {
                            nuovaStanza = Stanza.trovaStanza(stanzaCorrente.getPiano(), "Ufficio Vicino Scale");
                        } else {
                            msg.append("Non puoi entrare nell'ufficio vicino alle scale del piano di sopra. Ti manca la chiave degli uffici.");
                        }
                    } else {
                        msg.append("Non capisco dove vuoi andare.");
                    }
                    break;
                default:
                    msg.append("Non puoi entrare qui.");
                    return msg.toString();
            }
        }

        if (nuovaStanza != null) {
            descrizione.setStanzaCorrente(nuovaStanza);
        } else if (msg.length() == 0) {
            msg.append("Non capisco dove vuoi andare.");
        }

        return msg.toString();
    }

    private boolean matchesDestination(int piano, String destinazione, String nomeStanza) {
        Stanza stanza = Stanza.trovaStanza(piano, nomeStanza);
        if (stanza != null && (stanza.getNome().equalsIgnoreCase(destinazione) || stanza.getAlias().contains(destinazione))) {
            return true;
        }
        return false;
    }

    private boolean hasRequiredItem(GestioneGioco descrizione, String itemName) {
        for (Oggetto oggetto : descrizione.getInventario()) {
            if (oggetto.getNome().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }
}