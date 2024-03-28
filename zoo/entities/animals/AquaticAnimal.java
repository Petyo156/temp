package zoo.entities.animals;

public class AquaticAnimal extends BaseAnimal implements Animal{
    public AquaticAnimal(String name, String kind, double price) {
        super(name, kind, 2.50, price);
    }

    @Override
    public void eat() {
        setKg(super.getKg() + 7.50);
    }
    //Can only live in WaterArea!
}
