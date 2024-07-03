package com.mycompany.adventure;

import com.mycompany.implComandi.*;
import com.mycompany.parser.ParserOutput;
import com.mycompany.inizializzazione.InizializzazioneComandi;
import com.mycompany.inizializzazione.InizializzazioneStanze;
import com.mycompany.inizializzazione.InizializzazioneOggetti;
import com.mycompany.db.DatabaseManager;
import com.mycompany.type.Stanza;
import com.mycompany.type.TipoComandi;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.PrintStream;
import java.util.Scanner;

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

    public PayDayGame(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void inizializzazione() throws Exception {
        messaggi.clear();

        InizializzazioneComandi inizializzazioneComandi = new InizializzazioneComandi();
        inizializzazioneComandi.initCommandi(this);

        InizializzazioneStanze inizializzazioneStanze = new InizializzazioneStanze(dbManager);
        inizializzazioneStanze.initStanze(this);

        InizializzazioneOggetti inizializzazioneOggetti = new InizializzazioneOggetti(dbManager);
        inizializzazioneOggetti.initOggetti(this);

        Modifica apriGestore = new GestoreApri();
        this.assegna(apriGestore);
        Modifica ascoltaGestore = new GestoreAscolta();
        this.assegna(ascoltaGestore);
        Modifica disattivaGestore = new GestoreDisattiva();
        this.assegna(disattivaGestore);
        Modifica guardaGestore = new GestoreGuarda();
        this.assegna(guardaGestore);
        Modifica inventarioGestore = new GestoreInventario();
        this.assegna(inventarioGestore);
        Modifica movimentoGestore = new GestoreMovimento();
        this.assegna(movimentoGestore);
        Modifica prendiGestore = new GestorePrendi();
        this.assegna(prendiGestore);
        Modifica entraGestore = new GestoreEntra();
        this.assegna(entraGestore);
        Modifica scendiGestore = new GestoreScendi();
        this.assegna(scendiGestore);
        Modifica saliGestore = new GestoreSali();
        this.assegna(saliGestore);
        Modifica esciGestore = new GestoreEsci();
        this.assegna(esciGestore);
        Modifica attivaGestore = new GestoreAttiva();
        this.assegna(attivaGestore);
        Modifica ricattoGestore = new GestoreRicatto();
        this.assegna(ricattoGestore);

        Stanza hall = Stanza.trovaStanza(0, "Esterno dell'ingresso principale");
        setStanzaCorrente(hall);
    }

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
                    System.out.println("Sei stato arrestato perché le telecamere sono attive. Il gioco è terminato.");
                }
                return;
            }
            if ("Sala Controllo".equalsIgnoreCase(getStanzaCorrente().getNome())) {
                setGiocoTerminato(true);
                System.out.println("Sei stato catturato dalla guardia nella Sala Controllo. Il gioco è terminato.");
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
                            out.println("Ti trovi qui: " + getStanzaCorrente().getNome());
                            out.println("================================================");
                            out.println(getStanzaCorrente().getDescrizione());
                        }
                    }
                } else {
                    out.println("Non sai dove sei entrato perché è tutto buio.");
                }
            }
            if ("Garage/Uscita".equalsIgnoreCase(getStanzaCorrente().getNome())) {
                if (hasSoldiGioielli()) {
                    int bottinoFinale = bottinoBase + (isRicattoDirettore() ? bottinoExtra : 0);
                    setGiocoTerminato(true);
                    if (isRicattoDirettore()) {
                        out.println("Missione compiuta con successo! Hai usato le prove contro il direttore a tuo vantaggio, ottenendo una via di fuga sicura e ulteriori risorse. Il tuo futuro sembra luminoso. Il tuo bottino finale ammonta a " + bottinoFinale + " soldi e gioielli.");
                    } else {
                        out.println("Missione compiuta con successo! Hai completato la missione con successo, ma sai che qualcuno potrebbe scoprire le prove incriminanti contro il direttore che hai lasciato nel caveau. Il tuo futuro è incerto. Il tuo bottino finale ammonta a " + bottinoFinale + " soldi e gioielli.");
                    }
                } else {
                    out.println("Sei sicuro di voler uscire anche se ti manca ancora qualcosa di importante? (sì/no)");
                    Scanner scanner = new Scanner(System.in);
                    String risposta = scanner.nextLine().trim().toLowerCase();
                    if ("sì".equals(risposta) || "si".equals(risposta)) {
                        int bottinoFinale = bottinoBase;
                        if (isRicattoDirettore()) {
                            bottinoFinale += bottinoExtra;
                        }
                        setGiocoTerminato(true);
                        out.println("Hai deciso di uscire senza completare tutti gli obiettivi. Il tuo bottino finale ammonta a " + bottinoFinale + " euro oltre ai gioielli.");
                    } else {
                        out.println("Hai deciso di rimanere e completare la missione.");
                        setStanzaCorrente(cr);
                    }
                }
            }
        }
    }

    private boolean hasSoldiGioielli() {
        boolean haSoldiGioielli = getInventario().stream().anyMatch(o -> "soldi e gioielli".equalsIgnoreCase(o.getNome()));
        return haSoldiGioielli;
    }

    @Override
    public void assegna(Modifica o) {
        if (!osservatori.contains(o)) {
            osservatori.add(o);
        }
    }

    @Override
    public void rimuovi(Modifica o) {
        osservatori.remove(o);
    }

    @Override
    public void notificaGestori() {
        for (Modifica o : osservatori) {
            messaggi.add(o.aggiorna(this, parserOutput));
        }
    }

    @Override
    public String MessaggioIniziale() {
        return "L'avventura ha inizio";
    }

    @Override
    public boolean isQuadroElettricoDisattivato() {
        return quadroElettricoDisattivato;
    }

    @Override
    public void setQuadroElettricoDisattivato(boolean quadroElettricoDisattivato) {
        this.quadroElettricoDisattivato = quadroElettricoDisattivato;
    }

    @Override
    public boolean isGiocoTerminato() {
        return giocoTerminato;
    }

    @Override
    public void setGiocoTerminato(boolean giocoTerminato) {
        this.giocoTerminato = giocoTerminato;
    }

    @Override
    public boolean isTorciaAccesa() {
        return torciaAccesa;
    }

    @Override
    public void setTorciaAccesa(boolean torciaAccesa) {
        this.torciaAccesa = torciaAccesa;
    }

    @Override
    public boolean isRicattoDirettore() {
        return ricattoDirettore;
    }

    @Override
    public void setRicattoDirettore(boolean ricattoDirettore) {
        this.ricattoDirettore = ricattoDirettore;
    }

    @Override
    public void salvaPartita(String baseFileName) throws IOException {
        gestisciSalvataggi(baseFileName, ".");
    }

    @Override
    public GestioneGioco caricaPartita(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            return (GestioneGioco) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento della partita: " + e.getMessage());
            throw e;
        }
    }

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

    @Override
    public void gestisciSalvataggi(String baseFileName, String directory) throws IOException {
        List<String> salvataggi = elencoSalvataggi(directory);
        if (salvataggi.size() >= 5) {
            System.out.println("Hai raggiunto il numero massimo di salvataggi. Scegli un file da sovrascrivere:");
            for (int i = 0; i < salvataggi.size(); i++) {
                System.out.println((i + 1) + ". " + salvataggi.get(i));
            }
            Scanner scanner = new Scanner(System.in);
            int scelta = scanner.nextInt();
            if (scelta < 1 || scelta > salvataggi.size()) {
                System.out.println("Scelta non valida. Operazione annullata.");
                return;
            }
            File fileDaSovrascrivere = new File(directory, salvataggi.get(scelta - 1));
            if (!fileDaSovrascrivere.delete()) {
                System.out.println("Errore nella sovrascrittura del file. Operazione annullata.");
                return;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "save_" + baseFileName + "_" + timeStamp + ".dat";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(directory, fileName)))) {
            out.writeObject(this);
            System.out.println("Partita salvata con successo come " + fileName);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio della partita: " + e.getMessage());
            throw e;
        }
    }
}
