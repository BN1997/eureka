package model;

public class Condition {
 // Tipos de condições possíveis
 public enum Type {
     HEALTH_BELOW,    // Vida abaixo de X%
     HEALTH_ABOVE,    // Vida acima de X%
     MANA_BELOW,      // Mana abaixo de X%
     MANA_ABOVE,      // Mana acima de X%
     AND,            // Combinação de duas condições (E)
     OR              // Combinação de duas condições (OU)
 }
 
 private Type type;           // Tipo da condição
 private double value;        // Valor para comparação (quando aplicável)
 private Condition left;      // Condição esquerda (para AND/OR)
 private Condition right;     // Condição direita (para AND/OR)
 
 /**
  * Construtor para condições simples (vida/mana)
  * @param type Tipo da condição
  * @param value Valor para comparação
  */
 public Condition(Type type, double value) {
     this.type = type;
     this.value = value;
 }
 
 /**
  * Construtor para condições compostas (AND/OR)
  * @param type Tipo da condição (AND/OR)
  * @param left Condição esquerda
  * @param right Condição direita
  */
 public Condition(Type type, Condition left, Condition right) {
     this.type = type;
     this.left = left;
     this.right = right;
 }
 
 /**
  * Avalia se a condição é verdadeira com base nos valores atuais de vida e mana
  * @param health Valor atual de vida (0-100)
  * @param mana Valor atual de mana (0-100)
  * @return true se a condição for verdadeira
  */
 public boolean evaluate(double health, double mana) {
     switch (type) {
         case HEALTH_BELOW:
             return health <= value;
         case HEALTH_ABOVE:
             return health >= value;
         case MANA_BELOW:
             return mana <= value;
         case MANA_ABOVE:
             return mana >= value;
         case AND:
             return left.evaluate(health, mana) && right.evaluate(health, mana);
         case OR:
             return left.evaluate(health, mana) || right.evaluate(health, mana);
         default:
             return false;
     }
 }
 
 /**
  * Retorna uma representação em texto da condição
  */
 @Override
 public String toString() {
     switch (type) {
         case HEALTH_BELOW:
             return "Vida < " + value + "%";
         case HEALTH_ABOVE:
             return "Vida > " + value + "%";
         case MANA_BELOW:
             return "Mana < " + value + "%";
         case MANA_ABOVE:
             return "Mana > " + value + "%";
         case AND:
             return "(" + left + " E " + right + ")";
         case OR:
             return "(" + left + " OU " + right + ")";
         default:
             return "";
     }
 }
 
 /**
  * Cria uma condição para vida abaixo de um valor
  */
 public static Condition healthBelow(double value) {
     return new Condition(Type.HEALTH_BELOW, value);
 }
 
 /**
  * Cria uma condição para vida acima de um valor
  */
 public static Condition healthAbove(double value) {
     return new Condition(Type.HEALTH_ABOVE, value);
 }
 
 /**
  * Cria uma condição para mana abaixo de um valor
  */
 public static Condition manaBelow(double value) {
     return new Condition(Type.MANA_BELOW, value);
 }
 
 /**
  * Cria uma condição para mana acima de um valor
  */
 public static Condition manaAbove(double value) {
     return new Condition(Type.MANA_ABOVE, value);
 }
 
 /**
  * Combina duas condições com AND
  */
 public static Condition and(Condition left, Condition right) {
     return new Condition(Type.AND, left, right);
 }
 
 /**
  * Combina duas condições com OR
  */
 public static Condition or(Condition left, Condition right) {
     return new Condition(Type.OR, left, right);
 }
 
 // Getters
 public Type getType() { return type; }
 public double getValue() { return value; }
 public Condition getLeft() { return left; }
 public Condition getRight() { return right; }
}