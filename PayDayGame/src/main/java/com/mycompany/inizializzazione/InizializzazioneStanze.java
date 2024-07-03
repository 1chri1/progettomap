package com.mycompany.inizializzazione;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.db.DatabaseManager;
import com.mycompany.type.Stanza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe per l'inizializzazione delle stanze del gioco.
 * 
 * @autore Tommaso
 */
public class InizializzazioneStanze {

    private DatabaseManager dbManager;
    private Map<String, Stanza> stanzeMap = new HashMap<>();

    public InizializzazioneStanze(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void initStanze(GestioneGioco gioco) {
        inserisciStanzeNelDatabase();
        recuperaStanzeDalDatabase();
        creaCollegamentiStanze();
        aggiungiAlias();
    }

    private void inserisciStanzeNelDatabase() {
        // Crea le stanze e le inserisce nel database
        Connection connection = dbManager.getConnection();

        try {
            String insertRoomSQL = "INSERT INTO rooms (piano, nome, descrizione, guarda) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(insertRoomSQL);

            // Esterno
            pstmt.setInt(1, 0);
            pstmt.setString(2, "Esterno dell'ingresso principale");
            pstmt.setString(3, "Ci sono delle guardie davanti all'ingresso, trova un'altra soluzione.");
            pstmt.setString(4, "Due guardie sorvegliano l'ingresso principale.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Angolo destro della banca");
            pstmt.setString(3, "Puoi andare avanti, il palazzo continua.");
            pstmt.setString(4, "Il lato destro del palazzo, lontano dalle guardie.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Angolo sinistro della banca");
            pstmt.setString(3, "Puoi andare avanti, il palazzo continua.");
            pstmt.setString(4, "Il lato sinistro del palazzo, lontano dalle guardie.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Lato destro");
            pstmt.setString(3, "C'è la finestra delle scale, potresti provare ad entrare.");
            pstmt.setString(4, "La finestra delle scale è aperta, un ingresso alternativo.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Lato sinistro");
            pstmt.setString(3, "C'è la finestra del bagno, potresti provare ad entrare.");
            pstmt.setString(4, "La finestra del bagno è aperta, un ingresso alternativo.");
            pstmt.executeUpdate();

            // Piano terra
            pstmt.setInt(1, 0);
            pstmt.setString(2, "Hall");
            pstmt.setString(3, "Punto di ingresso principale, sorvegliato da due guardie.");
            pstmt.setString(4, "Per fortuna le telecamere sono disattivate, ora puoi andare nelle altre stanze.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Portineria");
            pstmt.setString(3, "");
            pstmt.setString(4, "Contiene le chiavi della stanza del direttore e degli uffici.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Bagno");
            pstmt.setString(3, "Un normalissimo bagno");
            pstmt.setString(4, "Un bagno pulito con tre cessi.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Sala Controllo");
            pstmt.setString(3, "");
            pstmt.setString(4, "La sala controllo con le telecamere di sicurezza, sorvegliata da una guardia.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Ufficio Vicino Scale");
            pstmt.setString(3, "Un semplice ufficio.");
            pstmt.setString(4, "Un ufficio ordinato vicino alle scale.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Ufficio Vicino Corridoio 1");
            pstmt.setString(3, "Un semplice ufficio");
            pstmt.setString(4, "Un ufficio con un registratore contenente una conversazione importante.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Magazzino");
            pstmt.setString(3, "E' pieno di materiali");
            pstmt.setString(4, "Contiene una torcia e altre forniture.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Scale Piano di Sopra");
            pstmt.setString(3, "");
            pstmt.setString(4, "Le scale che portano al piano superiore.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Corridoio 1");
            pstmt.setString(3, "");
            pstmt.setString(4, "Un corridoio che collega diverse stanze.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Corridoio 2");
            pstmt.setString(3, "");
            pstmt.setString(4, "Un corridoio che collega il bagno e il magazzino.");
            pstmt.executeUpdate();

            pstmt.setInt(1, 0);
            pstmt.setString(2, "Corridoio 3");
            pstmt.setString(3, "Cruciale per la storia del gioco");
            pstmt.setString(4, "Contiene il quadro elettrico per disattivare le telecamere.");
            pstmt.executeUpdate();

            // Seminterrato
            pstmt.setInt(1, -1);
            pstmt.setString(2, "Caveau");
            pstmt.setString(3, "Il caveau, un luogo sicuro dove vengono custoditi oggetti di valore.");
            pstmt.setString(4, "Contiene una cassetta con documenti per ricattare il direttore, soldi e gioielli.");
            pstmt.executeUpdate();

            pstmt.setInt(1, -1);
            pstmt.setString(2, "Ufficio Direttore");
            pstmt.setString(3, "L'ufficio del direttore, arredato con mobili eleganti.");
            pstmt.setString(4, "Contiene un tesserino per entrare nel caveau, nascosto nell'armadio.");
            pstmt.executeUpdate();

            pstmt.setInt(1, -1);
            pstmt.setString(2, "Scale Piano di Sotto");
            pstmt.setString(3, "");
            pstmt.setString(4, "Le scale che portano al piano inferiore.");
            pstmt.executeUpdate();

            pstmt.setInt(1, -1);
            pstmt.setString(2, "Garage/Uscita");
            pstmt.setString(3, "Il garage, che funge anche da uscita dall'edificio.");
            pstmt.setString(4, "Un garage vuoto con una macchina abbandonata.");
            pstmt.executeUpdate();

            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void recuperaStanzeDalDatabase() {
        // Recupera le stanze dal database e le mette nella mappa stanzeMap
        Connection connection = dbManager.getConnection();

        try {
            String query = "SELECT * FROM rooms";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int piano = rs.getInt("piano");
                String nome = rs.getString("nome");
                String descrizione = rs.getString("descrizione");
                String guarda = rs.getString("guarda");

                Stanza stanza = new Stanza(piano, nome, descrizione, guarda);
                stanzeMap.put(nome, stanza);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void creaCollegamentiStanze() {
        // Crea i collegamenti tra le stanze dopo averle recuperate dal database
        Stanza esternoIngressoPrincipale = stanzeMap.get("Esterno dell'ingresso principale");
        Stanza esternoAngoloDestro = stanzeMap.get("Angolo destro della banca");
        Stanza esternoAngoloSinistro = stanzeMap.get("Angolo sinistro della banca");
        Stanza esternoLatoDestro = stanzeMap.get("Lato destro");
        Stanza esternoLatoSinistro = stanzeMap.get("Lato sinistro");

        Stanza hall = stanzeMap.get("Hall");
        Stanza portineria = stanzeMap.get("Portineria");
        Stanza bagno = stanzeMap.get("Bagno");
        Stanza salaControllo = stanzeMap.get("Sala Controllo");
        Stanza ufficioVicinaScale = stanzeMap.get("Ufficio Vicino Scale");
        Stanza ufficioVicinaCorridoio1 = stanzeMap.get("Ufficio Vicino Corridoio 1");
        Stanza magazzino = stanzeMap.get("Magazzino");
        Stanza scalePT = stanzeMap.get("Scale Piano di Sopra");
        Stanza corridoio1 = stanzeMap.get("Corridoio 1");
        Stanza corridoio2 = stanzeMap.get("Corridoio 2");
        Stanza corridoio3 = stanzeMap.get("Corridoio 3");

        Stanza caveau = stanzeMap.get("Caveau");
        Stanza ufficioDirettore = stanzeMap.get("Ufficio Direttore");
        Stanza scaleS = stanzeMap.get("Scale Piano di Sotto");
        Stanza garageUscita = stanzeMap.get("Garage/Uscita");

        // Collegamenti esterno
        if (esternoIngressoPrincipale != null && esternoAngoloDestro != null) {
            esternoIngressoPrincipale.setEst(esternoAngoloDestro);
        }
        if (esternoIngressoPrincipale != null && esternoAngoloSinistro != null) {
            esternoIngressoPrincipale.setOvest(esternoAngoloSinistro);
        }
        if (esternoAngoloDestro != null) {
            esternoAngoloDestro.setNord(esternoLatoDestro);
            esternoAngoloDestro.setOvest(esternoIngressoPrincipale);
        }
        if (esternoAngoloSinistro != null) {
            esternoAngoloSinistro.setNord(esternoLatoSinistro);
            esternoAngoloSinistro.setEst(esternoIngressoPrincipale);
        }
        if (esternoLatoDestro != null) {
            esternoLatoDestro.setSud(esternoAngoloDestro);
            esternoLatoDestro.setOvest(null); // Finestra delle scale
        }
        if (esternoLatoSinistro != null) {
            esternoLatoSinistro.setSud(esternoAngoloSinistro);
            esternoLatoSinistro.setEst(null); // Finestra del bagno
        }

        // Collegamenti piano terra
        if (hall != null && corridoio1 != null) {
            hall.setNord(corridoio1);
        }
        if (hall != null && portineria != null) {
            hall.setOvest(portineria);
        }
        if (portineria != null) {
            portineria.setEst(hall);
        }
        if (bagno != null && corridoio2 != null) {
            bagno.setEst(corridoio2);
            bagno.setOvest(esternoLatoSinistro);
        }
        if (corridoio1 != null) {
            corridoio1.setNord(corridoio2);
            corridoio1.setSud(hall);
            corridoio1.setEst(ufficioVicinaCorridoio1);
        }
        if (corridoio2 != null) {
            corridoio2.setNord(corridoio3);
            corridoio2.setSud(corridoio1);
            corridoio2.setEst(magazzino);
            corridoio2.setOvest(bagno);
        }
        if (corridoio3 != null) {
            corridoio3.setSud(corridoio2);
            corridoio3.setEst(scalePT);
            corridoio3.setOvest(salaControllo);
        }
        if (ufficioVicinaScale != null && scalePT != null) {
            ufficioVicinaScale.setSud(scalePT);
        }
        if (ufficioVicinaCorridoio1 != null && corridoio1 != null) {
            ufficioVicinaCorridoio1.setOvest(corridoio1);
        }
        if (magazzino != null && corridoio2 != null) {
            magazzino.setOvest(corridoio2);
        }
        if (scalePT != null) {
            scalePT.setNord(ufficioVicinaScale);
            scalePT.setEst(esternoLatoDestro);
            scalePT.setOvest(corridoio3);
        }

        // Collegamenti seminterrato
        if (ufficioDirettore != null && scaleS != null) {
            ufficioDirettore.setNord(scaleS);
        }
        if (garageUscita != null && scaleS != null) {
            garageUscita.setSud(scaleS);
        }
        if (scaleS != null) {
            scaleS.setNord(garageUscita);
            scaleS.setSud(ufficioDirettore);
            scaleS.setOvest(caveau);
        }
        if (caveau != null && scaleS != null) {
            caveau.setEst(scaleS);
        }

        // Collegamenti esterno a piano terra
        if (esternoLatoDestro != null && scalePT != null) {
            esternoLatoDestro.setOvest(scalePT);  // Finestra delle scale
        }
        if (esternoLatoSinistro != null && bagno != null) {
            esternoLatoSinistro.setEst(bagno);  // Finestra del bagno
        }
    }

    private void aggiungiAlias() {
        Stanza hall = stanzeMap.get("Hall");
        if (hall != null) {
            hall.setAlias(new String[]{"ingresso", "entrata", "reception"});
        }

        Stanza bagno = stanzeMap.get("Bagno");
        if (bagno != null) {
            bagno.setAlias(new String[]{"toilette", "wc", "lavabo"});
        }

        Stanza caveau = stanzeMap.get("Caveau");
        if (caveau != null) {
            caveau.setAlias(new String[]{"cassaforte", "deposito", "tesoro"});
        }

        Stanza esternoIngressoPrincipale = stanzeMap.get("Esterno dell'ingresso principale");
        if (esternoIngressoPrincipale != null) {
            esternoIngressoPrincipale.setAlias(new String[]{"entrata principale", "ingresso principale"});
        }

        Stanza esternoAngoloDestro = stanzeMap.get("Angolo destro della banca");
        if (esternoAngoloDestro != null) {
            esternoAngoloDestro.setAlias(new String[]{"angolo destro", "lato destro angolo"});
        }

        Stanza esternoAngoloSinistro = stanzeMap.get("Angolo sinistro della banca");
        if (esternoAngoloSinistro != null) {
            esternoAngoloSinistro.setAlias(new String[]{"angolo sinistro", "lato sinistro angolo"});
        }

        Stanza esternoLatoDestro = stanzeMap.get("Lato destro");
        if (esternoLatoDestro != null) {
            esternoLatoDestro.setAlias(new String[]{"lato destro esterno", "esterno destro"});
        }

        Stanza esternoLatoSinistro = stanzeMap.get("Lato sinistro");
        if (esternoLatoSinistro != null) {
            esternoLatoSinistro.setAlias(new String[]{"lato sinistro esterno", "esterno sinistro"});
        }

        Stanza portineria = stanzeMap.get("Portineria");
        if (portineria != null) {
            portineria.setAlias(new String[]{"reception", "accoglienza"});
        }

        Stanza salaControllo = stanzeMap.get("Sala Controllo");
        if (salaControllo != null) {
            salaControllo.setAlias(new String[]{"sala sicurezza", "sala monitor"});
        }

        Stanza ufficioVicinaScale = stanzeMap.get("Ufficio Vicino Scale");
        if (ufficioVicinaScale != null) {
            ufficioVicinaScale.setAlias(new String[]{"ufficio scale", "ufficio vicino alle scale"});
        }

        Stanza ufficioVicinaCorridoio1 = stanzeMap.get("Ufficio Vicino Corridoio 1");
        if (ufficioVicinaCorridoio1 != null) {
            ufficioVicinaCorridoio1.setAlias(new String[]{"ufficio", "ufficio vicino corridoio"});
        }

        Stanza magazzino = stanzeMap.get("Magazzino");
        if (magazzino != null) {
            magazzino.setAlias(new String[]{"deposito", "ripostiglio"});
        }

        Stanza scalePT = stanzeMap.get("Scale Piano di Sopra");
        if (scalePT != null) {
            scalePT.setAlias(new String[]{"scale superiori", "scale per il piano di sopra"});
        }

        Stanza corridoio1 = stanzeMap.get("Corridoio 1");
        if (corridoio1 != null) {
            corridoio1.setAlias(new String[]{"primo corridoio", "corridoio uno"});
        }

        Stanza corridoio2 = stanzeMap.get("Corridoio 2");
        if (corridoio2 != null) {
            corridoio2.setAlias(new String[]{"secondo corridoio", "corridoio due"});
        }

        Stanza corridoio3 = stanzeMap.get("Corridoio 3");
        if (corridoio3 != null) {
            corridoio3.setAlias(new String[]{"terzo corridoio", "corridoio tre"});
        }

        Stanza ufficioDirettore = stanzeMap.get("Ufficio Direttore");
        if (ufficioDirettore != null) {
            ufficioDirettore.setAlias(new String[]{"ufficio", "ufficio capo"});
        }

        Stanza scaleS = stanzeMap.get("Scale Piano di Sotto");
        if (scaleS != null) {
            scaleS.setAlias(new String[]{"scale inferiori", "scale per il piano di sotto"});
        }

        Stanza garageUscita = stanzeMap.get("Garage/Uscita");
        if (garageUscita != null) {
            garageUscita.setAlias(new String[]{"uscita", "parcheggio"});
        }
    }

}
