import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by alterith on 2017/03/08.
 */
public class BP {
    public static void main(String[] args)throws FileNotFoundException {
        System.out.println("Please input the data set file name");


        Scanner input = new Scanner(System.in);
        String fileName = input.nextLine();
        File file = new File(fileName);
        Scanner set = new Scanner(file);


        ArrayList<String> al = new ArrayList<>();
        while (set.hasNext()) {
            al.add(set.nextLine());
            //System.out.println(set.nextLine());
        }
        Set<String> thresholdSet = new HashSet<>();
        for (int i = 0; i < al.size(); i++) {
            thresholdSet.add(al.get(i).substring(al.get(i).length()-1));
        }
        int numUnique = thresholdSet.size();



        String[] num = al.get(0).split(" ");
        int numLength = num.length;
        int inputNum = al.size();
        int[] thresholdVector = new int[inputNum*numUnique];
        ArrayList<String> thresholdList = new ArrayList<String>(thresholdSet);
        for (int i = 0; i < inputNum; i++) {
            int idx = thresholdList.indexOf(al.get(i).substring(al.get(i).length()-1));
            thresholdVector[idx+(i*numUnique)] = 1;
            al.set(i,al.get(i).substring(0,al.get(i).length()-1)+"-1");
        }
        double[][] dataSet = new double[inputNum][numLength];
        for (int i = 0; i < dataSet.length; i++) {
            for (int j = 0; j < dataSet[0].length; j++) {
                String[] s = al.get(i).split(" ");
                dataSet[i][j] = Double.parseDouble(s[j]);
            }
        }
        int[] threshold = new int[inputNum];
        for (int i = 0; i < inputNum; i++) {
            threshold[i] = (int)dataSet[i][numLength-1];
            dataSet[i][numLength-1] = -1;
            //System.out.println(al.get(i));
            //System.out.println(threshold[i]);
        }


        Random r = new Random();
        int hiddenNodeNumbers = r.nextInt(100-1) + 1;
        //int hiddenNodeNumbers = 2;
        double learningRate = 0.0025;
        double[][] w = new double[numLength][hiddenNodeNumbers];
        //double[][] bestW = new double[numLength][hiddenNodeNumbers];
        for (int i = 0; i < w.length; i++) {
            for (int j = 0; j < w[0].length; j++) {
                w[i][j] = -1 + 2*Math.random();
            }
        }
        double[][] u = new double[hiddenNodeNumbers+1][inputNum*numUnique];
        for (int i = 0; i < u.length; i++) {
            for (int j = 0; j < u[0].length; j++) {
                u[i][j] = -1+ 2*Math.random();
            }
        }

        int[] order = new int[numLength];
        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }

        double[][] w1 = NNLearn2(dataSet,w,u,order,learningRate,thresholdVector);

        for (int i = 0; i < w1.length; i++) {
            for (int j = 0; j < w1[0].length; j++) {
                System.out.println("The i,jth weight values are: " + i +" " + j);
                System.out.println(w1[i][j]+" ");
            }
            System.out.println();
        }
    }


    public static double[][] NNLearn2(double[][] x, double[][] w, double[][] u, int[] order, double learnRate, int[] t){
        //repeat the procedure for fixed number of iterations
        Random r = new Random();
        for (int i = 0; i < 20; i++) {
            //re=andomize the order of data which will be processed
            order = shuffle(order,r);
            double[] Y = new double[t.length];
            for (int j = 0; j < order.length; j++) {
                //process input to get hidden layer output
                double[] A1 = percept(w, x[order[j]]);
                double[] A = new double[A1.length+1];
                for (int k = 0; k < A1.length; k++) {
                    A[k] = A1[k];
                }
                A[A.length-1] = -1;
                //use A to get final output values
                Y = percept(u,A);

                //Create array to store the changes in each value in output layer
                double[] DO = new double[Y.length];
                //calculate the change in the values needed to reach desired output
                System.out.println("Ylen "+ Y.length);
                System.out.println("Tlen"+ t.length);
                for (int k = 0; k < Y.length; k++) {
                    DO[k] = Y[k]*(1-Y[k])*(t[k] - Y[k]);
                }

                //create array to store changes in each value in hidden layer
                double[] DH = new double[A.length];
                //calc changes needed
                for (int k = 0; k < A.length; k++) {
                    double sum = 0;
                    for (int l = 0; l < u[0].length; l++) {
                        sum += DO[l] * u[k][l];
                    }
                    DH[k] = A[k]*(1-A[k])*sum;
                }

                //update step

                //update u
                for (int k = 0; k < u.length; k++) {
                    for (int l = 0; l < u[0].length; l++) {
                        u[k][l] = u[k][l] + learnRate*DO[l]*A[k];
                    }
                }

                //update w

                for (int k = 0; k < w.length; k++) {
                    for (int l = 0; l < w[0].length; l++) {
                        w[k][l] = w[k][l] + learnRate*DH[l]*x[order[j]][k];
                    }
                }
            }
            double[] errorT = new double[t.length];
            for (int j = 0; j < errorT.length; j++) {
                errorT[j] = 0.5*Math.pow((t[j]-Y[j]),2);
                System.out.println("The error of the "+i+"th iteration is: "+ errorT[j]);
            }
        }
        return w;
    }





    public static double[] percept(double[][] w, double[] x){
        double[] i = new double[w[0].length];
        double sum = 0;
        for (int j = 0; j <w[0].length; j++) {
            for (int k = 0; k < w.length; k++) {
                //System.out.println(w[k][j]);
                //System.out.println(x[k]);
                sum += w[k][j]*x[k];
            }
            i[j] = 1/(1+(Math.pow(Math.E,sum)));
        }
        return i;
    }

    public static int[] shuffle(int[] array, Random random) {
        if (random == null) random = new Random();
        int count = array.length;
        for (int i = count; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
        return array;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}
