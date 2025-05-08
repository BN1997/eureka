package components;

import javax.swing.*;
import java.awt.*;

public class TabPanel extends JPanel {
	private Image backgroundImage = new ImageIcon(getClass().getResource("/assets/images/bg_tibia_00_fundo_background.png")).getImage();
	
	public TabPanel () {
		this.setForeground(Color.GRAY);
        setOpaque(false);
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
    }
}