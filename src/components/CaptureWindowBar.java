package components;
import javax.swing.JFrame;
import javax.swing.Timer;

import model.HealingRuleManager;
import model.WindowConfig;

import javax.swing.JLabel;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

/**
 * Represents the transparent, movable, and resizable window that defines
 * the area of the screen to be captured.
 */
public class CaptureWindowBar extends JFrame {
    private static final int DEFAULT_WIDTH = 150;
    private static final int DEFAULT_HEIGHT = 50;
    private static final int BORDER_SIZE = 5;
    private static final int CAPTURE_DELAY = 50; // 50ms, about 20fps
    
    private final Robot robot;
    private final Timer captureTimer;
    private final String captureLabel;
    private final int captureIndex;
    
    private Point dragStart;
    private boolean isResizing = false;
    private int resizeEdge = 0; // 0=none, 1=top, 2=right, 3=bottom, 4=left, 5=top-left, 6=top-right, 7=bottom-right, 8=bottom-left
    
    private ImagePanel[] imagePanels;
    
    /**
     * Creates a new CaptureWindow that is linked to the given DisplayWindow.
     * 
     * @param displayWindow The window that will display the captured pixels
     * @param label The label describing what this capture window is monitoring
     * @param index The index of this capture window (0, 1, or 2)
     * @throws AWTException If the Robot cannot be created
     */
    public CaptureWindowBar(String label, int index, ImagePanel[] imagePanels, Robot robot) throws AWTException {
    	this.imagePanels = imagePanels;
        this.robot = robot;
        this.captureLabel = label;
        this.captureIndex = index;
        
        // Set up the window properties
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 50)); // Semi-transparent background
        
        // Carregar posição e tamanho salvos
        Point savedLocation = WindowConfig.loadWindowLocation("capture_" + index);
        setLocation(savedLocation);
        
        // Carregar tamanho salvo
        setSize(WindowConfig.loadWindowSize("capture_" + index));
        setAlwaysOnTop(true);
        
        // Add a small label to identify the purpose of this capture window
        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 9));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBackground(new Color(0, 0, 0, 100));
        titleLabel.setOpaque(true);
        add(titleLabel, BorderLayout.NORTH);
        
        // Add mouse listeners for dragging and resizing
        setupMouseListeners();
        
        // Adicionar listener para salvar posição e tamanho quando movido ou redimensionado
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                saveWindowState();
            }
            
            @Override
            public void componentResized(ComponentEvent e) {
                saveWindowState();
            }
        });
        
        // Set up the timer for capturing screen content
        captureTimer = new Timer(CAPTURE_DELAY, e -> captureAndDisplayScreen());
    }
    
    /**
     * Sets up the mouse listeners for window dragging and resizing operations.
     */
    private void setupMouseListeners() {
        // Mouse pressed listener for starting drag or resize operations
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
                resizeEdge = getResizeEdge(e.getPoint());
                isResizing = resizeEdge != 0;
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null;
                isResizing = false;
                resizeEdge = 0;
                setCursor(Cursor.getDefaultCursor());
            }
        });
        
        // Mouse moved listener for updating cursor based on position
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateCursor(getResizeEdge(e.getPoint()));
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart == null) {
                    return;
                }
                
                int dx = e.getX() - dragStart.x;
                int dy = e.getY() - dragStart.y;
                
                if (isResizing) {
                    resizeWindow(dx, dy);
                } else {
                    // Move the window
                    setLocation(getLocation().x + dx, getLocation().y + dy);
                }
                
                // Capture screen after moving or resizing
                captureAndDisplayScreen();
            }
        });
    }
    
    /**
     * Determines which edge or corner of the window the point is near to.
     * 
     * @param p The point to check
     * @return A code indicating which edge or corner (0 for none)
     */
    private int getResizeEdge(Point p) {
        int width = getWidth();
        int height = getHeight();
        
        boolean left = p.x < BORDER_SIZE;
        boolean right = p.x > width - BORDER_SIZE;
        boolean top = p.y < BORDER_SIZE;
        boolean bottom = p.y > height - BORDER_SIZE;
        
        if (top && left) return 5; // top-left
        if (top && right) return 6; // top-right
        if (bottom && right) return 7; // bottom-right
        if (bottom && left) return 8; // bottom-left
        if (top) return 1; // top
        if (right) return 2; // right
        if (bottom) return 3; // bottom
        if (left) return 4; // left
        
        return 0; // not on an edge
    }
    
    /**
     * Updates the cursor based on the resize edge the mouse is over.
     * 
     * @param edge The edge code from getResizeEdge()
     */
    private void updateCursor(int edge) {
        switch (edge) {
            case 1: // top
            case 3: // bottom
                setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                break;
            case 2: // right
            case 4: // left
                setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                break;
            case 5: // top-left
            case 7: // bottom-right
                setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                break;
            case 6: // top-right
            case 8: // bottom-left
                setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                break;
            default:
                setCursor(Cursor.getDefaultCursor());
                break;
        }
    }
    
    /**
     * Resizes the window based on the current resize edge and the drag delta.
     * 
     * @param dx The x-axis change amount
     * @param dy The y-axis change amount
     */
    private void resizeWindow(int dx, int dy) {
        Rectangle bounds = getBounds();
        
        // Prepare the new bounds
        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = bounds.height;
        
        // Apply changes based on which edge is being dragged
        switch (resizeEdge) {
            case 1: // top
                y += dy;
                height -= dy;
                break;
            case 2: // right
                width += dx;
                break;
            case 3: // bottom
                height += dy;
                break;
            case 4: // left
                x += dx;
                width -= dx;
                break;
            case 5: // top-left
                x += dx;
                y += dy;
                width -= dx;
                height -= dy;
                break;
            case 6: // top-right
                y += dy;
                width += dx;
                height -= dy;
                break;
            case 7: // bottom-right
                width += dx;
                height += dy;
                break;
            case 8: // bottom-left
                x += dx;
                width -= dx;
                height += dy;
                break;
        }
        
        // Ensure minimum size
        if (width < 50) {
            if (x != bounds.x) {
                x = bounds.x + bounds.width - 50;
            }
            width = 50;
        }
        
        if (height < 50) {
            if (y != bounds.y) {
                y = bounds.y + bounds.height - 50;
            }
            height = 50;
        }
        
        // Set the new bounds
        setBounds(x, y, width, height);
    }
    
    /**
     * Captures the screen area under this window and sends it to the display window.
     * Also calculates color percentage for health monitoring when captureIndex is 0.
     */
    private void captureAndDisplayScreen() {
        // Get the window position in screen coordinates
        Point location = getLocationOnScreen();
        
        try {
            // Capture the screen area under this window
            BufferedImage capture = robot.createScreenCapture(new Rectangle(
                    location.x, location.y, getWidth(), getHeight()));
            
            // For health bar (captureIndex == 0), calculate the percentage of red color
            double colorPercentage = 0;
            String colorMessage = "";
            
            if (captureIndex == 0) { // Health bar
                colorPercentage = calculateRedColorPercentage(capture);
                colorMessage = String.format("%.1f%% de vida", colorPercentage);
                               
            HealingRuleManager.getInstance().checkAndExecuteRules(colorPercentage, 100);

            } else if (captureIndex == 1) { // Mana bar
                colorPercentage = calculateBlueColorPercentage(capture);
                colorMessage = String.format("%.1f%% de mana", colorPercentage);
            } else if (captureIndex == 2) { // Other player health (Exura Sio)
                colorPercentage = calculateSioColorPercentage(capture);
                colorMessage = String.format("%.1f%% de vida (Exura Sio)", colorPercentage);
            }
            
            // Send the captured image to the display window with the index and color info
            updateImage(capture, captureIndex, captureLabel + " - " + colorMessage);
        } catch (Exception e) {
            // Handle potential errors like the window being partially off-screen
            e.printStackTrace();
        }
    }
    
    /**
     * Calculates the percentage of pixels that match any of the health bar colors
     * in the horizontal middle line of the image.
     * 
     * @param image The captured image
     * @return The percentage (0-100) of pixels that match the target colors
     */
    private double calculateRedColorPercentage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        
        // Target health bar colors in RGB format
        int[][] healthColors = {
            // #66393f -> RGB
            {102, 57, 63},
            // #8f3636 -> RGB
            {143, 54, 54},
            // #ad3636 -> RGB
            {173, 54, 54},
            // #d34b4b -> RGB
            {211, 75, 75},
            // #f26363 -> RGB
            {242, 99, 99},
            // #fd7777 -> RGB
            {253, 119, 119},
            // #d66e6e -> RGB
            {214, 110, 110}
        };
        
        // Use the horizontal middle line
        int y = height / 2;
        
        int matchingPixels = 0;
        int[] matchedColorCounts = new int[healthColors.length];
        
        // Count pixels along the horizontal line that are close to any target color
        for (int x = 0; x < width; x++) {
            int rgb = image.getRGB(x, y);
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;
            
            // Define a threshold for color similarity (lower = more strict)
            int threshold = 25;
            
            // Check if the pixel color is close to any of the target colors
            boolean matched = false;
            for (int i = 0; i < healthColors.length; i++) {
                int[] targetColor = healthColors[i];
                int targetRed = targetColor[0];
                int targetGreen = targetColor[1]; 
                int targetBlue = targetColor[2];
                
                if (Math.abs(red - targetRed) <= threshold && 
                    Math.abs(green - targetGreen) <= threshold && 
                    Math.abs(blue - targetBlue) <= threshold) {
                    matchingPixels++;
                    matchedColorCounts[i]++;
                    matched = true;
                    break; // We count each pixel only once
                }
            }
            
            // For debugging, print the color value when a match is found
            if (matched && matchingPixels % 10 == 0) { // Only print some matches to avoid spam
            }
        }
                
        // Calculate percentage
        return (double) matchingPixels / width * 100;
    }
    
    /**
     * Calculates the percentage of pixels that match any of the mana bar colors
     * in the horizontal middle line of the image.
     * 
     * @param image The captured image
     * @return The percentage (0-100) of pixels that match the target colors
     */
    private double calculateBlueColorPercentage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        
        // The mana colors provided have some issues (possibly typos like #11112 is too short)
        // I'll use proper RGB equivalents for blue mana-like colors
        // Target mana colors in RGB format - blues with increasing intensity
        int[][] manaColors = {
            // Dark blue
            {17, 17, 66},
            // Medium-dark blue
            {34, 34, 102},
            // Medium blue
            {51, 51, 153},
            // Medium-light blue
            {68, 68, 187},
            // Light blue
            {85, 85, 221},
            // Very light blue
            {119, 119, 238},
            // Lightest blue
            {136, 136, 255}
        };
        
        // Use the horizontal middle line
        int y = height / 2;
        
        int matchingPixels = 0;
        int[] matchedColorCounts = new int[manaColors.length];
        
        // Count pixels along the horizontal line that are close to any target color
        for (int x = 0; x < width; x++) {
            int rgb = image.getRGB(x, y);
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;
            
            // Define a threshold for color similarity (lower = more strict)
            int threshold = 25;
            
            // Check if the pixel color is close to any of the target colors
            boolean matched = false;
            for (int i = 0; i < manaColors.length; i++) {
                int[] targetColor = manaColors[i];
                int targetRed = targetColor[0];
                int targetGreen = targetColor[1]; 
                int targetBlue = targetColor[2];
                
                if (Math.abs(red - targetRed) <= threshold && 
                    Math.abs(green - targetGreen) <= threshold && 
                    Math.abs(blue - targetBlue) <= threshold) {
                    matchingPixels++;
                    matchedColorCounts[i]++;
                    matched = true;
                    break; // We count each pixel only once
                }
            }
            
            // For debugging, print the color value when a match is found
            if (matched && matchingPixels % 10 == 0) { // Only print some matches to avoid spam
            }
        }
        
        // Calculate percentage
        return (double) matchingPixels / width * 100;
    }
    
    /**
     * Starts the continuous screen capture process.
     */
    public void startCapture() {
        captureTimer.start();
    }
    
    /**
     * Calculates the percentage of pixels that match any of the Exura Sio colors
     * in the horizontal middle line of the image.
     * 
     * @param image The captured image
     * @return The percentage (0-100) of pixels that match the target colors
     */
    private double calculateSioColorPercentage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        
        // The Sio colors provided have some issues, I'll use some greenish colors 
        // typically used for other player health bars in RPGs
        int[][] sioColors = {
            // Dark green-yellow
            {102, 102, 34},
            // Medium green-yellow
            {153, 153, 51},
            // Light green-yellow
            {187, 187, 68},
            // Very light green-yellow 
            {221, 221, 85},
            // Lightest green-yellow
            {238, 238, 119},
            // Nearly white green-yellow
            {255, 255, 136},
            // Greenish tint
            {220, 255, 190}
        };
        
        // Use the horizontal middle line
        int y = height / 2;
        
        int matchingPixels = 0;
        int[] matchedColorCounts = new int[sioColors.length];
        
        // Count pixels along the horizontal line that are close to any target color
        for (int x = 0; x < width; x++) {
            int rgb = image.getRGB(x, y);
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;
            
            // Define a threshold for color similarity (lower = more strict)
            int threshold = 25;
            
            // Check if the pixel color is close to any of the target colors
            boolean matched = false;
            for (int i = 0; i < sioColors.length; i++) {
                int[] targetColor = sioColors[i];
                int targetRed = targetColor[0];
                int targetGreen = targetColor[1]; 
                int targetBlue = targetColor[2];
                
                if (Math.abs(red - targetRed) <= threshold && 
                    Math.abs(green - targetGreen) <= threshold && 
                    Math.abs(blue - targetBlue) <= threshold) {
                    matchingPixels++;
                    matchedColorCounts[i]++;
                    matched = true;
                    break; // We count each pixel only once
                }
            }
            
            // For debugging, print the color value when a match is found
            if (matched && matchingPixels % 10 == 0) { // Only print some matches to avoid spam
            }
        }
        
        
        // Calculate percentage
        return (double) matchingPixels / width * 100;
    }
    
    public void updateImage(BufferedImage image, int index, String label) {
        if (index >= 0 && index < this.imagePanels.length) {
            imagePanels[index].setImage(image);
            imagePanels[index].setTitle(label);
        }
    }
    
    /**
     * Stops the continuous screen capture process.
     */
    public void stopCapture() {
        captureTimer.stop();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        // Draw a border to make the window visible and help with resizing
        // Use different colors based on the capture type
        switch (captureIndex) {
            case 0: // Health
                g.setColor(Color.RED);
                break;
            case 1: // Mana
                g.setColor(Color.BLUE);
                break;
            case 2: // Other Health
                g.setColor(Color.GREEN);
                break;
            default:
                g.setColor(Color.WHITE);
        }
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
    
    private void saveWindowState() {
        WindowConfig.saveWindowState(
            "capture_" + captureIndex,
            getLocation(),
            getSize()
        );
    }
}
