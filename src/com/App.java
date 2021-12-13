package com;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Project Fund : ");
        Double projectFund = input.nextDouble();
        System.out.println("Enter Experience Level : ");
        Double ExperienceLevel = input.nextDouble();
        FuzzyLogic testCase = new FuzzyLogic(projectFund, ExperienceLevel);
        testCase.performFL();
        input.close();

    }
}
