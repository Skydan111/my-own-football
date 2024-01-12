package com.skydan.playerStatistics;

import com.skydan.player.Player;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "position_type")
public class PlayerStatistics {

    @Id
    @SequenceGenerator(
            name = "player_id_sequence",
            sequenceName = "player_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "player_id_sequence"
    )
    private Integer id;

    @Column(nullable = false)
    private Integer currentTour;

    @Column(
            nullable = false,
            columnDefinition = "integer default 0"
    )
    private Integer wins;

    @Column(
            nullable = false,
            columnDefinition = "integer default 0"
    )
    private Integer goals;

    @ManyToOne
    @JoinColumn(
            name = "player_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "player_statistics_player_id_fk")
    )
    private Player player;

    public PlayerStatistics() {
    }

    public PlayerStatistics(Integer currentTour, Integer wins, Integer goals) {
        this.currentTour = currentTour;
        this.wins = wins;
        this.goals = goals;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCurrentTour() {
        return currentTour;
    }

    public void setCurrentTour(int currentTour) {
        this.currentTour = currentTour;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
