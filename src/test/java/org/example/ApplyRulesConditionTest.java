package org.example;

import org.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplyRulesConditionTest {
    private static final String answer1 = "Counted animals: 9\n" + " For rules: [HERBIVORE]";
    private static final String answer2 = "Counted animals: 6\n" + " For rules: [HERBIVORE OR CARNIVOROUS, SMALL]";
    private static final String answer3 = "Counted animals: 6\n" + " For rules: [OMNIVOROUS, ! HIGH]";
    private static final String rule1 = "HERBIVORE";
    private static final String rule2 = "HERBIVORE OR CARNIVOROUS,SMALL";
    private static final String rule3 = "OMNIVOROUS,! HIGH";

    private final ApplyRulesCondition applyRulesCondition = new ApplyRulesCondition();

    List<Animal> animals = new ArrayList<>();
    private RuleParser ruleParser;
    private BufferedReader bufferedReader;

    @BeforeEach
    void setUp(){
        bufferedReader = mock(BufferedReader.class);
        ruleParser = new RuleParser(bufferedReader);

        animals.add(new Animal(Weight.valueOf("LITE"), Height.valueOf("SMALL"), Type.valueOf("HERBIVORE")));
        animals.add(new Animal(Weight.valueOf("LITE"), Height.valueOf("LOW"), Type.valueOf("HERBIVORE")));
        animals.add(new Animal(Weight.valueOf("LITE"), Height.valueOf("HIGH"), Type.valueOf("HERBIVORE")));
        animals.add(new Animal(Weight.valueOf("LITE"), Height.valueOf("SMALL"), Type.valueOf("CARNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("LITE"), Height.valueOf("LOW"), Type.valueOf("CARNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("LITE"), Height.valueOf("HIGH"), Type.valueOf("CARNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("LITE"), Height.valueOf("SMALL"), Type.valueOf("OMNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("LITE"), Height.valueOf("LOW"), Type.valueOf("OMNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("LITE"), Height.valueOf("HIGH"), Type.valueOf("OMNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("MIDDLE"), Height.valueOf("SMALL"), Type.valueOf("HERBIVORE")));
        animals.add(new Animal(Weight.valueOf("MIDDLE"), Height.valueOf("LOW"), Type.valueOf("HERBIVORE")));
        animals.add(new Animal(Weight.valueOf("MIDDLE"), Height.valueOf("HIGH"), Type.valueOf("HERBIVORE")));
        animals.add(new Animal(Weight.valueOf("MIDDLE"), Height.valueOf("SMALL"), Type.valueOf("CARNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("MIDDLE"), Height.valueOf("LOW"), Type.valueOf("CARNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("MIDDLE"), Height.valueOf("HIGH"), Type.valueOf("CARNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("MIDDLE"), Height.valueOf("SMALL"), Type.valueOf("OMNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("MIDDLE"), Height.valueOf("LOW"), Type.valueOf("OMNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("MIDDLE"), Height.valueOf("HIGH"), Type.valueOf("OMNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("HEAVY"), Height.valueOf("SMALL"), Type.valueOf("HERBIVORE")));
        animals.add(new Animal(Weight.valueOf("HEAVY"), Height.valueOf("LOW"), Type.valueOf("HERBIVORE")));
        animals.add(new Animal(Weight.valueOf("HEAVY"), Height.valueOf("HIGH"), Type.valueOf("HERBIVORE")));
        animals.add(new Animal(Weight.valueOf("HEAVY"), Height.valueOf("SMALL"), Type.valueOf("CARNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("HEAVY"), Height.valueOf("LOW"), Type.valueOf("CARNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("HEAVY"), Height.valueOf("HIGH"), Type.valueOf("CARNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("HEAVY"), Height.valueOf("SMALL"), Type.valueOf("OMNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("HEAVY"), Height.valueOf("LOW"), Type.valueOf("OMNIVOROUS")));
        animals.add(new Animal(Weight.valueOf("HEAVY"), Height.valueOf("HIGH"), Type.valueOf("OMNIVOROUS")));
    }

    @Test
    void testProcessAnimalsRules() throws IOException {
        when(bufferedReader.readLine())
                .thenReturn(rule1, rule2, rule3, null);

        List<List<Map.Entry<String, Predicate<AnimalAttribute[]>>>> rule = ruleParser.readRules();
        List<String> countingCondition = applyRulesCondition.processAnimalsRules(animals, rule);

        assertTrue(countingCondition.get(0).contains(answer1));
        assertTrue(countingCondition.get(1).contains(answer2));
        assertTrue(countingCondition.get(2).contains(answer3));
    }
}
