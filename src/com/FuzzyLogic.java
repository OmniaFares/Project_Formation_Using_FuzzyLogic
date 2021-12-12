package com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class FuzzyLogic {
    int projectFund;
    int ExperienceLevel;
    Vector<tuple> Projectfund_FuzzySets = new Vector<tuple>();
    Vector<tuple> ExperienceLevel_FuzzySets =new Vector<tuple>();
    Vector<tuple> Risk_FuzzySets =new Vector<tuple>();

    public FuzzyLogic(int projectFund, int ExperienceLevel) {
        this.projectFund = projectFund;
        this.ExperienceLevel = ExperienceLevel;    
    }

    public void ConstructFuzzySets(){
        ArrayList<Double> output = new ArrayList<Double>(Arrays.asList(0.0,1.0,1.0,0.0));
        Projectfund_FuzzySets.add(new tuple("Very Low",new ArrayList<Integer>(Arrays.asList(0,0,10,30)),output));
        Projectfund_FuzzySets.add(new tuple("Low",new ArrayList<Integer>(Arrays.asList(10,30,40,60)),output));
        Projectfund_FuzzySets.add(new tuple("Medium",new ArrayList<Integer>(Arrays.asList(40,60,70,90)),output));
        Projectfund_FuzzySets.add(new tuple("High",new ArrayList<Integer>(Arrays.asList(70,90,100,100)),output));

        output = new ArrayList<Double>(Arrays.asList(0.0,1.0,0.0));
        ExperienceLevel_FuzzySets.add(new tuple("Beginner",new ArrayList<Integer>(Arrays.asList(0,15,30)),output));
        ExperienceLevel_FuzzySets.add(new tuple("Intermediate",new ArrayList<Integer>(Arrays.asList(15,30,45)),output));
        ExperienceLevel_FuzzySets.add(new tuple("Expert",new ArrayList<Integer>(Arrays.asList(30,60,60)),output));

        output = new ArrayList<Double>(Arrays.asList(0.0,1.0,0.0));
        Risk_FuzzySets.add(new tuple("Low",new ArrayList<Integer>(Arrays.asList(0,25,50)),output));
        Risk_FuzzySets.add(new tuple("Normal",new ArrayList<Integer>(Arrays.asList(25,50,75)),output));
        Risk_FuzzySets.add(new tuple("High",new ArrayList<Integer>(Arrays.asList(50,100,100)),output));
    }

    public Double LineEq(int input, int x1, int x2, Double y1, Double y2){
        Double slope = (y2 - y1) / (x2 -x1);
        Double intercept = y1 - (slope * x1);
        Double y = slope * input + intercept;
        return y;
    }
    public HashMap<String, Double> Fuzzification(int input,Vector<tuple> fuzzySets){
        HashMap<String, Double> Memberships = new HashMap<>();
        for(int i=0; i<fuzzySets.size(); i++){
                if(input < fuzzySets.get(i).set.get(0) || input > fuzzySets.get(i).set.get(fuzzySets.get(i).set.size()-1)){
                    Memberships.put(fuzzySets.get(i).name, 0.0);
                }
                else{
                    for(int j=0; j<fuzzySets.get(i).set.size(); j++){
                        if(input >= fuzzySets.get(i).set.get(j) && input <= fuzzySets.get(i).set.get(j+1)){
                            if(input == fuzzySets.get(i).set.get(j)){
                                Memberships.put(fuzzySets.get(i).name, fuzzySets.get(i).output.get(j));
                                break;
                            }
                            else if(input == fuzzySets.get(i).set.get(j+1)){
                                Memberships.put(fuzzySets.get(i).name, fuzzySets.get(i).output.get(j+1));
                                break;
                            }
                            else{
                                Memberships.put(fuzzySets.get(i).name, LineEq(input, fuzzySets.get(i).set.get(j), fuzzySets.get(i).set.get(j+1) , fuzzySets.get(i).output.get(j), fuzzySets.get(i).output.get(j+1)));
                            }
                        }
                    }
                }
        }
        return Memberships;
    }

    public HashMap<String, Double> Inference(HashMap<String, Double> projectFundMembership, HashMap<String, Double> experienceLevelMembership){
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

    public void DeFuzzification(){
        
    }

    public void performFL(){
        ConstructFuzzySets();
        HashMap<String, Double> projectFundMembership = Fuzzification(projectFund,Projectfund_FuzzySets);
        HashMap<String, Double> experienceLevelMembership = Fuzzification(ExperienceLevel,ExperienceLevel_FuzzySets);
        HashMap<String, Double> FinalRules = Inference(projectFundMembership, experienceLevelMembership);
        DeFuzzification();

    }

}