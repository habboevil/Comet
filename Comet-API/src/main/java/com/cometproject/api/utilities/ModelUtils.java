package com.cometproject.api.utilities;

public class ModelUtils {
    public static char heightToMap(int height) {
        return heightToMap((short) height);
    }

    public static char heightToMap(short height) {
        char[] mapping = "123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
        if (height >= 1 && height <= 35) {
            return mapping[height - 1];
        } else {
            return '0';
        }
    }

    public static int getHeight(char c) {
        return switch (c) {
            case '1' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            case 'a' -> 10;
            case 'b' -> 11;
            case 'c' -> 12;
            case 'd' -> 13;
            case 'e' -> 14;
            case 'f' -> 15;
            case 'g' -> 16;
            case 'h' -> 17;
            case 'i' -> 18;
            case 'j' -> 19;
            case 'k' -> 20;
            case 'l' -> 21;
            case 'm' -> 22;
            case 'n' -> 23;
            case 'o' -> 24;
            case 'p' -> 25;
            case 'q' -> 26;
            case 'r' -> 27;
            case 's' -> 28;
            case 't' -> 29;
            case 'u' -> 30;
            case 'v' -> 31;
            case 'w' -> 32;
            case 'x' -> 33;
            case 'y' -> 34;
            case 'z' -> 35;
            default -> 0;
        };
    }
}
