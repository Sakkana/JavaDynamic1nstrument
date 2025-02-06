package com.diy.HelloWorld;

import java.util.ArrayList;
import com.diy.HelloWorld.utils.*;
import java.util.HashMap;

public class HelloWorld {
    private static final String static_final_s1 = "static final string 1";
    private final String final_s2 = "final string 2";
    private static String static_s3 = "static string 3";
    private String normal_s4 = "normal string 4";

    public static void main(String[] args) {
        // test for core API
        String s = "Hello";
        System.out.println("Common User Application !");

        ArrayList<String> list = new ArrayList<>();
        list.add(s);

        System.out.println("Common User Application2 !");
        System.out.println(s + list.get(0));

        // test for Tree API

        // original return type: Ag3ntString
        // after instrument, expected: Ag3ntStringbase
        String s1 = static_method_m1(1, 2);
        System.out.println(s1);

//        static_method_m2(1, 2, "");
//        static_method_m3(0, 1.2);
//        static_method_m4(new Person("John", 30), "Hello, World!");
//
//        HashMap<Integer, String> hashMap = new HashMap<>();
//        hashMap.put(1, "One");
//        hashMap.put(2, "Two");
//        hashMap.put(3, "Three");
//        static_method_m5(new char[]{'a', 'b', 'c'}, hashMap);
//
//        static_method_m6(new int[]{1, 2, 3}, new String[]{"a", "b", "c"});
//        static_method_m7(5);
//        static_method_m8(new Integer[]{1, 2, 3}, 2);



        HelloWorld clazz = new HelloWorld();
        String s2 = clazz.non_static_method_m2(2);
        System.out.println(s2);

        clazz.test();
    }

    public static String static_method_m1(int id, int di) {
        return "return from static_method_m1";
    }
    public static String static_method_m2(int id, int di, String s1) {
        return "return from static_method_m2";
    }
    public static boolean static_method_m3(int id, double value) {
        return value > id;
    }

    public static String static_method_m4(Person person, String message) {
        return person.getName() + " says: " + message;
    }

    public static String static_method_m5(char[] chs, HashMap<Integer, String> map) {
        return null;
    }

    public static int static_method_m6(int[] numbers, String[] strings) {
        return numbers.length + strings.length;
    }

    public static double[] static_method_m7(int size) {
        double[] array = new double[size];
        for (int i = 0; i < size; i++) {
            array[i] = i * 1.5;
        }
        return array;
    }

    public static <T> boolean static_method_m8(T[] array, T element) {
        for (T item : array) {
            if (item.equals(element)) {
                return true;
            }
        }
        return false;
    }

//    public static String hackerMethod(int id, int di, String s) {
//        return s + " , " + id + " ," + di;
//    }

    private String non_static_method_m2(int id) {
        return "non-static method m2 - " + id;
    }

    private void test() {
        System.out.println("test");
    }
}

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}