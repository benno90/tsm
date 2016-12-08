package metaheuristics;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import utils.CSVwriter;
import utils.Instance;
import utils.Matrix;
import utils.Point;
import utils.TourWrapper;

/**
 *
 * @author benno
 */
public class AntColony {

    static boolean writeCSV = true;
    static int verbosity_level = 1;  // 0 - 2

    public static List<Point> solve(Instance instance, String instanceName) throws Error {
        Point[] points = instance.getPoints().toArray(new Point[instance.getPoints().size()]);
        // sort by index
        Arrays.sort(points);
        final ArrayList<Point> originalList = new ArrayList<>(Arrays.asList(points));
        final int tourSize = originalList.size();

        // check for correct id's
        // the algorithm depends on ordered id's from 0 to points.length -1
        if (originalList.get(0).getId() != 0 || originalList.get(points.length - 1).getId() != (points.length - 1)) {
            throw new Error("Index Error: \n"
                    + "id of first point: " + originalList.get(0).getId() + "\n"
                    + "id of last point: " + originalList.get(points.length - 1).getId());
        }

        if (writeCSV) {
            CSVwriter.openCSV(new File("tsp/TSP_csv/" + instanceName + "_ac.csv"));
        }

        // =========== P A R A M E T E R ===============
        // ----------------------------
        final int numberOfThreads = 1;              // multi-threading
        final int iterations = 200;                 // number of outer loop iterations
        final int pheromoneCheck = 10;               // frequency print min/max pheromone level
        final int zeroChangeCount = 100;              // main loop if solution does not imporve
        //-----------------------------

        //-----------------------------
        final double tau0 = 1;                      // initial pheromone value
        final int numberOfAnts = 500;
        final boolean eliteAntsHeuristic = true;    // only best ants deposit pheromone
        final int numberOfEliteAnts = 10;
        final double alpha = 1;                     // pheromone exponent
        final double beta = 5;                      // distance exponent
        final double rho = 0.3;                     // 
        final double maxF = 50;                     // max pheromone level
        final double minF = 0.04;                  // min pheromone level
        final boolean pheromoneBoundingScheme = true;     // decease min pheromone level from tau0 to minF continuously
        final boolean pheromoneReHeat = true;             // temporary increase of min pheromone level for some time
        final double Q = 7500 * 4 / numberOfEliteAnts; // -> nearest neighbour or optimal solution length
        // by choosing Q = optimal solution length the upper pheromone value  automatically bounded by 1/(rho)*1 = 1/rho
        // because max(Q/L) = 1 -> L = Q -> ONLY TRUE FOR ONE ANT
        // chose Q = optimalsolution*K/numberOfAnts -> upperbound for pheromone value ~ K/rho
        // =============================================

        if ((numberOfAnts % numberOfThreads) != 0) {
            throw new Error("numberOfAnts must be a multiple of numberOfThreads");
        }

        //
        // =========== V A R I A B L E S ===============
        final Matrix D = new Matrix(originalList);          // Distance Matrix
        print("distance matrix D initialized:", 2);
        print(D, 2);
        Matrix T = new Matrix(originalList.size(), tau0);   // Feromone Matrix
        print("feromone matrix T initilaited: ", 2);
        print(T, 2);
        Matrix R = new Matrix(originalList.size(), 0);      // Reinforcement Matrix

        ArrayList<Point> bestSolution = new ArrayList<>();
        double shortestTour = Double.MAX_VALUE;
        double longestTourOfIteration = 0;

        List<TourWrapper> eliteList = new ArrayList<>(numberOfEliteAnts);   // keep track of best tours
        double longestTourInEliteList = 0;  // keep track of longest tour in list

        int zeroChange = zeroChangeCount;
        boolean improved = false;
        int c = 0;

        // reheat scheme ----------------------
        double[] reHeat = getReheatVector(iterations, pheromoneReHeat);

        // =============================================
        while ((c < iterations) || (zeroChange > 0)) {
            c++;
            //for (int c = 0; c < iterations; c++) {
            print("============================== main loop #" + c, 1);
            R.reset();              // reset reinforcement matrix
            eliteList.clear();      // take best ants of each iteration
            longestTourOfIteration = 0;     // keep track of longest tour for monitoring purposes
            for (int k = 0; k < numberOfAnts / numberOfThreads; k++) {

                Ant[] ants = new Ant[numberOfThreads];
                Thread[] threads = new Thread[numberOfThreads];
                // create ants and start threads
                for (int i = 0; i < numberOfThreads; i++) {
                    ants[i] = new Ant(originalList, T, D, alpha, beta, i);
                    threads[i] = new Thread(ants[i]);
                    threads[i].start();
                }
                // wait for all threads to finish
                for (int i = 0; i < numberOfThreads; i++) {
                    try {
                        threads[i].join();
                    } catch (InterruptedException ex) {
                        System.err.println("ant was interupted: ");
                    }
                }

                improved = false;
                // evalute the solutions
                for (int i = 0; i < numberOfThreads; i++) {
                    double tourLengthOfAntK = ants[i].getTourLength();
                    List<Point> solutionOfAntK = ants[i].getTour();

                    print("tour length of ant #" + k + ": " + tourLengthOfAntK, 2);
                    //print("tour length of ant #" + k + ": " + utils.Utils.euclideanDistance2D(solutionOfAntK), 2);
                    if (eliteAntsHeuristic) {
                        // fill elite List
                        if (eliteList.size() < numberOfEliteAnts) {
                            print("eliteHeuristic - fillMode: tour of length " + tourLengthOfAntK + " added to eliteList", 2);
                            TourWrapper tw = new TourWrapper(solutionOfAntK, tourLengthOfAntK);
                            eliteList.add(tw);
                            Collections.sort(eliteList);
                            if (tourLengthOfAntK > longestTourInEliteList) {
                                longestTourInEliteList = tourLengthOfAntK;
                            }
                        } else if (tourLengthOfAntK < longestTourInEliteList) {
                            print(" ", 2);
                            print("eliteHeuristic - normalMode: tour of length " + tourLengthOfAntK + " added to eliteList", 2);
                            eliteList.remove(numberOfEliteAnts - 1);
                            TourWrapper tw = new TourWrapper(solutionOfAntK, tourLengthOfAntK);
                            eliteList.add(tw);
                            Collections.sort(eliteList);
                            longestTourInEliteList = eliteList.get(numberOfEliteAnts - 1).getTourLength();
                            print("longest tour in list: " + longestTourInEliteList, 2);
                            //print("currentList:", 2);
                            //for (int i = 0; i < numberOfEliteAnts; i++) {
                            //    print("#" + i + ": " + eliteList.get(i).getTourLength(), 2);
                            //}
                        }
                    } else {
                        // always update reinforcement matrix
                        print("update reinfocement matrix", 2);
                        for (int n = 0; n < tourSize - 2; n++) {
                            int j = n + 1;
                            int x = solutionOfAntK.get(n).getId();
                            int y = solutionOfAntK.get(n + 1).getId();
                            R.set(x, y, R.get(x, y) + (Q / tourLengthOfAntK));
                        }
                        print(R, 2);
                    }
                    if (tourLengthOfAntK < shortestTour) {
                        print("********* new overall best solution **************", 1);
                        print("length: " + tourLengthOfAntK, 1);
                        print("outer loop #" + c, 1);
                        shortestTour = tourLengthOfAntK;
                        bestSolution = new ArrayList<>(solutionOfAntK);
                        improved = true;
                        zeroChange = zeroChangeCount;
                    } 

                    if (tourLengthOfAntK > longestTourOfIteration) {
                        longestTourOfIteration = tourLengthOfAntK;
                    }

                }
            }
            if (eliteAntsHeuristic) {
                // update reinforcement matrix for elite ants
                for (int k = 0; k < numberOfEliteAnts; k++) {
                    List<Point> solOfAntK = eliteList.get(k).getTour();
                    double tourLOfAntK = eliteList.get(k).getTourLength();
                    for (int i = 0; i < tourSize - 2; i++) {
                        int j = i + 1;
                        int x = solOfAntK.get(i).getId();
                        int y = solOfAntK.get(i + 1).getId();
                        R.set(x, y, R.get(x, y) + (Q / tourLOfAntK));
                    }
                }

            }
            print("update pheromone trail", 2);
            // update pheromone trail
            T.multiply(1 - rho);
            T.add(R);
            if (pheromoneBoundingScheme) {
                T.bound(maxF, getLowerFeromoneBound(iterations, c, minF, tau0, reHeat));
            } else {
                T.bound(maxF, minF);
            }
            print(T, 2);

            if (c % pheromoneCheck == 0) {
                double[] minmax = T.getMaxMinValue();
                System.out.println("------------------------ pheromone check - loop #" + c);
                print("max(T) = " + minmax[0], 0);
                print("min(T) = " + minmax[1], 0);
            }

            if (writeCSV) {
                CSVwriter.write(c, shortestTour, longestTourOfIteration);
            }

            if ((c >= iterations) && !improved) {
                zeroChange--;
            } else {
            }
        }

        if (writeCSV) {
            CSVwriter.closeCSV();
        }

        return bestSolution;

    }

    private static double getLowerFeromoneBound(int totLoop, int loopNr, double minF, double initialF, double[] rH) {
        double reHeat = 0.0;
        if (loopNr < totLoop) {
            reHeat = rH[loopNr];
        }
        // cosine scheme
        //return 0.5 * (initialF - minF) * (Math.cos(Math.PI * loopNr / totLoop) + 1) + minF;
        // exponential scheme
        return (initialF - minF) * Math.exp(-10 * loopNr / totLoop) + minF + reHeat;

    }

    public static int getNextPoint(double[] imaginaryDistances, double imaginaryDistance) {
        Random rand = new Random();

        /*System.out.println("some imaginaryDistances: ");
        int a = imaginaryDistances.length >= 3 ? 3 : imaginaryDistances.length;
        for(int i = 0; i < a; i++) {
            System.out.print(imaginaryDistances[i] + " ");
        }*/
        double randomDistance = rand.nextDouble() * imaginaryDistance;
        double dummyDistance = 0;
        int index = 0;
        while (dummyDistance < randomDistance) {
            dummyDistance += imaginaryDistances[index];
            index++;
        }
        index--;
        if (index == -1) {
            return 0;
        } else {
            return index;
        }
    }

    static void print(String s, int level) {
        if (level <= verbosity_level) {
            System.out.println(s);
        }
    }

    static void print(Matrix m, int level) {
        if (level <= verbosity_level) {
            m.print();
        }
    }

    public static List<Point> solve(Instance instance) throws Error {
        return solve(instance, "default");
    }

    private static double[] getReheatVector(int iterations, boolean pheromoneReHeat) {
        int loops = iterations;
        int tau = loops / 5;
        double factor = 0.25;

        double[] reHeat = new double[iterations];

        for (int i = 0; i < loops; i++) {
            reHeat[i] = 0.0;
        }
        if (pheromoneReHeat) {
            for (int i = 4 * tau; i < 5 * tau; i++) {
                reHeat[i] = factor * 0.5 * (1 + Math.sin((double) i / (tau) * 2 * Math.PI - 4.5 * Math.PI));
            }
        }
        return reHeat;
    }

}
