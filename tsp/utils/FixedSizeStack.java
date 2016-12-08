/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.LinkedList;

/**
 *
 * @author benno
 */
public class FixedSizeStack<K> {

    public FixedSizeStack(int size) {
        this.maxSize = size;
        list = new LinkedList<K>();
        n = 0;
    }

    private final int maxSize;
    private LinkedList<K> list;
    private int n = 0; // n = number of elements in stack

    public void push(K element) {
        list.addFirst(element);
        n++;
        if (n == maxSize + 1) {
            list.removeLast();
            n--;
        }
    }

    public K pop() {
        n--;
        return list.removeFirst();
    }
    
    public int getSize() {
        return n;
    }
    
    public K get(int i) {
        if(i < maxSize) {
            return list.get(i);
        } else {
            return null;
        }
    }
    
    public void clear() {
        list.clear();
        n = 0;
    }

    @Override
    public String toString() {
        String s = "FixedSizeStack: ";
        for (int i = 0; i < list.size(); i++) {
            s = s + " " + list.get(i);
        }
        return s;
    }
    
    public static void main(String[] args) {
        FixedSizeStack<Integer> stack = new FixedSizeStack<>(4);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
        stack.push(6);
        stack.pop();
        stack.push(10);
        System.out.println(stack);
        System.out.println("get(3)" + stack.get(stack.getSize()-1));
        stack.clear();
        System.out.println("size: " + stack.getSize());
        System.out.println(stack);
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println(stack.getSize());
        System.out.println(stack);
        
    }

}
