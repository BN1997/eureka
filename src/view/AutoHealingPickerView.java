package view;

import javax.swing.*;
import java.awt.*;

public class AutoHealingPickerView extends JPanel {
    public AutoHealingPickerView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.DARK_GRAY);
        
        // Painel principal com BoxLayout vertical
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.DARK_GRAY);
        
        // --- Auto Healing Helper ---
        JLabel autoHealingLabel = new JLabel("Auto Healing Helper");
        autoHealingLabel.setForeground(Color.WHITE);
        autoHealingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        autoHealingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(autoHealingLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        JPanel healingPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        healingPanel.setBackground(Color.DARK_GRAY);
        healingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Spell Healing
        JPanel spellPanel = new JPanel();
        spellPanel.setLayout(new BoxLayout(spellPanel, BoxLayout.Y_AXIS));
        spellPanel.setBackground(Color.DARK_GRAY);
        JLabel spellLabel = new JLabel("Spell Healing");
        spellLabel.setForeground(Color.WHITE);
        spellLabel.setFont(new Font("Arial", Font.BOLD, 12));
        spellPanel.add(spellLabel);
        spellPanel.add(Box.createVerticalStrut(5));
        for (int i = 0; i < 2; i++) {
            JPanel slot = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            slot.setBackground(Color.DARK_GRAY);
            JLabel icon = new JLabel();
            icon.setPreferredSize(new Dimension(32, 32));
            icon.setOpaque(true);
            icon.setBackground(Color.GRAY); // Placeholder para ícone
            JSpinner percent = new JSpinner(new SpinnerNumberModel(90 - i*20, 0, 100, 1));
            JLabel percentLabel = new JLabel("%");
            percentLabel.setForeground(Color.WHITE);
            JButton infoBtn = new JButton("i");
            infoBtn.setMargin(new Insets(2, 4, 2, 4));
            slot.add(icon);
            slot.add(percent);
            slot.add(percentLabel);
            slot.add(infoBtn);
            spellPanel.add(slot);
            spellPanel.add(Box.createVerticalStrut(3));
        }
        healingPanel.add(spellPanel);

        // Potion Healing
        JPanel potionPanel = new JPanel();
        potionPanel.setLayout(new BoxLayout(potionPanel, BoxLayout.Y_AXIS));
        potionPanel.setBackground(Color.DARK_GRAY);
        JLabel potionLabel = new JLabel("Potion Healing");
        potionLabel.setForeground(Color.WHITE);
        potionLabel.setFont(new Font("Arial", Font.BOLD, 12));
        potionPanel.add(potionLabel);
        potionPanel.add(Box.createVerticalStrut(5));
        for (int i = 0; i < 2; i++) {
            JPanel slot = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            slot.setBackground(Color.DARK_GRAY);
            JLabel icon = new JLabel();
            icon.setPreferredSize(new Dimension(32, 32));
            icon.setOpaque(true);
            icon.setBackground(Color.DARK_GRAY); // Sem ícone
            JSpinner percent = new JSpinner(new SpinnerNumberModel(100, 0, 100, 1));
            JLabel percentLabel = new JLabel("%");
            percentLabel.setForeground(Color.WHITE);
            JButton infoBtn = new JButton("i");
            infoBtn.setMargin(new Insets(2, 4, 2, 4));
            slot.add(icon);
            slot.add(percent);
            slot.add(percentLabel);
            slot.add(infoBtn);
            potionPanel.add(slot);
            potionPanel.add(Box.createVerticalStrut(3));
        }
        healingPanel.add(potionPanel);

        mainPanel.add(healingPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // --- Friend Healing Helper ---
        JLabel friendHealingLabel = new JLabel("Friend Healing Helper");
        friendHealingLabel.setForeground(Color.WHITE);
        friendHealingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        friendHealingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(friendHealingLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        JPanel friendPanel = new JPanel(new BorderLayout(5, 0));
        friendPanel.setBackground(Color.DARK_GRAY);
        friendPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Select a Friend
        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.Y_AXIS));
        selectPanel.setBackground(Color.DARK_GRAY);
        JLabel selectLabel = new JLabel("Select a Friend");
        selectLabel.setForeground(Color.WHITE);
        selectPanel.add(selectLabel);
        JComboBox<String> friendCombo = new JComboBox<>(new String[]{""});
        selectPanel.add(friendCombo);
        friendPanel.add(selectPanel, BorderLayout.WEST);

        // Heal Friend List
        JPanel healListPanel = new JPanel();
        healListPanel.setLayout(new BoxLayout(healListPanel, BoxLayout.Y_AXIS));
        healListPanel.setBackground(Color.DARK_GRAY);
        JLabel healListLabel = new JLabel("Heal Friend List");
        healListLabel.setForeground(Color.WHITE);
        healListPanel.add(healListLabel);
        for (int i = 0; i < 2; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            row.setBackground(Color.DARK_GRAY);
            JButton addBtn = new JButton("+");
            JCheckBox enableUH = new JCheckBox("Enable UH");
            enableUH.setForeground(Color.WHITE);
            enableUH.setBackground(Color.DARK_GRAY);
            JSpinner percent = new JSpinner(new SpinnerNumberModel(100, 0, 100, 1));
            JLabel percentLabel = new JLabel("%");
            percentLabel.setForeground(Color.WHITE);
            JButton infoBtn = new JButton("i");
            infoBtn.setMargin(new Insets(2, 4, 2, 4));
            row.add(addBtn);
            row.add(enableUH);
            row.add(percent);
            row.add(percentLabel);
            row.add(infoBtn);
            healListPanel.add(row);
            healListPanel.add(Box.createVerticalStrut(3));
        }
        friendPanel.add(healListPanel, BorderLayout.CENTER);

        mainPanel.add(friendPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        // Botão Close
        JButton closeBtn = new JButton("Close");
        closeBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        mainPanel.add(closeBtn);

        add(mainPanel, BorderLayout.CENTER);
    }
} 