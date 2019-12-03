package com;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/13
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public class Student extends Person {

    private String tool;

    Student(String tool) {
        this.tool = tool;
    }

    public void dosth() {
        System.out.println("student dosth with" + tool);

    }
}
