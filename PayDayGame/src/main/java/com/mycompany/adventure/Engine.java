package com.mycompany.adventure;

import com.mycompany.parser.Parser;
import com.mycompany.parser.ParserOutput;
import com.mycompany.db.DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Classe principale per l'esecuzione del motore di gioco.
 */
public class Engine {

    private GestioneGioco game;
    private Parser parser;

    /**
     * Costruttore della classe Engine.
     * 
     * @param game l'istanza di GestioneGioco da utilizzare
     */
    public Engine(GestioneGioco game) {
        this.game = game;
        try {
            this.game.inizializzazione();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        try {
            Set<String> stopwords = Utility.caricaFile(new File("./resources/stopwords"));
            parser = new Parser(stopwords);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    /**
     * Metodo principale per l'esecuzione del gioco.
     */
    public void execute() {
        mostraMessaggioIniziale();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine() && !game.isGiocoTerminato()) {
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("salva")) {
                salvaPartita(scanner);
                continue;
            } else if (command.equalsIgnoreCase("carica")) {
                caricaPartita(scanner);
                continue;
            }
            ParserOutput p = parser.parse(command, game.getComandi(), game.getStanzaCorrente().getOggetti(), game.getInventario(), game.getStanze());
            if (p == null || p.getComando() == null) {
                System.out.println("Non capisco quello che mi vuoi dire.");
            } else {
                game.ProssimoSpostamento(p, System.out);
                if (game.isGiocoTerminato()) {
                    break;
                }
                if (game.getStanzaCorrente() == null) {
                    System.out.println("La tua avventura termina qui! Complimenti!");
                    System.exit(0);
                }
            }
            System.out.print("?> ");
        }
        scanner.close();
    }

    /**
     * Metodo per mostrare il messaggio iniziale del gioco.
     */
    private void mostraMessaggioIniziale() {
        System.out.println("====================================================");
        System.out.println("*                  PayDayGame 2024                 *");
        System.out.println("*                   developed by                   *");
        System.out.println("*              Alessandro Aldo Mangione            *");
        System.out.println("*                   Tommaso Palumbo                *");
        System.out.println("*                   Christian Vurchio              *");
        System.out.println("====================================================");
        System.out.println();
        System.out.println(game.MessaggioIniziale());
        System.out.println();
        System.out.println("Sei nascosto all'" + game.getStanzaCorrente().getNome());
        System.out.println(game.getStanzaCorrente().getDescrizione());
        System.out.println();
        System.out.print("?> ");
    }

    /**
     * Metodo per salvare la partita.
     * 
     * @param scanner l'istanza di Scanner per leggere l'input dell'utente
     */
    private void salvaPartita(Scanner scanner) {
        System.out.print("Inserisci il nome del salvataggio: ");
        String nomeSalvataggio = scanner.nextLine().trim();
        try {
            game.salvaPartita(nomeSalvataggio);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio della partita: " + e.getMessage());
        }
    }

    /**
     * Metodo per caricare una partita salvata.
     * 
     * @param scanner l'istanza di Scanner per leggere l'input dell'utente
     */
    private void caricaPartita(Scanner scanner) {
        List<String> salvataggi = game.elencoSalvataggi(".");
        if (salvataggi.isEmpty()) {
            System.out.println("Non ci sono salvataggi disponibili.");
        } else {
            System.out.println("Scegli un file da caricare:");
            for (int i = 0; i < salvataggi.size(); i++) {
                System.out.println((i + 1) + ". " + salvataggi.get(i));
            }
            int scelta = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (scelta < 1 || scelta > salvataggi.size()) {
                System.out.println("Scelta non valida. Operazione annullata.");
            } else {
                String fileDaCaricare = salvataggi.get(scelta - 1);
                try {
                    game = (GestioneGioco) game.caricaPartita(fileDaCaricare);
                    System.out.println("Partita caricata con successo.");
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Errore durante il caricamento della partita: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Metodo principale per l'avvio del programma.
     * 
     * @param args argomenti della riga di comando
     */
    public static void main(String[] args) {
        String dbUrl = "jdbc:h2:mem:testdb"; // Configurazione per il database in memoria
        String user = "user";
        String password = "password";

        DatabaseManager dbManager = DatabaseManager.getInstance();

        try {
            // Inizializza e connetti al database
            dbManager.initializeAndConnect(dbUrl, user, password);

            // Inizializza il gioco
            GestioneGioco game = new PayDayGame(dbManager); // Passiamo dbManager qui
            Engine engine = new Engine(game);
            engine.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.close();
        }
    }
}
