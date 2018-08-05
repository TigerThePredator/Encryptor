package com.github.tigerthepredator.encryptor;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Frame extends JFrame {
    private JPanel passwordPanel; // Panel where password will be typed
    private JLabel passwordLabel; // Label for password panel
    private JTextField passwordField; // Text field to type password in

    private JPanel encryptPanel; // Panel where data will be encrypted
    private JLabel encryptLabel; // LWEabel for encrypt panel
    private JTextArea encryptArea; // Text area to type plaintext
    private JPanel encryptButtonPanel; // Panel for the encryption buttons
    private JButton encryptCopyButton; // Button for copying to encrypt text area
    private JButton encryptButton; // Button for encryption
    private JButton encryptPasteButton; // Button for pasting to encrypt text area

    private JPanel decryptPanel; // Panel where data will be decrypted
    private JLabel decryptLabel; // Label for decrypt panel
    private JTextArea decryptArea; // Text area to type ciphertext
    private JPanel decryptButtonPanel; // Panel for decryption buttons
    private JButton decryptCopyButton; // Button for copying to decrypt text area
    private JButton decryptButton; // Button for decryption
    private JButton decryptPasteButton; // Button for pasting to decrypt text area

    public Frame() {
        // Set the layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        // Initialize password panel and its components
        passwordPanel = new JPanel();
        passwordLabel = new JLabel("Password: ");
        passwordPanel.add(passwordLabel);
        passwordField = new JTextField("Please type in your password here.");
        passwordField.setEditable(true);
        passwordPanel.add(passwordField);
        add(passwordPanel, BorderLayout.NORTH);

        // Add some glue
        add(Box.createVerticalGlue());

        // Initialize encryption panel and its components
        encryptPanel = new JPanel();
        encryptLabel = new JLabel("   Plaintext:");
        encryptPanel.add(encryptLabel);
        encryptArea = new JTextArea(5, 30);
        encryptArea.setEditable(true);
        encryptArea.setLineWrap(true);
        encryptArea.setWrapStyleWord(true);
        encryptPanel.add(new JScrollPane(encryptArea));
        encryptCopyButton = new JButton("Copy");
        encryptCopyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(encryptArea.getText()), null);
            }
        });
        encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ciphertext = Encryptor.encrypt(encryptArea.getText(), passwordField.getText());
                decryptArea.setText(ciphertext);
            }
        });
        encryptPasteButton = new JButton("Paste");
        encryptPasteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Transferable t = clipboard.getContents(this);
                    if (t != null)
                        encryptArea.setText((String) t.getTransferData(DataFlavor.stringFlavor));
                } catch (IOException | UnsupportedFlavorException io) {
                    io.printStackTrace();
                }
            }
        });
        encryptButtonPanel = new JPanel(new BorderLayout());
        encryptButtonPanel.add(encryptCopyButton, BorderLayout.NORTH);
        encryptButtonPanel.add(encryptButton, BorderLayout.CENTER);
        encryptButtonPanel.add(encryptPasteButton, BorderLayout.SOUTH);
        encryptPanel.add(encryptButtonPanel);
        add(encryptPanel, BorderLayout.CENTER);

        // Add some glue
        add(Box.createVerticalGlue());

        // Initialize decryption panel and its components
        decryptPanel = new JPanel();
        decryptLabel = new JLabel("Ciphertext:");
        decryptPanel.add(decryptLabel);
        decryptArea = new JTextArea(5, 30);
        decryptArea.setEditable(true);
        decryptArea.setLineWrap(true);
        decryptArea.setWrapStyleWord(true);
        decryptPanel.add(new JScrollPane(decryptArea));
        decryptCopyButton = new JButton("Copy");
        decryptCopyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(decryptArea.getText()), null);
            }
        });
        decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String plaintext = Encryptor.decrypt(decryptArea.getText(), passwordField.getText());
                encryptArea.setText(plaintext);
            }
        });
        decryptPasteButton = new JButton("Paste");
        decryptPasteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Transferable t = clipboard.getContents(this);
                    if (t != null)
                        decryptArea.setText((String) t.getTransferData(DataFlavor.stringFlavor));
                } catch (IOException | UnsupportedFlavorException io) {
                    io.printStackTrace();
                }
            }
        });
        decryptButtonPanel = new JPanel(new BorderLayout());
        decryptButtonPanel.add(decryptCopyButton, BorderLayout.NORTH);
        decryptButtonPanel.add(decryptButton, BorderLayout.CENTER);
        decryptButtonPanel.add(decryptPasteButton, BorderLayout.SOUTH);
        decryptPanel.add(decryptButtonPanel);
        add(decryptPanel);
    }
}
