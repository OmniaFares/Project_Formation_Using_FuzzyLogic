package com;

import java.util.ArrayList;

public class tuple{
    String name;
    ArrayList<Double> set = new ArrayList<Double>();
    ArrayList<Double> output = new ArrayList<Double>();

    public tuple(String name, ArrayList<Double> set, ArrayList<Double> output)
    {
        this.name = name;
        this.set = set;
        this.output = output;
    }
}
