package com.skydan.player;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PlayerDTOMapper implements Function<Player, PlayerDTO> {

    @Override
    public PlayerDTO apply(Player player) {
        return new PlayerDTO(
                player.getName(),
                player.getTeam(),
                player.getPosition()
        );
    }
}
