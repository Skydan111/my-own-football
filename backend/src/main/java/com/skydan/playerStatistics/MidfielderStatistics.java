package com.skydan.playerStatistics;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MIDFIELDER")
public class MidfielderStatistics extends PlayerStatistics {

    @Column(
            nullable = false,
            columnDefinition = "integer default 0"
    )
    private Integer assists;

    public MidfielderStatistics() {
        super();
    }

    public MidfielderStatistics(int currentTour, int wins, int goals, int assists) {
        super(currentTour, wins, goals);
        this.assists = assists;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }
}
