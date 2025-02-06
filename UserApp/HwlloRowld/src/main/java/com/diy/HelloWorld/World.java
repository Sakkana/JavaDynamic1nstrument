package com.diy.HelloWorld;
class World {
    // 实例字段
    private int instanceField;

    // 静态字段
    public static String staticField = "Hello, Static!";
    public World(int instanceField) {
        this.instanceField = instanceField;
    }
    public int getInstanceField() {
        return instanceField;
    }

    public static void main(String[] args) {

    }

}