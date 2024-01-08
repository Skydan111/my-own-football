package com.skydan.playerStatistics;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GOALKEEPER")
public class GoalkeeperStatistics extends PlayerStatistics {

    private Integer cleanSheets;

    public GoalkeeperStatistics() {
        super();
    }

    public GoalkeeperStatistics(int totalGames, int wins, int cleanSheets) {
        super(totalGames, wins);
        this.cleanSheets = cleanSheets;
    }

    public Integer getCleanSheets() {
        return cleanSheets;
    }

    public void setCleanSheets(Integer cleanSheets) {
        this.cleanSheets = cleanSheets;
    }
}
