package com.arena.ui;

import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

/** Pixel-art patterns. Each pattern is a String[] grid; each char maps to a palette color. */
public class Sprites {

    // --- Patterns (14 wide x 16 tall) ---

    public static final String[] KNIGHT = {
        "..............",
        "....DDDDDD....",
        "...DSSSSSSD...",
        "..DSSSSSSSSD..",
        "..DSKSSSSKSD..",
        "..DSSSSSSSSD..",
        "..DSSSSSSSSD..",
        "...DSSSSSSD...",
        "....DDDDDD....",
        "...BBBBBBBB...",
        "..BBRRRRRRBB..",
        "..BBRRRRRRBB..",
        "..BBBBBBBBBB..",
        "...BB....BB...",
        "...DD....DD...",
        "...DD....DD..."
    };

    public static final String[] MAGE = {
        "..............",
        "....PPPPPP....",
        "...PWWWWWWP...",
        "..PWWWWWWWWP..",
        "..PWKWWWWKWP..",
        "..PWWWWWWWWP..",
        "..PWWWWWWWWP..",
        "...PWWWWWWP...",
        "....PPPPPP....",
        "...PPPPPPPP...",
        "..PPPPPPPPPP..",
        "..PPYYYYYYPP..",
        "..PPPPPPPPPP..",
        "...PP....PP...",
        "...PP....PP...",
        "...DD....DD..."
    };

    public static final String[] GOBLIN = {
        "..............",
        "....GGGGGG....",
        "...GGGGGGGG...",
        "..GGKGGGGKGG..",
        "..GGGGGGGGGG..",
        "..GGDDDDDDGG..",
        "...GGGGGGGG...",
        "....GGGGGG....",
        "...GGGGGGGG...",
        "..GGBBBBBBGG..",
        "..GGBBBBBBGG..",
        "..GGGGGGGGGG..",
        "...GG....GG...",
        "...GG....GG...",
        "...DD....DD...",
        "...DD....DD..."
    };

    public static final String[] ORC = {
        "..............",
        "...GGGGGGGG...",
        "..GGGGGGGGGG..",
        "..GGKGGGGKGG..",
        "..GGGGGGGGGG..",
        "..GWDDDDDDWG..",
        "...GGGGGGGG...",
        "..RRRRRRRRRR..",
        ".RRRRRRRRRRRR.",
        ".RRRRRRRRRRRR.",
        ".RRRRRRRRRRRR.",
        ".RRRRRRRRRRRR.",
        "..RRRRRRRRRR..",
        "...GG....GG...",
        "...GG....GG...",
        "...DD....DD..."
    };

    public static final String[] DARKMAGE = {
        "..............",
        "....PPPPPP....",
        "...PDDDDDDP...",
        "..PDDDDDDDDP..",
        "..PDKDDDDKDP..",
        "..PDDDDDDDDP..",
        "..PDDDDDDDDP..",
        "...PDDDDDDP...",
        "....PPPPPP....",
        "...PPPPPPPP...",
        "..PPKKKKKKPP..",
        "..PPKKKKKKPP..",
        "..PPPPPPPPPP..",
        "...PP....PP...",
        "...PP....PP...",
        "...DD....DD..."
    };

    public static final String[] SKELETON = {
        "..............",
        "....WWWWWW....",
        "...WWWWWWWW...",
        "..WWKWWWWKWW..",
        "..WWWWWWWWWW..",
        "..WWDDDDDDWW..",
        "...WWWWWWWW...",
        "....WWWWWW....",
        "...WWWWWWWW...",
        "..WWWWWWWWWW..",
        ".WWWWWWWWWWWW.",
        ".WWWWWWWWWWWW.",
        "..WWWWWWWWWW..",
        "...WW....WW...",
        "...WW....WW...",
        "...WW....WW..."
    };

    public static final String[] DEMON = {
        "..............",
        ".D..........D.",
        ".DD........DD.",
        "..DDDDDDDDDD..",
        ".DRDDDDDDDDRD.",
        ".DRDDDDDDDDRD.",
        "..DDDDDDDDDD..",
        "..DDRRRRRRDD..",
        "...DDDDDDDD...",
        "..RRRRRRRRRR..",
        ".RRRRRRRRRRRR.",
        ".RRRRRRRRRRRR.",
        ".RRRRRRRRRRRR.",
        "..RRRRRRRRRR..",
        "...RR....RR...",
        "...DD....DD..."
    };

    public static String[] get(String key) {
        switch (key) {
            case "knight":   return KNIGHT;
            case "mage":     return MAGE;
            case "goblin":   return GOBLIN;
            case "orc":      return ORC;
            case "darkmage": return DARKMAGE;
            case "skeleton": return SKELETON;
            case "demon":    return DEMON;
            default:         return KNIGHT;
        }
    }

    public static Map<Character, Color> palette() {
        Map<Character, Color> p = new HashMap<>();
        p.put('.', Color.TRANSPARENT);
        p.put('D', Color.web("#1e293b"));
        p.put('S', Color.web("#cbd5e1"));
        p.put('K', Color.web("#0f172a"));
        p.put('B', Color.web("#3b82f6"));
        p.put('R', Color.web("#dc2626"));
        p.put('G', Color.web("#22c55e"));
        p.put('W', Color.web("#fef3c7"));
        p.put('P', Color.web("#a855f7"));
        p.put('Y', Color.web("#facc15"));
        p.put('O', Color.web("#f97316"));
        return p;
    }
}