package com.CS5520.athletier.Models;

import java.util.ArrayList;
import java.util.List;

public enum State {
    AL, AK, AZ, AR, CA, CO, CT, DE, FL, GA, HI, ID, IL, IN, IA, KS, KY, LA, ME, MD, MA, MI, MN, MS,
    MO, MT, NE, NV, NH, NJ, NM, NY, NC, ND, OH, OK, OR, PA, RI, SC, SD, TN, TX, UT, VT, VA, WA, WV,
    WI, WY;

    public static List<String> getAllStateNames() {
        State[] states = State.values();
        List<String> stateStrings = new ArrayList<>();
        for (State state : states) {
            stateStrings.add(state.name());
        }
        return stateStrings;
    }
}
