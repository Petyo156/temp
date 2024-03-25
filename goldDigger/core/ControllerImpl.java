package goldDigger.core;

import goldDigger.models.discoverer.Anthropologist;
import goldDigger.models.discoverer.Archaeologist;
import goldDigger.models.discoverer.Discoverer;
import goldDigger.models.discoverer.Geologist;
import goldDigger.models.operation.Operation;
import goldDigger.models.operation.OperationImpl;
import goldDigger.models.spot.Spot;
import goldDigger.models.spot.SpotImpl;
import goldDigger.repositories.DiscovererRepository;
import goldDigger.repositories.Repository;
import goldDigger.repositories.SpotRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static goldDigger.common.ConstantMessages.*;
import static goldDigger.common.ExceptionMessages.*;

public class ControllerImpl implements Controller {
    private Repository<Discoverer> discovererRepository;
    private Repository<Spot> spotRepository;

    private int inspectedSpots;

    public ControllerImpl() {
        this.discovererRepository = new DiscovererRepository();
        this.spotRepository = new SpotRepository();
    }

    @Override
    public String addDiscoverer(String kind, String discovererName) {
        Discoverer discoverer;
        switch (kind) {
            case "Anthropologist":
                discoverer = new Anthropologist(discovererName);
                break;
            case "Archaeologist":
                discoverer = new Archaeologist(discovererName);
                break;
            case "Geologist":
                discoverer = new Geologist(discovererName);
                break;
            default:
                throw new IllegalArgumentException(DISCOVERER_INVALID_KIND);
        }
        this.discovererRepository.add(discoverer);
        return String.format("Added %s: %s.", kind, discovererName);
    }

    @Override
    public String addSpot(String spotName, String... exhibits) {
        Spot spot = new SpotImpl(spotName);
        for (String e : exhibits) {
            spot.getExhibits().add(e);
        }
        this.spotRepository.add(spot);
        return String.format(SPOT_ADDED, spotName);
    }

    @Override
    public String excludeDiscoverer(String discovererName) {
        Discoverer discoverer = this.discovererRepository.byName(discovererName);
        if (null == discoverer) {
            throw new IllegalArgumentException(String.format(DISCOVERER_DOES_NOT_EXIST, discovererName));
        }
        this.discovererRepository.remove(discoverer);
        return String.format(DISCOVERER_EXCLUDE, discovererName);
    }

    @Override
    public String inspectSpot(String spotName) {
        List<Discoverer> availableDiscoverers;
        availableDiscoverers = this.discovererRepository.getCollection().stream()
                .filter(discoverer -> discoverer.getEnergy() > 45)
                .collect(Collectors.toList());
        if (availableDiscoverers.isEmpty()) {
            throw new IllegalArgumentException(SPOT_DISCOVERERS_DOES_NOT_EXISTS);
        }

        Spot spot = this.spotRepository.byName(spotName);
        Operation operation = new OperationImpl();
        operation.startOperation(spot,availableDiscoverers);
        inspectedSpots++;
        int excludedDiscoverers = this.discovererRepository.getCollection().size() - availableDiscoverers.size();
        return String.format(INSPECT_SPOT, spotName, excludedDiscoverers);
    }

    @Override
    public String getStatistics() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(FINAL_SPOT_INSPECT, inspectedSpots));
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(FINAL_DISCOVERER_INFO);
        this.discovererRepository.getCollection().forEach(
                discoverer -> {
                    stringBuilder.append(System.lineSeparator());
                    stringBuilder.append(String.format(FINAL_DISCOVERER_NAME, discoverer.getName()));
                    stringBuilder.append(System.lineSeparator());
                    stringBuilder.append(String.format(FINAL_DISCOVERER_ENERGY, discoverer.getEnergy()));
                    stringBuilder.append(System.lineSeparator());

                    String museums = discoverer.getMuseum().getExhibits().isEmpty() ? "None" :
                            String.join(FINAL_DISCOVERER_MUSEUM_EXHIBITS_DELIMITER, discoverer.getMuseum().getExhibits());
                    stringBuilder.append(String.format(FINAL_DISCOVERER_MUSEUM_EXHIBITS, museums));
                });

        return stringBuilder.toString();
    }
}
