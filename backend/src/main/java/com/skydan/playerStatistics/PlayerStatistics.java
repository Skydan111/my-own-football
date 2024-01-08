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

    @Column(
            nullable = false
    )
    private Integer totalGames;

    @Column(
            nullable = false
    )
    private Integer wins;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "player_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "player_statistics_player_id_fk")
    )
    private Player player;

    public PlayerStatistics() {
    }

    public PlayerStatistics(Integer totalGames, Integer wins) {
        this.totalGames = totalGames;
        this.wins = wins;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(Integer totalGames) {
        this.totalGames = totalGames;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
