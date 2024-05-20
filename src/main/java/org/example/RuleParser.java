package org.example;

import org.example.model.AnimalAttribute;
import org.example.model.Height;
import org.example.model.Type;
import org.example.model.Weight;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class RuleParser {
    private final BufferedReader rulesReader;
    private static final String separator = ",";
    private static final String OR = " OR ";
    private static final String NOT = "! ";
    private static final String EMPTY = "";

    public RuleParser(BufferedReader rulesReader) {
        this.rulesReader = rulesReader;
    }
    public List<List<Map.Entry<String, Predicate<AnimalAttribute[]>>>> readRules() throws IOException {
        List<List<Map.Entry<String, Predicate<AnimalAttribute[]>>>> rules = new ArrayList<>();

        String line;
        while ((line = rulesReader.readLine()) != null) {
            String[] ruleParts = line.split(separator);
            List<Map.Entry<String, Predicate<AnimalAttribute[]>>> rulePredicates = createRulePredicates(ruleParts);
            rules.add(rulePredicates);
        }
        return rules;
    }
    public List<Map.Entry<String, Predicate<AnimalAttribute[]>>> createRulePredicates(String[] rules) {
        List<Map.Entry<String, Predicate<AnimalAttribute[]>>> rulePredicates= new ArrayList<>();
        for (String rule : rules) {
            Predicate<AnimalAttribute[]> predicate = createRulePredicate(rule);
            rulePredicates.add(new AbstractMap.SimpleEntry<>(rule, predicate));
        }
        return rulePredicates;
    }
    private Predicate<AnimalAttribute[]> createRulePredicate(String rulePredicates){
        if (multipleRule(rulePredicates)) {
            return createMultipleRule(rulePredicates);
        } else {
            return createSingleRule(rulePredicates);
        }
    }
    private boolean multipleRule(String rulePredicates) {
        return rulePredicates.contains(OR);
    }
    private Predicate<AnimalAttribute[]> createMultipleRule(String rulePredicate){
        String[] multipleRules = rulePredicate.split(OR);

        return Arrays.stream(multipleRules)
                .map(this::createSingleRule)
                .reduce(Predicate::or)
                .orElse(t -> false);
    }
    private Predicate<AnimalAttribute[]> createSingleRule(String rulePredicates) {
        if (rulePredicates.contains(NOT)) {
            return createNegatePredicate(rulePredicates);
        } else {
            return createPredicate(rulePredicates);
        }
    }
    private Predicate<AnimalAttribute[]> createNegatePredicate(String rulePredicates) {
        return createPredicate(rulePredicates.replace(NOT, EMPTY)).negate();
    }

    private Predicate<AnimalAttribute[]> createPredicate(String rulePredicates){
        AnimalAttribute attribute = parseAttribute(rulePredicates);

        return (AnimalAttribute[] animalFields) -> {
            for (AnimalAttribute internalField : animalFields) {
                if (internalField.equals(attribute)) {
                    return true;
                }
            }
            return false;
        };
    }

    private AnimalAttribute parseAttribute(String attribute){
        try{
          return Weight.valueOf(attribute);
        } catch (IllegalArgumentException e){
            try {
                return Height.valueOf(attribute);
            } catch (IllegalArgumentException e1){
                try {
                   return Type.valueOf(attribute);
                } catch (IllegalArgumentException e2){
                    System.out.println("Cannot parse attribute = " + attribute);
                }
            }
        }
        return null;
    }

}
