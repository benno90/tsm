package main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import metaheuristics.AntColony;

import metaheuristics.GreedyInsertion;
import metaheuristics.NearestNeighbour;
import metaheuristics.Pilot;
import metaheuristics.RandomBuilding;
import metaheuristics.Intersection2Opt;
import metaheuristics.RandomizedNearestNeighbour;
import metaheuristics.SimulatedAnnealing;
import utils.Point;
import utils.Printer;
import utils.Instance;
import utils.Utils;

public class DemoMain {

    public static void main(String[] args) throws IOException {

        //runSingleTSPInstance("benno");
        runSingleTSPInstance("berlin52");
        //runSingleTSPInstance("bier127");
        //runSingleTSPInstance("tsp225");
        //runSingleTSPInstance("pr1002");
        //runSingleTSPInstance("pr2392");
        //runSingleTSPInstance("reseau_suisse");
        //runSingleTSPInstance("rl5915");
        //runSingleTSPInstance("sw24978");

        //runRandomInstances(20);
    }

    private static void runSingleTSPInstance(String instanceName) throws IOException {

        /* =============================================================*/
        // CHOOSE ALGORITHM: (greedy, nearest, pilot, random);
        //String alg = "greedy";
        //String alg = "nearest";
        //String alg = "pilot";
        //String alg = "random";
        //String alg = "randomizedNearest";
        String alg = "ant";

        /**
         * ***** 2opt *******
         */
        boolean twoOptheuristic = false;
        int max2opt_iterations = 500000;

        /**
         * ***** annealing ****
         */
        boolean simulatdAnnealing = false;
        int maxAnnealing_interations = 1;

        /* =============================================================*/
        String solutionName = instanceName;

        String pathToInstances = "tsp/TSP_Instances";
        String pathToSolutions = "tsp/TSP_Solutions";

        String instanceFilenameExtension = ".tsp";
        String solutionFilenameExtension = ".html";

        String pathToInstance = pathToInstances + "/" + instanceName + instanceFilenameExtension;
        String pathToSolution = pathToSolutions + "/" + solutionName + solutionFilenameExtension;

        System.out.println("Loading instance " + instanceName + "...");
        Instance instance = Instance.load(Paths.get(pathToInstance));

        System.out.println("Instance has " + instance.getPoints().size() + " points.");

        System.out.println("Start generating a solution...");
        System.out.println("Initial solution generated with: " + alg);
        if (twoOptheuristic) {
            System.out.println("2opt improvement: " + twoOptheuristic);
            System.out.println("max 2Opt iterations: " + max2opt_iterations);
        }
        if (simulatdAnnealing) {
            System.out.println("simulated annealing: " + simulatdAnnealing);
            System.out.println("#outer loop iterations: " + maxAnnealing_interations);
        }

        List<Point> solution = null;
        //List<Point> randomSolution = null;
        //List<Point> nearestNeighbourSol = null;
        //List<Point> pilotSolution = null;

        switch (alg) {
            case "nearest":
                solution = NearestNeighbour.solve(instance);
                break;
            case "greedy":
                solution = GreedyInsertion.solve(instance);
                break;
            case "pilot":
                solution = Pilot.solve(instance);
                break;
            case "random":
                solution = RandomBuilding.solve(instance);
                break;
            case "randomizedNearest":
                solution = RandomizedNearestNeighbour.solve(instance);
                break;
            case "ant":
                try {
                solution = AntColony.solve(instance,instanceName);
                } catch (Error e) {
                    System.out.println(e);
                }
                break;
        }

        /*System.out.println("greedy:");
        for (Point p : greedySolution) {
            System.out.println("id: " + p.getId());
        }
        System.out.println("random:");
        for (Point p : randomSolution) {
            System.out.println("id: " + p.getId());
        }
        System.out.println("nearestneighbour");
        for (Point p : nearestNeighbourSol) {
            System.out.println("id: " + p.getId());
        }*/
 /*          test pilot nearest neighbout subroutine         */
        //System.out.println("test pilot neares neighbout implementation: ");
        //System.out.println("evaluated distance: " + Pilot.testNearestNeighbour(instance));
        switch (alg) {
            case "nearest":
                System.out.println("NN Solution for " + instanceName + " has length: " + Utils.euclideanDistance2D(solution));
                break;
            case "greedy":
                System.out.println("Greedy Solution for " + instanceName + " has length: " + Utils.euclideanDistance2D(solution));
                break;
            case "pilot":
                System.out.println("Pilot Solution for " + instanceName + " has length: " + Utils.euclideanDistance2D(solution));
                break;
            case "random":
                System.out.println("Random Solution for " + instanceName + " has length: " + Utils.euclideanDistance2D(solution));
                break;
            case "randomizedNearest":
                System.out.println("Randomized NN Solution for " + instanceName + " has length: " + Utils.euclideanDistance2D(solution));
            case "ant":
                System.out.println("Ant Colony Solution for " + instanceName + " has length: " + Utils.euclideanDistance2D(solution));
        }

        // Generate Visualization of Result, will be stored in directory pathToSolutions
        Printer.writeToSVG(instance, solution, Paths.get(pathToSolution));

        if (simulatdAnnealing) {
            List<Point> bestSolution = null;
            List<Point> currentSolution = solution;
            double shortestTour = Double.MAX_VALUE;
            double currentTour = 0;
            for (int i = 0; i < maxAnnealing_interations; i++) {
                System.out.println("outerloop #" + i);
                currentSolution = SimulatedAnnealing.simulateAnnealing(currentSolution, instanceName);
                currentTour = Utils.euclideanDistance2D(currentSolution);
                if (currentTour < shortestTour) {
                    shortestTour = currentTour;
                    bestSolution = new LinkedList<>(currentSolution); // copy solution
                }
            }
            System.out.println("tour length after simulated annealing: " + shortestTour);
            System.out.println("number of points in tour: " + bestSolution.size());
            pathToSolution = pathToSolutions + "/" + solutionName + "_sa" + solutionFilenameExtension;
            Printer.writeToSVG(instance, bestSolution, Paths.get(pathToSolution));
            solution = bestSolution;
        }

        if (twoOptheuristic) {
            int count = 0;
            double oldDist = 0;
            double newDist = 0;
            while ((Intersection2Opt.run2opt(solution) != null) && count < max2opt_iterations) {
                newDist = Utils.euclideanDistance2D(solution);
                System.out.println("\n====================================================\n");
                System.out.println("2opt loop " + count + " --  new distance: " + newDist);
                if (newDist == oldDist) {
                    //break;
                }
                oldDist = newDist;
                count++;
                continue;
            }
            System.out.println("tour length after 2Opt: " + Utils.euclideanDistance2D(solution));
            System.out.println("number of points in tour: " + solution.size());
            pathToSolution = pathToSolutions + "/" + solutionName + "_2opt" + solutionFilenameExtension;
            Printer.writeToSVG(instance, solution, Paths.get(pathToSolution));
        }

    }

    private static void runRandomInstances(int numberOfInstances) throws IOException {
        //Run algorithms on some randomly generated instances

        String pathToSolutions = "RandomInstances_Solutions";
        List<Point> solution = null;

        System.out.println("Generating and solving " + numberOfInstances + " random TSP instances...");
        for (int i = 0; i < numberOfInstances; i++) {

            //Generate a random instance.
            int numPoints = 100 * i + 50;
            Instance randomInstance = Utils.getRandomTspInstance("" + i, "random instance with " + numPoints + " points.", numPoints);
            System.out.println("   Random instance number " + i + " generated with " + numPoints + " points.");

            //Get a random path (most likely quite long)
            solution = Utils.getAllPermutations(randomInstance.getPoints()).next();
            Printer.writeToSVG(randomInstance, solution, Paths.get(pathToSolutions, i + "_randomPath.html"));
            System.out.println("        Random Path has length: " + Utils.euclideanDistance2D(solution));

//			//Get the solution obtained with the nearest neighbor heuristic.
//			solution = NearestNeighbor.solve(randomInstance);
//			Printer.writeToSVG(randomInstance, solution, Paths.get(pathToSolutions, i + "_nearest.html"));
//			System.out.println("        Nearest Neighbor solution has length: " + Utils.euclideanDistance2D(solution));
            //Get the solution obtained with the greedy insertion heuristic.
            solution = GreedyInsertion.solve(randomInstance);
            Printer.writeToSVG(randomInstance, solution, Paths.get(pathToSolutions, i + "_greedy.html"));
            System.out.println("        Greedy Insertion solution has length: " + Utils.euclideanDistance2D(solution));

            System.out.println();

        }
    }

}
