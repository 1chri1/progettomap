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
        this.game.setEngine(this); // Passa l'istanza di Engine al gioco
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
     * Restituisce la finestra di gioco.
     *
     * @return la finestra di gioco
     */
    public GameWindow getGameWindow() {
        return gameWindow;
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
     * Metodo per leggere l'input dell'utente.
     *
     * @return la stringa inserita dall'utente
     */
    public String readInput() {
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "";
        }
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
     * Metodo per processare un comando tramite l'interfaccia grafica.
     *
     * @param command Il comando da processare.
     */
    public void processCommand(String command) {
        enqueueInput(command);
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
            Set<String> stopwords = UtilityEngine.caricaFileStopWords(new File("./resources/stopwords"));
            parser = new Parser(stopwords);
        } catch (IOException ex) {
            outputStream.println(ex);
        }
    }

    /**
     * Inizia una nuova partita.
     */
    public void nuovaPartita() {
        String dbUrl = "jdbc:h2:mem:testdb"; // Configurazione per il database in memoria
        String user = "user";
        String password = "password";

        DatabaseManager dbManager = DatabaseManager.getInstance();
        try {
            dbManager.close(); // Chiudi la connessione al database corrente se esiste
            dbManager.inizializzazioneEConnessione(dbUrl, user, password); // Reimposta il database
            game = new PayDayGame(dbManager); // Crea un nuovo oggetto GestioneGioco
            game.setOutputStream(outputStream); // Imposta l'output stream nel nuovo gioco
            game.setEngine(this); // Passa l'istanza di Engine al gioco
            GameWindow.clearOutput(); // Pulisce l'output prima di mostrare il messaggio iniziale
            inizializzaGioco(); // Inizializza il gioco
            
            UtilityEngine.mostraMessaggioIniziale(outputStream, game);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Salva la partita corrente.
     */
    private void gestoreSalva() {
        String nomeSalvataggio = null;
        while (true) {
            nomeSalvataggio = JOptionPane.showInputDialog(null, "Inserisci il nome del salvataggio:");
            if (nomeSalvataggio == null) { // Se l'utente ha cliccato "Cancel"
                GameWindow.appendOutput("Operazione di salvataggio annullata.");
                return; // Esce dal metodo senza salvare
            }
            if (!nomeSalvataggio.trim().isEmpty()) {
                break; // Esce dal ciclo se l'input non è vuoto
            }
            GameWindow.appendOutput("Il nome del salvataggio non può essere vuoto. Riprova.");
        }

        try {
            game.salvaPartita(nomeSalvataggio);
            partitaSalvata = true; // Indica che la partita è stata salvata
        } catch (IOException e) {
            GameWindow.appendOutput("Errore durante il salvataggio della partita: " + e.getMessage());
        }
    }

    /**
     * Carica una partita salvata.
     */
    public void gestoreCarica() {
        List<String> salvataggi = game.elencoSalvataggi(".");
        if (salvataggi.isEmpty()) {
            JOptionPane.showMessageDialog(gameWindow, "Non ci sono salvataggi disponibili.");
            SwingUtilities.invokeLater(() -> this.getGameWindow().showMenuPanel());
        } else {
            gameWindow.clearOutput(); // Pulisce l'output prima di mostrare il messaggio iniziale
            boolean sceltaValida = false;
            while (!sceltaValida) {
                StringBuilder elencoSalvataggi = new StringBuilder("Scegli un file da caricare:\n");
                for (int i = 0; i < salvataggi.size(); i++) {
                    elencoSalvataggi.append((i + 1)).append(". ").append(salvataggi.get(i)).append("\n");
                }
                String sceltaInput = JOptionPane.showInputDialog(null, elencoSalvataggi.toString());
                if (sceltaInput == null) { // Se l'utente ha cliccato "Cancel"
                    SwingUtilities.invokeLater(() -> this.getGameWindow().showMenuPanel());
                    break;
                }
                int scelta;
                try {
                    scelta = Integer.parseInt(sceltaInput);
                    if (scelta < 1 || scelta > salvataggi.size()) {
                        GameWindow.appendOutput("Scelta non valida. Inserisci un numero dall'elenco.");
                    } else {
                        String fileDaCaricare = salvataggi.get(scelta - 1);
                        try {
                            game = (GestioneGioco) game.caricaPartita(fileDaCaricare);
                            game.setOutputStream(outputStream); // Imposta l'output stream nel gioco caricato
                            game.setEngine(this); // Imposta l'istanza di Engine nel gioco caricato
                            GameWindow.appendOutput("Partita caricata con successo.");
                            sceltaValida = true;
                            partitaSalvata = false; // Resetta il flag per evitare il messaggio ripetuto
                        } catch (IOException | ClassNotFoundException e) {
                            GameWindow.appendOutput("Errore durante il caricamento della partita: " + e.getMessage());
                        }
                    }
                } catch (NumberFormatException e) {
                    GameWindow.appendOutput("Scelta non valida. Inserisci un numero dall'elenco.");
                }
            }
        }
    }

    /**
     * Esegue il ciclo principale del gioco.
     */
    public void execute() {
        gameWindow.showMenuPanel();
        while (true) {
            boolean giocoAttivo = true;
            while (giocoAttivo && !game.isUscitoDalGioco()) {
                if (partitaSalvata) {
                    int scelta = JOptionPane.showOptionDialog(null, "Vuoi rimanere nella partita corrente o vuoi tornare al menu principale?", "Scelta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Rimani", "Menu"}, "Rimani");
                    if (scelta == JOptionPane.NO_OPTION) {
                        giocoAttivo = false;
                        partitaSalvata = false;
                        gameWindow.showMenuPanel();
                        break;
                    } else if (scelta == JOptionPane.YES_OPTION) {
                        GameWindow.appendOutput("Sei rimasto nella partita");
                        partitaSalvata = false;
                    } else {
                        continue;
                    }
                }

                while (!game.isGiocoTerminato() && !partitaSalvata) {
                    String command = readInput();
                    if (command == null) {
                        continue;
                    }
                    if (command.equalsIgnoreCase("salva")) {
                        gestoreSalva();
                        break;
                    } else if (command.equalsIgnoreCase("carica")) {
                        int conferma = JOptionPane.showConfirmDialog(null, "Per caricare una partita salvata devi uscire dalla partita in corso e tornare al menu iniziale. \nVuoi procedere?", "Conferma Caricamento", JOptionPane.YES_NO_OPTION);
                        if (conferma == JOptionPane.YES_OPTION) {
                            int risposta = JOptionPane.showConfirmDialog(null, "Vuoi salvare la partita corrente prima di tornare al menu?", "Conferma Salvataggio", JOptionPane.YES_NO_OPTION);
                            if (risposta == JOptionPane.YES_OPTION) {
                                gestoreSalva();
                            }
                            giocoAttivo = false;
                            partitaSalvata = false;
                            gameWindow.showMenuPanel();
                        } else {
                            GameWindow.appendOutput("\nOperazione di caricamento annullata. Puoi continuare a giocare.\n");
                        }
                        break;
                    } else if (command.equalsIgnoreCase("esci")) {
                        int confermaEsci = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler uscire dal gioco?", "Conferma Uscita", JOptionPane.YES_NO_OPTION);
                        if (confermaEsci == JOptionPane.YES_OPTION) {
                            int rispostaSalva = JOptionPane.showConfirmDialog(null, "Vuoi salvare la partita corrente prima di uscire?", "Conferma Salvataggio", JOptionPane.YES_NO_OPTION);
                            if (rispostaSalva == JOptionPane.YES_OPTION) {
                                gestoreSalva();
                            } else {
                                GameWindow.appendOutput("\nStai per uscire dal gioco, attendere...");
                                game.setGiocoTerminato(true, 5);
                            }
                            giocoAttivo = false;
                        } else {
                            GameWindow.appendOutput("\nOperazione di uscita annullata. Puoi continuare a giocare.\n");
                        }
                        break;
                    }
                    ParserOutput p = parser.parse(command, game.getComandi(), game.getStanzaCorrente().getOggetti(), game.getInventario(), game.getStanze());
                    if (p == null || p.getComando() == null) {
                        GameWindow.appendOutput("\nNon capisco quello che mi vuoi dire.\n");
                    } else {
                        game.prossimoSpostamento(p, outputStream);
                        if (game.isGiocoTerminato()) {
                            partitaSalvata = false; // Impostiamo questo a false per assicurarsi che non mostri il menu di nuovo
                            break;  
                        }
                        if (game.getStanzaCorrente() == null) {
                            outputStream.println("La tua avventura termina qui! Complimenti!");
                            System.exit(0);
                        }
                    }
                }

                if (game.isGiocoTerminato()) {
                    gameWindow.showMenuPanel();
                    break;
                }
            }

            if (game.isUscitoDalGioco()) {
                game.setUscitoDalGioco(false);
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
            dbManager.inizializzazioneEConnessione(dbUrl, user, password);

            // Inizializza il gioco
            PayDayGame game = new PayDayGame(dbManager); // Passiamo dbManager qui
            Engine engine = new Engine(game);
            game.setEngine(engine); // Imposta l'engine nel gioco

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
        } finally {
            dbManager.close();
        }
    }
}
