package com.mycompany.inizializzazione;

import com.mycompany.adventure.GestioneGioco;
import com.mycompany.type.Comandi;
import com.mycompany.type.TipoComandi;

public class InizializzazioneComandi {

    public void initCommandi(GestioneGioco game) {
        Comandi nord = new Comandi(TipoComandi.NORD, "nord");
        nord.setAlias(new String[]{"Nord", "NORD", "avanti"});
        game.getComandi().add(nord);

        Comandi inventario = new Comandi(TipoComandi.INVENTARIO, "inventario");
        inventario.setAlias(new String[]{"inv"});
        game.getComandi().add(inventario);

        Comandi sud = new Comandi(TipoComandi.SUD, "sud");
        sud.setAlias(new String[]{"Sud", "SUD"});
        game.getComandi().add(sud);

        Comandi est = new Comandi(TipoComandi.EST, "est");
        est.setAlias(new String[]{"Est", "EST"});
        game.getComandi().add(est);

        Comandi ovest = new Comandi(TipoComandi.OVEST, "ovest");
        ovest.setAlias(new String[]{"Ovest", "OVEST"});
        game.getComandi().add(ovest);

        Comandi esci = new Comandi(TipoComandi.ESCI, "esci");
        esci.setAlias(new String[]{"end", "fine", "esci", "muori", "ammazzati", "ucciditi", "suicidati", "exit", "basta"});
        game.getComandi().add(esci);

        Comandi guarda = new Comandi(TipoComandi.GUARDA, "guarda");
        guarda.setAlias(new String[]{"osserva", "vedi", "trova", "cerca", "descrivi"});
        game.getComandi().add(guarda);

        Comandi prendi = new Comandi(TipoComandi.PRENDI, "prendi");
        prendi.setAlias(new String[]{"raccogli"});
        game.getComandi().add(prendi);

        Comandi apri = new Comandi(TipoComandi.APRI, "apri");
        apri.setAlias(new String[]{"aprire"});
        game.getComandi().add(apri);

        Comandi spingi = new Comandi(TipoComandi.SPINGI, "spingi");
        spingi.setAlias(new String[]{"premi"});
        game.getComandi().add(spingi);

        Comandi usa = new Comandi(TipoComandi.USA, "usa");
        usa.setAlias(new String[]{"utilizza", "combina"});
        game.getComandi().add(usa);

        Comandi chiudi = new Comandi(TipoComandi.CHIUDI, "chiudi");
        chiudi.setAlias(new String[]{"chiudere"});
        game.getComandi().add(chiudi);

        Comandi tira = new Comandi(TipoComandi.TIRA, "tira");
        tira.setAlias(new String[]{"estrai"});
        game.getComandi().add(tira);

        Comandi entra = new Comandi(TipoComandi.ENTRA, "entra");
        entra.setAlias(new String[]{"entrare","scavalca"});
        game.getComandi().add(entra);
        
        Comandi scendi = new Comandi(TipoComandi.SCENDI, "scendi");
        scendi.setAlias(new String[]{"scendere","vai giù"});
        game.getComandi().add(scendi);
        
        Comandi sali = new Comandi(TipoComandi.SALI, "sali");
        sali.setAlias(new String[]{"salire","vai sù"});
        game.getComandi().add(sali);

        Comandi accendi = new Comandi(TipoComandi.ACCENDI, "accendi");
        accendi.setAlias(new String[]{"illumina"});
        game.getComandi().add(accendi);

        Comandi disattiva = new Comandi(TipoComandi.DISATTIVA, "disattiva");
        disattiva.setAlias(new String[]{"spegni"});
        game.getComandi().add(disattiva);
        
        Comandi attiva = new Comandi(TipoComandi.ATTIVA, "attiva");
        attiva.setAlias(new String[]{"abilita"});
        game.getComandi().add(attiva);
       
        Comandi ascolta = new Comandi(TipoComandi.ASCOLTA, "ascolta");
        ascolta.setAlias(new String[]{"senti"});
        game.getComandi().add(ascolta);
        
        Comandi ricatto = new Comandi(TipoComandi.RICATTO, "ricatto");
        ricatto.setAlias(new String[]{"ricatta"});
        game.getComandi().add(ricatto);
    }
}