/*
 * Name: Keaton Love
 * Description: A Neural Network made to recognize MNIST handwritten digits for CSC 475
 * Due Date: 10/27
 */

import java.io.*;
import java.util.*;
import java.io.BufferedReader;

class Data {

    private int[][] oneHot = new int[1][10];
    private double[] pixles = new double[784];
    private int correctValue = 0;

    public void setOneHot(int n) {
        oneHot[0][n] = 1;
        correctValue = n;
    }

    public void getOneHot() {
        System.out.println(Arrays.toString(oneHot[0]));
    }

    public void setPixles(int i, double pixleValue) {
        double pixleNorm = pixleValue / 255;
        pixles[i-1] = pixleNorm;
        //System.out.println(i + " " + j + " " + pixles[i][j]);
    }

    public double[] getPixleValues() {
        return (pixles);
        /*for (double[] val : pixles){
            System.out.println(Arrays.toString(val));
        }*/
    }

    public void Art(){
        String character = Integer.toString(correctValue);
        for (int i = 0; i < 784; i++){
            if (i % 28 != 0){
                double value = pixles[i];
                if (pixles[i] > 0){System.out.print(character);}
                else{System.out.print(" ");}
            }
            else{
                System.out.println();
                if (pixles[i] > 0){System.out.print(character);}
                else{System.out.print(" ");}
            }
        }
    }

}


// Main class of the Neural Network
class MNIST_NeuralNet {

    public static Data[] trainingData = new Data[60000]; 
    public static Double[][] weights1 = new Double[30][784];
    public static Double[][] weights2 = new Double[10][30];
    public static Double[][] bias = new Double[10][30];
    
    public static void prompt() {

        // Greets and prompts the user
        System.out.println(
                "\nWelcome to Keaton's Neural Network that recognizes handwritten digits. \n\n\t\tWhat would you like to do?");
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println(weights1.length);
        //try {setData();} catch (Exception e) {System.out.println(e);}

        // Randomize weights for first layer
        for (int i = 0; i < weights1.length; i++){
            weights1[i][0] = (1-(-1)*(new Random().nextDouble()))-1;
        }
        // Randomize weights for second layer
        for (int i = 0; i < weights2.length; i++){
            weights2[i][0] = (1-(-1)*(new Random().nextDouble()))-1;
            bias[i][0] = (1-(-1)*(new Random().nextDouble()))-1;
        }
    }

    public static void setData() throws Exception {

        BufferedReader fileReader = null;
        final String Delimiter = ",";
        fileReader = new BufferedReader(new FileReader("mnist_train.csv"));
        String line = "";
        
        Data[] allData = new Data[60000];

        String[][] sixtyThousand = new String[60000][785];

        for (int i = 0; i < 60000; i++){
            line = fileReader.readLine();
        //while ((line = fileReader.readLine()) != null){
            Data currentInput = new Data();

            String[] values = line.split(Delimiter);

            int lenData = 0;
            int firstIdx = 0;
            int secondIdx = 0;
            for (String value : values) {

                if (lenData == 0){
                    currentInput.setOneHot(Integer.parseInt(value)); 
                }
                else {
                    currentInput.setPixles(lenData, Integer.parseInt(value));
                    //System.out.println(lenData + " " + firstIdx + " " + secondIdx);
                }
                lenData += 1;
            }

            trainingData[i] = currentInput;
            currentInput.Art();
        }

        // Random variable
        Random rand = new Random();
		
        // Randomize the order of training data
		for (int i = 0; i < trainingData.length; i++) {
			int randomIndexToSwap = rand.nextInt(trainingData.length);
			Data temp = trainingData[randomIndexToSwap];
			trainingData[randomIndexToSwap] = trainingData[i];
			trainingData[i] = temp;
        }

        fileReader.close();

    }

    public static void forwardFeed(Data input, int layer){
        double summation = 0;
        //input = input.getPixleValues();
    }

    public static double sigmoid(double z){
        return (1/(1+Math.exp(-z)));
    }

    public static double sigmoidPrime(double z){
        return (sigmoid(z)*(1-sigmoid(z)));
    }

}