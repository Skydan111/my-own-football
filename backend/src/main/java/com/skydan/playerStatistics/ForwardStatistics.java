package com.skydan.playerStatistics;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FORWARD")
public class ForwardStatistics extends PlayerStatistics {

    private Integer goals;

    public ForwardStatistics() {
        super();
    }

    public ForwardStatistics(int totalGames, int wins, int goals) {
        super(totalGames, wins);
        this.goals = goals;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }
}
