package com.skydan.player;

import com.skydan.playerStatistics.*;
import com.skydan.user.AppUser;
import com.skydan.user.Team;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "player")
public class Player {

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
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Enumerated(EnumType.STRING)
    private Team team;

    @OneToOne(mappedBy = "player")
    private PlayerStatistics playerStatistics;

    @ManyToMany(mappedBy = "players")
    private List<AppUser> users = new ArrayList<>();

    public Player() {
    }

    public Player(String name, Position position, Team team) {
        this.name = name;
        this.position = position;
        this.team = team;
        this.playerStatistics = createStatistics();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<AppUser> getUsers() {
        return users;
    }

    public PlayerStatistics getPlayerStatistics() {
        return playerStatistics;
    }

    public void setPlayerStatistics(PlayerStatistics playerStatistics) {
        this.playerStatistics = playerStatistics;
    }

    private PlayerStatistics createStatistics() {
        return switch (position) {
            case GOALKEEPER -> new GoalkeeperStatistics();
            case DEFENDER -> new DefenderStatistics();
            case MIDFIELDER -> new MidfielderStatistics();
            case FORWARD -> new ForwardStatistics();
            default -> throw new IllegalArgumentException("Invalid position");
        };
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", team=" + team +
                ", playerStatistics=" + playerStatistics +
                '}';
    }
}
