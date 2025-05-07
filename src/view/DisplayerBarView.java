package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import components.ImagePanel;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;

public class DisplayerBarView extends JPanel {
    private final ImagePanel[] imagePanels;

    public DisplayerBarView() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1, 10, 10));
        mainPanel.setBackground(Color.DARK_GRAY);

        imagePanels = new ImagePanel[3];
        imagePanels[0] = new ImagePanel("Vida do Personagem");
        mainPanel.add(imagePanels[0]);
        imagePanels[1] = new ImagePanel("Mana do Personagem");
        mainPanel.add(imagePanels[1]);
        imagePanels[2] = new ImagePanel("Vida do outro Personagem (Exura Sio)");
        mainPanel.add(imagePanels[2]);

        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.DARK_GRAY);
        infoPanel.setForeground(Color.WHITE);
        infoPanel.add(new JLabel("<html><body style='color:white'>"+
                "Posicione e redimensione as janelas transparentes sobre as barras de vida e mana no jogo</body></html>"));
        add(infoPanel, BorderLayout.SOUTH);
    }
} 