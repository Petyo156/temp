package zoo.entities.animals;

public class TerrestrialAnimal extends BaseAnimal implements Animal{
    public TerrestrialAnimal(String name, String kind, double price) {
        super(name, kind, 5.50, price);
    }

    @Override
    public void eat() {
        setKg(super.getKg()+5.70);
    }
}
