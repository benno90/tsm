package main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import metaheuristics.AntColony;

import metaheuristics.GreedyInsertion;
import metaheuristics.NearestNeighbour;
import metaheuristics.Pilot;
import metaheuristics.RandomBuilding;
import metaheuristics.Intersection2Opt;
import metaheuristics.Random2Opt;
import metaheuristics.SimulatedAnnealing;
import utils.Point;
import utils.Printer;
import utils.Instance;
import utils.Utils;

public class TestMain {

    public static void main(String[] args) throws IOException {

        runSingleTSPInstance("test1");
        //runSingleTSPInstance("test2");
        //runRandomInstances(20);
    }

    private static void runSingleTSPInstance(String instanceName) throws IOException {


        /* =============================================================*/
        String solutionName = instanceName;

        String pathToInstances = "tsp/TSP_TestInstances";
        String pathToSolutions = "tsp/TSP_TestSolutions";

        String instanceFilenameExtension = ".tsp";
        String solutionFilenameExtension = ".html";

        String pathToInstance = pathToInstances + "/" + instanceName + instanceFilenameExtension;
        String pathToSolution = pathToSolutions + "/" + solutionName + solutionFilenameExtension;

        System.out.println("Loading instance " + instanceName + "...");
        Instance instance = Instance.load(Paths.get(pathToInstance));

        System.out.println("Instance has " + instance.getPoints().size() + " points.");

        System.out.println("Start generating a solution...");

        /* =============================================================*/
        List<Point> solution = null;

        /* =================== testing random2opt ======================*/
        /*Point[] points = instance.getPoints().toArray(new Point[instance.getPoints().size()]);
        Arrays.sort(points);
        System.out.println("loaded case: ");
        for(Point p : points) {
            System.out.print("#" + p.getId() + ", ");
        }
        ArrayList<Point> tour = new ArrayList<>(Arrays.asList(points));
        Random2Opt.run2opt(tour);

        //pathToSolution = pathToSolutions + "/" + solutionName + "_2opt" + solutionFilenameExtension;
        Printer.writeToSVG(instance, tour, Paths.get(pathToSolution));*/
        /* =============================================================*/
        
        
        /* =================== testing annealing =======================*/
        /*Point[] points = instance.getPoints().toArray(new Point[instance.getPoints().size()]);
        //Arrays.sort(points);
        System.out.println("loaded case: ");
        for(Point p : points) {
            System.out.print("#" + p.getId() + ", ");
        }
        ArrayList<Point> tour = new ArrayList<>(Arrays.asList(points));
        SimulatedAnnealing.simulateAnnealing(tour); 

        //pathToSolution = pathToSolutions + "/" + solutionName + "_2opt" + solutionFilenameExtension;
        Printer.writeToSVG(instance, tour, Paths.get(pathToSolution));*/
        /* =============================================================*/
        
        /* ================== testing ant colony =======================*/
        List<Point> tour = AntColony.solve(instance);
        System.out.println("shortest tour: " + utils.Utils.euclideanDistance2D(tour));
        Printer.writeToSVG(instance, tour, Paths.get(pathToSolution));
    }
}
