/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.IOException;
import static java.lang.Math.abs;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import metaheuristics.NearestNeighbour;
import metaheuristics.Random2Opt;
import metaheuristics.RandomBuilding;
import metaheuristics.RandomizedNearestNeighbour;
import metaheuristics.SimulatedAnnealing;

/**
 *
 * @author benno
 */
public class SA_Parameters {

    public static void main(String[] args) throws IOException {

        //evaluateParameters("benno");
        //evaluateParameters("berlin52");
        //evaluateParameters("bier127");
        //evaluateParameters("tsp225");
        //evaluateParameters("pr1002");
        //evaluateParameters("pr2392");
        //evaluateParameters("reseau_suisse");
        evaluateParameters("rl5915");
        //evaluateParameters("sw24978");
    }

    private static void evaluateParameters(String instanceName) throws IOException {


        /* =============================================================*/
        String solutionName = instanceName;

        String pathToInstances = "tsp/TSP_Instances";

        String instanceFilenameExtension = ".tsp";

        String pathToInstance = pathToInstances + "/" + instanceName + instanceFilenameExtension;

        System.out.println("Loading instance " + instanceName + "...");
        Instance instance = Instance.load(Paths.get(pathToInstance));

        System.out.println("Instance has " + instance.getPoints().size() + " points.");

        /* ============================================================*/
        /* =================== evaluate parameters =======================*/
        int N = 1000;
        
        
        /* =================================================================*/
        List<Point> solution = NearestNeighbour.solve(instance);
        //List<Point> solution = RandomizedNearestNeighbour.solve(instance);
        //List<Point> solution = RandomBuilding.solve(instance);
        //System.out.println("generated nearest neighbour solution");
        List<Point> randomSol;
        List<Double> tourLenghts = new ArrayList<>();
        double originalLength = Utils.euclideanDistance2D(solution);
        System.out.println("length: " + originalLength);

        //======== progress bar =========/
        int pc = N / 50;

        for (int i = 0; i < N; i++) {
            if (i % pc == 0) {
                System.out.write('|');
                System.out.flush();
            }
            randomSol = Random2Opt.run2opt(new LinkedList<Point>(solution));
            tourLenghts.add(Utils.euclideanDistance2D(randomSol));
            //System.out.println(tourLenghts.get(i));
        }
        System.out.println("");

        double meanChange = 0;
        for (int i = 0; i < N; i++) {
            meanChange += abs(originalLength - tourLenghts.get(i));
        }
        meanChange = meanChange / N;
        // settings that produced good results

        double initialAcceptance = 0.13;
        double startTemp = -meanChange / Math.log(initialAcceptance);
        double endAcceptance = 0.000001;
        double endTemp = -meanChange / Math.log(endAcceptance);

        System.out.println("mean change delta = " + meanChange);
        System.out.println("initialTemp = " + startTemp);
        System.out.println("endTemp = " + endTemp);

    }

}
