package com.skydan.player;

import com.skydan.user.Team;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<PlayerDTO> getPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/team/{team}")
    public List<PlayerDTO> getTeamPlayers(@PathVariable("team") Team team) {
        return playerService.getAllTeamPlayers(team);
    }

    @GetMapping("/position/{position}")
    public List<PlayerDTO> getPlayersWithPosition(@PathVariable("position") Position position) {
        return playerService.getAllPlayersWithPosition(position);
    }
}
