package view;

import java.awt.AWTException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class FrameInitializerView extends JFrame {
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 400;
    
    public FrameInitializerView() {
    	this.initializeAppFrame();
    }
    
    private void initializeTab() {
        try {
            JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Displayer", new DisplayerBarView());
	        tabbedPane.addTab("Auto Healing", new JPanel());
	        tabbedPane.addTab("Informações", new JPanel());
	        tabbedPane.addTab("Extra", new JPanel());
	        
	        setContentPane(tabbedPane);
		} catch (AWTException e) {
			e.printStackTrace();
		}
    }
    
    private void initializeAppFrame() {
        setTitle("Eureka");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        
        this.initializeTab();
    }
}
        