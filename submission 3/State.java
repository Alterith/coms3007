
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class State {
    //value of state
    private int x;
    //possible actions
    private int y;
    //rewards for moving to a specific position
    private double value;
    //probability transition value per state
    private String[] actions;
    //rewards for moving to a specific position
    private int[] rewards;
    //probability transition value per state
    private double[] transitionStateProbability;
    //possible transition states
    private int[][] transitionStates;
    //hashtable for different probabilities and different states
    private HashMap<String, List<State>> moveState;
    private HashMap<String, List<Double>> moveProbability;
    private HashMap<String, List<Double>> transitionReward;
    private HashMap<String, Double> test;
    
    private String[] formattedAction;
    
    private double[] QValues;
    
    private boolean terminal;
    public State(int x, int y){ 
        this.x = x;
        this.y = y;
        value = 0;
        actions = new String[]{"left", "right", "up", "down"};
        formattedAction = new String[]{"l", "r", "u", "d"};
        moveState = new HashMap<>();
        moveProbability = new HashMap<>();
        transitionReward = new HashMap<>();
        test = new HashMap<>();
        QValues = new double[4];
        terminal = false;
        
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @return the actions
     */
    public String[] getActions() {
        return actions;
    }

    /**
     * @return the rewards
     */
    public int[] getRewards() {
        return rewards;
    }

    /**
     * @param rewards the rewards to set
     */
    public void setRewards(int[] rewards) {
        this.rewards = rewards;
    }
    
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * @return the transitionStateProbability
     */
    public double[] getTransitionStateProbability() {
        return transitionStateProbability;
    }

    /**
     * @param transitionStateProbability the transitionStateProbability to set
     */
    public void setTransitionStateProbability(double[] transitionStateProbability) {
        this.transitionStateProbability = transitionStateProbability;
    }

    /**
     * @return the transitionStates
     */
    public int[][] getTransitionStates() {
        return transitionStates;
    }

    /**
     * @param transitionStates the transitionStates to set
     */
    public void setTransitionStates(int[][] transitionStates) {
        this.transitionStates = transitionStates;
    }
    
    public double probabilityTransition(String action, State s1){
        int idx = -1;
        for (int i = 0; i < actions.length; i++) {
            if(action.equalsIgnoreCase(actions[i])){
                idx = i;
                break;
            }
        }
        return 0;
    }
    //add probs and states according to move
    public void addHashState(String action, List s){
        List<State> copyS = new ArrayList<State>();
        for (int i = 0; i < s.size(); i++) {
            copyS.add(new State(1, 1));   
        }
        Collections.copy(copyS, s); 
        moveState.put(action, copyS);
    }
    
    public void addHashTransition(String action, List r){
        List<Double> copyR = new ArrayList<Double>();
        for (int i = 0; i < r.size(); i++) {
            copyR.add(1.0);   
        }
        Collections.copy(copyR, r); 
        moveProbability.put(action, r);
    }    
    
    
    public void addTransitionReward(String action, List r){
        List<Double> copyR = new ArrayList<Double>();
        for (int i = 0; i < r.size(); i++) {
            copyR.add(1.0);   
        }
        Collections.copy(copyR, r); 
        transitionReward.put(action, copyR);
    } 

    
    public int transitionSize(){
        return transitionReward.size();
    }
    
    public void TestStuff(){
        System.out.println("\nThe position "+x+","+y+" has for\n");
        for (int i = 0; i < actions.length; i++) {
            
            List<Double> d = transitionReward.get(actions[i]);
            System.out.print("action "+actions[i]+" rewards: ");
            for (int j = 0; j < d.size(); j++) {
                System.out.print(d.get(j).doubleValue()+"    ");   
            }
            System.out.println(""); 
        }
        
    }
    //given action name return its index
    public int getActionIndex(String s){
        int idx = 0;
        for (int i = 0; i < actions.length; i++) {
            if(actions[i].equalsIgnoreCase(s)){
                idx = i;
                break;
            }
        }
        return idx;
    }
    //get the probability value per index
    public double probabilityValue(String action, State s){
        List<Double> probs = moveProbability.get(action);
        //System.out.println("Probs size: " + probs.size() + "for state: "+x+","+y);
        List<State> states = moveState.get(action);
        int idx = states.indexOf(s);
        return probs.get(idx).doubleValue();
    }
    
    
    public double rewardValue(String action, State s){
        List<Double> reward = transitionReward.get(action);
        List<State> states = moveState.get(action);
        int idx = states.indexOf(s);
        return reward.get(idx).doubleValue();
    }
    
    public List<State> returnState(String action){
        List<Double> reward = transitionReward.get(action);
        List<State> states = moveState.get(action);
        return states;
    }
    
    public double newValueCalculation(){
        double maxTransition = -Double.MAX_VALUE;
        for (int i = 0; i < actions.length; i++) {
            List<State> states = moveState.get(actions[i]);
            double transitionTempValue = 0;
            for (int j = 0; j < states.size(); j++) {
                double prob = probabilityValue(actions[i], states.get(j));
                double reward = rewardValue(actions[i], states.get(j));
                double valueTransitionState = states.get(j).getValue();
                transitionTempValue += prob*(reward + /*gamma of 1*/1*valueTransitionState);
            }
            //System.out.println("The probability");
            //System.out.println("trans temp val is:" + transitionTempValue+ " for direction: " + actions[i]);
            if(transitionTempValue>maxTransition){
                maxTransition = transitionTempValue;
            }
        }
        return maxTransition;
    }
    
    public String policy(){
        String move = "";
        double maxTransition = -Double.MAX_VALUE;
        for (int i = 0; i < actions.length; i++) {
            List<State> states = moveState.get(actions[i]);
            double transitionTempValue = 0;
            for (int j = 0; j < states.size(); j++) {
                double prob = probabilityValue(actions[i], states.get(j));
                double reward = rewardValue(actions[i], states.get(j));
                double valueTransitionState = states.get(j).getValue();
                transitionTempValue += prob*(reward + /*gamma of 1*/1*valueTransitionState);
            }
            //System.out.println("The probability");
            //System.out.println("trans temp val is:" + transitionTempValue+ " for direction: " + actions[i]);
            if(transitionTempValue>maxTransition){
                maxTransition = transitionTempValue;
                if(actions[i].equalsIgnoreCase("up")){
                    move = "d";
                }else if(actions[i].equalsIgnoreCase("down")){
                    move = "u";
                }else{
                    move = formattedAction[i];
                }
            }
        }
        return move;
    }
    
    public String episilonGreedy(){
        double episilon = 0.1;
        boolean probability = new Random().nextInt((int) (episilon*100))==0;
        int idx = 0;
        if(!probability){
            for (int i = 0; i < QValues.length; i++) {
                if(QValues[i]>QValues[idx]){
                    idx = i;
                }else if(QValues[i]==QValues[idx]){
                    double test = 4;
                    boolean probtest = new Random().nextInt((int) (test))==0;
                    if(probtest){
                        idx = i;
                    }
                }
            }
        
        }else{
            Random r = new Random();
            idx = r.nextInt(4);
        }
        
        return actions[idx];
    }

    /**
     * @return the QValues
     */
    public double[] getQValues() {
        return QValues;
    }

    /**
     * @param QValues the QValues to set
     */
    public void setQValues(double[] QValues) {
        this.QValues = QValues;
    }

    /**
     * @return the terminal
     */
    public boolean isTerminal() {
        return terminal;
    }

    /**
     * @param terminal the terminal to set
     */
    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }
    
    public String moveArgmax(){
        int idx = 0;
        for (int i = 0; i < QValues.length; i++) {
            //System.out.print(" " + QValues[i]+" ");
                if(QValues[i]>QValues[idx]){
                    idx = i;
                }
        }
        if(formattedAction[idx].equalsIgnoreCase("u")){
            return "d";
        }else if(formattedAction[idx].equalsIgnoreCase("d")){
            return "u";
        }
        //System.out.print("\t"+QValues[idx]+" ");
        return formattedAction[idx];
    }
}
