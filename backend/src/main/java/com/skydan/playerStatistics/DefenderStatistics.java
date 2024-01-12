package com.skydan.playerStatistics;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DEFENDER")
public class DefenderStatistics extends PlayerStatistics {

    @Column(
            nullable = false,
            columnDefinition = "integer default 0"
    )
    private Integer tackles;

    public DefenderStatistics() {
        super();
    }

    public DefenderStatistics(int currentTour, int wins, int goals, int tackles) {
        super(currentTour, wins, goals);
        this.tackles = tackles;
    }

    public Integer getTackles() {
        return tackles;
    }

    public void setTackles(Integer tackles) {
        this.tackles = tackles;
    }
}
