package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {	
    private BufferedImage image;
    private String title;
    private final JLabel titleLabel;
    private final JPanel imageDisplayPanel;
    private final JLabel statsLabel;
    
    public ImagePanel(String initialTitle) {
        setLayout(new BorderLayout(0, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setBackground(Color.BLACK);
        
        this.title = initialTitle;
        
        titleLabel = new JLabel(initialTitle);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        imageDisplayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                renderImage(g);
            }
        };
        imageDisplayPanel.setBackground(Color.BLACK);
        imageDisplayPanel.setPreferredSize(new Dimension(250, 100));
        add(imageDisplayPanel, BorderLayout.CENTER);
        
        // Create stats label at bottom
        statsLabel = new JLabel("Aguardando análise...");
        statsLabel.setForeground(Color.YELLOW);
        statsLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
        statsLabel.setHorizontalAlignment(JLabel.CENTER);
        add(statsLabel, BorderLayout.SOUTH);
    }
    
    /**
     * Sets the image to display and repaints the panel.
     * 
     * @param image The image to display
     */
    public void setImage(BufferedImage image) {
        this.image = image;
        imageDisplayPanel.repaint();
    }
    
    /**
     * Sets the title of this image panel and updates the stats display.
     * 
     * @param title The title to display
     */
    public void setTitle(String title) {
        this.title = title;
        
        // Update the main title
        titleLabel.setText(title);
        
        // Extract percentage info for the stats label if available
        if (title.contains("%")) {
            String[] parts = title.split(" - ");
            if (parts.length > 1) {
                statsLabel.setText(parts[1]);
                
                // Set color based on the percentage value
                try {
                    String percentText = parts[1].split("%")[0];
                    double percent = Double.parseDouble(percentText);
                    
                    if (percent < 30) {
                        statsLabel.setForeground(Color.RED);
                    } else if (percent < 70) {
                        statsLabel.setForeground(Color.YELLOW);
                    } else {
                        statsLabel.setForeground(Color.GREEN);
                    }
                } catch (Exception e) {
                    // If parsing fails, keep default color
                    statsLabel.setForeground(Color.YELLOW);
                }
            }
        }
    }
    
    /**
     * Renders the image onto the graphics context.
     * 
     * @param g The graphics context
     */
    private void renderImage(Graphics g) {
        if (image == null) {
            // No image to display yet
            g.setColor(Color.GRAY);
            g.drawString("Aguardando captura...", 20, imageDisplayPanel.getHeight() / 2);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable antialiasing for smoother image
        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        // Calculate scaling to fit the panel while maintaining aspect ratio
        double imageRatio = (double) image.getWidth() / image.getHeight();
        double panelRatio = (double) imageDisplayPanel.getWidth() / imageDisplayPanel.getHeight();
        
        int drawWidth, drawHeight;
        int offsetX = 0, offsetY = 0;
        
        if (imageRatio > panelRatio) {
            // Image is wider than panel proportionally
            drawWidth = imageDisplayPanel.getWidth();
            drawHeight = (int) (drawWidth / imageRatio);
            offsetY = (imageDisplayPanel.getHeight() - drawHeight) / 2;
        } else {
            // Image is taller than panel proportionally
            drawHeight = imageDisplayPanel.getHeight();
            drawWidth = (int) (drawHeight * imageRatio);
            offsetX = (imageDisplayPanel.getWidth() - drawWidth) / 2;
        }
        
        // Draw the scaled image
        g2d.drawImage(image, offsetX, offsetY, drawWidth, drawHeight, null);
        
        // Draw a border around the image
        g2d.setColor(Color.WHITE);
        g2d.drawRect(offsetX, offsetY, drawWidth - 1, drawHeight - 1);
        
        // Draw the analysis line (horizontal middle line)
        int lineY = offsetY + drawHeight / 2;
        g2d.setColor(Color.YELLOW);
        g2d.drawLine(offsetX, lineY, offsetX + drawWidth, lineY);
        
        // Label the analysis line
        g2d.setFont(new Font("Arial", Font.PLAIN, 9));
        g2d.drawString("Linha de análise", offsetX + 5, lineY - 5);
        
        // Mark target colors on the right side
        int colorBoxSize = 10;
        int colorX = offsetX + drawWidth - colorBoxSize - 5;
        int colorY = offsetY + 10;
        
        // Draw color boxes
        if (title.contains("vida") && !title.contains("Exura Sio")) {
            // Health colors
            Color[] healthColors = {
                new Color(102, 57, 63),  // #66393f
                new Color(143, 54, 54),  // #8f3636
                new Color(173, 54, 54),  // #ad3636
                new Color(211, 75, 75),  // #d34b4b
                new Color(242, 99, 99),  // #f26363
                new Color(253, 119, 119), // #fd7777
                new Color(214, 110, 110)  // #d66e6e
            };
            
            g2d.drawString("Cores alvo:", colorX - 65, colorY + 5);
            
            for (int i = 0; i < healthColors.length; i++) {
                g2d.setColor(healthColors[i]);
                g2d.fillRect(colorX, colorY + (i * (colorBoxSize + 3)), colorBoxSize, colorBoxSize);
                g2d.setColor(Color.WHITE);
                g2d.drawRect(colorX, colorY + (i * (colorBoxSize + 3)), colorBoxSize, colorBoxSize);
            }
        } else if (title.contains("mana")) {
            // Mana colors 
            Color[] manaColors = {
                new Color(17, 17, 66),
                new Color(34, 34, 102),
                new Color(51, 51, 153),
                new Color(68, 68, 187),
                new Color(85, 85, 221),
                new Color(119, 119, 238),
                new Color(136, 136, 255)
            };
            
            g2d.drawString("Cores alvo:", colorX - 65, colorY + 5);
            
            for (int i = 0; i < manaColors.length; i++) {
                g2d.setColor(manaColors[i]);
                g2d.fillRect(colorX, colorY + (i * (colorBoxSize + 3)), colorBoxSize, colorBoxSize);
                g2d.setColor(Color.WHITE);
                g2d.drawRect(colorX, colorY + (i * (colorBoxSize + 3)), colorBoxSize, colorBoxSize);
            }
        } else if (title.contains("Exura Sio")) {
            // Sio colors (greenish)
            Color[] sioColors = {
                new Color(102, 102, 34),
                new Color(153, 153, 51),
                new Color(187, 187, 68),
                new Color(221, 221, 85),
                new Color(238, 238, 119),
                new Color(255, 255, 136),
                new Color(220, 255, 190)
            };
            
            g2d.drawString("Cores alvo:", colorX - 65, colorY + 5);
            
            for (int i = 0; i < sioColors.length; i++) {
                g2d.setColor(sioColors[i]);
                g2d.fillRect(colorX, colorY + (i * (colorBoxSize + 3)), colorBoxSize, colorBoxSize);
                g2d.setColor(Color.WHITE);
                g2d.drawRect(colorX, colorY + (i * (colorBoxSize + 3)), colorBoxSize, colorBoxSize);
            }
        }
    }
}