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

public class Engine {

    private GestioneGioco game;
    private Parser parser;
    private boolean partitaSalvata;

    /**
     * Costruttore per inizializzare il gioco e il parser.
     *
     * @param game L'oggetto GestioneGioco che rappresenta il gioco.
     */
    public Engine(GestioneGioco game) {
        this.game = game;
        inizializzaGioco();
        inizializzaParser();
    }

    /**
     * Metodo per inizializzare il gioco.
     */
    private void inizializzaGioco() {
        try {
            this.game.inizializzazione();
        } catch (Exception ex) {
            System.err.println(ex);
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
            System.err.println(ex);
        }
    }

    /**
     * Esegue il ciclo principale del gioco.
     */
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            mostraMenuIniziale(scanner);
            boolean giocoAttivo = true;

            while (giocoAttivo && !game.isUscitoDalGioco()) {
                if (partitaSalvata) {
                    System.out.println("Vuoi rimanere nella partita corrente o vuoi tornare al menu principale? (rimani/menu)");
                    String scelta = scanner.nextLine().trim().toLowerCase();
                    if (scelta.equals("menu")) {
                        giocoAttivo = false;
                        partitaSalvata = false; // Resetta il flag per evitare il messaggio ripetuto
                        break;
                    } else if (scelta.equals("rimani")) {
                        partitaSalvata = false; // Resetta il flag per continuare nella partita corrente
                    } else {
                        System.out.println("Scelta non valida. Operazione annullata.");
                        continue;
                    }
                }

                while (scanner.hasNextLine() && !game.isGiocoTerminato() && !partitaSalvata) {
                    String command = scanner.nextLine();
                    if (command.equalsIgnoreCase("salva")) {
                        salvaPartita(scanner);
                        break;
                    } else if (command.equalsIgnoreCase("carica")) {
                        System.out.println("Per caricare una partita salvata devi uscire dalla partita in corso e tornare al menu iniziale.");
                        System.out.println("Vuoi procedere? (sì/no)");
                        String conferma = scanner.nextLine().trim().toLowerCase();
                        if (conferma.equals("sì") || conferma.equals("si")) {
                            System.out.println("Vuoi salvare la partita corrente prima di tornare al menu? (sì/no)");
                            String risposta = scanner.nextLine().trim().toLowerCase();
                            if (risposta.equals("sì") || risposta.equals("si")) {
                                salvaPartita(scanner);
                            }
                            giocoAttivo = false;
                            partitaSalvata = false;
                        } else {
                            System.out.println("Operazione di caricamento annullata. Puoi continuare a giocare.");
                        }
                        break;
                    } else if (command.equalsIgnoreCase("esci")) {
                        System.out.println("Sei sicuro di voler uscire dal gioco? (sì/no)");
                        String confermaEsci = scanner.nextLine().trim().toLowerCase();
                        if (confermaEsci.equals("sì") || confermaEsci.equals("si")) {
                            System.out.println("Vuoi salvare la partita corrente prima di uscire? (sì/no)");
                            String rispostaSalva = scanner.nextLine().trim().toLowerCase();
                            if (rispostaSalva.equals("sì") || rispostaSalva.equals("si")) {
                                salvaPartita(scanner);
                            }
                            else {
                                game.fermaTimer(); // Ferma il timer quando si esce senza salvare
                            }
                            System.out.println("Hai deciso di uscire dal gioco. Arrivederci!");
                            giocoAttivo = false;
                            game.setUscitoDalGioco(true);
                        } else {
                            System.out.println("Operazione di uscita annullata. Puoi continuare a giocare.");
                        }
                        break;
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

                if (game.isGiocoTerminato()) {
                    break;
                }
            }

            if (game.isUscitoDalGioco()) {
                game.setUscitoDalGioco(false); // Resetta lo stato per il prossimo ciclo
            }
        }

        scanner.close();
    }

    /**
     * Mostra il menu iniziale del gioco.
     *
     * @param scanner L'oggetto Scanner per leggere l'input dell'utente.
     */
    private void mostraMenuIniziale(Scanner scanner) {
        System.out.println("====================================================");
        System.out.println("*                  PayDayGame 2024                 *");
        System.out.println("*                   developed by                   *");
        System.out.println("*              Alessandro Aldo Mangione            *");
        System.out.println("*                   Tommaso Palumbo                *");
        System.out.println("*                   Christian Vurchio              *");
        System.out.println("====================================================");
        System.out.println();
        System.out.println("1. Inizia una nuova partita");
        System.out.println("2. Carica partita");
        System.out.println("3. Esci");

        boolean sceltaValida = false;
        while (!sceltaValida) {
            System.out.print("Scegli un'opzione: ");
            String scelta = scanner.nextLine().trim().toLowerCase();
            switch (scelta) {
                case "1":
                    nuovaPartita();
                    mostraMessaggioIniziale();
                    sceltaValida = true;
                    break;
                case "2":
                    caricaPartita(scanner);
                    sceltaValida = true;
                    break;
                case "3":
                    System.out.println("Grazie per aver giocato! Alla prossima!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Scelta non valida. Per favore, seleziona 1, 2 o 3.");
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
            inizializzaGioco(); // Inizializza il gioco
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra il messaggio iniziale del gioco.
     */
    private void mostraMessaggioIniziale() {
        System.out.println();
        System.out.println(Incipit());
        System.out.println(game.MessaggioIniziale());
        System.out.println();
        System.out.println("Sei nascosto all'" + game.getStanzaCorrente().getNome());
        System.out.println(game.getStanzaCorrente().getDescrizione());
        System.out.println();
        System.out.print("?> ");
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
     *
     * @param scanner L'oggetto Scanner per leggere l'input dell'utente.
     */
    private void salvaPartita(Scanner scanner) {
        System.out.print("Inserisci il nome del salvataggio: ");
        String nomeSalvataggio = scanner.nextLine().trim();
        try {
            game.salvaPartita(nomeSalvataggio);
            partitaSalvata = true; // Indica che la partita è stata salvata
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio della partita: " + e.getMessage());
        }
    }

    /**
     * Carica una partita salvata.
     *
     * @param scanner L'oggetto Scanner per leggere l'input dell'utente.
     */
    private void caricaPartita(Scanner scanner) {
        List<String> salvataggi = game.elencoSalvataggi(".");
        if (salvataggi.isEmpty()) {
            System.out.println("Non ci sono salvataggi disponibili.");
            mostraMenuIniziale(scanner); // Torna al menu iniziale
        } else {
            boolean sceltaValida = false;
            while (!sceltaValida) {
                System.out.println("Scegli un file da caricare:");
                for (int i = 0; i < salvataggi.size(); i++) {
                    System.out.println((i + 1) + ". " + salvataggi.get(i));
                }

                String sceltaInput = scanner.nextLine();
                int scelta;
                try {
                    scelta = Integer.parseInt(sceltaInput);
                    if (scelta < 1 || scelta > salvataggi.size()) {
                        System.out.println("Scelta non valida. Inserisci un numero dall'elenco.");
                    } else {
                        String fileDaCaricare = salvataggi.get(scelta - 1);
                        try {
                            game = (GestioneGioco) game.caricaPartita(fileDaCaricare);
                            System.out.println("Partita caricata con successo.");
                            sceltaValida = true;
                            partitaSalvata = false; // Resetta il flag per evitare il messaggio ripetuto
                        } catch (IOException | ClassNotFoundException e) {
                            System.err.println("Errore durante il caricamento della partita: " + e.getMessage());
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Scelta non valida. Inserisci un numero dall'elenco.");
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
            engine.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.close();
        }
    }
}
