package com.yilnz.util.principle;

public class AddPrinciple {
    public static int add(int a, int b){
        int c = a ^ b;
        int d = a & b;
        if(d == 0) return c;
        return add(c, d << 1);
    }
}
