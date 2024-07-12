package com.mycompany.swing;

import com.mycompany.adventure.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe che rappresenta la finestra principale del gioco.
 */
public class GameWindow extends JFrame {
    private static JTextArea OUTPUT_AREA;
    private JTextField inputField;
    private JButton sendButton;
    private Engine engine;
    private JPanel mainPanel;
    private MenuPanel menuPanel;

    /**
     * Costruttore della classe GameWindow.
     * 
     * @param engine l'engine del gioco
     */
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

    /**
     * Elabora l'input dell'utente.
     */
    private void processInput() {
        String input = inputField.getText();
        inputField.setText("");
        if (input != null && !input.trim().isEmpty()) {
            engine.processCommand(input);
            OUTPUT_AREA.append(input + "\n");
        }
    }

    /**
     * Restituisce l'area di output.
     * 
     * @return l'area di output
     */
    public JTextArea getOutputArea() {
        return OUTPUT_AREA;
    }

    /**
     * Aggiunge testo all'area di output.
     * 
     * @param text il testo da aggiungere
     */
    public static void appendOutput(String text) {
        SwingUtilities.invokeLater(() -> {
            OUTPUT_AREA.append(text + "\n");
            OUTPUT_AREA.setCaretPosition(OUTPUT_AREA.getDocument().getLength());
        });
    }

    /**
     * Pulisce l'area di output.
     */
    public static void clearOutput() {
        OUTPUT_AREA.setText("");
    }

    /**
     * Mostra il pannello del menu.
     */
    public void showMenuPanel() {
        clearOutput();
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "menu");
    }

    /**
     * Mostra il pannello del gioco.
     */
    public void showGamePanel() {
        clearOutput();
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "game");
    }

    /**
     * Gestisce le azioni del menu.
     * 
     * @param e l'evento di azione
     */
    private void handleMenuAction(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        if (source == menuPanel.getNuovaPartitaButton()) {
            engine.nuovaPartita();
            showGamePanel();
        } else if (source == menuPanel.getCaricaButton()) {
            engine.gestoreCarica();
            showGamePanel();
        } else if (source == menuPanel.getEsciButton()) {
            System.exit(0);
        }
    }

    /**
     * Abilita o disabilita l'input dell'utente.
     * 
     * @param enabled true per abilitare, false per disabilitare
     */
    public void setInputEnabled(boolean enabled) {
        inputField.setEnabled(enabled);
        sendButton.setEnabled(enabled);
    }

    /**
     * Restituisce il pannello del menu.
     * 
     * @return il pannello del menu
     */
    public MenuPanel getMenuPanel() {
        return menuPanel;
    }
}
