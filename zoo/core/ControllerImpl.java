package zoo.core;

import zoo.entities.animals.Animal;
import zoo.entities.animals.AquaticAnimal;
import zoo.entities.animals.TerrestrialAnimal;
import zoo.entities.areas.Area;
import zoo.entities.areas.LandArea;
import zoo.entities.areas.WaterArea;
import zoo.entities.foods.Food;
import zoo.entities.foods.Meat;
import zoo.entities.foods.Vegetable;
import zoo.repositories.FoodRepository;
import zoo.repositories.FoodRepositoryImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static zoo.common.ConstantMessages.*;
import static zoo.common.ExceptionMessages.*;

public class ControllerImpl implements Controller {
    private FoodRepositoryImpl foodRepository;
    private Collection<Area> areas = new ArrayList<>();


    @Override
    public String addArea(String areaType, String areaName) {
        Area area;
        switch (areaType) {
            case "LandArea":
                area = new LandArea(areaName);
                break;
            case "WaterArea":
                area = new WaterArea(areaName);
                break;
            default:
                throw new NullPointerException(INVALID_AREA_TYPE);
        }
        areas.add(area);
        return String.format(SUCCESSFULLY_ADDED_AREA_TYPE, areaType);
    }

    @Override
    public String buyFood(String foodType) {
        Food food;
        switch (foodType) {
            case "Vegetable":
                food = new Vegetable();
                break;
            case "Meat":
                food = new Meat();
                break;
            default:
                throw new NullPointerException(INVALID_FOOD_TYPE);
        }
        foodRepository.add(food);
        return String.format(SUCCESSFULLY_ADDED_FOOD_TYPE, foodType);
    }

    private Food returnFood(String foodType) {
        Food food = null;
        switch (foodType) {
            case "Vegetable":
                food = new Vegetable();
                break;
            case "Meat":
                food = new Meat();
                break;
        }
        return food;
    }

    @Override
    public String foodForArea(String areaName, String foodType) {
        Food food = returnFood(foodType);
        if (null == food) {
            throw new IllegalArgumentException(String.format(NO_FOOD_FOUND, foodType));
        }
        for (Area a : areas) {
            if (a.getName().equals(areaName)) {
                a.addFood(food);
                foodRepository.remove(food);
            }
        }
        return String.format(SUCCESSFULLY_ADDED_FOOD_IN_AREA, foodType, areaName);
    }

    @Override
    public String addAnimal(String areaName, String animalType, String animalName, String kind, double price) {
        Animal animal;
        switch (animalType) {
            case "AquaticAnimal":
                animal = new AquaticAnimal(animalName, kind, price);
                break;
            case "TerrestrialAnimal":
                animal = new TerrestrialAnimal(animalName, kind, price);
                break;
            default:
                throw new IllegalArgumentException(INVALID_ANIMAL_TYPE);
        }

        Area area = returnArea(areaName);
        if (area.getCapacity() < area.getAnimals().size()) {
            return NOT_ENOUGH_CAPACITY;
        } else {
            boolean areaAndAnimalAreLandBased = area.getClass().getSimpleName().equals("LandArea") &&
                    animal.getClass().getSimpleName().equals("TerrestrialAnimal");
            boolean areaAndAnimalAreWaterBased = area.getClass().getSimpleName().equals("WaterArea") &&
                    animal.getClass().getSimpleName().equals("AquaticAnimal");
            if (!areaAndAnimalAreWaterBased && !areaAndAnimalAreLandBased) {
                return AREA_NOT_SUITABLE;
            }
        }
        area.addAnimal(animal);
        return String.format(SUCCESSFULLY_ADDED_ANIMAL_IN_AREA, animalType, areaName);
    }

    private Area returnArea(String areaName) {
        Area area = null;
        switch (areaName) {
            case "LandArea":
                area = new LandArea(areaName);
                break;
            case "WaterArea":
                area = new WaterArea(areaName);
                break;
        }
        return area;
    }

    @Override
    public String feedAnimal(String areaName) {
        int n = 0;
        Area area = returnArea(areaName);
        for (Animal a : area.getAnimals()) {
            a.eat();
            n++;
        }
        return String.format(ANIMALS_FED, n);
    }

    @Override
    public String calculateKg(String areaName) {
        double sumKg = 0;
        Area area = returnArea(areaName);
        for (Animal a : area.getAnimals()) {
            sumKg += a.getKg();
        }
        return String.format(KILOGRAMS_AREA, areaName, sumKg);
    }

    @Override
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        for (Area a : areas) {
            sb.append(a.getInfo());
            sb.append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}
