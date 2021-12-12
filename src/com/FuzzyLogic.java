package com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class FuzzyLogic {
    int projectFund;
    int ExperienceLevel;
    Vector<pair> Projectfund_FuzzySets = new Vector<pair>();
    Vector<pair> ExperienceLevel_FuzzySets =new Vector<pair>();
    Vector<pair> Risk_FuzzySets =new Vector<pair>();

    public FuzzyLogic(int projectFund, int ExperienceLevel) {
        this.projectFund = projectFund;
        this.ExperienceLevel = ExperienceLevel;    
    }

    public void ConstructFuzzySets(){
        ArrayList<Double> output = new ArrayList<Double>(Arrays.asList(0.0,1.0,1.0,0.0));
        Projectfund_FuzzySets.add(new pair("Very Low",new ArrayList<Integer>(Arrays.asList(0,0,10,30)),output));
        Projectfund_FuzzySets.add(new pair("Low",new ArrayList<Integer>(Arrays.asList(10,30,40,60)),output));
        Projectfund_FuzzySets.add(new pair("Medium",new ArrayList<Integer>(Arrays.asList(40,60,70,90)),output));
        Projectfund_FuzzySets.add(new pair("High",new ArrayList<Integer>(Arrays.asList(70,90,100,100)),output));

        output = new ArrayList<Double>(Arrays.asList(0.0,1.0,0.0));
        ExperienceLevel_FuzzySets.add(new pair("Beginner",new ArrayList<Integer>(Arrays.asList(0,15,30)),output));
        ExperienceLevel_FuzzySets.add(new pair("Intermediate",new ArrayList<Integer>(Arrays.asList(15,30,45)),output));
        ExperienceLevel_FuzzySets.add(new pair("Expert",new ArrayList<Integer>(Arrays.asList(30,60,60)),output));

        output = new ArrayList<Double>(Arrays.asList(0.0,1.0,0.0));
        Risk_FuzzySets.add(new pair("Low",new ArrayList<Integer>(Arrays.asList(0,25,50)),output));
        Risk_FuzzySets.add(new pair("Normal",new ArrayList<Integer>(Arrays.asList(25,50,75)),output));
        Risk_FuzzySets.add(new pair("High",new ArrayList<Integer>(Arrays.asList(50,100,100)),output));
    }

    public Double LineEq(int input, int x1, int x2, Double y1, Double y2){
        Double slope = (y2 - y1) / (x2 -x1);
        Double intercept = y1 - (slope * x1);
        Double y = slope * input + intercept;
        return y;
    }
    public Vector<Double> Fuzzifiction(int input,Vector<pair> fuzzySets){
        Vector<Double> Memberships = new Vector<>();
        for(int i=0; i<fuzzySets.size(); i++){
                if(input < fuzzySets.get(i).set.get(0) || input > fuzzySets.get(i).set.get(fuzzySets.get(i).set.size()-1)){
                    Memberships.add(0.0);
                }
                else{
                    for(int j=0; j<fuzzySets.get(i).set.size(); j++){
                        if(input >= fuzzySets.get(i).set.get(j) && input <= fuzzySets.get(i).set.get(j+1)){
                            if(input == fuzzySets.get(i).set.get(j)){
                                Memberships.add(fuzzySets.get(i).output.get(j));
                                break;
                            }
                            else if(input == fuzzySets.get(i).set.get(j+1)){
                                Memberships.add(fuzzySets.get(i).output.get(j+1));
                                break;
                            }
                            else{
                                Memberships.add(LineEq(input, fuzzySets.get(i).set.get(j), fuzzySets.get(i).set.get(j+1) , fuzzySets.get(i).output.get(j), fuzzySets.get(i).output.get(j+1)));
                            }
                        }
                    }
                }
        }
        return Memberships;
    }

    public void Inference(){
        
    }

    public void DeFuzzifiction(){
        
    }

    public void performFL(){
        ConstructFuzzySets();
        Vector<Double> membershipForFund = Fuzzifiction(projectFund,Projectfund_FuzzySets);
        Vector<Double> membershipForExp = Fuzzifiction(ExperienceLevel,ExperienceLevel_FuzzySets);
        Inference();
        DeFuzzifiction();

    }

}