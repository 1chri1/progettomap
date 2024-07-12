package com.mycompany.swing;

import javax.swing.*;
import java.io.OutputStream;

/**
 * Classe che reindirizza l'output di un flusso di byte verso una JTextArea.
 * Utile per mostrare l'output della console in un'interfaccia grafica.
 */
public class TextAreaOutputStream extends OutputStream {
    private final JTextArea textArea;

    /**
     * Costruttore che inizializza il TextAreaOutputStream con una JTextArea specificata.
     * 
     * @param textArea la JTextArea in cui verrà reindirizzato l'output
     */
    public TextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    /**
     * Scrive un singolo byte nella JTextArea.
     * 
     * @param b il byte da scrivere
     */
    @Override
    public void write(int b) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(String.valueOf((char) b));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }

    /**
     * Scrive una porzione di un array di byte nella JTextArea.
     * 
     * @param b l'array di byte
     * @param off l'offset da cui iniziare a scrivere
     * @param len il numero di byte da scrivere
     */
    @Override
    public void write(byte[] b, int off, int len) {
        final String text = new String(b, off, len);
        SwingUtilities.invokeLater(() -> {
            textArea.append(text);
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }
}
