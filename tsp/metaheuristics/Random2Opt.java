/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metaheuristics;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import utils.Point;
import utils.Utils;

/**
 *
 * @author benno
 */
public class Random2Opt {

    private static boolean verbose = false;

    private static void print(String s) {
        if (verbose) {
            System.out.println(s);
        }
    }

    public static List<Point> run2opt(List<Point> tour) {
        int[] lines = evaluateNextRandomMove(tour);
        double newTourLength = evaluateNewTourLength(lines, Utils.euclideanDistance2D(tour), tour);
        //System.out.println("tourlength1: " + newTourLength);
        return executeMove(tour, lines);
    }

    /**
     * Evaluate the new distance after a 2opt move without having to traverse the whole tour.
     * @param points
     * @param tourlength
     * @param tour
     * @return 
     */
    public static double evaluateNewTourLength(final int[] points, final double tourlength, final List<Point> tour) {
        Point A1 = tour.get(points[0]);
        Point A2 = tour.get(points[1]);
        Point B1 = tour.get(points[2]);
        Point B2 = tour.get(points[3]);
        double oldDist = Utils.euclideanDistance2D(A1, A2) + Utils.euclideanDistance2D(B1, B2);
        double newDist = Utils.euclideanDistance2D(A1, B1) + Utils.euclideanDistance2D(A2, B2);
        return tourlength - oldDist + newDist;
    }

    
    /**
     * Evaluate a random 2opt move
     * @param tour 
     * @return array with four points representing the two edges that should be flipped.
     */
    public static int[] evaluateNextRandomMove(final List<Point> tour) {

        Random rand = new Random();
        
        int a1 = 2; int a2 = 3; int b1 = 4; int b2 = 5;

        // evaluate random segments
        boolean condition = true;
        while (condition) {
            a1 = rand.nextInt(tour.size() - 3);
            print("a1 = " + a1);
            b1 = rand.nextInt(tour.size() - 3) + 2;
            print("b1 = " + b1);
            if ((b1 - a1) >= 2) {
                condition = false;
            }
        }
        
        a2 = a1 + 1;
        b2 = b1 + 1;
        print("switching line " + a1 + ", " + a2 + "  with  " + b1 + ", " + b2);
        int[] points = new int[4];
        points[0] = a1;
        points[1] = a2;
        points[2] = b1;
        points[3] = b2;
        return points;
    }

    public static List<Point> executeMove(List<Point> tour, final int[] points) {

        int a1 = points[0];
        int a2 = points[1];
        int b1 = points[2];
        int b2 = points[3];

        ListIterator<Point> itB = tour.listIterator(b2 + 1);
        ListIterator<Point> itA = tour.listIterator(a2 + 1);

        Point B2 = itB.previous();
        Point B1 = itB.previous();

        print("B2 set to #" + B2.getId() + ", B1 set to #" + B1.getId());

        Point A2 = itA.previous();
        Point A1 = itA.previous();

        print("A2 set to #" + A2.getId() + ", A1 set to #" + A1.getId());

        LinkedList<Point> reverseList = new LinkedList<>();
        // itB now points to B1;
        Point x = itB.previous();
        // fill reverseList
        while (!x.equals(A2)) {
            reverseList.addLast(x);
            print("added to reverseList: " + x.getId());
            x = itB.previous();
        }

        print("reverselist:  ");
        for (Point p : reverseList) {
            print("#" + p.getId() + ", ");
        }
        print("");
        // itB now points to A2
        //print("B1: " + B1.getId());
        itB.set(B1); // -> next point after A1 -> B1
        ListIterator<Point> reverseIt = reverseList.listIterator();
        itB.next();
        while (reverseIt.hasNext()) {
            itB.next();
            itB.set(reverseIt.next());

        }
        itB.next();
        // the next point must be A2
        itB.set(A2);
        //print("new tour:  ");
        //for (Point p : tour) {
        //    print("#" + p.getId() + ", ");
        //}
        //            print("new tour length: " + Utils.euclideanDistance2D(tour));
        return tour;

    }

}
