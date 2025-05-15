package utils;

import java.awt.Point;
import java.awt.Dimension;
import java.io.*;
import java.util.Properties;

public class WindowConfigUtil {
    private static final String CONFIG_FILE = "window_positions.properties";
    private static Properties properties;
    
    static {
        properties = new Properties();
        loadConfig();
    }
    
    public static void loadConfig() {
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            properties.load(in);
        } catch (IOException e) {
            // Se o arquivo não existir, será criado ao salvar
            System.out.println("Arquivo de configuração não encontrado. Será criado ao salvar.");
        }
    }
    
    public static void saveConfig() {
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            properties.store(out, "Window Positions Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveWindowState(String windowName, Point location, Dimension size) {
        properties.setProperty(windowName + ".x", String.valueOf(location.x));
        properties.setProperty(windowName + ".y", String.valueOf(location.y));
        properties.setProperty(windowName + ".width", String.valueOf(size.width));
        properties.setProperty(windowName + ".height", String.valueOf(size.height));
        saveConfig();
    }
    
    public static Point loadWindowLocation(String windowName) {
        try {
            int x = Integer.parseInt(properties.getProperty(windowName + ".x", "100"));
            int y = Integer.parseInt(properties.getProperty(windowName + ".y", "100"));
            return new Point(x, y);
        } catch (NumberFormatException e) {
            return new Point(100, 100);
        }
    }
    
    public static Dimension loadWindowSize(String windowName) {
        try {
            int width = Integer.parseInt(properties.getProperty(windowName + ".width", "150"));
            int height = Integer.parseInt(properties.getProperty(windowName + ".height", "50"));
            return new Dimension(width, height);
        } catch (NumberFormatException e) {
            return new Dimension(150, 50);
        }
    }
} 