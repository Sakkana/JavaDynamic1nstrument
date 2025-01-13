package com.diy.HelloWorld;

import java.util.ArrayList;
import com.diy.HelloWorld.utils.*;

public class HelloWorld {
    private static final String static_final_s1 = "static final string 1";
    private final String final_s2 = "final string 2";
    private final String static_s3 = "static string 3";
    private String noemal_s4 = "normal string 4";

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
        Object s1 = static_method_m1(1);
        System.out.println(s1);

        HelloWorld clazz = new HelloWorld();
        String s2 = clazz.non_static_method_m2(2);
        System.out.println(s2);

        clazz.test();
    }

    public static String static_method_m1(int id) {
        return new Ag3ntString(static_final_s1 + " - from Ag3ntString").getString();
    }

    private String non_static_method_m2(int id) {
        return "non-static method m2 - " + id;
    }

    private void test() {
        System.out.println("test");
    }
}