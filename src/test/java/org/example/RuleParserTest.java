package org.example;

import org.example.model.AnimalAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class RuleParserTest {

    private static final String rule1 = "HERBIVORE";
    private static final String rule2 = "HERBIVORE OR CARNIVOROUS,SMALL";
    private static final String rule3 = "OMNIVOROUS,! HIGH";

    private RuleParser ruleParser;
    private BufferedReader bufferedReader;

    @BeforeEach
    void setUp() {
        bufferedReader = mock(BufferedReader.class);
        ruleParser = new RuleParser(bufferedReader);
    }

    @Test
    void testReadRule() throws IOException {
        when(bufferedReader.readLine()).thenReturn(rule1, rule2, rule3, null);

        List<List<Map.Entry<String, Predicate<AnimalAttribute[]>>>> actualRules = ruleParser.readRules();

        assertNotNull(actualRules);
        assertEquals(3, actualRules.size());

        assertTrue(actualRules.get(0).get(0).getKey().equals("HERBIVORE"));

        assertTrue(actualRules.get(1).get(0).getKey().equals("HERBIVORE OR CARNIVOROUS"));
        assertTrue(actualRules.get(1).get(1).getKey().equals("SMALL"));

        assertTrue(actualRules.get(2).get(0).getKey().equals("OMNIVOROUS"));
        assertTrue(actualRules.get(2).get(1).getKey().equals("! HIGH"));
    }
    @Test
    void testCreateRulePredicates() throws IOException {
        String[] ruleArray = new String[]{"HERBIVORE", "HERBIVORE OR CARNIVOROUS, SMALL","OMNIVOROUS, ! HIGH"};

        when(bufferedReader.readLine())
                .thenReturn(ruleArray[0])
                .thenReturn(ruleArray[1])
                .thenReturn(ruleArray[2])
                .thenReturn(null);

        List<Map.Entry<String, Predicate<AnimalAttribute[]>>> actualRules = ruleParser.createRulePredicates(ruleArray);

        assertNotNull(actualRules);
        assertTrue(actualRules.get(0).getKey().equals("HERBIVORE"));
        assertTrue(actualRules.get(1).getKey().equals("HERBIVORE OR CARNIVOROUS, SMALL"));
        assertTrue(actualRules.get(2).getKey().equals("OMNIVOROUS, ! HIGH"));

        assertFalse(actualRules.get(0).getKey().equals("OMNIVOROUS"));
        assertFalse(actualRules.get(1).getKey().equals("CARNIVOROUS OR HERBIVORE, HIGH"));
        assertFalse(actualRules.get(2).getKey().equals("CARNIVOROUS, HIGH"));
    }
    @Test
    void testCreateSingleRule() throws IOException {
        when(bufferedReader.readLine())
                .thenReturn(rule1, rule2, rule3, null);

        List<List<Map.Entry<String, Predicate<AnimalAttribute[]>>>> actualRules = ruleParser.readRules();

        assertNotNull(actualRules);
        assertTrue(actualRules.get(0).size() < 2);
        assertFalse(actualRules.get(1).size() < 2);
        assertFalse(actualRules.get(2).size() < 2);

    }
    @Test
    void testCreateMultipleRule() throws IOException {
        when(bufferedReader.readLine())
                .thenReturn(rule1, rule2, rule3, null);

        List<List<Map.Entry<String, Predicate<AnimalAttribute[]>>>> actualRules = ruleParser.readRules();
        assertNotNull(actualRules);
        assertFalse(actualRules.get(0).size() > 1);
        assertTrue(actualRules.get(1).size() > 1);
        assertTrue(actualRules.get(2).size() > 1);
    }

    @Test
    void testCreateNegatePredicate() throws IOException {
        when(bufferedReader.readLine())
                .thenReturn(rule1, rule2, rule3, null);

        List<List<Map.Entry<String, Predicate<AnimalAttribute[]>>>> actualRules = ruleParser.readRules();
        assertNotNull(actualRules);

        assertTrue(actualRules.get(0).get(0).getKey().equals("HERBIVORE"));

        assertTrue(actualRules.get(1).get(0).getKey().equals("HERBIVORE OR CARNIVOROUS"));
        assertTrue(actualRules.get(1).get(1).getKey().equals("SMALL"));

        assertTrue(actualRules.get(2).get(0).getKey().equals("OMNIVOROUS"));
        assertTrue(actualRules.get(2).get(1).getKey().equals("! HIGH"));
    }
}
