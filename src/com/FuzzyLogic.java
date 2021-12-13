package com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class FuzzyLogic {
    Double projectFund;
    Double ExperienceLevel;
    Vector<tuple> Projectfund_FuzzySets = new Vector<>();
    Vector<tuple> ExperienceLevel_FuzzySets = new Vector<>();
    Vector<tuple> Risk_FuzzySets = new Vector<>();

    public FuzzyLogic(Double projectFund, Double ExperienceLevel) {
        this.projectFund = projectFund;
        this.ExperienceLevel = ExperienceLevel;
    }

    public void ConstructFuzzySets() {
        ArrayList<Double> output = new ArrayList<>(Arrays.asList(0.0, 1.0, 1.0, 0.0));
        Projectfund_FuzzySets.add(new tuple("Very Low", new ArrayList<>(Arrays.asList(0.0, 0.0, 10.0, 30.0)), output));
        Projectfund_FuzzySets.add(new tuple("Low", new ArrayList<>(Arrays.asList(10.0, 30.0, 40.0, 60.0)), output));
        Projectfund_FuzzySets.add(new tuple("Medium", new ArrayList<>(Arrays.asList(40.0, 60.0, 70.0, 90.0)), output));
        Projectfund_FuzzySets.add(new tuple("High", new ArrayList<>(Arrays.asList(70.0, 90.0, 100.0, 100.0)), output));

        output = new ArrayList<>(Arrays.asList(0.0, 1.0, 0.0));
        ExperienceLevel_FuzzySets.add(new tuple("Beginner", new ArrayList<>(Arrays.asList(0.0, 15.0, 30.0)), output));
        ExperienceLevel_FuzzySets.add(new tuple("Intermediate", new ArrayList<>(Arrays.asList(15.0, 30.0, 45.0)), output));
        ExperienceLevel_FuzzySets.add(new tuple("Expert", new ArrayList<>(Arrays.asList(30.0, 60.0, 60.0)), output));

        output = new ArrayList<>(Arrays.asList(0.0, 1.0, 0.0));
        Risk_FuzzySets.add(new tuple("Low", new ArrayList<>(Arrays.asList(0.0, 25.0, 50.0)), output));
        Risk_FuzzySets.add(new tuple("Normal", new ArrayList<>(Arrays.asList(25.0, 50.0, 75.0)), output));
        Risk_FuzzySets.add(new tuple("High", new ArrayList<>(Arrays.asList(50.0, 100.0, 100.0)), output));
    }

    public Double LineEq(Double input, Double x1, Double x2, Double y1, Double y2) {
        Double slope = (y2 - y1) / (x2 - x1);
        Double intercept = y1 - (slope * x1);
        return (slope * input) + intercept;
    }

    public HashMap<String, Double> Fuzzification(Double input, Vector<tuple> fuzzySets) {
        HashMap<String, Double> Memberships = new HashMap<>();
        for (tuple fuzzySet : fuzzySets) {
            if (input < fuzzySet.set.get(0) || input > fuzzySet.set.get(fuzzySet.set.size() - 1)) {
                Memberships.put(fuzzySet.name, 0.0);
            } else {
                for (int j = 0; j < fuzzySet.set.size(); j++) {
                    if (input >= fuzzySet.set.get(j) && input <= fuzzySet.set.get(j + 1)) {
                        if (input == fuzzySet.set.get(j)) {
                            Memberships.put(fuzzySet.name, fuzzySet.output.get(j));
                        } else if (input == fuzzySet.set.get(j + 1)) {
                            Memberships.put(fuzzySet.name, fuzzySet.output.get(j + 1));
                        } else {
                            Memberships.put(fuzzySet.name,
                                    LineEq(input, fuzzySet.set.get(j), fuzzySet.set.get(j + 1),
                                            fuzzySet.output.get(j), fuzzySet.output.get(j + 1)));
                        }
                        break;
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

    public Double DeFuzzification(HashMap<String, Double> FinalRules) {
        Double sumLower = 0.0, sumUpper = 0.0, centroid = 0.0;

        for (tuple fuzzySet : Risk_FuzzySets) {
            centroid = fuzzySet.set.stream().mapToDouble(a -> a).sum() / fuzzySet.set.size();
            sumUpper += centroid * FinalRules.get(fuzzySet.name);
        }

        for (Double val : FinalRules.values()) {
            sumLower += val;
        } 

        return sumUpper / sumLower;
    }

    public void performFL() {
        ConstructFuzzySets();
        HashMap<String, Double> projectFundMembership = Fuzzification(projectFund, Projectfund_FuzzySets);
        HashMap<String, Double> experienceLevelMembership = Fuzzification(ExperienceLevel, ExperienceLevel_FuzzySets);
        HashMap<String, Double> FinalRules = Inference(projectFundMembership, experienceLevelMembership);
        Double output = DeFuzzification(FinalRules);
        HashMap<String, Double> outputMemberShip = Fuzzification(output, Risk_FuzzySets);

        Double max = -1.0;
        String result = "";

        for (String key : outputMemberShip.keySet()) {
            if(outputMemberShip.get(key) > max){
                max = outputMemberShip.get(key);
                result = key;
            }
            System.out.println(key + " " + outputMemberShip.get(key));
        }

        System.out.println("Predicted Value (Risk) = " + output);
        System.out.println("Risk will be " + result);
    }

}