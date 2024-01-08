package com.skydan.playerStatistics;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DEFENDER")
public class DefenderStatistics extends PlayerStatistics {

    private Integer tackles;

    public DefenderStatistics() {
        super();
    }

    public DefenderStatistics(int totalGames, int wins, int tackles) {
        super(totalGames, wins);
        this.tackles = tackles;
    }

    public Integer getTackles() {
        return tackles;
    }

    public void setTackles(Integer tackles) {
        this.tackles = tackles;
    }
}
