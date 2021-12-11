package com;

import java.util.ArrayList;

public class pair{
    String name;
    ArrayList<Integer> set = new ArrayList<Integer>();
    ArrayList<Double> output = new ArrayList<Double>();

    public pair(String name, ArrayList<Integer> set, ArrayList<Double> output)
    {
        this.name = name;
        this.set = set;
        this.output = output;
    }
}
