package goldDigger.repositories;

import goldDigger.models.discoverer.Discoverer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DiscovererRepository implements Repository<Discoverer>{
    private Collection<Discoverer> discoverers;

    public DiscovererRepository() {
        this.discoverers = new ArrayList<>();
    }

    @Override
    public Collection<Discoverer> getCollection() {
        return Collections.unmodifiableCollection(discoverers);
    }

    @Override
    public void add(Discoverer entity) {
        this.discoverers.add(entity);
    }

    @Override
    public boolean remove(Discoverer entity) {
        return this.discoverers.remove(entity);
    }

    @Override
    public Discoverer byName(String name) {
        Discoverer obj = null;
        for (Discoverer discoverer:discoverers) {
            if(discoverer.getName().equals(name)) {
                obj = discoverer;
            }
        }
        return obj;
    }
}
