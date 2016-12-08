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
import java.util.ListIterator;
import utils.FixedSizeStack;

/**
 *
 * @author benno
 */
public class RandomizedNearestNeighbour {
    
    private static Random rand;

    public static List<Point> solve(Instance instance) {
        Point[] points = instance.getPoints().toArray(new Point[instance.getPoints().size()]);
        ArrayList<Point> remaining = new ArrayList<>(Arrays.asList(points));
        ArrayList<Point> solution = new ArrayList<>(points.length);
        
        
        /* check for duplicates */
        //  Utils.checkDuplicates(remaining);
        
        //----------- random settings -------------
        FixedSizeStack<Integer> stack = new FixedSizeStack(2);   // store the closest neighbors
        rand = new Random();

        Point start = remaining.remove(0);
        solution.add(start);
        //System.out.println("start point #id: " + start.getId());

        double tentative_distance;
        double dist;
        Point current;
        int indexToRemove = 0;
        int numberOfLoops = remaining.size() - 1;
        for (int i = 0; i <= numberOfLoops; i++) {
            //System.out.println("outerloop #" + i);
            tentative_distance = Double.MAX_VALUE;
            current = solution.get(i);
            stack.clear();
            for (int j = 0; j < remaining.size(); j++) {
                dist = getDistance(remaining.get(j), current);
                if (dist < tentative_distance) {
                    tentative_distance = dist;
                    //indexToRemove = j;
                    stack.push(j);
                }
            }
            indexToRemove = evaluateIndexToRemove(stack);
            solution.add(remaining.remove(indexToRemove));
        }

        return solution;
    }
    
    private static int evaluateIndexToRemove(FixedSizeStack<Integer> stack) {
        //System.out.println("number of potential neighbors: " + stack.getSize());
        int index = rand.nextInt(stack.getSize());
        //System.out.println("chosen neighbor: " + index);
        return stack.get(index);
        //return stack.get(rand.nextInt(stack.getSize()));
    }
    private static double getDistance(Point a, Point b) {
        double distX = a.getX() - b.getX();
        double distY = a.getY() - b.getY();
        return Math.sqrt(distX * distX + distY * distY);
    }

}
