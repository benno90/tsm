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

/**
 *
 * @author benno
 */
public class RandomBuilding {

    public static List<Point> solve(Instance instance) {
        Point[] points = instance.getPoints().toArray(new Point[instance.getPoints().size()]);

        ArrayList<Integer> list = new ArrayList<Integer>();
        System.out.println("points.length: " + points.length);
        for (int i = 0; i < points.length; i++) {
            list.add(i);
        }
        // max index: points.length -1
        

        double tentative_distance = Double.MAX_VALUE;
        System.out.println("start first loop - current tentative distance: " + tentative_distance);
        
        List<Point> tentative_solution = new ArrayList<>(points.length);
        for (int i = 0; i < 10000; i++) {
            java.util.Collections.shuffle(list);
            double tempDist = 0;
            for(int j = 0; j < points.length - 1; j++) {
                tempDist += getDistance(points[list.get(j)], points[list.get(j+1)]);
            }
            // close the loop
            tempDist += getDistance(points[list.get(points.length - 1)], points[list.get(0)]);
            if(tempDist < tentative_distance) {
                System.out.println("loop " + i + " - new shortest path distance: " + tempDist);
                tentative_distance = tempDist;
                tentative_solution.clear();
                for(int k = 0; k < points.length; k++) {
                    tentative_solution.add(points[list.get(k)]);
                }
                tentative_solution.add(points[list.get(0)]);
            }
        }
        
        return tentative_solution;

        /*Arrays.sort(points, (p1, p2) -> Integer.compare(p1.getId(), p2.getId()));

        //Array with the indices of the next nodes
        int[] nextIndices = new int[points.length];

        //Initial partial tour 0 -> 1 -> 0
        nextIndices[0] = 1;

        //Find the best position to insert for each remaining point
        for (int i = 2; i < points.length; i++) {
            double lowestDistanceIncrease = Double.POSITIVE_INFINITY;
            int lowestDistanceIncreaseIdx = -1;

            for (int j = 0; j < i; j++) {
                //Increased cost of tour if point i is inserted in place j
                double distanceIncrease = Utils.euclideanDistance2D(points[j], points[i]) + Utils.euclideanDistance2D(points[i], points[nextIndices[j]]) - Utils.euclideanDistance2D(points[j], points[nextIndices[j]]);
                if (distanceIncrease < lowestDistanceIncrease) {
                    lowestDistanceIncrease = distanceIncrease;
                    lowestDistanceIncreaseIdx = j;
                }
            }

            nextIndices[i] = nextIndices[lowestDistanceIncreaseIdx];
            nextIndices[lowestDistanceIncreaseIdx] = i;
        }

        //Walk along next indices to build solution.
        List<Point> solution = new ArrayList<>(points.length);
        int j = 0;
        for (int i = 0; i < points.length; i++) {
            solution.add(points[j]);
            j = nextIndices[j];
        }

        return solution;*/
    }
    
    private static double getDistance(Point a, Point b) {
        double distX = a.getX() - b.getX();
        double distY = a.getY() - b.getY();
        return Math.sqrt(distX*distX + distY*distY);
    }

}
