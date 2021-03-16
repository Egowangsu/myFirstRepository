package com.wyx.domain;

import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        UUID uuid=UUID.randomUUID();
        String s = uuid.toString();
        s.replaceAll("-", "");
        System.out.println(s);
        System.out.println(s.length());
    }
}
