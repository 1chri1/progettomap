package com.mycompany.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Pannello del menu principale del gioco, con pulsanti per avviare una nuova partita,
 * caricare una partita esistente o uscire dal gioco.
 */
public class MenuPanel extends BackgroundPanel {
    private JButton nuovaPartitaButton;
    private JButton caricaButton;
    private JButton esciButton;

    /**
     * Costruttore per il pannello del menu.
     * 
     * @param listener l'ActionListener per gestire gli eventi dei pulsanti
     * @param backgroundImagePath il percorso dell'immagine di sfondo
     */
    public MenuPanel(ActionListener listener, String backgroundImagePath) {
        super(backgroundImagePath); // Passa il percorso dell'immagine di sfondo al costruttore di BackgroundPanel
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        nuovaPartitaButton = new JButton("Nuova Partita");
        caricaButton = new JButton("Carica");
        esciButton = new JButton("Esci");

        Dimension buttonSize = new Dimension(200, 50);
        nuovaPartitaButton.setPreferredSize(buttonSize);
        caricaButton.setPreferredSize(buttonSize);
        esciButton.setPreferredSize(buttonSize);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nuovaPartitaButton, gbc);
        gbc.gridy = 1;
        add(caricaButton, gbc);
        gbc.gridy = 2;
        add(esciButton, gbc);

        nuovaPartitaButton.addActionListener(listener);
        caricaButton.addActionListener(listener);
        esciButton.addActionListener(listener);
    }

    /**
     * Restituisce il pulsante "Nuova Partita".
     * 
     * @return il pulsante "Nuova Partita"
     */
    public JButton getNuovaPartitaButton() {
        return nuovaPartitaButton;
    }

    /**
     * Restituisce il pulsante "Carica".
     * 
     * @return il pulsante "Carica"
     */
    public JButton getCaricaButton() {
        return caricaButton;
    }

    /**
     * Restituisce il pulsante "Esci".
     * 
     * @return il pulsante "Esci"
     */
    public JButton getEsciButton() {
        return esciButton;
    }
}
