package com.mycompany.inizializzazione;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.db.DatabaseManager;
import com.mycompany.type.Oggetto;
import com.mycompany.type.OggettoContenitore;
import com.mycompany.type.Stanza;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

/**
 * Classe che gestisce l'inizializzazione degli oggetti nel gioco.
 */
public class InizializzazioneOggetti {

    private final DatabaseManager DB_MANAGER;

    /**
     * Costruttore per InizializzazioneOggetti.
     * 
     * @param dbManager il gestore del database
     */
    public InizializzazioneOggetti(DatabaseManager dbManager) {
        this.DB_MANAGER = dbManager;
    }

    /**
     * Inizializza gli oggetti di gioco nelle stanze appropriate.
     * 
     * @param gioco l'istanza del gioco da inizializzare con gli oggetti
     */
    public void initOggetti(GestioneGioco gioco) {
        Stanza bagno = Stanza.trovaStanza(0, "Bagno");
        Stanza ufficioVicinoCorridoio1 = Stanza.trovaStanza(0, "Ufficio Vicino Corridoio 1");
        Stanza magazzino = Stanza.trovaStanza(0, "Magazzino");
        Stanza portineria = Stanza.trovaStanza(0, "Portineria");
        Stanza corridoio3 = Stanza.trovaStanza(0, "Corridoio 3");
        Stanza caveau = Stanza.trovaStanza(-1, "Caveau");
        Stanza ufficioDirettore = Stanza.trovaStanza(-1, "Ufficio Direttore");

        // Inizializzazione oggetti per il bagno
        if (bagno != null) {
            Oggetto water1 = new Oggetto(1, "water1", "Un cesso pulito.");
            Oggetto water2 = new Oggetto(2, "water2", "Un cesso sporco.");
            Oggetto water3 = new Oggetto(3, "water3", "Un cesso normale.");
            bagno.getOggetti().add(water1);
            bagno.getOggetti().add(water2);
            bagno.getOggetti().add(water3);
            insertOggetto(water1, bagno);
            insertOggetto(water2, bagno);
            insertOggetto(water3, bagno);
        }

        // Inizializzazione oggetti per l'ufficio vicino al corridoio 1
        if (ufficioVicinoCorridoio1 != null) {
            Oggetto registratore = new Oggetto(4, "registratore", "Registratore con conversazione (per scoprire il codice della cassetta nel caveau).");
            registratore.setAlias(new String[]{"registratore", "conversazione"});
            ufficioVicinoCorridoio1.getOggetti().add(registratore);
            registratore.setAscoltabile(true);
            registratore.setPrendibile(true);
            insertOggetto(registratore, ufficioVicinoCorridoio1);
        }

        // Inizializzazione oggetti per il magazzino
        if (magazzino != null) {
            Oggetto torcia = new Oggetto(5, "torcia", "Una torcia elettrica.");
            torcia.setAlias(new String[]{"torcia", "luce"});
            torcia.setPrendibile(true);
            torcia.setDisattivabile(true);
            magazzino.getOggetti().add(torcia);
            insertOggetto(torcia, magazzino);
        }

        // Inizializzazione oggetti per la portineria
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

        // Inizializzazione oggetti per il corridoio 3
        if (corridoio3 != null) {
            Oggetto quadroElettrico = new Oggetto(8, "quadro elettrico", "Quadro elettrico.");
            quadroElettrico.setDisattivabile(true);
            quadroElettrico.setAlias(new String[]{"quadro", "elettrico"});
            corridoio3.getOggetti().add(quadroElettrico);
            insertOggetto(quadroElettrico, corridoio3);
        }

        // Inizializzazione oggetti per il caveau
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

            Oggetto soldi = new Oggetto(10, "soldi", "Soldi (al centro della stanza).");
            soldi.setAlias(new String[]{"soldi"});
            soldi.setPrendibile(true);
            caveau.getOggetti().add(soldi);
            insertOggetto(soldi, caveau);
            
            Oggetto gioielli = new Oggetto(11, "gioielli", "Gioielli (al centro della stanza).");
            gioielli.setAlias(new String[]{"gioielli"});
            gioielli.setPrendibile(true);
            caveau.getOggetti().add(gioielli);
            insertOggetto(gioielli, caveau);
            
            Oggetto direttore = new Oggetto(12, "direttore", "capo");
            direttore.setAlias(new String[]{"direttore"});
            
            caveau.getOggetti().add(direttore);
            insertOggetto(direttore, caveau);
        }

        // Inizializzazione oggetti per l'ufficio del direttore
        if (ufficioDirettore != null) {
            OggettoContenitore armadio = new OggettoContenitore("armadio", "Un armadio grande e robusto.", Set.of("armadio"));
            armadio.setApribile(true);
            ufficioDirettore.getOggetti().add(armadio);
            insertOggetto(armadio, ufficioDirettore);

            Oggetto tesserinoCaveau = new Oggetto(13, "tesserino caveau", "Tesserino per entrare nel caveau (nell’armadio).");
            tesserinoCaveau.setAlias(new String[]{"tesserino"});
            tesserinoCaveau.setPrendibile(true);
            armadio.aggiungi(tesserinoCaveau);
            insertOggetto(tesserinoCaveau, ufficioDirettore);
        }
    }

    /**
     * Inserisce un oggetto nel database.
     * 
     * @param oggetto l'oggetto da inserire
     * @param stanza la stanza in cui si trova l'oggetto
     */
    private void insertOggetto(Oggetto oggetto, Stanza stanza) {
        String insertOggettoSQL = "INSERT INTO objects (name, description, room_id) VALUES (?, ?, (SELECT id FROM rooms WHERE nome = ?))";
        try (PreparedStatement pstmt = DB_MANAGER.getConnection().prepareStatement(insertOggettoSQL)) {
            pstmt.setString(1, oggetto.getNome());
            pstmt.setString(2, oggetto.getDescrizione());
            pstmt.setString(3, stanza.getNome());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
