package com.mycompany.swing;

import com.mycompany.adventure.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    private static JTextArea OUTPUT_AREA;
    private JTextField inputField;
    private JButton sendButton;
    private Engine engine;
    private JPanel mainPanel;
    private MenuPanel menuPanel;

    public GameWindow(Engine engine) {
        this.engine = engine;
        setTitle("PayDayGame");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        mainPanel = new JPanel(new BorderLayout());
        OUTPUT_AREA = new JTextArea();
        OUTPUT_AREA.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(OUTPUT_AREA);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processInput();
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processInput();
            }
        });

        menuPanel = new MenuPanel(e -> handleMenuAction(e), "./resources/background.jpg");
        
        add(menuPanel, "menu");
        add(mainPanel, "game");

        showMenuPanel();
    }

    private void processInput() {
        String input = inputField.getText();
        inputField.setText("");
        if (input != null && !input.trim().isEmpty()) {
            engine.processCommand(input);
            OUTPUT_AREA.append(input + "\n");
        }
    }

    public JTextArea getOutputArea() {
        return OUTPUT_AREA;
    }

   public static void appendOutput(String text) {
    SwingUtilities.invokeLater(() -> {
        OUTPUT_AREA.append(text + "\n");
        OUTPUT_AREA.setCaretPosition(OUTPUT_AREA.getDocument().getLength());
    });
}


    public static void clearOutput() {
        OUTPUT_AREA.setText("");
    }

    public void showMenuPanel() {
        clearOutput();
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "menu");
    }

    public void showGamePanel() {
        clearOutput();
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "game");
    }

    private void handleMenuAction(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        if (source == menuPanel.getNuovaPartitaButton()) {
            engine.nuovaPartita();
            showGamePanel();
        } else if (source == menuPanel.getCaricaButton()) {
            engine.caricaPartita();
            showGamePanel();
        } else if (source == menuPanel.getEsciButton()) {
            System.exit(0);
        }
    }
    
    public void setInputEnabled(boolean enabled) {
        inputField.setEnabled(enabled);
        sendButton.setEnabled(enabled);
    }
    
    public MenuPanel getMenuPanel() {
        return menuPanel;
    }


}
