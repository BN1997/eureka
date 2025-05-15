package model;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;                                              

public class HealingRuleManager {
 private static HealingRuleManager instance;
 private final PriorityQueue<HealingRule> healingRules;
 private Robot robot;
 
 private HealingRuleManager() {
     healingRules = new PriorityQueue<>(Comparator.comparingInt(HealingRule::getPriority));

     try {
		this.robot = new Robot();
	} catch (AWTException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	this.initializeDefaultRules();
     System.out.print(healingRules);
     System.out.print(healingRules);

      }
 
 public static HealingRuleManager getInstance() {
     if (instance == null) {
         instance = new HealingRuleManager();
     }
     return instance;
 }
 
 public void setRobot(Robot robot) {
     this.robot = robot;
 }
 
 public Robot getRobot() {
     return this.robot;
 }
 
 public void initializeDefaultRules() {
     if (robot == null) return;
     
     
     // Great Health Potion (F1) - Prioridade 1
     this.healingRules.add(new HealingRule(
         new Condition(Condition.Type.HEALTH_BELOW, 80),
         new HealingAction(HealingAction.ActionType.USE_POTION, "Great Health Potion", 1, KeyEvent.VK_F),
         robot
     ));
     
     // Exura Ico (F2) - Prioridade 2
     healingRules.add(new HealingRule(
         new Condition(Condition.Type.HEALTH_BELOW, 92),
         new HealingAction(HealingAction.ActionType.CAST_SPELL, "Exura Ico", 2, KeyEvent.VK_F),
         robot
     ));
     
     // Mana Potion (F3) - Prioridade 3
     healingRules.add(new HealingRule(
         new Condition(Condition.Type.MANA_BELOW, 60),
         new HealingAction(HealingAction.ActionType.USE_POTION, "Mana Potion", 3, KeyEvent.VK_F),
         robot
     ));
 }
 
 public void checkAndExecuteRules(double health, double mana) {
     if (robot == null) return;
     
     List<HealingRule> rulesToExecute = new ArrayList<>();
          
     for (HealingRule rule : healingRules) {
    	 
         if (rule.shouldExecute(health, mana)) {
             rulesToExecute.add(rule);
         }
     }
          
     if (!rulesToExecute.isEmpty()) {
         HealingRule highestPriorityRule = rulesToExecute.getFirst();
         
         System.out.println("h" + highestPriorityRule);

             
         if (highestPriorityRule != null) {
             highestPriorityRule.execute();
             
             rulesToExecute.removeFirst();
         }
     }
 }
}