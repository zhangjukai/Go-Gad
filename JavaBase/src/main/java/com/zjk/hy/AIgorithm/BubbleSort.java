package com.zjk.hy.AIgorithm;

public class BubbleSort {
    public static void main(String[] args) {
         // int a[] = {6,8,7,9,1,5,3,4,2};
         int a[] = {1,2,3,4,5,6,7,8,9};
         // 1,2,3,4,5,6,7,8,9
        sort(a);
        print(a);
    }

    private static void print(int[] a){
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]+"  ");
        }
    }
    private static void sort(int[] a){
        for (int i = 0; i < a.length-1; i++) {
            System.out.println("外层循环");
            boolean flag = true;
            for (int j = 0; j < a.length-1-i; j++) {
                if(a[j]>a[j+1]) {
                    swap(a,j,j+1);
                    flag = false;
                }
                System.out.println("内循环：i="+i+",j="+j);
            }
            if (flag) {
                break;
            }
        }
    }

    private static void swap(int[] a,int i,int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
