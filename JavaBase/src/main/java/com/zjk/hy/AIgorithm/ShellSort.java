package com.zjk.hy.AIgorithm;

public class ShellSort {
    public static void main(String[] args) {
        int a[] = {8,6,7,9,1,5,3,4,2};
        // int a[] = {1,2,3,4,5,6,7,8,9};
        sort(a);
        print(a);
    }
    private static void sort(int[] a){
        int h = 1;
        while(h <= a.length/3) {
            h = h*3+1;
        }
        for (int gap = h; gap > 0 ; gap=(gap-1)/3) {
            for (int i = gap; i < a.length; i++) {
                for (int j = i; j > gap-1; j-=gap) {
                    if(a[j] < a[j-gap]){
                        swap(a,j,j-gap);
                    }
                }
            }
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
