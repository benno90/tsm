/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author benno
 */
public class Matrix {

    private double[][] M;
    private final int size;

    public Matrix(List<Point> list) {
        size = list.size();

        M = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                M[i][j] = utils.Utils.euclideanDistance2D(list.get(i), list.get(j));
            }
        }
    }

    public Matrix(int size, double defaultvalue) {
        this.size = size;
        M = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                M[i][j] = defaultvalue;
            }
        }
    }

    /**
     * Adds another matrix to this matrix.
     *
     * @param other
     */
    public void add(Matrix other) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                M[i][j] = M[i][j] + other.get(i, j);
            }
        }
    }

    public void multiply(double factor) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                M[i][j] = M[i][j] * factor;
            }
        }
    }

    public void reset() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                M[i][j] = 0;
            }
        }
    }

    public void bound(double maxValue, double minValue) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                M[i][j] = Math.min(Math.max(M[i][j], minValue), maxValue);
            }
        }
    }

    public double get(int i, int j) {
        return M[i][j];
    }

    public double[] getMaxMinValue() {
        double[] ret = new double[2];
        ret[0] = 0.0; // max
        ret[1] = Double.MAX_VALUE; // min
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(M[i][j] > ret[0]) {
                    ret[0] = M[i][j];
                }
                if(M[i][j] < ret[1]) {
                    ret[1] = M[i][j];
                }
            }
        }
        return ret;
    }

    public void set(int i, int j, double value) {
        M[i][j] = value;
    }

    public int getSize() {
        return size;
    }

    public void print() {
        System.out.println("\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%.2f\t", M[i][j]);
            }
            System.out.print("\n");
        }
        System.out.println("\n");
    }

    public static void main(String[] args) {
        ArrayList<Point> list = new ArrayList<>();
        list.add(new Point(0, 1, 1));
        list.add(new Point(1, 2, 3));
        list.add(new Point(2, 0, 4));
        Matrix m = new Matrix(list);
        m.print();
    }

}
