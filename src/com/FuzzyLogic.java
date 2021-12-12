package com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class FuzzyLogic {
    int projectFund;
    int ExperienceLevel;
    Vector<tuple> Projectfund_FuzzySets = new Vector<>();
    Vector<tuple> ExperienceLevel_FuzzySets = new Vector<>();
    Vector<tuple> Risk_FuzzySets = new Vector<>();

    public FuzzyLogic(int projectFund, int ExperienceLevel) {
        this.projectFund = projectFund;
        this.ExperienceLevel = ExperienceLevel;
    }

    public void ConstructFuzzySets() {
        ArrayList<Double> output = new ArrayList<>(Arrays.asList(0.0, 1.0, 1.0, 0.0));
        Projectfund_FuzzySets.add(new tuple("Very Low", new ArrayList<>(Arrays.asList(0, 0, 10, 30)), output));
        Projectfund_FuzzySets.add(new tuple("Low", new ArrayList<>(Arrays.asList(10, 30, 40, 60)), output));
        Projectfund_FuzzySets.add(new tuple("Medium", new ArrayList<>(Arrays.asList(40, 60, 70, 90)), output));
        Projectfund_FuzzySets.add(new tuple("High", new ArrayList<>(Arrays.asList(70, 90, 100, 100)), output));

        output = new ArrayList<>(Arrays.asList(0.0, 1.0, 0.0));
        ExperienceLevel_FuzzySets.add(new tuple("Beginner", new ArrayList<>(Arrays.asList(0, 15, 30)), output));
        ExperienceLevel_FuzzySets.add(new tuple("Intermediate", new ArrayList<>(Arrays.asList(15, 30, 45)), output));
        ExperienceLevel_FuzzySets.add(new tuple("Expert", new ArrayList<>(Arrays.asList(30, 60, 60)), output));

        output = new ArrayList<>(Arrays.asList(0.0, 1.0, 0.0));
        Risk_FuzzySets.add(new tuple("Low", new ArrayList<>(Arrays.asList(0, 25, 50)), output));
        Risk_FuzzySets.add(new tuple("Normal", new ArrayList<>(Arrays.asList(25, 50, 75)), output));
        Risk_FuzzySets.add(new tuple("High", new ArrayList<>(Arrays.asList(50, 100, 100)), output));
    }

    public Double LineEq(int input, int x1, int x2, Double y1, Double y2) {
        double slope = (y2 - y1) / (x2 - x1);
        double intercept = y1 - (slope * x1);
        return (slope * input) + intercept;
    }

    public HashMap<String, Double> Fuzzification(int input, Vector<tuple> fuzzySets) {
        HashMap<String, Double> Memberships = new HashMap<>();
        for (tuple fuzzySet : fuzzySets) {
            if (input < fuzzySet.set.get(0) || input > fuzzySet.set.get(fuzzySet.set.size() - 1)) {
                Memberships.put(fuzzySet.name, 0.0);
            } else {
                for (int j = 0; j < fuzzySet.set.size(); j++) {
                    if (input >= fuzzySet.set.get(j) && input <= fuzzySet.set.get(j + 1)) {
                        if (input == fuzzySet.set.get(j)) {
                            Memberships.put(fuzzySet.name, fuzzySet.output.get(j));
                            break;
                        } else if (input == fuzzySet.set.get(j + 1)) {
                            Memberships.put(fuzzySet.name, fuzzySet.output.get(j + 1));
                            break;
                        } else {
                            Memberships.put(fuzzySet.name,
                                    LineEq(input, fuzzySet.set.get(j), fuzzySet.set.get(j + 1),
                                            fuzzySet.output.get(j), fuzzySet.output.get(j + 1)));
                        }
                    }
                }
            }
        }
        return Memberships;
    }

    public HashMap<String, Double> Inference(HashMap<String, Double> projectFundMembership, HashMap<String, Double> experienceLevelMembership) {
        HashMap<String, Double> Rules = new HashMap<>();

        Double low = Math.max(projectFundMembership.get("High"), experienceLevelMembership.get("Expert"));
        Rules.put("Low", low);

        Double normal = Math.min(projectFundMembership.get("Medium"),
                Math.max(experienceLevelMembership.get("Intermediate"), experienceLevelMembership.get("Beginner")));
        Rules.put("Normal", normal);

        //Double high1 = projectFundMembership.get("Very Low");
        //Double high2 = Math.min(projectFundMembership.get("Low"), experienceLevelMembership.get("Beginner"));
        Double high = Math.max(projectFundMembership.get("Very Low"),
                Math.min(projectFundMembership.get("Low"), experienceLevelMembership.get("Beginner")));
        Rules.put("High", high);

        return Rules;
    }

    public void DeFuzzification() {

    }

    public void performFL() {
        ConstructFuzzySets();
        HashMap<String, Double> projectFundMembership = Fuzzification(projectFund, Projectfund_FuzzySets);
        HashMap<String, Double> experienceLevelMembership = Fuzzification(ExperienceLevel, ExperienceLevel_FuzzySets);
        HashMap<String, Double> FinalRules = Inference(projectFundMembership, experienceLevelMembership);
        DeFuzzification();

    }

}