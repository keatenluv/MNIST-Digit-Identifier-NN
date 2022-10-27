/*
 * Name: Keaton Love
 * CWID: 102-57-333
 * Assignment #2
 * Description: A Neural Network using Stocashtic Gradient Descent and Backpropogation
*               made to recognize MNIST handwritten digits for CSC 475
 * Due Date: 10/27
 */

import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.util.stream.*;
import java.util.Scanner;

// Data class to make accessing input data easier and print ASCII art
class Data {

    private double[] oneHot = new double[10];
    private double[] pixles = new double[784];
    private int correctValue = 0;

    // Sets the value of the correct classification the digit
    public void setOneHot(int n) {
        oneHot[n] = 1;
        correctValue = n;
    }

    // Returns the oneHot array
    public double[] getOneHot() {
        return (oneHot);
    }

    // Sets the value of pixles for the input
    public void setPixles(int i, double pixleValue) {
        double pixleNorm = pixleValue / 255;
        pixles[i-1] = pixleNorm;
    }

    // Returns all pixle values for the input
    public double[] getPixleValues() {
        return (pixles);
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
    public static Double[] bias0 = new Double[30];
    public static Double[] bias = new Double[10];
    public static boolean trained = false;

    public static void main(String[] args) {
        try {setData("mnist_train.csv");} catch (Exception e) {System.out.println(e);}

        Scanner scanner = new Scanner(System.in);

        System.out.println(
                "\nWelcome to Keaton's Neural Network that recognizes handwritten digits. \n\n\t\tWhat would you like to do?");
        System.out.println();
        /*try{
            while (true){
                System.out.println("\n[1] Train the network");
                System.out.println();
                System.out.println("\n[2] Load a pre-trained network");
                System.out.println();
                String input = scanner.nextLine();
                System.out.println(input);
                if (input == "train"){trainNetwork();}
                else if(input == "2"){}
            }
        } catch (Exception e){ System.out.println(e);}*/
        trainNetwork();

    }

    // Method to print the accuracy of the network after each epoch
    public static void printAccuracy(int[] correct, int[] all){
        System.out.println();
        for (int i = 0; i < correct.length; i++){
            System.out.print(i + " = " + correct[i] + "/" + all[i] + " ");
            if (i == 5){System.out.println();}
        }
        System.out.print(" Accuracy = " + Arrays.stream(correct).sum() + "/60000 " + ((Arrays.stream(correct).sum()/60000.0)*100) + "%");
        System.out.println();
    }

    public static void runTesting(){
        Arrays.fill(trainingData, null);
        try {setData("mnist_test.csv");} catch (Exception e) {System.out.println(e);}

        int[] totalCorrect = new int[10];
        int[] totalOfNums = new int[10];

        for (int i = 0; i < 10000; i++){
            double[] hOutput = (forwardFeed(trainingData[i].getPixleValues()));
            double[] fOutput = (forwardFeed(hOutput));

            // Find the correct classification of input and the Networks calculated digit
            int maxAt = 0;
            int maxAt2 = 0;

            // Gets max from inputs one hot and from the networks final layer
            for (int idx = 0; idx < fOutput.length; idx++) {
                maxAt = fOutput[idx] > fOutput[maxAt] ? idx : maxAt;
            }
            for (int idx = 0; idx < trainingData[i].getOneHot().length; idx++) {
                maxAt2 = (trainingData[i].getOneHot())[idx] > (trainingData[i].getOneHot())[maxAt2] ? idx : maxAt2;
            }

            // Add one to totalCorrect if the networks output is correct
            if (maxAt2 == maxAt){
                totalCorrect[maxAt] += 1;
            }
            // Keeps track of total numbers
            totalOfNums[maxAt2] += 1; 

            printAccuracy(totalCorrect, totalOfNums);
        }
    }

    public static void loadNetwork(){

    }

    // Method to train the network includes SGD and Back propagation algorithims
    public static void trainNetwork() {
        
        // Flag for user input menu
        trained = true;

        // Inital randomization for the networks weights and biases
        randomizeWnB();

        // Initalize 
        double learningRate = 1.5;
        double miniBatchSize = 10.0;
        int epochs = 30;

        // Loop for the 30 Epochs
        for (int kms = 0; kms < epochs; kms++){

            // Randomize training data
            Randomize();

            
            int[] totalCorrect = new int[10];
            int[] totalOfNums = new int[10];

            // Contains backpropogation
            for (int i = 0; i < 6000; i++){

                // Setup variables used in SGD and backprop
                double[] HiddenBiasGradientSum  = new double[weights1.length];
                double[] FinalBiasGradientSum = new double[weights2.length];
                double[][] weightGradientH = new double[weights1.length][weights1[0].length];
                double[][] weightGradientF = new double[weights2.length][weights2[0].length];

                for (int j = 0; j < 10; j++){
                    // Hidden Layer Output and Final Layer output
                    double[] hOutput = (forwardFeed(trainingData[(i*10)+j].getPixleValues()));
                    double[] fOutput = (forwardFeed(hOutput));

                    // Find the correct classification of input and the Networks calculated digit
                    int maxAt = 0;
                    int maxAt2 = 0;

                    // Gets max from inputs one hot and from the networks final layer
                    for (int idx = 0; idx < fOutput.length; idx++) {
                        maxAt = fOutput[idx] > fOutput[maxAt] ? idx : maxAt;
                    }
                    for (int idx = 0; idx < trainingData[(i*10+j)].getOneHot().length; idx++) {
                        maxAt2 = (trainingData[(i*10+j)].getOneHot())[idx] > (trainingData[(i*10+j)].getOneHot())[maxAt2] ? idx : maxAt2;
                    }

                    // Add one to totalCorrect if the networks output is correct
                    if (maxAt2 == maxAt){
                        totalCorrect[maxAt] += 1;
                    }
                    // Keeps track of total numbers
                    totalOfNums[maxAt2] += 1; 

                    // Calculate Bias Gradient for final layer
                    double[] tempBGF = new double[10];
                    for (int x = 0; x < weights2.length; x++){
                        tempBGF[x] = (fOutput[x] - (trainingData[(i*10)+j].getOneHot())[x]) * fOutput[x] * (1 - fOutput[x]);
                        FinalBiasGradientSum[x] += tempBGF[x];
                    }

                    // Calculate Bias Gradient for hidden layer
                    double[] tempBGH = new double[30];
                    for (int x = 0; x < weights2[0].length; x++){
                        double sum = 0;
                        for (int y = 0; y < weights2.length; y ++){
                            sum += (weights2[y][x] * tempBGF[y]);
                        }
                        tempBGH[x] = sum * hOutput[x] * (1 - hOutput[x]);
                        HiddenBiasGradientSum[x] += tempBGH[x];
                    }

                    // Calculate weight gradient for final layer
                    for (int x = 0; x < weights2.length; x++){
                        for (int y = 0; y < weights2[0].length; y++){
                            weightGradientF[x][y] += hOutput[y] * tempBGF[x]; 
                        }
                    }

                    // Calculate weight gradient for Hidden layer
                    for (int x = 0; x < weights1.length; x++){
                        for (int y = 0; y < weights1[0].length; y++){
                            weightGradientH[x][y] += (trainingData[(i*10)+j].getPixleValues())[y] * tempBGH[x]; 
                        }
                    }

                }

                // Gradient Descent
                // Update final layer bias
                for (int x = 0; x < FinalBiasGradientSum.length; x++){
                    bias[x] = bias[x] - (learningRate/miniBatchSize) * HiddenBiasGradientSum[x];
                }

                // Update hidden layer bias
                for (int x = 0; x < HiddenBiasGradientSum.length; x++){
                    bias0[x] = bias0[x] - (learningRate/miniBatchSize) * HiddenBiasGradientSum[x];
                }

                // Update first set of weights
                for (int x = 0; x < weights1.length; x++){
                    for (int y = 0; y < weights1[0].length; y++){
                        weights1[x][y] = (weights1[x][y] - (learningRate/miniBatchSize) * weightGradientH[x][y]);
                    }
                }

                // Update second set of weights
                for (int x = 0; x < weights2.length; x++){
                    for (int y = 0; y < weights2[0].length; y++){
                        weights2[x][y] = (weights2[x][y] - (learningRate/miniBatchSize) * weightGradientF[x][y]);
                    }
                }
            }
            System.out.println("Epoch: " + kms);
            printAccuracy(totalCorrect, totalOfNums);
        }
        runTesting();

    }

    public static void setData(String file) throws Exception {

        // Setup for the file reader to read data
        BufferedReader fileReader = null;
        final String Delimiter = ",";
        fileReader = new BufferedReader(new FileReader(file));
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

            // Add the current input's Data class to array of all data && increment index
            trainingData[index] = currentInput;
            index += 1;
        }

        // Close the file reader
        fileReader.close();

    }

    // The feedforward part of the network
    public static double[] forwardFeed(double[] input){
        double summation = 0;

        // Feedforward for first layer of the network
        if (input.length == 784){
            double[] layerOutput = new double[30];
            for (int j = 0; j < 30; j++){
                for (int i = 0; i < input.length; i++){
                    summation += input[i] * weights1[j][i];
                }
                layerOutput[j] = sigmoid(summation+bias0[j]);
            }
            return (layerOutput);
        }
        // Feedforward for the second layer of the network
        else{
            double[] finalOutput = new double[10];
            for (int j = 0; j < 10; j++){
                for (int i = 0; i < input.length; i++){
                    summation += input[i] * weights2[j][i];
                }
                finalOutput[j] = sigmoid(summation+bias[j]); 
            }

            return (finalOutput);
        }
    }

    // Sigmoid activation function of a neuron
    public static double sigmoid(double z){
        return (1/(1+Math.exp(-z)));
    }

    public static void randomizeWnB(){
        // Randomize weights for first layer
        for (int i = 0; i < weights1.length; i++){
            bias0[i] = (2*((new Random().nextDouble()))-1);
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
    }

    // Randomizes the training data
    public static void Randomize(){
        // Random variable  
        Random rand = new Random();
		
        // Shuffles the order of training data
		for (int i = 0; i < trainingData.length; i++) {
			int randomIndexToSwap = rand.nextInt(trainingData.length);
			Data temp = trainingData[randomIndexToSwap];
			trainingData[randomIndexToSwap] = trainingData[i];
			trainingData[i] = temp;
        }
    }

}