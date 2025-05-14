package components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TabPanel extends JPanel {
	private Image backgroundImage;
	private TexturePaint texturePaint;
	
	public TabPanel() {
		this.setForeground(Color.GRAY);
		setOpaque(false);
		initializeBackground();
	}
	
	private void initializeBackground() {
		try {
			// Carrega a imagem
			backgroundImage = new ImageIcon(getClass().getResource("/assets/images/bg_tibia_00_fundo_background.png")).getImage();
			
			// Cria um BufferedImage com o tamanho da imagem original
			BufferedImage bufferedImage = new BufferedImage(
				backgroundImage.getWidth(null),
				backgroundImage.getHeight(null),
				BufferedImage.TYPE_INT_ARGB
			);
			
			// Desenha a imagem original no BufferedImage
			Graphics2D g2d = bufferedImage.createGraphics();
			g2d.drawImage(backgroundImage, 0, 0, null);
			g2d.dispose();
			
			// Cria o TexturePaint com o padrão
			texturePaint = new TexturePaint(
				bufferedImage,
				new Rectangle(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight())
			);
			
		} catch (Exception e) {
			e.printStackTrace();
			// Fallback para cor sólida em caso de erro
			setOpaque(true);
			setBackground(new Color(60, 60, 60));
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (texturePaint != null) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setPaint(texturePaint);
			g2d.fillRect(0, 0, getWidth(), getHeight());
			g2d.dispose();
		}
	}
	
	// Método para mudar a imagem de fundo em runtime se necessário
	public void setBackgroundImage(Image newImage) {
		this.backgroundImage = newImage;
		initializeBackground();
		repaint();
	}
}