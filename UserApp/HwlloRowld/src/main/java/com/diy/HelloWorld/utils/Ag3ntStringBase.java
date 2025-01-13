package com.diy.HelloWorld.utils;

public class Ag3ntStringBase {
    String string;

    public Ag3ntStringBase() {
        string = null;
    }

    public Ag3ntStringBase(String str) {
        string = new String(str);
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
