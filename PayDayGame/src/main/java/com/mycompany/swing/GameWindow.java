package com.mycompany.swing;

import com.mycompany.adventure.Engine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    private static JTextArea outputArea;
    private JTextField inputField;
    private JButton sendButton;
    private Engine engine;

    public GameWindow(Engine engine) {
        this.engine = engine;
        setTitle("PayDayGame");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

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
    }

    private void processInput() {
        String input = inputField.getText();
        inputField.setText("");
        if (input != null && !input.trim().isEmpty()) {
            engine.processCommand(input);
            outputArea.append(input + "\n"); // Aggiungi questo per visualizzare il comando nell'outputArea
        }
    }

    public JTextArea getOutputArea() {
        return outputArea;
    }

    public static void appendOutput(String text) { // Metodo statico per aggiungere testo all'outputArea
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
}
