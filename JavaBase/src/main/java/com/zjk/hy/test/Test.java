package com.zjk.hy.test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
        String str = "assdwqbbbadbsq";
        /*Map<String, Long> map = Stream.of(str.split("")).collect(Collectors.groupingBy(String::toString, Collectors.counting()));
        LinkedHashMap<String, Long> linkedHashMap = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(b-> {
                    linkedHashMap.put(b.getKey(),b.getValue());
                    System.out.println(b.getKey()+"_"+b.getValue());
                });*/
        TreeMap<String, Long> result = Arrays.stream(str.split(""))
                .sorted()
//                     .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
                .collect(Collectors.groupingBy(Function.identity(),TreeMap::new,Collectors.counting()));
        System.out.println(result);
    }
    public static void get() {
        System.out.println("Get");
    }

}
