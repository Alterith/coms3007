
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class MDP {
    
    static int[][] rightWall = {
        {1,0},
        {2,0},
        {6,1},
        {7,1},
        {1,6},
        {2,6},
        {3,6},
        {8,6}
    };
    
    static int[][] leftWall = {
        {1,1},
        {2,1},
        {6,2},
        {7,2},
        {1,7},
        {2,7},
        {3,7},
        {8,7}
    };
    
    static int[][] upWall = {
        {3,1},
        {6,0},
        {6,1},
        {8,7},
        {1,8},
        {1,9}
    };
    
    static int[][] downWall = {
        {2,1},
        {5,0},
        {5,1},
        {7,7},
        {0,8},
        {0,9}
    };
    
    static int[][] permeableRightWall = {
        {8,1},
        {9,6}
    };
    
    static int[][] permeableLeftWall = {
        {8,2},
        {9,7}
    };
    
    static int[][] permeableUpWall = {
        {1,7}
    };
    
    static int[][] permeableDownWall = {
        {0,7}
    };
    static State[][] gridWorld;
    
    public static void main(String[] args) {
        
        createWorld();
        
        //valueIteration();
        /*
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(gridWorld[i][j].isTerminal()+"\t");
            }
            System.out.println("");
        }
        */
        //valueIteration();
        sarsa();
        
        //gridWorld[2][1].TestStuff();
        //System.out.println("\nPhew done setting up mostly");

    }
    
    public static void createWorld(){
        //initialize states
        gridWorld= new State[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gridWorld[i][j] = new State(i, j);
            }
        }
        
        gridWorld[9][9].setTerminal(true);
        
        //assign relevent values condition specific
        for (int i = 0; i < gridWorld.length; i++) {
            for (int j = 0; j < 10; j++) {
                //gonna need spec conditions for boundaries maybe a second loop
                
                //go left
                if((j-1)>=0){
                    //if teleporting tile change value
                    List<State> stateList=new ArrayList<>();
                    List<Double> probabilityList=new ArrayList<>();
                    List<Double> rewardList=new ArrayList<>();
                    //in bounds
                    
                    //check if some type of wall exists
                    boolean solidwall = false;
                    boolean permeableWall = false;
                    for (int k = 0; k < leftWall.length; k++) {
                        if((i==leftWall[k][0])&&(j==leftWall[k][1])){
                            solidwall = true;
                            break;
                        }  
                    }
                    
                    for (int k = 0; k < permeableLeftWall.length; k++) {
                        if((i==permeableLeftWall[k][0])&&(j==permeableLeftWall[k][1])){
                            permeableWall = true;
                            break;
                        }
                    }
                    
                    String direction = "left";                   
                    if(solidwall == true){
                        stateList.add(gridWorld[i][j]);
                        rewardList.add(-5.0);
                        probabilityList.add(1.0);
                    }else if(permeableWall == true){
                        stateList.add(gridWorld[i][j-1]);
                        rewardList.add(-3.0);
                        probabilityList.add(1.0);
                    }else{
                        // collision with a
                        if((i==3)&&((j-1)==1)){
                            stateList.add(gridWorld[7][2]);
                            stateList.add(gridWorld[0][9]);
                            probabilityList.add(0.6);
                            probabilityList.add(0.4);
                            rewardList.add(-1.0);
                            rewardList.add(-1.0);
                        }else if((i==1)&&((j-1)==4)){
                            //collision with b
                            stateList.add(gridWorld[5][7]);
                            stateList.add(gridWorld[1][2]);
                            probabilityList.add(0.4);
                            probabilityList.add(0.6);
                            rewardList.add(-1.0);
                            rewardList.add(-1.0);
                        }else if((i==4)&&((j-1)==8)){
                            //collision with c
                            stateList.add(gridWorld[9][6]);
                            stateList.add(gridWorld[2][5]);
                            probabilityList.add(0.7);
                            probabilityList.add(0.3);
                            rewardList.add(-1.0);
                            rewardList.add(-1.0);
                        }else{
                            stateList.add(gridWorld[i][j-1]);
                            rewardList.add(-1.0);
                            probabilityList.add(1.0);
                        }
                        //stateList.add(gridWorld[i][j-1]);
                        //rewardList.add(-1.0);
                    }
                    gridWorld[i][j].addHashState(direction, stateList);
                    gridWorld[i][j].addHashTransition(direction, probabilityList);
                    gridWorld[i][j].addTransitionReward(direction, rewardList);
                }else{
                    //out of bounds
                    //if teleporting tile change value
                    List<State> stateList=new ArrayList<>();
                    List<Double> probabilityList=new ArrayList<>();
                    List<Double> rewardList=new ArrayList<>();
                    //in bounds
                    String direction = "left";
                    stateList.add(gridWorld[i][j]);
                    probabilityList.add(1.0);
                    rewardList.add(-5.0);
                    gridWorld[i][j].addHashState(direction, stateList);
                    gridWorld[i][j].addHashTransition(direction, probabilityList);
                    gridWorld[i][j].addTransitionReward(direction, rewardList);
                }
                //go right
                if((j+1)< 10){
                    //if teleporting tile change value
                    List<State> stateList=new ArrayList<>();
                    List<Double> probabilityList=new ArrayList<>();
                    List<Double> rewardList=new ArrayList<>();
                    //in bounds
                    //check if next to wall
                    boolean solidwall = false;
                    boolean permeableWall = false;
                    for (int k = 0; k < rightWall.length; k++) {
                            if((i==rightWall[k][0])&&(j==rightWall[k][1])){
                                solidwall = true;
                                break;
                        }  
                    }
                    
                    for (int k = 0; k < permeableRightWall.length; k++) {
                        if((i==permeableRightWall[k][0])&&(j==permeableRightWall[k][1])){
                            permeableWall = true;
                            break;
                        }
                    }
                    String direction = "right";
                    if(j == 8 && i == 9){
                        rewardList.add(100.0);
                        stateList.add(gridWorld[i][j+1]);
                        probabilityList.add(1.0);
                    }else{
                        if(solidwall == true){
                            rewardList.add(-5.0);
                            probabilityList.add(1.0);
                            stateList.add(gridWorld[i][j]);
                        }else if(permeableWall == true){
                            rewardList.add(-3.0);
                            probabilityList.add(1.0);
                            stateList.add(gridWorld[i][j+1]);
                        }else{
                        // collision with a
                        if((i==3)&&((j+1)==1)){
                            stateList.add(gridWorld[7][2]);
                            stateList.add(gridWorld[0][9]);
                            probabilityList.add(0.6);
                            probabilityList.add(0.4);
                            rewardList.add(-1.0);
                            rewardList.add(-1.0);
                        }else if((i==1)&&((j+1)==4)){
                            //collision with b
                            stateList.add(gridWorld[5][7]);
                            stateList.add(gridWorld[1][2]);
                            probabilityList.add(0.4);
                            probabilityList.add(0.6);
                            rewardList.add(-1.0);
                            rewardList.add(-1.0);
                        }else if((i==4)&&((j+1)==8)){
                            //collision with c
                            stateList.add(gridWorld[9][6]);
                            stateList.add(gridWorld[2][5]);
                            probabilityList.add(0.7);
                            probabilityList.add(0.3);
                            rewardList.add(-1.0);
                            rewardList.add(-1.0);
                        }else{
                            stateList.add(gridWorld[i][j+1]);
                            rewardList.add(-1.0);
                            probabilityList.add(1.0);
                        }
                        }
                        
                    }
                    
                    gridWorld[i][j].addHashState(direction, stateList);
                    gridWorld[i][j].addHashTransition(direction, probabilityList);
                    gridWorld[i][j].addTransitionReward(direction, rewardList);
                }else{
                    //if teleporting tile change value
                    List<State> stateList=new ArrayList<>();
                    List<Double> probabilityList=new ArrayList<>();
                    List<Double> rewardList=new ArrayList<>();
                    //in bounds
                    String direction = "right";
                    stateList.add(gridWorld[i][j]);
                    probabilityList.add(1.0);
                    rewardList.add(-5.0);
                    gridWorld[i][j].addHashState(direction, stateList);
                    gridWorld[i][j].addHashTransition(direction, probabilityList);
                    gridWorld[i][j].addTransitionReward(direction, rewardList);
                }
                
                //go up
                if((i-1)>=0){
                    //if teleporting tile change value
                    List<State> stateList=new ArrayList<>();
                    List<Double> probabilityList=new ArrayList<>();
                    List<Double> rewardList=new ArrayList<>();
                    //in bounds
                    
                    //check if some type of wall exists
                    boolean solidwall = false;
                    boolean permeableWall = false;
                    for (int k = 0; k < upWall.length; k++) {
                        if((i==upWall[k][0])&&(j==upWall[k][1])){
                            solidwall = true;
                            break;
                        }  
                    }
                    
                    for (int k = 0; k < permeableUpWall.length; k++) {
                        if((i==permeableUpWall[k][0])&&(j==permeableUpWall[k][1])){
                            permeableWall = true;
                            break;
                        }
                    }
                    
                    String direction = "up";
                    if(solidwall == true){
                        stateList.add(gridWorld[i][j]);
                        rewardList.add(-5.0);
                        probabilityList.add(1.0);
                    }else if(permeableWall == true){
                        stateList.add(gridWorld[i-1][j]);
                        rewardList.add(-3.0);
                        probabilityList.add(1.0);
                    }else{
                        // collision with a
                        if(((i-1)==3)&&((j)==1)){
                            stateList.add(gridWorld[7][2]);
                            stateList.add(gridWorld[0][9]);
                            probabilityList.add(0.6);
                            probabilityList.add(0.4);
                            rewardList.add(-1.0);
                            rewardList.add(-1.0);
                        }else if(((i-1)==1)&&((j)==4)){
                            //collision with b
                            stateList.add(gridWorld[5][7]);
                            stateList.add(gridWorld[1][2]);
                            probabilityList.add(0.4);
                            probabilityList.add(0.6);
                            rewardList.add(-1.0);
                            rewardList.add(-1.0);
                        }else if(((i-1)==4)&&((j)==8)){
                            //collision with c
                            stateList.add(gridWorld[9][6]);
                            stateList.add(gridWorld[2][5]);
                            probabilityList.add(0.7);
                            probabilityList.add(0.3);
                            rewardList.add(-1.0);
                            rewardList.add(-1.0);
                        }else{
                            stateList.add(gridWorld[i-1][j]);
                            rewardList.add(-1.0);
                            probabilityList.add(1.0);
                        }
                    }
                    gridWorld[i][j].addHashState(direction, stateList);
                    gridWorld[i][j].addHashTransition(direction, probabilityList);
                    gridWorld[i][j].addTransitionReward(direction, rewardList);
                }else{
                    //if teleporting tile change value
                    List<State> stateList=new ArrayList<>();
                    List<Double> probabilityList=new ArrayList<>();
                    List<Double> rewardList=new ArrayList<>();
                    //in bounds
                    String direction = "up";
                    stateList.add(gridWorld[i][j]);
                    probabilityList.add(1.0);
                    rewardList.add(-5.0);
                    gridWorld[i][j].addHashState(direction, stateList);
                    gridWorld[i][j].addHashTransition(direction, probabilityList);
                    gridWorld[i][j].addTransitionReward(direction, rewardList);
                }
                
                //go down
                if((i+1)<10){
                    //if teleporting tile change value
                    List<State> stateList=new ArrayList<>();
                    List<Double> probabilityList=new ArrayList<>();
                    List<Double> rewardList=new ArrayList<>();
                    //in bounds
                    //check if next to wall
                    boolean solidwall = false;
                    boolean permeableWall = false;
                    for (int k = 0; k < downWall.length; k++) {
                            if((i==downWall[k][0])&&(j==downWall[k][1])){
                                solidwall = true;
                                break;
                        }  
                    }
                    
                    for (int k = 0; k < permeableDownWall.length; k++) {
                        if((i==permeableDownWall[k][0])&&(j==permeableDownWall[k][1])){
                            permeableWall = true;
                            break;
                        }
                    }
                    
                    
                    String direction = "down";
                    
                    if(i == 8 && j == 9){
                        rewardList.add(100.0);
                        stateList.add(gridWorld[i+1][j]);
                        probabilityList.add(1.0);
                    }else{
                        if(solidwall == true){
                            rewardList.add(-5.0);
                            probabilityList.add(1.0);
                            stateList.add(gridWorld[i][j]);
                        }else if(permeableWall == true){
                            rewardList.add(-3.0);
                            probabilityList.add(1.0);
                            stateList.add(gridWorld[i+1][j]);
                        }else{
                        // collision with a
                        if(((i+1)==3)&&((j)==1)){
                            stateList.add(gridWorld[7][2]);
                            stateList.add(gridWorld[0][9]);
                            probabilityList.add(0.6);
                            probabilityList.add(0.4);
                            rewardList.add(-1.0);
                            rewardList.add(-1.0);
                        }else if(((i+1)==1)&&((j)==4)){
                            //collision with b
                            stateList.add(gridWorld[5][7]);
                            stateList.add(gridWorld[1][2]);
                            probabilityList.add(0.4);
                            probabilityList.add(0.6);
                            rewardList.add(-1.0);
                            rewardList.add(-1.0);
                        }else if(((i+1)==4)&&((j)==8)){
                            //collision with c
                            stateList.add(gridWorld[9][6]);
                            stateList.add(gridWorld[2][5]);
                            probabilityList.add(0.7);
                            probabilityList.add(0.3);
                            rewardList.add(-1.0);
                            rewardList.add(-1.0);
                        }else{
                            stateList.add(gridWorld[i+1][j]);
                            rewardList.add(-1.0);
                            probabilityList.add(1.0);
                        }
                        }
                        
                    }
                    
                    gridWorld[i][j].addHashState(direction, stateList);
                    gridWorld[i][j].addHashTransition(direction, probabilityList);
                    gridWorld[i][j].addTransitionReward(direction, rewardList);
                }else{
                    //if teleporting tile change value
                    List<State> stateList=new ArrayList<>();
                    List<Double> probabilityList=new ArrayList<>();
                    List<Double> rewardList=new ArrayList<>();
                    //in bounds
                    String direction = "down";
                    stateList.add(gridWorld[i][j]);
                    probabilityList.add(1.0);
                    rewardList.add(-5.0);
                    gridWorld[i][j].addHashState(direction, stateList);
                    gridWorld[i][j].addHashTransition(direction, probabilityList);
                    gridWorld[i][j].addTransitionReward(direction, rewardList);
                }
            } 
        }
    }
    
    public static void valueIteration(){
        
        for (int k = 0; k < 150; k++) {
            
        
        double[][] newValue = new double[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                newValue[i][j] = gridWorld[i][j].newValueCalculation();
            }
            //System.out.println("\n");
        }
        newValue[9][9] = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                //System.out.print(newValue[i][j]+"  ");
                gridWorld[i][j].setValue(newValue[i][j]);
                //System.out.print(gridWorld[i][j].getValue()+"  ");
            }
            //System.out.println("\n");
        }
        }
        
     for (int i = (gridWorld.length-1); i >= 0; i--) {
            for (int j = 0; j < gridWorld.length; j++) {
                System.out.print(gridWorld[i][j].getValue()+"\t");
                
            }
            System.out.println("\n");
        }
        System.out.println("\n");
        for (int i = (gridWorld.length-1); i >= 0; i--) {
            for (int j = 0; j < gridWorld.length; j++) {
                if(i == 3 && j == 1){
                    System.out.print("t\t");
                }else if(i == 1 && j == 4){
                    System.out.print("t\t");
                }else if(i == 4 && j == 8){
                    System.out.print("t\t");
                }else if(i == 9 && j == 9){
                    System.out.print("X\t");
                }else{
                    System.out.print(gridWorld[i][j].policy()+"\t");
                }
                
                
            }
            System.out.println("\n");
        }
        
    }
    
    public static void sarsa(){
        int x = 0;
        int y = 0;
        String move = "right";
        String episilonMove = "";
        double alpha = 0.3;
        double gamma = 0.9;
        for (int k = 0; k < 1; k++) {
            
        while (!gridWorld[x][y].isTerminal()){
            List<State> possibleStates = gridWorld[x][y].returnState(move);
            double[] qvals = gridWorld[x][y].getQValues();
            for (int i = 0; i < possibleStates.size(); i++) {
                episilonMove = possibleStates.get(i).episilonGreedy();
                double reward = gridWorld[x][y].rewardValue(move, possibleStates.get(i));
                int idx = gridWorld[x][y].getActionIndex(move);
                double probability = gridWorld[x][y].probabilityValue(move, possibleStates.get(i));
                int episilonIdx = possibleStates.get(i).getActionIndex(episilonMove);
                double[] qvalsNext = possibleStates.get(i).getQValues();
                qvals[idx] = qvals[idx] + alpha*(reward + gamma*qvalsNext[episilonIdx]*probability - qvals[idx]);
                gridWorld[x][y].setQValues(qvals);
            }
            Random r = new Random();
            int nextState = r.nextInt(1);
            x = possibleStates.get(nextState).getX();
            y = possibleStates.get(nextState).getY();
            move = episilonMove;
            if(move.equalsIgnoreCase("down")){
                System.out.println("The move taken is: "+ "up");
            }else if(move.equalsIgnoreCase("up")){
                System.out.println("The move taken is: "+ "down");
            }else{
                System.out.println("The move taken is: "+ move);
            }
            
            System.out.println("x,y: " + y + "," + x);
        }
        }
        for (int i = (gridWorld.length-1); i >= 0; i--) {
            for (int j = 0; j < gridWorld[0].length; j++) {
                
                if(i == 3 && j == 1){
                    System.out.print("t\t");
                }else if(i == 1 && j == 4){
                    System.out.print("t\t");
                }else if(i == 4 && j == 8){
                    System.out.print("t\t");
                }else if(i == 9 && j == 9){
                    System.out.print("X\t");
                }else{
                    System.out.print(gridWorld[i][j].moveArgmax()+"\t"); 
                }
            }
            System.out.println("\n");
        }
    }
    
}
