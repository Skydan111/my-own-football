package com.skydan.player;

import com.skydan.playerStatistics.*;
import com.skydan.user.Team;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private static final int WINS_MULTIPLIER = 3;
    private static final int GOALS_MULTIPLIER = 2;
    private static final int CLEAN_SHEETS_MULTIPLIER = 4;
    private static final int ASSISTS_MULTIPLIER = 2;

    private final PlayerRepository playerRepository;
    private final PlayerDTOMapper playerDTOMapper;

    public PlayerService(PlayerRepository playerRepository, PlayerDTOMapper playerDTOMapper) {
        this.playerRepository = playerRepository;
        this.playerDTOMapper = playerDTOMapper;
    }

    public List<PlayerDTO> getAllPlayers () {
        return playerRepository.findAll()
                .stream()
                .map(playerDTOMapper)
                .collect(Collectors.toList());
    }

    public List<PlayerDTO> getAllTeamPlayers(Team team) {
        return playerRepository.findPlayersByTeam(team)
                .stream()
                .map(playerDTOMapper)
                .collect(Collectors.toList());
    }

    public List<PlayerDTO> getAllPlayersWithPosition(Position position) {
        return playerRepository.findPlayersByPosition(position)
                .stream()
                .map(playerDTOMapper)
                .collect(Collectors.toList());
    }

    public int calculateTotalPoints(Player player) {
        int totalPoints = 0;
        for (PlayerStatistics statistics : player.getStatsList()) {
            totalPoints += calculatePointsForStatistics(statistics);
        }
        return totalPoints;
    }

    public int calculatePointsForCurrentTour(Player player, int currentTour) {
        int totalPoints = 0;
        for (PlayerStatistics statistics : player.getStatsList()) {
            if (statistics.getCurrentTour() == currentTour) {
                totalPoints += calculatePointsForStatistics(statistics);
            }
        }
        return totalPoints;
    }


    private int calculatePointsForStatistics(PlayerStatistics statistics) {
        return switch (statistics.getPlayer().getPosition()) {
            case GOALKEEPER -> calculateGoalkeeperPoints((GoalkeeperStatistics) statistics);
            case DEFENDER -> calculateDefenderPoints((DefenderStatistics) statistics);
            case MIDFIELDER -> calculateMidfielderPoints((MidfielderStatistics) statistics);
            case FORWARD -> calculateForwardPoints((ForwardStatistics) statistics);
            default -> throw new IllegalArgumentException("Invalid position");
        };
    }

    private int calculateGoalkeeperPoints(GoalkeeperStatistics goalkeeperStatistics) {
        return goalkeeperStatistics.getWins() * WINS_MULTIPLIER +
                goalkeeperStatistics.getGoals() * GOALS_MULTIPLIER +
                goalkeeperStatistics.getCleanSheets() * CLEAN_SHEETS_MULTIPLIER;
    }

    private int calculateDefenderPoints(DefenderStatistics defenderStatistics) {
        return defenderStatistics.getWins() * WINS_MULTIPLIER +
                defenderStatistics.getGoals() * GOALS_MULTIPLIER +
                defenderStatistics.getTackles();
    }

    private int calculateMidfielderPoints(MidfielderStatistics midfielderStatistics) {
        return midfielderStatistics.getWins() * WINS_MULTIPLIER +
                midfielderStatistics.getGoals() * GOALS_MULTIPLIER +
                midfielderStatistics.getAssists() * ASSISTS_MULTIPLIER;
    }

    private int calculateForwardPoints(ForwardStatistics forwardStatistics) {
        return forwardStatistics.getWins() * WINS_MULTIPLIER +
                forwardStatistics.getGoals() * GOALS_MULTIPLIER +
                forwardStatistics.getAssists() * ASSISTS_MULTIPLIER;
    }
}
