package model;

import java.awt.Robot;
import java.awt.event.KeyEvent;

public class HealingAction {
    public enum ActionType { CAST_SPELL, USE_POTION }
    private ActionType type;
    private String actionName;
    private int priority;
    private int keyCode;

    public HealingAction(ActionType type, String actionName, int priority, int keyCode) {
        this.type = type;
        this.actionName = actionName;
        this.priority = priority;
        this.keyCode = keyCode;
    }
    
    public void execute(Robot robot) {
        robot.keyPress(keyCode);
        try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        robot.keyRelease(keyCode);
    }
    
    public int getPriority() { 
        return priority; 
    }
}