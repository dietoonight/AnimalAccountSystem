package org.example;

import org.example.model.Animal;
import org.example.model.AnimalAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ApplyRulesCondition {
    public List<String> processAnimalsRules(List<Animal> animals, List<List<Map.Entry<String, Predicate<AnimalAttribute[]>>>> rules) {
        List<String> results = new ArrayList<>();

        for (List<Map.Entry<String, Predicate<AnimalAttribute[]>>> rulesGroup : rules) {
            long count = animals.stream().map(animal -> new AnimalAttribute[]{animal.weight, animal.height, animal.type})
                    .filter(rulesGroup.stream().map(Map.Entry::getValue).reduce(Predicate::and).orElse(t -> false)
                    )
                    .count();
            String result = String.format("Counted animals: %d\n For rules: %s\n", count, rulesGroup.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
            results.add(result);
        }
        return results;
    }
}
