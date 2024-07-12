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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Engine {

    private GestioneGioco game;
    private Parser parser;
    private boolean partitaSalvata;
    private PrintStream outputStream;
    private BlockingQueue<String> inputQueue;
    private GameWindow gameWindow;

    public Engine(GestioneGioco game) {
        this.game = game;
        this.outputStream = System.out;
        this.inputQueue = new LinkedBlockingQueue<>();
        UtilityEngine.inizializzaGioco(this.game, this.outputStream);
        UtilityEngine.inizializzaParser(this.parser, new File("./resources/stopwords"), this.outputStream);
        this.game.setEngine(this);
    }

    public void setOutputStream(PrintStream outputStream) {
        this.outputStream = outputStream;
        this.game.setOutputStream(outputStream);
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    public String readInput() {
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "";
        }
    }

    public void enqueueInput(String input) {
        inputQueue.offer(input);
    }

    public void processCommand(String command) {
        enqueueInput(command);
    }

    public void nuovaPartita() {
        String dbUrl = "jdbc:h2:mem:testdb";
        String user = "user";
        String password = "password";

        DatabaseManager dbManager = DatabaseManager.getInstance();
        try {
            dbManager.close();
            dbManager.inizializzazioneEConnessione(dbUrl, user, password);
            game = new PayDayGame(dbManager);
            game.setOutputStream(outputStream);
            game.setEngine(this);
            GameWindow.clearOutput();
            UtilityEngine.inizializzaGioco(game, outputStream);
            UtilityEngine.mostraMessaggioIniziale(outputStream, game);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carica una partita salvata.
     */
    public void caricaPartita() {
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
                        outputStream.println("Scelta non valida. Inserisci un numero dall'elenco.");
                    } else {
                        String fileDaCaricare = salvataggi.get(scelta - 1);
                        try {
                            game = (GestioneGioco) game.caricaPartita(fileDaCaricare);
                            game.setOutputStream(outputStream); // Imposta l'output stream nel gioco caricato
                            game.setEngine(this); // Imposta l'istanza di Engine nel gioco caricato
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
                        outputStream.println("Sei rimasto nella partita");
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
                        UtilityEngine.salvaPartita(game, outputStream);
                        break;
                    } else if (command.equalsIgnoreCase("carica")) {
                        int conferma = JOptionPane.showConfirmDialog(null, "Per caricare una partita salvata devi uscire dalla partita in corso e tornare al menu iniziale. \nVuoi procedere?", "Conferma Caricamento", JOptionPane.YES_NO_OPTION);
                        if (conferma == JOptionPane.YES_OPTION) {
                            int risposta = JOptionPane.showConfirmDialog(null, "Vuoi salvare la partita corrente prima di tornare al menu?", "Conferma Salvataggio", JOptionPane.YES_NO_OPTION);
                            if (risposta == JOptionPane.YES_OPTION) {
                                UtilityEngine.salvaPartita(game, outputStream);
                            }
                            giocoAttivo = false;
                            partitaSalvata = false;
                            gameWindow.showMenuPanel();
                        } else {
                            outputStream.println("Operazione di caricamento annullata. Puoi continuare a giocare.");
                        }
                        break;
                    } else if (command.equalsIgnoreCase("esci")) {
                        int confermaEsci = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler uscire dal gioco?", "Conferma Uscita", JOptionPane.YES_NO_OPTION);
                        if (confermaEsci == JOptionPane.YES_OPTION) {
                            int rispostaSalva = JOptionPane.showConfirmDialog(null, "Vuoi salvare la partita corrente prima di uscire?", "Conferma Salvataggio", JOptionPane.YES_NO_OPTION);
                            if (rispostaSalva == JOptionPane.YES_OPTION) {
                                UtilityEngine.salvaPartita(game, outputStream);
                            } else {
                                outputStream.println("Stai per uscire dal gioco, attendere...");
                                game.setGiocoTerminato(true, 5);
                            }
                            giocoAttivo = false;
                        } else {
                            outputStream.println("Operazione di uscita annullata. Puoi continuare a giocare.");
                        }
                        break;
                    }
                    ParserOutput p = parser.parse(command, game.getComandi(), game.getStanzaCorrente().getOggetti(), game.getInventario(), game.getStanze());
                    if (p == null || p.getComando() == null) {
                        outputStream.println("Non capisco quello che mi vuoi dire.");
                    } else {
                        game.prossimoSpostamento(p, outputStream);
                        if (game.isGiocoTerminato()) {
                            partitaSalvata = false;
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
                    gameWindow.showMenuPanel();
                    break;
                }
            }

            if (game.isUscitoDalGioco()) {
                game.setUscitoDalGioco(false);
            }
        }
    }

    public static void main(String[] args) {
        String dbUrl = "jdbc:h2:mem:testdb";
        String user = "user";
        String password = "password";

        DatabaseManager dbManager = DatabaseManager.getInstance();

        try {
            dbManager.inizializzazioneEConnessione(dbUrl, user, password);

            PayDayGame game = new PayDayGame(dbManager);
            Engine engine = new Engine(game);
            game.setEngine(engine);

            GameWindow gameWindow = new GameWindow(engine);
            TextAreaOutputStream taOutputStream = new TextAreaOutputStream(gameWindow.getOutputArea());
            PrintStream ps = new PrintStream(taOutputStream);
            engine.setOutputStream(ps);
            engine.setGameWindow(gameWindow);

            SwingUtilities.invokeLater(() -> gameWindow.setVisible(true));

            engine.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbManager.close();
        }
    }
}
