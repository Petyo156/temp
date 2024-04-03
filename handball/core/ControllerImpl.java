package handball.core;

import handball.entities.equipment.ElbowPad;
import handball.entities.equipment.Equipment;
import handball.entities.equipment.Kneepad;
import handball.entities.gameplay.Gameplay;
import handball.entities.gameplay.Indoor;
import handball.entities.gameplay.Outdoor;
import handball.entities.team.Bulgaria;
import handball.entities.team.Germany;
import handball.entities.team.Team;
import handball.repositories.EquipmentRepository;
import handball.repositories.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static handball.common.ConstantMessages.*;
import static handball.common.ExceptionMessages.*;

public class ControllerImpl implements Controller {
    private Repository equipment;
    private Collection<Gameplay> gameplays;

    public ControllerImpl() {
        equipment = new EquipmentRepository();
        gameplays = new ArrayList<>();
    }

    @Override
    public String addGameplay(String gameplayType, String gameplayName) {
        Gameplay gameplay;
        switch (gameplayType) {
            case "Indoor":
                gameplay = new Indoor(gameplayName);
                break;
            case "Outdoor":
                gameplay = new Outdoor(gameplayName);
                break;
            default:
                throw new NullPointerException(INVALID_GAMEPLAY_TYPE);
        }
        gameplays.add(gameplay);
        return String.format(SUCCESSFULLY_ADDED_GAMEPLAY_TYPE, gameplayType);
    }

    @Override
    public String addEquipment(String equipmentType) {
        Equipment equipment1;
        switch (equipmentType) {
            case "Kneepad":
                equipment1 = new Kneepad();
                break;
            case "ElbowPad":
                equipment1 = new ElbowPad();
                break;
            default:
                throw new IllegalArgumentException(INVALID_EQUIPMENT_TYPE);
        }
        equipment.add(equipment1);
        return String.format(SUCCESSFULLY_ADDED_EQUIPMENT_TYPE, equipmentType);
    }

    @Override
    public String equipmentRequirement(String gameplayName, String equipmentType) {
        Equipment equipment1 = equipment.findByType(equipmentType);
        if (null == equipment1) {
            throw new IllegalArgumentException(String.format(NO_EQUIPMENT_FOUND, equipmentType));
        }
        Gameplay gameplay = getGameplayByName(gameplayName);
        gameplay.addEquipment(equipment1);
        equipment.remove(equipment1);
        return String.format(SUCCESSFULLY_ADDED_EQUIPMENT_IN_GAMEPLAY, equipmentType, gameplay.getName());
    }

    private Gameplay getGameplayByName(String gameplayName) {
        Gameplay gameplay = gameplays.stream()
                .filter(gameplay1 -> gameplay1.getName().equals(gameplayName))
                .findFirst()
                .orElse(null);
        return gameplay;
    }

    @Override
    public String addTeam(String gameplayName, String teamType, String teamName, String country, int advantage) {
        Team team;
        switch (teamType) {
            case "Bulgaria":
                team = new Bulgaria(teamName, country, advantage);
                break;
            case "Germany":
                team = new Germany(teamName, country, advantage);
                break;
            default:
                throw new IllegalArgumentException(INVALID_TEAM_TYPE);
        }
        Gameplay gameplay = getGameplayByName(gameplayName);
        if (gameplay.getClass().getSimpleName().equals("Indoor") && teamType.equals("Germany") ||
                gameplay.getClass().getSimpleName().equals("Outdoor") && teamType.equals("Bulgaria")) {
            gameplay.addTeam(team);
            return String.format(SUCCESSFULLY_ADDED_TEAM_IN_GAMEPLAY, teamType, gameplayName);
        }
        return GAMEPLAY_NOT_SUITABLE;
    }

    @Override
    public String playInGameplay(String gameplayName) {
        Gameplay gameplay = getGameplayByName(gameplayName);
        Collection<Team> teamsList = gameplay.getTeam();
        int sum = 0;
        for (Team team : teamsList) {
            team.play();
            sum++;
        }
        return String.format(TEAMS_PLAYED, sum);
    }

    @Override
    public String percentAdvantage(String gameplayName) {
        Gameplay gameplay = getGameplayByName(gameplayName);
        Collection<Team> teamsList = gameplay.getTeam();
        int sum = 0;
        for (Team team : teamsList) {
            sum += team.getAdvantage();
        }
        return String.format(ADVANTAGE_GAMEPLAY, gameplayName, sum);
    }

    @Override
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        for (Gameplay gameplay : gameplays) {
            sb.append(gameplay);
            sb.append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}
