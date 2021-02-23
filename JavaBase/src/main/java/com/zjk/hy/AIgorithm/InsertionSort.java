package com.zjk.hy.AIgorithm;

public class InsertionSort {
    public static void main(String[] args) {
        //int a[] = {8,6,7,9,1,5,3,4,2};
        int a[] = {1,2,3,4,5,6,7,8,9};
        sort(a);
        print(a);
    }
    private static void sort(int[] a){
        for (int i = 1; i < a.length; i++) {
            for (int j = i; j > 0; j--) {
                System.out.println("内层循环");
                if(a[j] < a[j-1]){
                    swap(a,j,j-1);
                }else {
                    break;
                }
            }
            System.out.println("外层循环");
        }
    }
    private static void print(int[] a){
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]+"  ");
        }
    }
    private static void swap(int[] a,int i,int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
