package components;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.Icon;

public class Tab extends JTabbedPane {
	private Image backgroundImage = new ImageIcon(getClass().getResource("/assets/images/bg_tibia_00_fundo_background.png")).getImage();
	
	public Tab() {
		this.setForeground(Color.GRAY);
        this.setOpaque(false);
        
        this.setUI(new BasicTabbedPaneUI() {
            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                Icon icon = getIconForTab(tabIndex);
                int iconWidth = (icon != null) ? icon.getIconWidth() + 8 : 0;
                return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + iconWidth + 20;
            }

            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                Icon icon = getIconForTab(tabIndex);
                int iconHeight = (icon != null) ? icon.getIconHeight() : 0;
                return Math.max(super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) + 8, iconHeight + 12);
            }

            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                              int x, int y, int w, int h, boolean isSelected) {
                if (isSelected) {
                    g.setColor(new Color(80, 80, 80));
                    g.fillRect(x, y, w, h);
                } else {
                    g.setColor(new Color(60, 60, 60));
                    g.fillRect(x, y, w, h);
                }
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                        int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 220, 220)); // Borda clara
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRect(x, y, w - 1, h - 1);
                g2.dispose();
            }

            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics,
                                     int tabIndex, String title, Rectangle textRect, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(font);
                g2.setColor(isSelected ? Color.WHITE : new Color(220, 220, 220));

                // Centraliza verticalmente o texto ao lado do ícone
                Icon icon = getIconForTab(tabIndex);
                int iconWidth = (icon != null) ? icon.getIconWidth() : 0;
                int iconHeight = (icon != null) ? icon.getIconHeight() : 0;
                int textX = textRect.x;
                int textY = textRect.y + metrics.getAscent();
                if (icon != null) {
                    int iconY = textRect.y + (textRect.height - iconHeight) / 2;
                    icon.paintIcon(Tab.this, g2, textX, iconY);
                    textX += iconWidth + 6; // Espaço entre ícone e texto
                }
                g2.drawString(title, textX, textY);
                g2.dispose();
            }

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {}

            public Icon getIconForTab(int tabIndex) {
                return Tab.this.getIconAt(tabIndex);
            }
        });
	}

	@Override
	protected void paintComponent(Graphics g) {	
	    if (backgroundImage != null) {
	        int iw = backgroundImage.getWidth(null);
	        int ih = backgroundImage.getHeight(null);
	        for (int x = 0; x < getWidth(); x += iw) {
	            for (int y = 0; y < getHeight(); y += ih) {
	                g.drawImage(backgroundImage, x, y, this);
	            }
	        }
	    }
	    
	    super.paintComponent(g);
	}
}
