package components;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.Icon;
import java.awt.image.BufferedImage;

public class Tab extends JTabbedPane {
	private Image backgroundImage;
	private TexturePaint texturePaint;
	
	public Tab() {
		this.setForeground(Color.GRAY);
		this.setOpaque(false);
		initializeBackground();
		
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
				Graphics2D g2d = (Graphics2D) g.create();
				try {
					if (isSelected) {
						g2d.setColor(new Color(80, 80, 80, 200));
					} else {
						g2d.setColor(new Color(60, 60, 60, 180));
					}
					g2d.fillRect(x, y, w, h);
				} finally {
					g2d.dispose();
				}
			}

			@Override
			protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
										int x, int y, int w, int h, boolean isSelected) {
				Graphics2D g2 = (Graphics2D) g.create();
				try {
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(new Color(220, 220, 220)); // Borda clara
					g2.setStroke(new BasicStroke(1.0f));
					g2.drawRect(x, y, w - 1, h - 1);
				} finally {
					g2.dispose();
				}
			}

			@Override
			protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics,
									 int tabIndex, String title, Rectangle textRect, boolean isSelected) {
				Graphics2D g2 = (Graphics2D) g.create();
				try {
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
				} finally {
					g2.dispose();
				}
			}

			@Override
			protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {}

			public Icon getIconForTab(int tabIndex) {
				return Tab.this.getIconAt(tabIndex);
			}
		});
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
			try {
				g2d.drawImage(backgroundImage, 0, 0, null);
			} finally {
				g2d.dispose();
			}
			
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
		Graphics2D g2d = (Graphics2D) g.create();
		try {
			if (texturePaint != null) {
				g2d.setPaint(texturePaint);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
			super.paintComponent(g2d);
		} finally {
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
