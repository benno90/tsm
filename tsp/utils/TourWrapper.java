/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.List;

/**
 * Auxiliary class to keep track of tour-lists
 * @author benno
 */
public class TourWrapper implements Comparable<TourWrapper>{
    
    private double tourLenght;
    private List<Point> tour;
    
    public TourWrapper(List<Point> t, double l) {
        tour = t;
        tourLenght = l;
    }

    @Override
    public int compareTo(TourWrapper o) {
        // shorter tourlength is better
        return Double.compare(tourLenght, o.tourLenght);
    }
    
    public List<Point> getTour() {
        return tour;
    }
    
    public double getTourLength() {
        return tourLenght;
    }
    
    
}
