package com.skydan.playerStatistics;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GOALKEEPER")
public class GoalkeeperStatistics extends PlayerStatistics {

    @Column(
            nullable = false,
            columnDefinition = "integer default 0"
    )
    private Integer cleanSheets;

    public GoalkeeperStatistics() {
        super();
    }

    public GoalkeeperStatistics(int currentTour, int wins, int goals, int cleanSheets) {
        super(currentTour, wins, goals);
        this.cleanSheets = cleanSheets;
    }

    public Integer getCleanSheets() {
        return cleanSheets;
    }

    public void setCleanSheets(Integer cleanSheets) {
        this.cleanSheets = cleanSheets;
    }
}
