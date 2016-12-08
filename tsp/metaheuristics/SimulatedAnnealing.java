package metaheuristics;

import java.io.File;
import static java.lang.Math.exp;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import utils.CSVwriter;
import utils.Point;
import utils.Utils;

/**
 *
 * @author benno
 */
public class SimulatedAnnealing {

    // ==================  S E T T I N G S ==================
    
    private static boolean verbose = false;

    // write solutions to csv in tsp/TSP_csv
    private static boolean writeCSV = true;
    private static int interval = 1000;

    // ------------- cooling scheme -----------------
    
    private static double T0 = 420;        // initial temperature
    private static double Tf = 1;           // final temperature
    private static double a = 0.995;        // cooling -> has no effect if alternativeCoolingSchem = true
    
    private static boolean alternativeCoolingScheme = true; // exponential cooling scheme
    private static int targetLoopCount = 4*1000*1000;
    private static int zeroChangeCount = 100*1000;         // # iterations without improvement after T = Tf
    

    // S E T T I N G S
    //
    // berlin52:
    //    T0 = 420 -> based on 0.13 initial acceptance rate (SA_Parameters run with nearestneighbor-initial sol)
    //    Tf = 1 -> exponential cooling scheme
    //    targetLoopCount = 4*10^6
    //    zeroChangeCount = 300*10^3
    // bier127:
    //    T0 = 3750 -> based on 0.13 initial acceptance rate (SA_Parameters run with nearestneighbor-initial sol)
    //    Tf = 1 -> exponential cooling scheme
    //    targetLoopCount = 4*10^6
    //    zeroChangeCount = 300*10^3
    // pr1002:
    //    T0 = 6000 -> based on 0.13 initial acceptance rate (SA_Parameters run with nearestneighbor-initial sol)
    //    Tf = 1 -> exponential cooling scheme
    //    targetLoopCount = 30*10^6
    //    zeroChangeCount = 5*10^7
    // rl6915:
    //    T0 = 7000 -> based on 0.13 initial acceptance rate (SA_Parameters run with nearestneighbor-initial sol)
    //    Tf = 1 -> exponential cooling scheme
    //    targetLoopCount = 10*10^6
    //    zeroChangeCount = 10^6
    // swiss_reseau:
    //    T0 = 1100 -> based on 0.13 initial acceptance rate (SA_Parameters run with nearestneighbor-initial sol)
    //    Tf = 1 -> exponential cooling scheme
    //    targetLoopCount = 10*10^6
    //    zeroChangeCount = 10^6
    // sw24978:
    //    T0 = 4500 -> based on 0.13 initial acceptance rate (SA_Parameters run with nearestneighbor-initial sol)
    //    Tf = 1 -> exponential cooling scheme
    //    targetLoopCount = 20*10^6
    //    zeroChangeCount = 10^6
    //--------------------------------------

    public static List<Point> simulateAnnealing(List<Point> l) {
        return simulateAnnealing(l, "default");
    }

    public static List<Point> simulateAnnealing(List<Point> l, String instanceName) {

        if (writeCSV) {
            CSVwriter.openCSV(new File("tsp/TSP_csv/" + instanceName + ".csv"));
        }

        Random rand = new Random();
        double u;

        // ------------- intialize variables ---------
        LinkedList<Point> tour = new LinkedList<>(l);   // create internal copy -> working tour
        double T = T0;                                  // temperature
        double S = Utils.euclideanDistance2D(l);        // tour length from last accepted solution
        double best_solution = S;                       // best overall tourlength
        LinkedList<Point> s_star = new LinkedList<>(l); // overall best solution
        // -------------------------------------------

        // -------- temporaries ---------------
        double tour_length;                             // length of new tour
        double D;                                       // distance old - new tour length -> if positive: improvement
        // -------------------------------------

        int loopcount = 0;
        int zeroChange = zeroChangeCount;
        while (T > Tf || (zeroChange > 0)) {            // stop loop if T<Tf and zerochange < 0
            print("---------------------------------");
            print("annealing loop #" + loopcount);
            loopcount++;
            print("temperature: " + T);
            
            int[] points = Random2Opt.evaluateNextRandomMove(l);                // evaluate next random move
            tour_length = Random2Opt.evaluateNewTourLength(points, S, tour);    // evaluate next tour_length
            
            D = tour_length - S;
            print("new length: " + tour_length);
            
            if (D < 0) {
                print("better solution, accepted!");
                // always accept improved solution -> execute and save tour_length
                Random2Opt.executeMove(tour, points);
                S = tour_length;
            } else {
                u = rand.nextDouble();
                print("u: " + u);
                print("exp(-D/T): " + exp(-D / T));
                print("D: " + D);
                if (exp(-D / T) > u) {
                    // accept worse solution with some probability -> execute and save tour_length
                    Random2Opt.executeMove(tour, points);
                    print("worse, but accepted!");
                    S = tour_length;
                } else {
                    // do nothing -> tour was not altered
                }
            }
            if (tour_length < best_solution) {
                best_solution = tour_length;
                s_star = new LinkedList<>(tour);                                // copy solution into s_star
                System.out.println("*********************************");
                System.out.println("new overall best tour: " + best_solution);
                System.out.println("iteration #" + loopcount);
                System.out.println("temperature = " + T);
                zeroChange = zeroChangeCount;                                   // reset changeCount
            }
            if (writeCSV && (loopcount % interval) == 0) {
                CSVwriter.write(loopcount, T, S);
            }

            if (alternativeCoolingScheme) {
                T = evaluateT(loopcount);
            } else {
                T = a * T;
            }
            
            zeroChange--;
        }

        if (writeCSV) {
            CSVwriter.closeCSV();
        }
        return s_star;
    }

    private static double evaluateT(int loopNr) {
        // cosine cooling scheme
        //return (Math.cos(loopNr * (Math.PI) / (targetLoopCount)) + 1) * T0 * 0.5; 
        
        // exponential cooling scheme
        return T0*(Math.exp(-5f*loopNr/(targetLoopCount))); // exponential cooling scheme
        
        // exponential superposed with small amplitude sine wave
        //return T0*((Math.exp(-5f*loopNr/(targetLoopCount))) + 0.05*(0.5*(1+Math.sin(Math.PI*20*loopNr/targetLoopCount))));
        
    }

    private static void print(String s) {
        if (verbose) {
            System.out.println(s);
        }
    }
}
