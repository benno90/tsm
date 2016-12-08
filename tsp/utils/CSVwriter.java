/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ashraf
 *
 */
public class CSVwriter {

    //Delimiter used in CSV file
    private static final String DELIMITER = ",";
    private static final String NEW_LINE = "\n";

    //CSV file header
    private static final String FILE_HEADER = "#iteration, temperature, tour_length";

    private static PrintWriter writer;

    public static void openCSV(File file) {

        try {
            writer = new PrintWriter(file);
            //Write the CSV file header
            writer.append(FILE_HEADER);

            //Add a new line separator after the header
            writer.append(NEW_LINE);
            writer.flush();

        } catch (Exception e) {
            System.out.println("------------- error in openCSV --------------");
            e.printStackTrace();
        }
    }

    public static void closeCSV() {
        try {
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("------------- error in closeCSV -----------------");
            e.printStackTrace();
        }
    }

    public static void write(int iteration, double temperature, double tour_length) {
        try {
            writer.printf("%d\t%.2f\t%.2f%n", iteration, temperature, tour_length);
            /*writer.print(iteration);
            writer.print(DELIMITER);
            writer.print(temperature);
            writer.print(DELIMITER);
            writer.print(tour_length);
            writer.print(DELIMITER);
            writer.print(NEW_LINE);*/
            writer.flush();
        } catch (Exception e) {
            System.out.println("------------- error in writeCSV -----------------");
            e.printStackTrace();

        }
    }
    
    public static void main(String[] args)  {
        File file = new File("tsp/TSP_csv/test.csv");
        CSVwriter.openCSV(file);
        CSVwriter.write(3, 10, 10);
        CSVwriter.write(5, 13, 20);
        CSVwriter.write(7, 23, 30);
        CSVwriter.write(9, 44, 40);
        CSVwriter.write(11, 55, 50);
        CSVwriter.closeCSV();
    }
}
