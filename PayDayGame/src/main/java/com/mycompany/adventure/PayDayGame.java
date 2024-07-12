package com.mycompany.adventure;

import com.mycompany.db.DatabaseManager;
import com.mycompany.implComandi.*;
import com.mycompany.inizializzazione.InizializzazioneComandi;
import com.mycompany.inizializzazione.InizializzazioneOggetti;
import com.mycompany.inizializzazione.InizializzazioneStanze;
import com.mycompany.parser.ParserOutput;
import com.mycompany.swing.GameWindow;
import com.mycompany.thread.TimerGuardia;
import com.mycompany.type.Stanza;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Classe che rappresenta il gioco PayDay.
 * Estende GestioneGioco e implementa GestoreComandi per la gestione dei comandi.
 */
public class PayDayGame extends GestioneGioco implements GestoreComandi, Serializable {

    private static final long serialVersionUID = 1L;
    private final List<Modifica> OSSERVATORI = new ArrayList<>();
    private ParserOutput parserOutput;
    private final List<String> MESSAGGI = new ArrayList<>();
    private final int BOTTINO_BASE = 100000;
    private final int BOTTINO_EXTRA = 50000;
    private boolean quadroElettricoDisattivato;
    private boolean giocoTerminato;
    private boolean torciaAccesa;
    private boolean ricattoDirettore = false;
    private transient DatabaseManager dbManager;
    private transient TimerGuardia timerGuardia;
    private transient Thread timerThread;
    private boolean timerAttivo;
    private int tempoRimastoTimer; // in secondi
    private boolean uscitoDalGioco;
    private transient PrintStream outputStream;
    private transient Engine engine;

    /**
     * Costruttore del gioco PayDay.
     *
     * @param dbManager L'oggetto DatabaseManager per gestire la connessione al database.
     */
    public PayDayGame(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        this.outputStream = System.out; // Default to System.out
    }

    /**
     * Imposta l'engine del gioco.
     *
     * @param engine l'engine da impostare
     */
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    // Metodi di inizializzazione e gestione del gioco

    /**
     * Inizializza il gioco, i comandi, le stanze e gli oggetti.
     *
     * @throws Exception se si verifica un errore durante l'inizializzazione
     */
    @Override
    public void inizializzazione() throws Exception {
        MESSAGGI.clear();

        InizializzazioneComandi inizializzazioneComandi = new InizializzazioneComandi();
        inizializzazioneComandi.initCommandi(this);

        InizializzazioneStanze inizializzazioneStanze = new InizializzazioneStanze(dbManager);
        inizializzazioneStanze.initStanze(this);

        InizializzazioneOggetti inizializzazioneOggetti = new InizializzazioneOggetti(dbManager);
        inizializzazioneOggetti.initOggetti(this);

        // Assegna i gestori dei comandi
        this.assegna(new GestoreApri());
        this.assegna(new GestoreAscolta());
        this.assegna(new GestoreDisattiva());
        this.assegna(new GestoreGuarda());
        this.assegna(new GestoreInventario());
        this.assegna(new GestoreMovimento());
        this.assegna(new GestorePrendi());
        this.assegna(new GestoreEntra());
        this.assegna(new GestoreScendi());
        this.assegna(new GestoreSali());
        this.assegna(new GestoreAttiva());
        this.assegna(new GestoreRicatta());

        engine.getGameWindow().setInputEnabled(true); // Assicura che l'input sia abilitato

        Stanza hall = Stanza.trovaStanza(0, "Esterno dell'ingresso principale");
        setStanzaCorrente(hall);
    }

    /**
     * Gestisce il prossimo spostamento del giocatore.
     *
     * @param p   L'output del parser contenente il comando e altri dettagli
     * @param out Il PrintStream per l'output dei MESSAGGI
     */
    @Override
    public void prossimoSpostamento(ParserOutput p, PrintStream out) {
        parserOutput = p;
        MESSAGGI.clear();
        String MSG_NOUSCITA="\nHai deciso di rimanere e completare la missione.\n";
        
        if (p.getComando() == null) {
            out.println("Non ho capito cosa devo fare! Prova con un altro comando.");
        } else {
            Stanza cr = getStanzaCorrente();
            notificaGestori();
            boolean move = !cr.equals(getStanzaCorrente()) && getStanzaCorrente() != null;
            if (!MESSAGGI.isEmpty()) {
                for (String m : MESSAGGI) {
                    if (m.length() > 0) {
                        out.println(m);
                    }
                }
            }

            if ("Sala Controllo".equalsIgnoreCase(getStanzaCorrente().getNome())) {
                out.println("\nSei stato catturato dalla guardia nella Sala Controllo. Il gioco e' terminato.");
                setGiocoTerminato(true,5);
                return;
            }
            if (move) {
            if (!isQuadroElettricoDisattivato() || isTorciaAccesa()) {
                if (("Angolo destro della banca".equalsIgnoreCase(getStanzaCorrente().getNome())) ||
                    ("Angolo sinistro della banca".equalsIgnoreCase(getStanzaCorrente().getNome()))) {
                    out.println("\nTi trovi all'" + getStanzaCorrente().getNome());
                    out.println(getStanzaCorrente().getDescrizione() + "\n");
                } else {
                    if (("Lato destro".equalsIgnoreCase(getStanzaCorrente().getNome())) ||
                        ("Lato sinistro".equalsIgnoreCase(getStanzaCorrente().getNome()))) {
                        out.println("\nTi trovi sul " + getStanzaCorrente().getNome() + " dell'edificio");
                        out.println(getStanzaCorrente().getDescrizione() + "\n");
                    } else {
                        if (!("Corridoio 1".equalsIgnoreCase(getStanzaCorrente().getNome()) ||
                            "Corridoio 2".equalsIgnoreCase(getStanzaCorrente().getNome()) || 
                            "Corridoio 3".equalsIgnoreCase(getStanzaCorrente().getNome()))) {

                            if (!("Hall".equalsIgnoreCase(getStanzaCorrente().getNome()) && !isQuadroElettricoDisattivato())) {
                                out.println("\nTi trovi qui: " + getStanzaCorrente().getNome());
                                out.println("================================================");
                                out.println(getStanzaCorrente().getDescrizione() + "\n");
                            }
                        }
                    }
                }
            } else {
                out.println("\nNon sai dove sei entrato perche' e' tutto buio.\n");
            }
        }
            if ("Garage/Uscita".equalsIgnoreCase(getStanzaCorrente().getNome())) {
                int bottinoFinale=0;
                if (hasSoldi() && hasGioielli()) {
                    bottinoFinale = BOTTINO_BASE + (isRicattoDirettore() ? BOTTINO_EXTRA : 0);
                    if (isRicattoDirettore()) {
                        out.println("\nMissione compiuta con successo! Hai usato le prove contro il direttore a tuo vantaggio, ottenendo una via di fuga sicura e ulteriori risorse.\nIl tuo futuro sembra luminoso. Il tuo bottino finale ammonta a " + bottinoFinale + " soldi e gioielli.");
                    } else {
                        out.println("\nMissione compiuta con successo! Hai completato la missione con successo, ma sai che qualcuno potrebbe scoprire le prove incriminanti contro il direttore che hai lasciato nel caveau.\nIl tuo futuro e' incerto. Il tuo bottino finale ammonta a " + bottinoFinale + " soldi e gioielli.");
                    }
                    setGiocoTerminato(true, 10);
                    out.println("\nSei riuscito a scappare in tempo!"); // Messaggio di successo
                } else if (hasSoldi()) {
                    int risposta = JOptionPane.showConfirmDialog(null, "Hai preso i soldi ma ti mancano ancora i gioielli. Sei sicuro di voler uscire?", "Conferma Uscita", JOptionPane.YES_NO_OPTION);
                    if (risposta == JOptionPane.YES_OPTION) {
                        bottinoFinale = BOTTINO_BASE;
                        if (isRicattoDirettore()) {
                            bottinoFinale += BOTTINO_EXTRA;
                        }
                        GameWindow.appendOutput("\nHai deciso di uscire senza prendere i gioielli. Il tuo bottino finale ammonta a " + bottinoFinale + " euro.");
                        setGiocoTerminato(true, 10);
                    } else {
                        outputStream.println(MSG_NOUSCITA);
                        setStanzaCorrente(cr);
                    }
                } else if (hasGioielli()) {
                    int risposta = JOptionPane.showConfirmDialog(null, "\nHai preso i gioielli ma ti mancano ancora i soldi. Sei sicuro di voler uscire?", "Conferma Uscita", JOptionPane.YES_NO_OPTION);
                    if (risposta == JOptionPane.YES_OPTION) {
                        bottinoFinale = 0;
                        if (isRicattoDirettore()) {
                            bottinoFinale += BOTTINO_EXTRA;
                        }
                        GameWindow.appendOutput("\nHai deciso di uscire senza prendere i soldi. Il tuo bottino finale ammonta a " + bottinoFinale + " euro oltre ai gioielli.");
                        setGiocoTerminato(true, 10);
                    } else {
                        outputStream.println(MSG_NOUSCITA);
                        setStanzaCorrente(cr);
                    }
                } else {
                    int risposta = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler uscire anche se ti manca ancora qualcosa di importante?", "Conferma Uscita", JOptionPane.YES_NO_OPTION);
                    if (risposta == JOptionPane.YES_OPTION) {
                        bottinoFinale = 0;
                        if (isRicattoDirettore()) {
                            bottinoFinale += BOTTINO_EXTRA;
                        }
                        GameWindow.appendOutput("\nHai deciso di uscire senza completare tutti gli obiettivi. Il tuo bottino finale ammonta a " + bottinoFinale + " euro.");
                        setGiocoTerminato(true, 10);
                    } else {
                        outputStream.println(MSG_NOUSCITA);
                        setStanzaCorrente(cr);
                    }
                }
            }
        }
    }

    // Metodi di gestione del timer

    /**
     * Avvia il timer della guardia.
     *
     * @param minuti la durata del timer in minuti
     */
    @Override
    public void startTimer(int minuti) {
        if (timerGuardia == null) {
            timerGuardia = new TimerGuardia(minuti, this);
            timerThread = new Thread(timerGuardia);
            timerThread.start();
        }
    }

    // Metodi di gestione dello stato del gioco

    /**
     * Verifica se il quadro elettrico è disattivato.
     *
     * @return true se il quadro elettrico è disattivato, altrimenti false
     */
    @Override
    public boolean isQuadroElettricoDisattivato() {
        return quadroElettricoDisattivato;
    }

    /**
     * Imposta lo stato del quadro elettrico.
     *
     * @param quadroElettricoDisattivato true se il quadro elettrico è disattivato, altrimenti false
     */
    @Override
    public void setQuadroElettricoDisattivato(boolean quadroElettricoDisattivato) {
        this.quadroElettricoDisattivato = quadroElettricoDisattivato;
    }

    /**
     * Verifica se il gioco è terminato.
     *
     * @return true se il gioco è terminato, altrimenti false
     */
    @Override
    public boolean isGiocoTerminato() {
        return giocoTerminato;
    }

    /**
     * Imposta lo stato di terminazione del gioco.
     *
     * @param giocoTerminato true se il gioco è terminato, altrimenti false
     */
    @Override
    public void setGiocoTerminato(boolean giocoTerminato, int secondi) {
        if (giocoTerminato) {
            // Disabilita l'input e stampa il messaggio
            engine.getGameWindow().setInputEnabled(false);

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(() -> {
                this.giocoTerminato = true;
                uscitoDalGioco = true;
                scheduler.shutdown();
                // Torna al menu principale
                SwingUtilities.invokeLater(() -> engine.getGameWindow().showMenuPanel());
            }, secondi, TimeUnit.SECONDS);

        } else {
            this.giocoTerminato = false;
            uscitoDalGioco = false;
            engine.getGameWindow().setInputEnabled(true); // Assicura che l'input sia abilitato
        }
    }


    /**
     * Verifica se il giocatore è uscito dal gioco.
     *
     * @return true se il giocatore è uscito dal gioco, altrimenti false
     */
    @Override
    public boolean isUscitoDalGioco() {
        return uscitoDalGioco;
    }

    /**
     * Imposta lo stato di uscita dal gioco.
     *
     * @param uscitoDalGioco true se il giocatore è uscito dal gioco, altrimenti false
     */
    @Override
    public void setUscitoDalGioco(boolean uscitoDalGioco) {
        this.uscitoDalGioco = uscitoDalGioco;
    }

    /**
     * Verifica se la torcia è accesa.
     *
     * @return true se la torcia è accesa, altrimenti false
     */
    @Override
    public boolean isTorciaAccesa() {
        return torciaAccesa;
    }

    /**
     * Imposta lo stato della torcia.
     *
     * @param torciaAccesa true se la torcia è accesa, altrimenti false
     */
    @Override
    public void setTorciaAccesa(boolean torciaAccesa) {
        this.torciaAccesa = torciaAccesa;
    }

    /**
     * Verifica se il direttore è ricattato.
     *
     * @return true se il direttore è ricattato, altrimenti false
     */
    @Override
    public boolean isRicattoDirettore() {
        return ricattoDirettore;
    }

    /**
     * Imposta lo stato di ricatto del direttore.
     *
     * @param ricattoDirettore true se il direttore è ricattato, altrimenti false
     */
    @Override
    public void setRicattoDirettore(boolean ricattoDirettore) {
        this.ricattoDirettore = ricattoDirettore;
    }

    /**
     * Verifica se il timer è attivo.
     *
     * @return true se il timer è attivo, altrimenti false
     */
    @Override
    public boolean isTimerAttivo() {
        return timerAttivo;
    }

    /**
     * Imposta lo stato del timer.
     *
     * @param timerAttivo true se il timer è attivo, altrimenti false
     */
    @Override
    public void setTimerAttivo(boolean timerAttivo) {
        this.timerAttivo = timerAttivo;
    }

    /**
     * Restituisce il timer della guardia.
     *
     * @return il timer della guardia
     */
    @Override
    public TimerGuardia getTimerGuardia() {
        return timerGuardia;
    }

    /**
     * Imposta il timer della guardia.
     *
     * @param timerGuardia il timer della guardia da impostare
     */
    @Override
    public void setTimerGuardia(TimerGuardia timerGuardia) {
        this.timerGuardia = timerGuardia;
    }

    /**
     * Restituisce il thread del timer.
     *
     * @return il thread del timer
     */
    @Override
    public Thread getTimerThread() {
        return timerThread;
    }

    /**
     * Imposta il thread del timer.
     *
     * @param timerThread il thread del timer da impostare
     */
    @Override
    public void setTimerThread(Thread timerThread) {
        this.timerThread = timerThread;
    }

    // Metodi di gestione del salvataggio del gioco

    /**
     * Salva lo stato del gioco corrente su file.
     *
     * @param baseFileName il nome base del file di salvataggio
     * @throws IOException se si verifica un errore durante il salvataggio
     */
    @Override
    public void salvaPartita(String baseFileName) throws IOException {
        if (timerGuardia != null) {
            timerAttivo = true;
            tempoRimastoTimer = timerGuardia.getTempoRimasto(); // Ottieni il tempo rimanente in secondi
            TimerGuardia.stop(); // Ferma il timer
        } else {
            timerAttivo = false;
        }
        gestisciSalvataggi(baseFileName, ".");
    }
    /**
     * Carica lo stato del gioco da un file di salvataggio.
     *
     * @param filePath il percorso del file di salvataggio
     * @return l'oggetto GestioneGioco caricato
     * @throws IOException se si verifica un errore durante il caricamento
     * @throws ClassNotFoundException se la classe non viene trovata durante il caricamento
     */
    @Override
    public GestioneGioco caricaPartita(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            PayDayGame giocoCaricato = (PayDayGame) in.readObject();
            // Reimposta il dbManager dopo il caricamento
            giocoCaricato.dbManager = DatabaseManager.getInstance();
            engine.getGameWindow().setInputEnabled(true); // Assicura che l'input sia abilitato
            
            if (giocoCaricato.timerAttivo) {
                int minutiRimasti = giocoCaricato.tempoRimastoTimer / 60;
                outputStream.println("Tempo rimasto per il timer caricato: " + minutiRimasti + " minuti (" + giocoCaricato.tempoRimastoTimer + " secondi).");
                giocoCaricato.startTimer(minutiRimasti); // Riavvia il timer con il tempo rimanente in minuti
                outputStream.println("Timer riavviato con successo.");
            }
            return giocoCaricato;
        } catch (IOException | ClassNotFoundException e) {
            outputStream.println("Errore durante il caricamento della partita: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Restituisce l'elenco dei file di salvataggio.
     *
     * @param directory la directory in cui cercare i file di salvataggio
     * @return la lista dei nomi dei file di salvataggio
     */
    @Override
    public List<String> elencoSalvataggi(String directory) {
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            return new ArrayList<>();
        }

        File[] files = dir.listFiles((d, name) -> name.startsWith("save_") && name.endsWith(".dat"));
        if (files == null || files.length == 0) {
            return new ArrayList<>();
        }

        return Stream.of(files)
                .sorted((f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()))
                .map(File::getName)
                .collect(Collectors.toList());
    }

    /**
     * Gestisce i file di salvataggio, sovrascrivendo quelli esistenti se necessario.
     *
     * @param baseFileName il nome base del file di salvataggio
     * @param directory    la directory in cui salvare i file
     * @throws IOException se si verifica un errore durante il salvataggio
     */
    @Override
    public void gestisciSalvataggi(String baseFileName, String directory) throws IOException {
        List<String> salvataggi = elencoSalvataggi(directory);
        boolean salvataggioCompleto = false;

        if (salvataggi.size() >= 5) {
            StringBuilder elencoSalvataggi = new StringBuilder("Hai raggiunto il numero massimo di salvataggi.\nScegli un file da sovrascrivere:\n");
            for (int i = 0; i < salvataggi.size(); i++) {
                elencoSalvataggi.append((i + 1)).append(". ").append(salvataggi.get(i)).append("\n");
            }

            int scelta = -1;

            // Leggi l'input per la scelta del file da sovrascrivere
            while (scelta < 1 || scelta > salvataggi.size()) {
                String sceltaInput = JOptionPane.showInputDialog(null, elencoSalvataggi.toString());
                if (sceltaInput == null) { // Se l'utente ha cliccato "Cancel"
                    outputStream.println("Operazione di sovrascrittura annullata.");
                    return;
                }
                try {
                    scelta = Integer.parseInt(sceltaInput);
                    if (scelta < 1 || scelta > salvataggi.size()) {
                        outputStream.println("Scelta non valida. Inserisci un numero dall'elenco.");
                        scelta = -1; // Indica una scelta non valida
                    }
                } catch (NumberFormatException e) {
                    outputStream.println("Scelta non valida. Inserisci un numero dall'elenco.");
                    scelta = -1; // Indica una scelta non valida
                }
            }

            String fileDaSovrascrivere = salvataggi.get(scelta - 1);
            File file = new File(directory, fileDaSovrascrivere);
            if (!file.delete()) {
                outputStream.println("Errore nella sovrascrittura del file. Operazione annullata.");
                return;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "save_" + baseFileName + "_" + timeStamp + ".dat";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(directory, fileName)))) {
            out.writeObject(this);
            outputStream.println("Partita salvata con successo come " + fileName);
            salvataggioCompleto = true;
        } catch (IOException e) {
            outputStream.println("Errore durante il salvataggio della partita: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Assegna un osservatore al gioco.
     *
     * @param o L'osservatore da assegnare
     */
    @Override
    public void assegna(Modifica o) {
        if (!OSSERVATORI.contains(o)) {
            OSSERVATORI.add(o);
        }
    }

    /**
     * Rimuove un osservatore dal gioco.
     *
     * @param o L'osservatore da rimuovere
     */
    @Override
    public void rimuovi(Modifica o) {
        OSSERVATORI.remove(o);
    }

    /**
     * Notifica tutti i gestori dei comandi delle modifiche.
     */
    @Override
    public void notificaGestori() {
        for (Modifica o : OSSERVATORI) {
            MESSAGGI.add(o.aggiorna(this, parserOutput));
        }
    }

    /**
     * Verifica se l'inventario contiene soldi.
     *
     * @return true se l'inventario contiene soldi, altrimenti false
     */
    private boolean hasSoldi() {
        return getInventario().stream().anyMatch(o -> "soldi".equalsIgnoreCase(o.getNome()));
    }

    /**
     * Verifica se l'inventario contiene gioielli.
     *
     * @return true se l'inventario contiene gioielli, altrimenti false
     */
    private boolean hasGioielli() {
        return getInventario().stream().anyMatch(o -> "gioielli".equalsIgnoreCase(o.getNome()));
    }

    /**
     * Imposta l'output stream per l'output.
     *
     * @param outputStream il PrintStream da impostare
     */
    @Override
    public void setOutputStream(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Restituisce l'output stream per l'output.
     *
     * @return il PrintStream per l'output
     */
    @Override
    public PrintStream getOutputStream() {
        return outputStream;
    }
}
