package com.diy.HelloWorld.utils;

public class Ag3ntString extends Ag3ntStringBase{

    public Ag3ntString(String str) {
        super(str);
    }

    public boolean contains(String substring) {
        return this.getString().contains(substring);
    }
}
