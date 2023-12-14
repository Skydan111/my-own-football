package com.skydan.user;

import java.util.List;

public record AppUserDTO (
        Integer id,
        String name,
        String email,
        Integer age,
        Team team,
        List<String> roles,
        String username
){
}
