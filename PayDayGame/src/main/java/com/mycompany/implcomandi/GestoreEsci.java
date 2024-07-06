package com.mycompany.implComandi;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.parser.ParserOutput;
import com.mycompany.type.TipoComandi;
import java.io.Serializable;
import java.util.Scanner;

public class GestoreEsci implements Modifica, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String aggiorna(GestioneGioco descrizione, ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo() == TipoComandi.ESCI) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Sei sicuro di voler uscire dal gioco? (sì/no) ");
            String risposta = scanner.nextLine().trim().toLowerCase();

            if (risposta.equals("sì") || risposta.equals("si")) {
                System.out.println("Hai deciso di uscire dal gioco. Arrivederci!");
                descrizione.setGiocoTerminato(true);
                descrizione.setUscitoDalGioco(true); // Imposta uscitoDalGioco a true
            } 
            else if (risposta.equals("no")) {
                System.out.println("Hai deciso di rimanere nel gioco.");
            }
            else
            {
                return "La risposta non è chiara, rimani nel gioco.";
            }
        }
        return "";
    }
}
