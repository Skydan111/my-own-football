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

    @Transient
    private int totalPoints;


    @OneToMany(
            mappedBy = "player",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<PlayerStatistics> statsList = new ArrayList<>();

    @ManyToMany(mappedBy = "players")
    private List<AppUser> users = new ArrayList<>();

    public Player() {
    }

    public Player(String name, Position position, Team team) {
        this.name = name;
        this.position = position;
        this.team = team;
        this.totalPoints = 0;
        PlayerStatistics statistics = createStatistics();
        addStatistics(statistics);
    }

    public Player(String name, Position position, Team team, PlayerStatistics playerStatistics) {
        this.name = name;
        this.position = position;
        this.team = team;
        this.totalPoints = 0;
        PlayerStatistics statistics = playerStatistics != null ? playerStatistics : createStatistics();
        addStatistics(statistics);
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

    public int getTotalPoints(PlayerService playerService) {
        calculateTotalPoints(playerService);
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<AppUser> getUsers() {
        return users;
    }

    public void addStatistics(PlayerStatistics statistics) {
        statistics.setPlayer(this);
        statsList.add(statistics);
    }

    public void removeStatistics(PlayerStatistics statistics) {
        if (statistics != null && statsList.contains(statistics)) {
            statistics.setPlayer(null);
            statsList.remove(statistics);
        }
    }

    public List<PlayerStatistics> getStatsList() {
        return statsList;
    }

    public void setStatsList(List<PlayerStatistics> statsList) {
        this.statsList = statsList;
    }

    public void calculateTotalPoints(PlayerService playerService) {
        totalPoints = playerService.calculateTotalPoints(this);
    }

    public int calculatePointsForCurrentTour(PlayerService playerService, int currentTour) {
        return playerService.calculatePointsForCurrentTour(this, currentTour);
    }

    private PlayerStatistics createStatistics() {
        PlayerStatistics statistics = switch (position) {
            case GOALKEEPER -> new GoalkeeperStatistics();
            case DEFENDER -> new DefenderStatistics();
            case MIDFIELDER -> new MidfielderStatistics();
            case FORWARD -> new ForwardStatistics();
            default -> throw new IllegalArgumentException("Invalid position");
        };

        addStatistics(statistics);
        return statistics;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", team=" + team +
                ", totalPoints=" + totalPoints +
                '}';
    }
}
