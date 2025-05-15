package model;

import java.awt.Robot;

public class HealingRule {
    private Condition condition;
    private HealingAction action;
    private Robot robot;

    public HealingRule(Condition condition, HealingAction action, Robot robot) {
        this.condition = condition;
        this.action = action;
        this.robot = robot;
    }
    
    public boolean shouldExecute(double health, double mana) {
        return condition.evaluate(health, mana);
    }
    
    public void execute() { 
        action.execute(robot); 
    }
    
    public int getPriority() { 
        return action.getPriority(); 
    }
}