package com.mycompany.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Classe che rappresenta un pannello con un'immagine di sfondo.
 */
public class BackgroundPanel extends JPanel {
    private final Image SFONDO;

    /**
     * Costruttore della classe BackgroundPanel.
     * 
     * @param fileName il nome del file dell'immagine di sfondo
     */
    public BackgroundPanel(String fileName) {
        SFONDO = new ImageIcon(fileName).getImage();
    }

    /**
     * Metodo per dipingere il componente.
     * 
     * @param g il contesto grafico da utilizzare per disegnare
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(SFONDO, 0, 0, getWidth(), getHeight(), this);
    }
}
