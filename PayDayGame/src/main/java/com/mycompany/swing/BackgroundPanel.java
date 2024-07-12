/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.swing;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private final Image SFONDO;

    public BackgroundPanel(String fileName) {
        SFONDO = new ImageIcon(fileName).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(SFONDO, 0, 0, getWidth(), getHeight(), this);
    }
}
