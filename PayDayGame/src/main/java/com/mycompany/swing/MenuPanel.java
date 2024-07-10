package com.mycompany.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanel extends BackgroundPanel {
    private JButton nuovaPartitaButton;
    private JButton caricaButton;
    private JButton esciButton;

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

    public JButton getNuovaPartitaButton() {
        return nuovaPartitaButton;
    }

    public JButton getCaricaButton() {
        return caricaButton;
    }

    public JButton getEsciButton() {
        return esciButton;
    }
}
