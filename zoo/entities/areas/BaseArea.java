package zoo.entities.areas;

import zoo.entities.animals.Animal;
import zoo.entities.foods.Food;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static zoo.common.ExceptionMessages.*;

public abstract class BaseArea implements Area {
    private String name;
    private int capacity;
    private Collection<Food> foods = new ArrayList<>();
    private Collection<Animal> animals = new ArrayList<>();

    public BaseArea(String name, int capacity) {
        setName(name);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    private void setName(String name) {
        if (null == name || name.trim().isEmpty()) {
            throw new NullPointerException(AREA_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Collection<Animal> getAnimals() {
        return this.animals;
    }

    @Override
    public Collection<Food> getFoods() {
        return this.foods;
    }

    @Override
    public int sumCalories() {
        int res = 0;
        for (Food food : foods) {
            res += food.getCalories();
        }
        return res;
    }

    @Override
    public void addAnimal(Animal animal) {
        if (capacity > animals.size()) {
            animals.add(animal);
        } else {
            throw new IllegalStateException(NOT_ENOUGH_CAPACITY);
        }
    }

    @Override
    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    @Override
    public void addFood(Food food) {
        foods.add(food);
    }

    @Override
    public void feed() {
        for (Animal a : animals) {
            a.eat();
        }
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        //sb.append(String.format("%s (%s):%n"),this.name,getSimpleName());
        String animals = "";
        if(this.animals.isEmpty()){
            animals = "none";
        } else {
            animals = this.animals.stream().map(Animal::getName)
                    .collect(Collectors.joining(" "));
        }
        sb.append(String.format("%s (%s)", this.name, this.getClass().getSimpleName()));
        sb.append(System.lineSeparator());
        sb.append(animals);
        sb.append(System.lineSeparator());
        sb.append(String.format("Foods: %d", foods.size()));
        sb.append(System.lineSeparator());
        sb.append(String.format("Calories: %d", sumCalories()));
        return sb.toString();
    }
}
