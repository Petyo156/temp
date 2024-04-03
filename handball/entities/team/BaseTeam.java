package handball.entities.team;

import static handball.common.ExceptionMessages.*;

public abstract class BaseTeam implements Team {
    private String name;
    private String country;
    private int advantage;

    public BaseTeam(String name, String country, int advantage) {
        setName(name);
        setCountry(country);
        setAdvantage(advantage);
    }

    private void setCountry(String country) {
        if (null == country || country.isBlank() || country.isEmpty()) {
            throw new NullPointerException(TEAM_COUNTRY_NULL_OR_EMPTY);
        }
        this.country = country;
    }

    public void setAdvantage(int advantage) {
        if(advantage <= 0){
            throw new IllegalArgumentException(TEAM_ADVANTAGE_BELOW_OR_EQUAL_ZERO);
        }
        this.advantage = advantage;
    }

    @Override
    public void setName(String name) {
        if (null == name || name.isBlank() || name.isEmpty()) {
            throw new NullPointerException(TEAM_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    @Override
    public void play() {
        //?
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAdvantage() {
        return advantage;
    }
}
