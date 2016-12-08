/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metaheuristics;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import utils.Point;
import utils.Utils;

/**
 *
 * @author benno
 */
public class Intersection2Opt {

    private static boolean verbose = true;

    private static void print(String s) {
        if (verbose) {
            System.out.println(s);
        }
    }

    public static List<Point> run2opt(List<Point> tour) {
        //System.out.println("%%%%%%%%%% run2opt entered %%%%%%%%%%%");
        ListIterator<Point> itA = tour.listIterator();
        ListIterator<Point> itB = null;
        Point A1 = null;
        Point A2 = itA.next();
        //print("A2 initialized to: #" + A2.getId());
        Point B1 = null;
        Point B2 = null;
        int position = 2;
        boolean quit = false;
        boolean found = false;
        while (itA.hasNext() && position < tour.size() - 2) {
            A1 = A2;
            //print("A1 set to: #" + A1.getId());
            A2 = itA.next();
            //print("A2 set to: #" + A2.getId());
            itB = tour.listIterator(position); // itB is ahead of itA by one element
            if (itB.hasNext()) {
                B2 = itB.next();
            } else {
                break;
            }
            while (itB.hasNext()) {
                B1 = B2;
                //print("B1 set to: #" + B1.getId());
                B2 = itB.next();
                //print("B2 set to: #" + B2.getId());
                if (intersect(A1, A2, B1, B2)) {
                    quit = true;
                    found = true;
                    break;
                }
            }
            if (quit) {
                break;
            }
            position++;
        } 
        
        // check last line intersection

        if (found) {

            //print("intersection found!");
            print("line-sgment #" + A1.getId() + " #" + A2.getId() + "  intersects #" + B1.getId() + " #" + B2.getId());
            //print("current tour length: " + Utils.euclideanDistance2D(tour));
            // intersection found
            // two options:
            // 1: replace B2 & A1
            // 2: replace A2 & B1

            Point dummy;
            dummy = itB.previous();
            //print("itB.previous(): " + dummy.getId());
            dummy = itA.previous();
            //print("itA.previous(): " + dummy.getId());
            // itB now points to B2;
            // itA now points to A2;

            LinkedList<Point> reverseList = new LinkedList<>();
            itB.previous(); // itB now points to B1;
            Point x = itB.previous();
            // fill reverseList
            while (!x.equals(A2)) {
                reverseList.addLast(x);
                //print("added to reverseList: " + x.getId());
                x = itB.previous();
            }

            //print("reverselist:  ");
            //for (Point p : reverseList) {
                //print("#" + p.getId() + ", ");
            //}
            //System.out.println("");

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
            
        } else {
            System.out.println("loop terminated without finding any intersection");
            return null;
        }

    }

    public static boolean intersect(Point a1, Point a2, Point b1, Point b2) {

        Line2D a = new Line2D.Double(a1.getX(), a1.getY(), a2.getX(), a2.getY());
        Line2D b = new Line2D.Double(b1.getX(), b1.getY(), b2.getX(), b2.getY());

        if (a.intersectsLine(b)) {
            Point p1 = new Point(0, a2.getX() - a1.getX(), a2.getY() - a1.getY());
            Point p2 = new Point(0, b2.getX() - b1.getX(), b2.getY() - b1.getY());
            /*if (crossp(p1, p2) == 0) {
                System.out.println("parallel segments found ! -> no intersection .......");
                return false;
            } else {
                return true;
            }*/
            return true;

        } else {
            return false;
        }
    }

    public static void main(String[] args) {

        // testing intersect
        /*
        Point a1 = new Point(0, 0, 0);
        Point a2 = new Point(0, 1, 1);
        Point b1 = new Point(0, 0, 1);
        Point b2 = new Point(0, 1, 0);

        System.out.println("test1  " + intersect(a1, a2, b1, b2));
        
        a1 = new Point(0, 0, 0);
        a2 = new Point(0, 2, 0);
        b1 = new Point(0, 0, 1);
        b2 = new Point(0, 0, 3);

        System.out.println("test2  " + intersect(a1, a2, b1, b2));

        a1 = new Point(0, 0, 1);
        a2 = new Point(0, 0, 2);
        b1 = new Point(0, 0, 0);
        b2 = new Point(0, 0, 3);

        System.out.println("test3  " + intersect(a1, a2, b1, b2));
        
        a1 = new Point(0, 0, 0);
        a2 = new Point(0, 1, 1);
        b1 = new Point(0, 0, 0);
        b2 = new Point(0, 0, 3);

        System.out.println("test3  " + intersect(a1, a2, b1, b2));
        */
        // testing twoopt
        ArrayList<Point> list = new ArrayList<>();
        list.add(new Point(0, 0, 2));
        list.add(new Point(1, 1, 1));
        list.add(new Point(2, 1, 3));
        list.add(new Point(3, 2, 1));
        list.add(new Point(4, 2, 3));
        list.add(new Point(5, 1, 0));
        run2opt(list);

        System.out.print("new order: ");
        for (Point p : list) {
            System.out.print(p.getId() + ", ");
        }
        
        run2opt(list);
        // twoOpt - testCase2
        /*ArrayList<Point> list2 = new ArrayList<>();
        list2.add(new Point(1, 0, 0));
        list2.add(new Point(2, 1, 1));
        list2.add(new Point(3, 3, 2));
        list2.add(new Point(4, 3, 0));
        list2.add(new Point(5, 3, -1));
        list2.add(new Point(6, 2, 0));
        list2.add(new Point(7, 1, 3));
        run2opt(list2);*/
        
        // twoOpt - testCase1
        /*ArrayList<Point> list1 = new ArrayList<>();
        list1.add(new Point(1, 0, 0));
        list1.add(new Point(2, 1, 1));
        list1.add(new Point(3, 3, 2));
        list1.add(new Point(4, 2, 3));
        list1.add(new Point(5, 1, 3));
        list1.add(new Point(6, 3, 0));
        list1.add(new Point(7, 1, -1));
        run2opt(list1);*/
    }

    /*public static boolean intersect(Point a1, Point a2, Point b1, Point b2) {
        // a = vector pointing from a1 to a2
        Point a = new Point(0, a2.getX() - a1.getX(), a2.getY() - a1.getY());
        // b = vector pointing from a1 to a2
        Point b = new Point(0, b2.getX() - b1.getX(), b2.getY() - b1.getY());
        
        if(crossp(a,b) == 0) {
            // parallel line segments cannot intersect
            return false;
        }
        
        
        
        
    }
     */
    private static double crossp(Point a, Point b) {
        return a.getX() * b.getY() - a.getY() * b.getX();
    }

    private static double dotp(Point a, Point b) {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }
}
