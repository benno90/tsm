package metaheuristics;

import java.util.ArrayList;
import java.util.List;
import static metaheuristics.AntColony.getNextPoint;
import utils.Matrix;
import utils.Point;

/**
 *
 * @author benno
 */
public class Ant implements Runnable {

    // paramters 
    private final ArrayList<Point> originalList;
    private final Matrix T;
    private final Matrix D;
    private final double alpha;
    private final double beta;
    private final int threadNumber;
    private final Point startPoint;

    // loop variables
    private ArrayList<Point> solutionOfAntK;
    private ArrayList<Point> remainingPoints;
    private double tourLengthOfAntK;
    private double tourSize;

    public Ant(final ArrayList<Point> originalList,
            final Matrix T,
            final Matrix D,
            final double alpha,
            final double beta,
            final int threadNumber) {
        this.originalList = originalList;
        this.T = T;
        this.D = D;
        this.alpha = alpha;
        this.beta = beta;
        this.threadNumber = threadNumber;

        solutionOfAntK = new ArrayList<>(originalList.size());
        remainingPoints = new ArrayList<>(originalList);
        tourLengthOfAntK = 0;
        tourSize = originalList.size();
        startPoint = originalList.get(0);
    }

    @Override
    public void run() {
        // inner loop for the ant k - similar to nearest neighbour
        // loop variables
        Point current = remainingPoints.remove(0);                        // always start at first point
        solutionOfAntK.add(current);
        Point next;
        double[] imaginaryDistances;
        double imaginaryDistance;
        int x, y; // -> index of points: -> to get the correct values form the matrices T & D
        int indexToRemove;
        for (int i = 0; i < tourSize - 1; i++) {
            imaginaryDistances = new double[remainingPoints.size()];
            imaginaryDistance = 0;
            for (int j = 0; j < remainingPoints.size(); j++) {
                next = remainingPoints.get(j);
                x = current.getId();
                y = next.getId();
                imaginaryDistances[j] = (Math.pow(T.get(x, y), alpha)) / (Math.pow(D.get(x, y), beta));
                imaginaryDistance += imaginaryDistances[j];
            }
            indexToRemove = getNextPoint(imaginaryDistances, imaginaryDistance);
            tourLengthOfAntK += utils.Utils.euclideanDistance2D(remainingPoints.get(indexToRemove), current);
            current = remainingPoints.remove(indexToRemove);
            solutionOfAntK.add(current);

        }
        // add distance from last point to start points
        tourLengthOfAntK += utils.Utils.euclideanDistance2D(solutionOfAntK.get(-1 + solutionOfAntK.size()), startPoint);

    }
    
    public double getTourLength() {
        return tourLengthOfAntK;
    }
    
    public List<Point> getTour() {
        return solutionOfAntK;
    }
}
