package com.zjk.hy.test;

import java.util.*;

public class StrSort {

    public static void main(String[] args) {
        String str = "assdwqbbbadbsq";
        HashMap<String,Integer> map = new HashMap<>();
        extracted(str, map);
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(b-> {
                    linkedHashMap.put(b.getKey(),b.getValue());
                    System.out.println(b.getKey()+"_"+b.getValue());
                });
    }

    private static void extracted(String str, HashMap<String, Integer> map) {
        String c = "";
        Integer ccount = null;
        for (int i = 0; i < str.length(); i++) {
            c = String.valueOf(str.charAt(i));
            ccount = map.get(c);
            if(ccount==null){
                ccount = 1;
            }
            map.put(c,ccount+1);
        }
    }

}
