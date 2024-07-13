package com.ssomar.score.utils.emums;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import static org.bukkit.Color.AQUA;

public class CustomColor {

    public static Color valueOf(String s) {

        s = s.toUpperCase();
        switch (s) {
            case "AQUA":
                return Color.AQUA;
            case "BLACK":
                return Color.BLACK;
            case "BLUE":
                return Color.BLUE;
            case "FUCHSIA":
                return Color.FUCHSIA;
            case "GRAY":
                return Color.GRAY;
            case "GREEN":
                return Color.GREEN;
            case "LIME":
                return Color.LIME;
            case "MAROON":
                return Color.MAROON;
            case "NAVY":
                return Color.NAVY;
            case "OLIVE":
                return Color.OLIVE;
            case "ORANGE":
                return Color.ORANGE;
            case "PURPLE":
                return Color.PURPLE;
            case "RED":
                return Color.RED;
            case "SILVER":
                return Color.SILVER;
            case "TEAL":
                return Color.TEAL;
            case "WHITE":
                return Color.WHITE;
            case "YELLOW":
                return Color.YELLOW;
            case "NO_COLOR":
                return getNullColor();
            default:
                return null;
        }
    }

    public static Color getNullColor() {
        return Color.fromARGB(0,0,0,0);
    }

    public static String getName(Color color) {
        if (AQUA.equals(color)) {
            return "AQUA";
        } else if (color.equals(Color.BLACK)) {
            return "BLACK";
        } else if (color.equals(Color.BLUE)) {
            return "BLUE";
        } else if (color.equals(Color.FUCHSIA)) {
            return "FUCHSIA";
        } else if (color.equals(Color.GRAY)) {
            return "GRAY";
        } else if (color.equals(Color.GREEN)) {
            return "GREEN";
        } else if (color.equals(Color.LIME)) {
            return "LIME";
        } else if (color.equals(Color.MAROON)) {
            return "MAROON";
        } else if (color.equals(Color.NAVY)) {
            return "NAVY";
        } else if (color.equals(Color.OLIVE)) {
            return "OLIVE";
        } else if (color.equals(Color.ORANGE)) {
            return "ORANGE";
        } else if (color.equals(Color.PURPLE)) {
            return "PURPLE";
        } else if (color.equals(Color.RED)) {
            return "RED";
        } else if (color.equals(Color.SILVER)) {
            return "SILVER";
        } else if (color.equals(Color.TEAL)) {
            return "TEAL";
        } else if (color.equals(Color.WHITE)) {
            return "WHITE";
        } else if (color.equals(Color.YELLOW)) {
            return "YELLOW";
        } else if (color.equals(getNullColor())) {
            return "NO_COLOR";
        }
        return "AQUA";
    }

    public static Color[] values() {
        return new Color[]{
                AQUA,
                Color.BLACK,
                Color.BLUE,
                Color.FUCHSIA,
                Color.GRAY,
                Color.GREEN,
                Color.LIME,
                Color.MAROON,
                Color.NAVY,
                Color.OLIVE,
                Color.ORANGE,
                Color.PURPLE,
                Color.RED,
                Color.SILVER,
                Color.TEAL,
                Color.WHITE,
                Color.YELLOW,
                getNullColor()
        };
    }

    public static Color getNext(Color color) {
        if (AQUA.equals(color)) {
            return Color.BLACK;
        } else if (color.equals(Color.BLACK)) {
            return Color.BLUE;
        } else if (color.equals(Color.BLUE)) {
            return Color.FUCHSIA;
        } else if (color.equals(Color.FUCHSIA)) {
            return Color.GRAY;
        } else if (color.equals(Color.GRAY)) {
            return Color.GREEN;
        } else if (color.equals(Color.GREEN)) {
            return Color.LIME;
        } else if (color.equals(Color.LIME)) {
            return Color.MAROON;
        } else if (color.equals(Color.MAROON)) {
            return Color.NAVY;
        } else if (color.equals(Color.NAVY)) {
            return Color.OLIVE;
        } else if (color.equals(Color.OLIVE)) {
            return Color.ORANGE;
        } else if (color.equals(Color.ORANGE)) {
            return Color.PURPLE;
        } else if (color.equals(Color.PURPLE)) {
            return Color.RED;
        } else if (color.equals(Color.RED)) {
            return Color.SILVER;
        } else if (color.equals(Color.SILVER)) {
            return Color.TEAL;
        } else if (color.equals(Color.TEAL)) {
            return Color.WHITE;
        } else if (color.equals(Color.WHITE)) {
            return Color.YELLOW;
        } else if (color.equals(Color.YELLOW)) {
            return getNullColor();
        } else {
            return Color.AQUA;
        }
    }

    public static Color fromChatColor(ChatColor color) {
        switch (color){
            case BLACK:
                return Color.BLACK;
            case DARK_BLUE:
                return Color.NAVY;
            case DARK_GREEN:
                return Color.GREEN;
            case DARK_AQUA:
                return Color.TEAL;
            case DARK_RED:
                return Color.MAROON;
            case DARK_PURPLE:
                return Color.PURPLE;
            case GOLD:
                return Color.ORANGE;
            case GRAY:
                return Color.GRAY;
            case DARK_GRAY:
                return Color.SILVER;
            case BLUE:
                return Color.BLUE;
            case GREEN:
                return Color.LIME;
            case AQUA:
                return Color.AQUA;
            case RED:
                return Color.RED;
            case LIGHT_PURPLE:
                return Color.FUCHSIA;
            case YELLOW:
                return Color.YELLOW;
            case WHITE:
                return Color.WHITE;
            case MAGIC:
                return AQUA;
            case BOLD:
                return AQUA;
            case STRIKETHROUGH:
                return AQUA;
            case UNDERLINE:
                return AQUA;
            case ITALIC:
                return AQUA;
            case RESET:
                return AQUA;
            default:
                return AQUA;
        }
    }
}
