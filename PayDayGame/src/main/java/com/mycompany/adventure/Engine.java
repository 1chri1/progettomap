package com.mycompany.adventure;

import com.mycompany.parser.Parser;
import com.mycompany.parser.ParserOutput;
import com.mycompany.db.DatabaseManager;
import com.mycompany.swing.GameWindow;
import com.mycompany.swing.TextAreaOutputStream;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Engine {

    private GestioneGioco game;
    private Parser parser;
    private boolean partitaSalvata;
    private PrintStream outputStream;
    private BlockingQueue<String> inputQueue;
    private GameWindow gameWindow;

    /**
     * Costruttore per inizializzare il gioco e il parser.
     *
     * @param game L'oggetto GestioneGioco che rappresenta il gioco.
     */
    public Engine(GestioneGioco game) {
        this.game = game;
        this.outputStream = System.out; // Default to System.out
        this.inputQueue = new LinkedBlockingQueue<>();
        inizializzaGioco();
        inizializzaParser();
    }

    /**
     * Imposta l'output stream per il gioco.
     *
     * @param outputStream Il nuovo output stream.
     */
    public void setOutputStream(PrintStream outputStream) {
        this.outputStream = outputStream;
        this.game.setOutputStream(outputStream); // Imposta l'output stream nel gioco
    }

    /**
     * Imposta la finestra di gioco.
     *
     * @param gameWindow La finestra di gioco.
     */
    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    /**
     * Metodo per inserire input nella coda.
     *
     * @param input L'input da inserire.
     */
    public void enqueueInput(String input) {
        inputQueue.offer(input);
    }

    /**
     * Metodo per inizializzare il gioco.
     */
    private void inizializzaGioco() {
        try {
            this.game.inizializzazione();
        } catch (Exception ex) {
            outputStream.println(ex);
        }
    }

    /**
     * Metodo per inizializzare il parser.
     */
    private void inizializzaParser() {
        try {
            Set<String> stopwords = Utility.caricaFile(new File("./resources/stopwords"));
            parser = new Parser(stopwords);
        } catch (IOException ex) {
            outputStream.println(ex);
        }
    }

    /**
     * Esegue il ciclo principale del gioco.
     */
    public void execute() {
        boolean running = true;

        while (running) {
            mostraMenuIniziale();
            boolean giocoAttivo = true;

            while (giocoAttivo && !game.isUscitoDalGioco()) {
                if (partitaSalvata) {
                    outputStream.println("Vuoi rimanere nella partita corrente o vuoi tornare al menu principale? (rimani/menu)");
                    String scelta = readInput().trim().toLowerCase();
                    if (scelta.equals("menu")) {
                        giocoAttivo = false;
                        partitaSalvata = false; // Resetta il flag per evitare il messaggio ripetuto
                        break;
                    } else if (scelta.equals("rimani")) {
                        partitaSalvata = false; // Resetta il flag per continuare nella partita corrente
                    } else {
                        outputStream.println("Scelta non valida. Operazione annullata.");
                        continue;
                    }
                }

                while (!game.isGiocoTerminato() && !partitaSalvata) {
                    String command = readInput();
                    if (command.equalsIgnoreCase("salva")) {
                        salvaPartita();
                        break;
                    } else if (command.equalsIgnoreCase("carica")) {
                        outputStream.println("Per caricare una partita salvata devi uscire dalla partita in corso e tornare al menu iniziale.");
                        outputStream.println("Vuoi procedere? (sì/no)");
                        String conferma = readInput().trim().toLowerCase();
                        if (conferma.equals("sì") || conferma.equals("si")) {
                            outputStream.println("Vuoi salvare la partita corrente prima di tornare al menu? (sì/no)");
                            String risposta = readInput().trim().toLowerCase();
                            if (risposta.equals("sì") || risposta.equals("si")) {
                                salvaPartita();
                            }
                            giocoAttivo = false;
                            partitaSalvata = false;
                        } else {
                            outputStream.println("Operazione di caricamento annullata. Puoi continuare a giocare.");
                        }
                        break;
                    } else if (command.equalsIgnoreCase("esci")) {
                        outputStream.println("Sei sicuro di voler uscire dal gioco? (sì/no)");
                        String confermaEsci = readInput().trim().toLowerCase();
                        if (confermaEsci.equals("sì") || confermaEsci.equals("si")) {
                            outputStream.println("Vuoi salvare la partita corrente prima di uscire? (sì/no)");
                            String rispostaSalva = readInput().trim().toLowerCase();
                            if (rispostaSalva.equals("sì") || rispostaSalva.equals("si")) {
                                salvaPartita();
                            } else {
                                game.fermaTimer(); // Ferma il timer quando si esce senza salvare
                            }
                            outputStream.println("Hai deciso di uscire dal gioco. Arrivederci!");
                            giocoAttivo = false;
                            game.setUscitoDalGioco(true);
                        } else {
                            outputStream.println("Operazione di uscita annullata. Puoi continuare a giocare.");
                        }
                        break;
                    }
                    ParserOutput p = parser.parse(command, game.getComandi(), game.getStanzaCorrente().getOggetti(), game.getInventario(), game.getStanze());
                    if (p == null || p.getComando() == null) {
                        outputStream.println("Non capisco quello che mi vuoi dire.");
                    } else {
                        game.ProssimoSpostamento(p, outputStream);
                        if (game.isGiocoTerminato()) {
                            break;
                        }
                        if (game.getStanzaCorrente() == null) {
                            outputStream.println("La tua avventura termina qui! Complimenti!");
                            System.exit(0);
                        }
                    }
                    outputStream.print("?> ");
                }

                if (game.isGiocoTerminato()) {
                    break;
                }
            }

            if (game.isUscitoDalGioco()) {
                game.setUscitoDalGioco(false); // Resetta lo stato per il prossimo ciclo
            }
        }
    }

    /**
     * Mostra il menu iniziale del gioco.
     */
    private void mostraMenuIniziale() {
        outputStream.println("====================================================");
        outputStream.println("*                  PayDayGame 2024                 *");
        outputStream.println("*                   developed by                   *");
        outputStream.println("*              Alessandro Aldo Mangione            *");
        outputStream.println("*                   Tommaso Palumbo                *");
        outputStream.println("*                   Christian Vurchio              *");
        outputStream.println("====================================================");
        outputStream.println();
        outputStream.println("1. Inizia una nuova partita");
        outputStream.println("2. Carica partita");
        outputStream.println("3. Esci");

        boolean sceltaValida = false;
        while (!sceltaValida) {
            outputStream.print("Scegli un'opzione: ");
            String scelta = readInput().trim().toLowerCase();
            switch (scelta) {
                case "1":
                    nuovaPartita();
                    mostraMessaggioIniziale();
                    sceltaValida = true;
                    break;
                case "2":
                    caricaPartita();
                    sceltaValida = true;
                    break;
                case "3":
                    outputStream.println("Grazie per aver giocato! Alla prossima!");
                    System.exit(0);
                    break;
                default:
                    outputStream.println("Scelta non valida. Per favore, seleziona 1, 2 o 3.");
                    break;
            }
        }
    }

    /**
     * Inizia una nuova partita.
     */
    private void nuovaPartita() {
        String dbUrl = "jdbc:h2:mem:testdb"; // Configurazione per il database in memoria
        String user = "user";
        String password = "password";

        DatabaseManager dbManager = DatabaseManager.getInstance();
        try {
            dbManager.close(); // Chiudi la connessione al database corrente se esiste
            dbManager.initializeAndConnect(dbUrl, user, password); // Reimposta il database
            game = new PayDayGame(dbManager); // Crea un nuovo oggetto GestioneGioco
            game.setOutputStream(outputStream); // Imposta l'output stream nel nuovo gioco
            inizializzaGioco(); // Inizializza il gioco
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra il messaggio iniziale del gioco.
     */
    private void mostraMessaggioIniziale() {
        outputStream.println();
        outputStream.println(Incipit());
        outputStream.println(game.MessaggioIniziale());
        outputStream.println();
        outputStream.println("Sei nascosto all'" + game.getStanzaCorrente().getNome());
        outputStream.println(game.getStanzaCorrente().getDescrizione());
        outputStream.println();
        outputStream.print("?> ");
    }

    /**
     * Restituisce l'incipit del gioco, inclusi l'obiettivo e il piano della rapina.
     *
     * @return l'incipit del gioco
     */
    public static String Incipit() {
        return "Benvenuto in PayDay!\n\n"
               + "In una citta' corrotta, dove la legge e' solo un lontano ricordo, tu e la tua banda di ladri\n"
               + "avete un obiettivo ambizioso: rapinare la banca piu' sorvegliata della citta'.\n\n"
               + "L'obiettivo e' semplice, ma pericoloso: infiltrati nella banca, evita le guardie e le telecamere,\n"
               + "e disattiva il quadro elettrico per oscurare le telecamere di sicurezza.\n\n"
               + "Ma ricorda, una volta disattivato il quadro, l'oscurità sarà totale. Dovrai trovare una torcia\n"
               + "per orientarti nel buio.\n\n"
               + "Inoltre, il caveau non sarà facilmente accessibile. Dovrai cercare le chiavi della stanza del direttore,\n"
               + "che ti sveleranno il modo di accedere al caveau.\n\n"
               + "All'interno del caveau, troverai una fortuna in soldi e gioielli. Ma attenzione,\n"
               + "il direttore della banca nasconde un segreto: delle prove compromettenti che possono\n"
               + "essere usate per ricattarlo e ottenere un bottino piu' alto.\n\n"
               + "Il tempo e' contro di te. Le guardie sono sempre all'erta e ogni passo falso puo' costarti caro.\n"
               + "Pianifica i tuoi movimenti con attenzione, raccogli tutto il bottino possibile e scappa dal garage.\n\n"
               + "Buona fortuna, e che la tua avventura abbia inizio!";
    }

    
    /**
     * Salva la partita corrente.
     */
    private void salvaPartita() {
        outputStream.print("Inserisci il nome del salvataggio: ");
        String nomeSalvataggio = readInput().trim();
        try {
            game.salvaPartita(nomeSalvataggio);
            partitaSalvata = true; // Indica che la partita è stata salvata
        } catch (IOException e) {
            outputStream.println("Errore durante il salvataggio della partita: " + e.getMessage());
        }
    }

    /**
     * Carica una partita salvata.
     */
    private void caricaPartita() {
        List<String> salvataggi = game.elencoSalvataggi(".");
        if (salvataggi.isEmpty()) {
            outputStream.println("Non ci sono salvataggi disponibili.");
            mostraMenuIniziale(); // Torna al menu iniziale
        } else {
            boolean sceltaValida = false;
            while (!sceltaValida) {
                outputStream.println("Scegli un file da caricare:");
                for (int i = 0; i < salvataggi.size(); i++) {
                    outputStream.println((i + 1) + ". " + salvataggi.get(i));
                }

                String sceltaInput = readInput();
                int scelta;
                try {
                    scelta = Integer.parseInt(sceltaInput);
                    if (scelta < 1 || scelta > salvataggi.size()) {
                        outputStream.println("Scelta non valida. Inserisci un numero dall'elenco.");
                    } else {
                        String fileDaCaricare = salvataggi.get(scelta - 1);
                        try {
                            game = (GestioneGioco) game.caricaPartita(fileDaCaricare);
                            game.setOutputStream(outputStream); // Imposta l'output stream nel gioco caricato
                            outputStream.println("Partita caricata con successo.");
                            sceltaValida = true;
                            partitaSalvata = false; // Resetta il flag per evitare il messaggio ripetuto
                        } catch (IOException | ClassNotFoundException e) {
                            outputStream.println("Errore durante il caricamento della partita: " + e.getMessage());
                        }
                    }
                } catch (NumberFormatException e) {
                    outputStream.println("Scelta non valida. Inserisci un numero dall'elenco.");
                }
            }
        }
    }

    /**
     * Metodo principale per avviare il gioco.
     *
     * @param args Argomenti della riga di comando.
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
            
            // Crea la finestra di gioco
            GameWindow gameWindow = new GameWindow(engine);
            TextAreaOutputStream taOutputStream = new TextAreaOutputStream(gameWindow.getOutputArea());
            PrintStream ps = new PrintStream(taOutputStream);
            engine.setOutputStream(ps);
            engine.setGameWindow(gameWindow);
            
            SwingUtilities.invokeLater(() -> {
                gameWindow.setVisible(true);
            });

            engine.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.close();
        }
    }

    /**
     * Metodo per leggere l'input dell'utente.
     *
     * @return la stringa inserita dall'utente
     */
    private String readInput() {
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "";
        }
    }

    /**
     * Metodo per processare un comando tramite l'interfaccia grafica.
     *
     * @param command Il comando da processare.
     */
    public void processCommand(String command) {
        enqueueInput(command);
    }
}
