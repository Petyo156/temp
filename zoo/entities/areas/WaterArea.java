package zoo.entities.areas;

import zoo.entities.animals.Animal;
import zoo.entities.foods.Food;

import java.util.Collection;

public class WaterArea extends BaseArea implements Area{
    public WaterArea(String name) {
        super(name, 10);
    }
}
