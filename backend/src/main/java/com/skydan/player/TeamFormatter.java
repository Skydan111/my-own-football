package com.skydan.player;

import com.skydan.user.Team;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class TeamFormatter implements Formatter<Team> {

    @Override
    public Team parse(String text, Locale locale) throws ParseException {
        return Team.valueOf(text.toUpperCase());
    }

    @Override
    public String print(Team object, Locale locale) {
        return object.name().toLowerCase();
    }
}
