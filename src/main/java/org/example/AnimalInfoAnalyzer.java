package org.example;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.example.model.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class AnimalInfoAnalyzer {
    private static final String pathToAnimalFile = "src/main/resources/animals.csv";
    private static final String pathToRulesFile = "src/main/resources/rules.csv";
    private static final ApplyRulesCondition applyRulesCondition = new ApplyRulesCondition();

    public static void main(String[] args) {

        List<List<Map.Entry<String, Predicate<AnimalAttribute[]>>>> rule = null;
        List<Animal> animals = new ArrayList<>();

        try (BufferedReader rulesReader = new BufferedReader(new FileReader(pathToRulesFile)))
        {
            RuleParser ruleParser = new RuleParser(rulesReader);
            rule = ruleParser.readRules();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error when reading a file: " + e.getMessage());
        }

        try (CSVReader reader = new CSVReader(new FileReader(pathToAnimalFile))) {
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                var weight = lineInArray[0];
                var height = lineInArray[1];
                var type = lineInArray[2];

                animals.add(new Animal(Weight.valueOf(weight), Height.valueOf(height), Type.valueOf(type)));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error when reading a file: " + e.getMessage());
        } catch (CsvValidationException e) {
            System.out.println("CSV data validation errors detected: " + e.getMessage());
        }

        List<String> countingCondition = applyRulesCondition.processAnimalsRules(animals, rule);
        for (String result : countingCondition){
            System.out.println(result);
        }

    }
}