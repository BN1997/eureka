package components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class ComboBox extends JComboBox<String> {
	private Image backgroundImage = new ImageIcon(getClass().getResource("/assets/images/bg_tibia_00_fundo_background.png")).getImage();
	
    public ComboBox(String[] itens) {
        super(itens);

        setFont(new Font("Arial", Font.BOLD, 14));
        setForeground(new Color(220, 220, 220));
        setBackground(new Color(60, 60, 60));
        setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 2));
        setFocusable(false);
        
        this.customComponent();
    }

    private void customComponent() {
        setUI(new BasicComboBoxUI() {
            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                try {                    
                    int iw = backgroundImage.getWidth(null);
                    int ih = backgroundImage.getHeight(null);
                    for (int x = 0; x < bounds.width; x += iw) {
                        for (int y = 0; y < bounds.height; y += ih) {
                            g.drawImage(backgroundImage, x, y, null);
                        }
                    }
                } catch (Exception e) {
                    g.setColor(new Color(60, 60, 60));
                    g.fillRect(0, 0, bounds.width, bounds.height);
                }
            }

            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        int w = getWidth();
                        int h = getHeight();
                        int[] xPoints = {w / 2 - 4, w / 2 + 4, w / 2};
                        int[] yPoints = {h / 2 - 2, h / 2 - 2, h / 2 + 4};
                        g.setColor(new Color(220, 220, 220));
                        g.fillPolygon(xPoints, yPoints, 3);
                    }
                };
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setContentAreaFilled(false);
                button.setBackground(Color.RED);
                return button;
            }

            @Override
            protected void installDefaults() {
                super.installDefaults();
                comboBox.setFocusable(false);
            }
        });
        
        this.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
             
                if (isSelected) {
                    label.setBackground(Color.RED);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.LIGHT_GRAY);
                    label.setForeground(Color.BLACK);
                }
                return label;
            }
        });
    }
}
