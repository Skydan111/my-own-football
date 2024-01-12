package com.skydan.playerStatistics;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FORWARD")
public class ForwardStatistics extends PlayerStatistics {

    @Column(
            nullable = false,
            columnDefinition = "integer default 0"
    )
    private Integer assists;

    public ForwardStatistics() {
        super();
    }

    public ForwardStatistics(int currentTour, int wins, int goals, int assists) {
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
