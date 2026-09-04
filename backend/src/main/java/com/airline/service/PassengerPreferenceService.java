package com.airline.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerPreferenceService {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MealOption {
        private String code;
        private String name;
        private String description;
        private Boolean isGlutenFree;
        private Boolean isVegetarian;
    }

    public List<MealOption> getSpecialMealOptions() {
        return List.of(
            MealOption.builder().code("VGML").name("Vegetarian Vegan Meal").description("Strict vegetarian meal containing no animal products").isGlutenFree(false).isVegetarian(true).build(),
            MealOption.builder().code("AVML").name("Asian Vegetarian Meal").description("Spiced Indian style vegetarian dishes").isGlutenFree(false).isVegetarian(true).build(),
            MealOption.builder().code("GFML").name("Gluten Intolerant Meal").description("Prepared strictly without gluten-containing ingredients").isGlutenFree(true).isVegetarian(false).build(),
            MealOption.builder().code("KSML").name("Kosher Certified Meal").description("Prepared in accordance with Jewish dietary laws").isGlutenFree(false).isVegetarian(false).build(),
            MealOption.builder().code("MOML").name("Muslim Halal Meal").description("Prepared in accordance with Islamic dietary standards").isGlutenFree(false).isVegetarian(false).build()
        );
    }
}
