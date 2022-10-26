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
    private double[][] pixles = new double[28][28];
    private int correctValue = 0;

    public void setOneHot(int n) {
        oneHot[0][n] = 1;
        correctValue = n;
    }

    public void getOneHot() {
        System.out.println(Arrays.toString(oneHot[0]));
    }

    public void setPixles(int i, int j, double pixleValue) {
        double pixleNorm = pixleValue / 255;
        pixles[i][j] = pixleNorm;
        //System.out.println(i + " " + j + " " + pixles[i][j]);
    }

    public void getPixleValues() {
        for (double[] val : pixles){
            System.out.println(Arrays.toString(val));
        }
    }

    public void Art(){
        String character = Integer.toString(correctValue);
        for (int i = 0; i < pixles.length; i++){
            System.out.println();
            for (int j = 0; j < 28; j++){
                double value = pixles[i][j];
                if (pixles[i][j] > 0){System.out.print(character);}
                else{System.out.print(" ");}
            }
        }
    }

}

// Perceptron class
class Perceptron {

    static Random r = new Random();

    public static double Weight = r.nextGaussian();
    public static double Input;

    public static void main(String[] args) {
        Input += Double.parseDouble(args[0]);
    }

    // Method to return output (true or false) of perceptron
    public boolean output(double[][] inputs, double[][] weights) {

        // Initialize sum of products
        double SOP = 0;

        // Sum the product of all inputs and weights
        for (int i = 0; i < inputs[0].length; i++) {
            SOP += inputs[0][i] * weights[i][0];
        }

        System.out.println(SOP + "");
        if (SOP == 1) {
            return false;
        }
        return true;
    }
}

// Main class of the Neural Network
class MNIST_NeuralNet {

    public static void prompt() {

        // Greets and prompts the user
        System.out.println(
                "\nWelcome to Keaton's Neural Network that recognizes handwritten digits. \n\n\t\tWhat would you like to do?");

        System.out.println();
    }

    public static void main(String[] args) {
        try {setData();} catch (Exception e) {System.out.println(e);}

        /*
         * for (int i = 0; i < 100; i++){
         * // Statment to generate random number in range -1 to 1
         * System.out.println(new Random().nextDouble(((1-(-1))))-1);
         * }
         */
    }

    public static void setData() throws Exception {

        BufferedReader fileReader = null;
        final String Delimiter = ",";
        fileReader = new BufferedReader(new FileReader("mnist_train.csv"));
        String line = "";
        
        Data[] allData = new Data[10];

        /*for (int i = 0; i < 10; i++){

            line = fileReader.readLine();*/
        while ((line = fileReader.readLine()) != null){
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
                    firstIdx = (lenData-1) / 28;
                    secondIdx = (lenData-1) % 28;
                    currentInput.setPixles(firstIdx, secondIdx, Integer.parseInt(value));
                    //System.out.println(lenData + " " + firstIdx + " " + secondIdx);
                }
                lenData += 1;
            }

            currentInput.Art();

            //allData[i] = currentInput;
        }

        fileReader.close();
    }



    public static void create() {
        double[][] inputs = new double[1][256];
        double[][] weights = new double[256][1];

        // Fill Weights with random variables as initial 'guess'
        for (int i = 0; i < 256; i++) {

            weights[i][0] = Math.random();
            System.out.println("Weight " + i + ": " + weights[i][0]);
        }

        Perceptron precy = new Perceptron();

        precy.output(inputs, weights);
    }

}