/*
 * Name: Keaton Love
 * Description: A Neural Network made to recognize MNIST handwritten digits for CSC 475
 * Due Date: 10/27
 */

import java.io.*;
import java.util.*;
import java.io.BufferedReader;

class Data {

    private int[] oneHot = new int[10];
    private double[] pixles = new double[784];
    private int correctValue = 0;

    public void setOneHot(int n) {
        oneHot[n] = 1;
        correctValue = n;
    }

    public int[] getOneHot() {
        return (oneHot);
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
            if (i % 28 == 0){System.out.println();}
            if (pixles[i] > .01){System.out.print(character);}
            else{System.out.print(" ");}
        }
    }
}


// Main class of the Neural Network
class MNIST_NeuralNet {

    public static Data[] trainingData = new Data[60000]; 
    public static Double[][] weights1 = new Double[30][784];
    public static Double[][] weights2 = new Double[10][30];
    public static Double[] bias = new Double[10];
    
    public static void prompt() {

        // Greets and prompts the user
        System.out.println(
                "\nWelcome to Keaton's Neural Network that recognizes handwritten digits. \n\n\t\tWhat would you like to do?");
        System.out.println();
    }

    public static void main(String[] args) {
        try {setData();} catch (Exception e) {System.out.println(e);}


        // Randomize weights for first layer
        for (int i = 0; i < weights1.length; i++){
            for (int j = 0; j < weights1[0].length; j++){
                weights1[i][j] = (2*((new Random().nextDouble()))-1);
            }
        }
        // Randomize weights for second layer
        for (int i = 0; i < weights2.length; i++){
            bias[i] = (2*((new Random().nextDouble()))-1);
            for (int j = 0; j < weights2[0].length; j++){
                weights2[i][j] = (2*((new Random().nextDouble()))-1);
                
            }
        }

        

        for (int j = 0; j < 100; j++){double[] hiddenOutput = new double[30];
            for (int i = 0; i < 10; i++){
                //System.out.println("Output #" + i + ":" + forwardFeed(trainingData[i].getPixleValues(), 0, 0));

                double temp = forwardFeed(trainingData[i].getPixleValues(), 0, 0);
                hiddenOutput[i] = temp;

            }

            System.out.println(forwardFeed(hiddenOutput, 0, 1));
        }
        
        
        
        
    }

    public static void setData() throws Exception {

        // Setup for the file reader
        BufferedReader fileReader = null;
        final String Delimiter = ",";
        fileReader = new BufferedReader(new FileReader("mnist_train.csv"));
        String line = "";

        // Keeping track of current # of inputs
        int index = 0;

        /*
         * Reads a line of data from the csv and creates 
         * a data object. Then places that object into an array
         */
        while ((line = fileReader.readLine()) != null){
            
            Data currentInput = new Data();

            String[] values = line.split(Delimiter);

            int lenData = 0;
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

            trainingData[index] = currentInput;
            
            index += 1;
            
        }
       

        // Random variable
        Random rand = new Random();
		
        // Shuffles the order of training data
		for (int i = 0; i < trainingData.length; i++) {
			int randomIndexToSwap = rand.nextInt(trainingData.length);
			Data temp = trainingData[randomIndexToSwap];
			trainingData[randomIndexToSwap] = trainingData[i];
			trainingData[i] = temp;
        }

        // Close the file reader
        fileReader.close();

    }

    public static double forwardFeed(double[] input, int row, int layer){
        double summation = 0;
        if (layer == 0){
            for (int i = 0; i < input.length; i++){
                summation += input[i] * weights1[row][i];
            }
        }
        else {
            for (int i = 0; i < input.length; i++){
                summation += input[i] * weights2[row][i];
            }
            summation += bias[row];
        }
        return (sigmoid(summation));
    }

    public static double sigmoid(double z){
        return (1/(1+Math.exp(-z)));
    }

    public static double sigmoidPrime(double z){
        return (sigmoid(z)*(1-sigmoid(z)));
    }

}