package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import components.CaptureWindowBar;
import components.ImagePanel;
import components.Tab;
import components.TabPanel;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;

public class DisplayerBarView extends TabPanel {
    private final ImagePanel[] imagePanels;

    public DisplayerBarView() throws AWTException {
    	TabPanel mainPanel = new TabPanel();
        mainPanel.setLayout(new GridLayout(3, 1, 10, 10));
        mainPanel.setBackground(Color.DARK_GRAY);

        imagePanels = new ImagePanel[3];
        imagePanels[0] = new ImagePanel("Vida do Personagem");
        mainPanel.add(imagePanels[0]);
        imagePanels[1] = new ImagePanel("Mana do Personagem");
        mainPanel.add(imagePanels[1]);
        imagePanels[2] = new ImagePanel("Vida do outro Personagem (Exura Sio)");
        mainPanel.add(imagePanels[2]);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.DARK_GRAY);
        infoPanel.setForeground(Color.WHITE);
        infoPanel.add(new JLabel("<html><body style='color:white'>"+
                "Posicione e redimensione as janelas transparentes sobre as barras de vida e mana no jogo</body></html>"));
        add(infoPanel, BorderLayout.SOUTH);
        
        
        // Create three capture windows and link them to the display window
        // 1. Capture window for player's health
        CaptureWindowBar healthCaptureWindow = new CaptureWindowBar("Vida do Personagem", 0, imagePanels);
        healthCaptureWindow.setVisible(true);
        healthCaptureWindow.startCapture();
        
        // 2. Capture window for player's mana
        CaptureWindowBar manaCaptureWindow = new CaptureWindowBar("Mana do Personagem", 1, imagePanels);
        manaCaptureWindow.setVisible(true);
        manaCaptureWindow.startCapture();
        
        // 3. Capture window for other player's health (for Exura Sio)
        CaptureWindowBar otherHealthCaptureWindow = new CaptureWindowBar("Vida do outro Personagem (Exura Sio)", 2, imagePanels);
        otherHealthCaptureWindow.setVisible(true);
        otherHealthCaptureWindow.startCapture(); 
        
    }
} 