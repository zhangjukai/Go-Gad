package com.zjk.hy.test;

public class Test01 {
    public class Test2 extends Test01 {
        public Test2() {
            if (n == 0x10)
                n = 10;
            else
                n = 0_10; // 1*8^1+0*8^0
        }
        public int get() { return n; }
    }

    int n = 0x10; // 1*16^1+0*16^0 = 16
    public Test01() {
        n = 0b10; // 2
    }
    public static void main(String[] args) {
        final Test2 t2 = new Test01().new Test2();
        System.out.println(t2.get());
    }

}
