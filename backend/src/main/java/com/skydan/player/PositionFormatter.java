package com.skydan.player;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class PositionFormatter implements Formatter<Position> {

    @Override
    public Position parse(String text, Locale locale) throws ParseException {
        return Position.valueOf(text.toUpperCase());
    }

    @Override
    public String print(Position object, Locale locale) {
        return object.name().toLowerCase();
    }
}
