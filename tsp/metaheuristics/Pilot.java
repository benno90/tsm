/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metaheuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import utils.Instance;
import utils.Point;
import utils.Utils;
import java.lang.Math;
import java.util.LinkedList;

/**
 *
 * @author benno
 */
public class Pilot {

    public static List<Point> solve(Instance instance) {
        Point[] points = instance.getPoints().toArray(new Point[instance.getPoints().size()]);

        LinkedList<Point> remaining = new LinkedList<>(Arrays.asList(points));
        LinkedList<Point> solution = new LinkedList<>();
        Point start = remaining.removeFirst();
        //System.out.println("start at #" + start.getId());
        solution.addFirst(start);

        Point p; // temporary point
        double tentativeDistance; // tentative lowest distance
        Point tentativePoint = start; // tentative point which leads to shortest tour
        double dist; // temporary distance
        Point last = start; // to calculate first distance

        int numberOfLoops = remaining.size() - 1;
        for (int i = 0; i <= numberOfLoops; i++) {
            //System.out.println("outerloop #" + i);
            //System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++\n");
            tentativeDistance = Double.MAX_VALUE;
            for (int j = 0; j < remaining.size(); j++) {
                //System.out.println("    innerloop #" + j);
                // evaluate the next choice using the pilot heuristic
                p = remaining.removeFirst(); // next point
                //System.out.println("-------------- check point #" + p.getId());
                ArrayList<Point> subList = new ArrayList<>(remaining); // remaining points without sublist
                //System.out.print("points in subtour: ");
                //printList(subList);
                dist = nearestNeighbour(subList, p, start); // get tour-distance starting at p and ending at 'start'
                dist += getDistance(last, p); // add distance from last point in solution to p
                //System.out.println("distance: " + dist);
                remaining.addLast(p); // append point p again.
                if (dist < tentativeDistance) {
                    tentativeDistance = dist;
                    tentativePoint = p;
                }
            }
            //System.out.println("point with shortest distance: #" + tentativePoint.getId());
            //System.out.print("current solution: ");
            solution.addLast(tentativePoint);
            last = tentativePoint;
            //printList(solution);
            remaining.remove(tentativePoint);
        }

        return solution;
    }

    public static double testNearestNeighbour(Instance instance) {
        Point[] points = instance.getPoints().toArray(new Point[instance.getPoints().size()]);
        ArrayList<Point> list = new ArrayList<>(Arrays.asList(points));
        Point start = list.remove(0);
        Point end = start;
        return nearestNeighbour(list, start, end);
    }

    /**
     * Returns the total distance of a subtour that starts at 'startPoint' and
     * ends at 'entPoint' and visits all points in the points list.
     *
     * @param points - list of points to visit
     * @param startPoint
     * @param endPoint
     * @return distance of subtour
     */
    private static double nearestNeighbour(List<Point> points, Point startPoint, Point endPoint) {
        double tentative_distance; // to evaluate nearest neighbour
        double cummulative_distance = 0; // cummulative distance of tour
        double dist; // temporary variable
        Point current = startPoint;
        //System.out.println("start at: #" + current.getId());

        int indexToRemove = 0;
        int numberOfLoops = points.size() - 1;

        for (int i = 0; i <= numberOfLoops; i++) {
            //System.out.println("outerloop #" + i);
            tentative_distance = Double.MAX_VALUE;
            // evaluate nearest neighbour
            for (int j = 0; j < points.size(); j++) {
                dist = getDistance(points.get(j), current);
                if (dist < tentative_distance) {
                    tentative_distance = dist;
                    indexToRemove = j;
                }
            }
            //System.out.println("next point: #" + points.get(indexToRemove).getId());
            cummulative_distance += getDistance(points.get(indexToRemove), current);
            current = points.remove(indexToRemove);
        }
        // add distance to endpoint
        cummulative_distance += getDistance(current, endPoint);
        return cummulative_distance;
    }

    private static double getDistance(Point a, Point b) {
        double distX = a.getX() - b.getX();
        double distY = a.getY() - b.getY();
        return Math.sqrt(distX * distX + distY * distY);
    }

    private static void printList(List<Point> l) {
        for (Point p : l) {
            System.out.print(p.getId() + ", ");
        }
        System.out.println("");
    }

}
