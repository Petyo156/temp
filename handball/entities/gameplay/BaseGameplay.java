package handball.entities.gameplay;

import handball.entities.equipment.Equipment;
import handball.entities.team.Team;

import java.util.ArrayList;
import java.util.Collection;

import static handball.common.ExceptionMessages.GAMEPLAY_NAME_NULL_OR_EMPTY;
import static handball.common.ExceptionMessages.TEAM_NAME_NULL_OR_EMPTY;

public abstract class BaseGameplay implements Gameplay {
    private String name;
    private int capacity;
    private Collection<Equipment> equipments;
    private Collection<Team> teams;

    public BaseGameplay(String name, int capacity) {
        setName(name);
        this.capacity = capacity;
        this.equipments = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    private void setName(String name) {
        if (null == name || name.isBlank() || name.isEmpty()) {
            throw new NullPointerException(GAMEPLAY_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    @Override
    public int allProtection() {
        int sum = 0;
        for (Equipment equipment : equipments) {
            sum += equipment.getProtection();
        }
        return sum;
    }

    @Override
    public void addTeam(Team team) {
        teams.add(team);
    }

    @Override
    public void removeTeam(Team team) {
        teams.remove(team);
    }

    @Override
    public void addEquipment(Equipment equipment) {
        equipments.add(equipment);
    }

    @Override
    public void teamsInGameplay() {
        for (Team team : teams) {
            team.play();
        }
    }

    @Override
    public Collection<Team> getTeam() {
        return teams;
    }

    @Override
    public Collection<Equipment> getEquipments() {
        return equipments;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%s %s%n", name, getClass().getSimpleName()));
        stringBuilder.append("Team: ");
        if(teams.isEmpty()){
            stringBuilder.append("none");
        } else {
            teams.forEach(team -> stringBuilder.append(team.getName() + " "));
        }
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(String.format("Equipment: %d, Protection: %d", equipments.size(), allProtection()));
        return stringBuilder.toString().trim();
    }
}
