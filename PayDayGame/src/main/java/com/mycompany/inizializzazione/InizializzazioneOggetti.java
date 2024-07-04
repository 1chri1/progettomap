package com.mycompany.inizializzazione;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.db.DatabaseManager;
import com.mycompany.type.Oggetto;
import com.mycompany.type.OggettoContenitore;
import com.mycompany.type.Stanza;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class InizializzazioneOggetti {

    private final DatabaseManager dbManager;

    public InizializzazioneOggetti(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void initOggetti(GestioneGioco gioco) {
        Stanza bagno = Stanza.trovaStanza(0, "Bagno");
        Stanza ufficioVicinoCorridoio1 = Stanza.trovaStanza(0, "Ufficio Vicino Corridoio 1");
        Stanza magazzino = Stanza.trovaStanza(0, "Magazzino");
        Stanza portineria = Stanza.trovaStanza(0, "Portineria");
        Stanza corridoio3 = Stanza.trovaStanza(0, "Corridoio 3");
        Stanza caveau = Stanza.trovaStanza(-1, "Caveau");
        Stanza ufficioDirettore = Stanza.trovaStanza(-1, "Ufficio Direttore");

        if (bagno != null) {
            Oggetto cesso1 = new Oggetto(1, "cesso1", "Un cesso pulito.");
            Oggetto cesso2 = new Oggetto(2, "cesso2", "Un cesso sporco.");
            Oggetto cesso3 = new Oggetto(3, "cesso3", "Un cesso normale.");
            bagno.getOggetti().add(cesso1);
            bagno.getOggetti().add(cesso2);
            bagno.getOggetti().add(cesso3);
            insertOggetto(cesso1, bagno);
            insertOggetto(cesso2, bagno);
            insertOggetto(cesso3, bagno);
        }

        if (ufficioVicinoCorridoio1 != null) {
            Oggetto registratore = new Oggetto(4, "registratore", "Registratore con conversazione (per scoprire il codice della cassetta nel caveau).");
            registratore.setAlias(new String[]{"registratore", "conversazione"});
            ufficioVicinoCorridoio1.getOggetti().add(registratore);
            registratore.setAscoltabile(true);
            registratore.setPrendibile(true);
            insertOggetto(registratore, ufficioVicinoCorridoio1);
        }

        if (magazzino != null) {
            Oggetto torcia = new Oggetto(5, "torcia", "Una torcia elettrica.");
            torcia.setAlias(new String[]{"torcia", "luce"});
            torcia.setPrendibile(true);
            torcia.setDisattivabile(true);
            magazzino.getOggetti().add(torcia);
            insertOggetto(torcia, magazzino);
        }

        if (portineria != null) {
            Oggetto chiaviDirettore = new Oggetto(6, "chiavi direttore", "Chiavi della stanza del direttore.");
            chiaviDirettore.setAlias(new String[]{"chiavi direttore", "chiavi"});
            chiaviDirettore.setPrendibile(true);
            portineria.getOggetti().add(chiaviDirettore);
            insertOggetto(chiaviDirettore, portineria);

            Oggetto chiaviUffici = new Oggetto(7, "chiavi uffici", "Chiavi dei due uffici.");
            chiaviUffici.setAlias(new String[]{"chiavi uffici", "chiavi"});
            chiaviUffici.setPrendibile(true);
            portineria.getOggetti().add(chiaviUffici);
            insertOggetto(chiaviUffici, portineria);
        }

        if (corridoio3 != null) {
            Oggetto quadroElettrico = new Oggetto(8, "quadro elettrico", "Quadro elettrico.");
            quadroElettrico.setDisattivabile(true);
            quadroElettrico.setAlias(new String[]{"quadro", "elettrico"});
            corridoio3.getOggetti().add(quadroElettrico);
            insertOggetto(quadroElettrico, corridoio3);
        }

        if (caveau != null) {
            OggettoContenitore cassetta = new OggettoContenitore("cassetta", "Cassetta contenente i documenti del direttore.", Set.of("cassetta"));
            cassetta.setApribile(true);
            caveau.getOggetti().add(cassetta);
            Oggetto documentiRicatto = new Oggetto(9, "documenti ricatto", "Documenti per ricattare il direttore.");
            documentiRicatto.setAlias(new String[]{"documenti", "ricatto"});
            documentiRicatto.setPrendibile(true);
            cassetta.aggiungi(documentiRicatto);
            insertOggetto(cassetta, caveau);
            insertOggetto(documentiRicatto, caveau);

            Oggetto soldi = new Oggetto(10, "soldi ", "Soldi(al centro della stanza).");
            soldi.setAlias(new String[]{"soldi"});
            soldi.setPrendibile(true);
            caveau.getOggetti().add(soldi);
            insertOggetto(soldi, caveau);
            
            Oggetto gioielli = new Oggetto(10, "gioielli", "Gioielli (al centro della stanza).");
            gioielli.setAlias(new String[]{"gioielli"});
            gioielli.setPrendibile(true);
            caveau.getOggetti().add(gioielli);
            insertOggetto(gioielli, caveau);
        }

        if (ufficioDirettore != null) {
            OggettoContenitore armadio = new OggettoContenitore("armadio", "Un armadio grande e robusto.", Set.of("armadio"));
            armadio.setApribile(true);
            ufficioDirettore.getOggetti().add(armadio);
            insertOggetto(armadio, ufficioDirettore);

            Oggetto tesserinoCaveau = new Oggetto(12, "tesserino caveau", "Tesserino per entrare nel caveau (nell’armadio).");
            tesserinoCaveau.setAlias(new String[]{"tesserino"});
            tesserinoCaveau.setPrendibile(true);
            armadio.aggiungi(tesserinoCaveau);
            insertOggetto(tesserinoCaveau, ufficioDirettore);
        }
    }

    private void insertOggetto(Oggetto oggetto, Stanza stanza) {
        String insertOggettoSQL = "INSERT INTO objects (name, description, room_id) VALUES (?, ?, (SELECT id FROM rooms WHERE nome = ?))";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(insertOggettoSQL)) {
            pstmt.setString(1, oggetto.getNome());
            pstmt.setString(2, oggetto.getDescrizione());
            pstmt.setString(3, stanza.getNome());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
