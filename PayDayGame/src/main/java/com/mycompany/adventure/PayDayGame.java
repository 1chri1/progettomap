package com.mycompany.adventure;

import com.mycompany.db.DatabaseManager;
import com.mycompany.implComandi.GestoreApri;
import com.mycompany.implComandi.GestoreAscolta;
import com.mycompany.implComandi.GestoreAttiva;
import com.mycompany.implComandi.GestoreDisattiva;
import com.mycompany.implComandi.GestoreEntra;
import com.mycompany.implComandi.GestoreEsci;
import com.mycompany.implComandi.GestoreGuarda;
import com.mycompany.implComandi.GestoreInventario;
import com.mycompany.implComandi.GestoreMovimento;
import com.mycompany.implComandi.GestorePrendi;
import com.mycompany.implComandi.GestoreRicatta;
import com.mycompany.implComandi.GestoreSali;
import com.mycompany.implComandi.GestoreScendi;
import com.mycompany.implComandi.Modifica;
import com.mycompany.inizializzazione.InizializzazioneComandi;
import com.mycompany.inizializzazione.InizializzazioneOggetti;
import com.mycompany.inizializzazione.InizializzazioneStanze;
import com.mycompany.meteo.Meteo;
import com.mycompany.parser.ParserOutput;
import com.mycompany.thread.TimerGuardia;
import com.mycompany.type.Comandi;
import com.mycompany.type.Oggetto;
import com.mycompany.type.Stanza;
import com.mycompany.type.TipoComandi;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.SwingUtilities;

public class PayDayGame extends GestioneGioco implements GestoreComandi, Serializable {

     private static final long serialVersionUID = 1L;
    private final List<Modifica> osservatori = new ArrayList<>();
    private ParserOutput parserOutput;
    private final List<String> messaggi = new ArrayList<>();
    private final int bottinoBase = 100000;
    private final int bottinoExtra = 50000;
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
    
    // Aggiungi un metodo per impostare l'engine
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
        messaggi.clear();

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
        this.assegna(new GestoreEsci());
        this.assegna(new GestoreAttiva());
        this.assegna(new GestoreRicatta());

        // Stampa informazioni meteo
        Meteo.stampaMeteo("Rome");

        Stanza hall = Stanza.trovaStanza(0, "Esterno dell'ingresso principale");
        setStanzaCorrente(hall);
    }

    /**
     * Gestisce il prossimo spostamento del giocatore.
     *
     * @param p   L'output del parser contenente il comando e altri dettagli
     * @param out Il PrintStream per l'output dei messaggi
     */
    @Override
    public void ProssimoSpostamento(ParserOutput p, PrintStream out) {
        parserOutput = p;
        messaggi.clear();
        if (p.getComando() == null) {
            out.println("Non ho capito cosa devo fare! Prova con un altro comando.");
        } else {
            Stanza cr = getStanzaCorrente();
            notificaGestori();
            boolean move = !cr.equals(getStanzaCorrente()) && getStanzaCorrente() != null;
            if (!messaggi.isEmpty()) {
                for (String m : messaggi) {
                    if (m.length() > 0) {
                        out.println(m);
                    }
                }
            }
            if (isGiocoTerminato()) {
                if (p.getComando().getTipo() == TipoComandi.ESCI) {
                    return;
                }
                if ("Hall".equalsIgnoreCase(getStanzaCorrente().getNome()) && !isQuadroElettricoDisattivato()) {
                    out.println("Sei stato arrestato perche' le telecamere sono attive. Il gioco e' terminato.");
                }
                return;
            }
            if ("Sala Controllo".equalsIgnoreCase(getStanzaCorrente().getNome())) {
                setGiocoTerminato(true);
                out.println("Sei stato catturato dalla guardia nella Sala Controllo. Il gioco e' terminato.");
                fermaTimer();  // Ferma il timer
                return;
            }
            if (move) {
                if (!isQuadroElettricoDisattivato() || isTorciaAccesa()) {
                    if (("Angolo destro della banca".equalsIgnoreCase(getStanzaCorrente().getNome())) ||
                        ("Angolo sinistro della banca".equalsIgnoreCase(getStanzaCorrente().getNome()))) {
                        out.println("Ti trovi all'" + getStanzaCorrente().getNome());
                        out.println(getStanzaCorrente().getDescrizione());
                    } else {
                        if (("Lato destro".equalsIgnoreCase(getStanzaCorrente().getNome())) ||
                            ("Lato sinistro".equalsIgnoreCase(getStanzaCorrente().getNome()))) {
                            out.println("Ti trovi sul " + getStanzaCorrente().getNome() + " dell'edificio");
                            out.println(getStanzaCorrente().getDescrizione());
                        } else {
                            if (!("Corridoio 1".equalsIgnoreCase(getStanzaCorrente().getNome()) ||
                                "Corridoio 2".equalsIgnoreCase(getStanzaCorrente().getNome()) || 
                                "Corridoio 3".equalsIgnoreCase(getStanzaCorrente().getNome()))) { 
                                out.println("Ti trovi qui: " + getStanzaCorrente().getNome());
                                out.println("================================================");
                                out.println(getStanzaCorrente().getDescrizione());  
                            }
                        }
                    }
                } else {
                    out.println("Non sai dove sei entrato perche' e' tutto buio.");
                }
            }
            if ("Garage/Uscita".equalsIgnoreCase(getStanzaCorrente().getNome())) {
                if (hasSoldiGioielli()) {
                    int bottinoFinale = bottinoBase + (isRicattoDirettore() ? bottinoExtra : 0);
                    if (isRicattoDirettore()) {
                        out.println("Missione compiuta con successo! Hai usato le prove contro il direttore a tuo vantaggio, ottenendo una via di fuga sicura e ulteriori risorse.\nIl tuo futuro sembra luminoso. Il tuo bottino finale ammonta a " + bottinoFinale + " soldi e gioielli.");
                        setGiocoTerminato(true);
                        fermaTimer(); // Ferma il timer
                    } else {
                        out.println("Missione compiuta con successo! Hai completato la missione con successo, ma sai che qualcuno potrebbe scoprire le prove incriminanti contro il direttore che hai lasciato nel caveau.\nIl tuo futuro e' incerto. Il tuo bottino finale ammonta a " + bottinoFinale + " soldi e gioielli.");
                        setGiocoTerminato(true);
                        fermaTimer(); // Ferma il timer
                    }
                    out.println("Sei riuscito a scappare in tempo!"); // Messaggio di successo
                } else {
                    out.println("Sei sicuro di voler uscire anche se ti manca ancora qualcosa di importante? (si'/no)");
                    Scanner scanner = new Scanner(System.in);
                    String risposta = scanner.nextLine().trim().toLowerCase();
                    if ("si'".equals(risposta) || "si".equals(risposta)) {
                        int bottinoFinale = bottinoBase;
                        if (isRicattoDirettore()) {
                            bottinoFinale += bottinoExtra;
                        }
                        out.println("Hai deciso di uscire senza completare tutti gli obiettivi. Il tuo bottino finale ammonta a " + bottinoFinale + " euro oltre ai gioielli.");
                        setGiocoTerminato(true);
                        fermaTimer(); // Ferma il timer
                    } else {
                        out.println("Hai deciso di rimanere e completare la missione.");
                        setStanzaCorrente(cr);
                    }
                }
            }
        }
    }

    // Metodi di gestione del timer

    /**
     * Ferma il timer della guardia se è attivo.
     */
    @Override
    public void fermaTimer() {
        if (timerGuardia != null) {
            timerGuardia.stop();
            timerGuardia = null;
            timerThread = null;
        }
        timerAttivo = false;
    }

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
     * Imposta lo stato del gioco come terminato.
     *
     * @param giocoTerminato true se il gioco è terminato, altrimenti false
     */
    @Override
    public void setGiocoTerminato(boolean giocoTerminato) {
        this.giocoTerminato = giocoTerminato;
        if (giocoTerminato) {
            uscitoDalGioco = true; // Imposta uscitoDalGioco a true quando il gioco è terminato
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
            timerGuardia.stop(); // Ferma il timer
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
            outputStream.println("Timer attivo: " + giocoCaricato.timerAttivo); // Debug per verificare lo stato del timer
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

        if (salvataggi.size() >= 5) {
            outputStream.println("Hai raggiunto il numero massimo di salvataggi. Scegli un file da sovrascrivere:");
            for (int i = 0; i < salvataggi.size(); i++) {
                outputStream.println((i + 1) + ". " + salvataggi.get(i));
            }

            final int[] scelta = new int[1];
            scelta[0] = -1;

            // Leggi l'input per la scelta del file da sovrascrivere
            while (scelta[0] < 1 || scelta[0] > salvataggi.size()) {
                try {
                    synchronized (engine) {
                        engine.wait(); // Attendi fino a quando non viene notificato
                    }
                    String sceltaInput = engine.readInput(); // Leggi l'input
                    scelta[0] = Integer.parseInt(sceltaInput);
                    if (scelta[0] < 1 || scelta[0] > salvataggi.size()) {
                        outputStream.println("Scelta non valida. Inserisci un numero dall'elenco.");
                        scelta[0] = -1; // Indica una scelta non valida
                    }
                } catch (NumberFormatException | InterruptedException e) {
                    outputStream.println("Scelta non valida. Inserisci un numero dall'elenco.");
                    scelta[0] = -1; // Indica una scelta non valida
                }
            }

            String fileDaSovrascrivere = salvataggi.get(scelta[0] - 1);
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
        } catch (IOException e) {
            outputStream.println("Errore durante il salvataggio della partita: " + e.getMessage());
            throw e;
        }
    }

    public void notifyInput() {
        synchronized (this) {
            this.notify();
        }
    }      


    /**
     * Assegna un osservatore al gioco.
     *
     * @param o L'osservatore da assegnare
     */
    @Override
    public void assegna(Modifica o) {
        if (!osservatori.contains(o)) {
            osservatori.add(o);
        }
    }

    /**
     * Rimuove un osservatore dal gioco.
     *
     * @param o L'osservatore da rimuovere
     */
    @Override
    public void rimuovi(Modifica o) {
        osservatori.remove(o);
    }

    /**
     * Notifica tutti i gestori dei comandi delle modifiche.
     */
    @Override
    public void notificaGestori() {
        for (Modifica o : osservatori) {
            messaggi.add(o.aggiorna(this, parserOutput));
        }
    }

    /**
     * Restituisce il messaggio iniziale del gioco.
     *
     * @return Il messaggio iniziale
     */
    @Override
    public String MessaggioIniziale() {
        return "L'avventura ha inizio";
    }

    /**
     * Verifica se l'inventario contiene sia soldi che gioielli.
     *
     * @return true se l'inventario contiene sia soldi che gioielli, altrimenti false
     */
    private boolean hasSoldiGioielli() {
        boolean haSoldi = getInventario().stream().anyMatch(o -> "soldi".equalsIgnoreCase(o.getNome()));
        boolean haGioielli = getInventario().stream().anyMatch(o -> "gioielli".equalsIgnoreCase(o.getNome()));
        return haSoldi && haGioielli;
    }

    @Override
    public void setOutputStream(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public PrintStream getOutputStream() {
        return outputStream;
    }
}
