package com.skydan.playerStatistics;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MIDFIELDER")
public class MidfielderStatistics extends PlayerStatistics {

    private Integer assists;

    public MidfielderStatistics() {
        super();
    }

    public MidfielderStatistics(int totalGames, int wins, int assists) {
        super(totalGames, wins);
        this.assists = assists;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }
}
