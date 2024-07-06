package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;
import java.util.Scanner;

/**
 * Classe che gestisce il comando di uscita dal gioco.
 */
public class GestoreEsci implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Aggiorna lo stato del gioco in base al comando di uscita.
     * 
     * @param descrizione la descrizione del gioco
     * @param parserOutput l'output del parser che contiene il comando
     * @return messaggio di risposta al giocatore
     */
    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo() == TipoComandi.ESCI) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Sei sicuro di voler uscire dal gioco senza salvare? (sì/no) ");
            System.out.print("\nPer salvare digita il comando 'salva'\n");
            String risposta = scanner.nextLine().trim().toLowerCase();

            if (risposta.equals("sì") || risposta.equals("si")) {
                System.out.println("Hai deciso di uscire dal gioco. Arrivederci!");
                descrizione.setGiocoTerminato(true);
            } else {
                return "Hai deciso di rimanere nel gioco.";
            }
        }
        return "";
    }
}
