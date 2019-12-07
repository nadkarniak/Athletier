package com.CS5520.athletier.Models;

public enum Tier {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE;

    public int toInt() {
        switch (this) {
            case FIVE:
                return 5;
            case FOUR:
                return 4;
            case THREE:
                return 3;
            case TWO:
                return 2;
            default:
                return 1;
        }
    }

    public Tier fromInt(int tierInt) {
        if (tierInt == 5) {
            return Tier.FIVE;
        } else if (tierInt == 4) {
            return Tier.FOUR;
        } else if (tierInt == 3) {
            return Tier.THREE;
        } else if (tierInt == 2) {
            return Tier.TWO;
        } else {
            return Tier.ONE;
        }
    }

    public static Tier fromExp(int exp) {
        if (exp <= 10) {
            return Tier.ONE;
        } else if (exp <= 20 ) {
            return Tier.TWO;
        } else if (exp <= 50) {
            return Tier.THREE;
        } else if (exp <= 100) {
            return Tier.FOUR;
        } else {
            return Tier.FIVE;
        }
    }

}
