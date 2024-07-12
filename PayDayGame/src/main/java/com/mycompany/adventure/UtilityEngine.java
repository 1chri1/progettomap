package com.mycompany.adventure;

import com.mycompany.meteo.Meteo;
import com.mycompany.type.Oggetto;
import com.mycompany.type.Stanza;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe di utilità per operazioni comuni nel gioco.
 * Fornisce metodi per la gestione di file e inventario.
 */
public class UtilityEngine {

    /**
     * Carica un file e restituisce un set di stringhe.
     * 
     * @param file il file da caricare
     * @return un set di stringhe contenenti le righe del file
     * @throws IOException in caso di errore durante la lettura del file
     */
    public static Set<String> caricaFileStopWords(File file) throws IOException {
        Set<String> set = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (reader.ready()) {
            set.add(reader.readLine().trim().toLowerCase());
        }
        reader.close();
        return set;
    }
    /**
     * Mostra il messaggio iniziale del gioco.
     */
    public static void mostraMessaggioIniziale(PrintStream outputStream, GestioneGioco game) {
        // Stampa informazioni meteo
        Meteo.stampaMeteo("Rome");
        outputStream.println();
        outputStream.println(incipit());
        outputStream.println();
        Stanza stanzaCorrente = game.getStanzaCorrente();
        outputStream.println("Sei nascosto all'" + stanzaCorrente.getNome());
        outputStream.println(stanzaCorrente.getDescrizione());
        outputStream.println();
        outputStream.print("?> ");
    }

    /**
     * Restituisce l'incipit del gioco, inclusi l'obiettivo e il piano della rapina.
     *
     * @return l'incipit del gioco
     */
    public static String incipit() {
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
               + "Buona fortuna, e che la tua avventura abbia inizio!\n"
               + "===========================================================================================================";
    }
}