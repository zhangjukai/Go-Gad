package com.zjk.hy.se.collection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Spliterator;

public class LinkedListTest {
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList();
        list.add("A");
        list.add(1,"B");
        System.out.println(list.toString());
        LinkedList<String> clone = (LinkedList<String>) list.clone();
        clone.addFirst("0");
        System.out.println(list.toString());
        System.out.println(clone.toString());
        System.out.println(list.contains("A"));
        Iterator<String> iterator = list.descendingIterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        list.offerLast("C");
        list.offer("D");
        list.offerFirst("A");
        System.out.println(list.toString());
        System.out.println(list.pop());
        Spliterator<String> spliterator = list.spliterator();
    }
}
