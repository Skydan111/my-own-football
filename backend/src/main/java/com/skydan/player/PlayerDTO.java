package com.skydan.player;

import com.skydan.user.Team;

public record PlayerDTO(
        String name,
        Team team,
        Position position
) {
}
