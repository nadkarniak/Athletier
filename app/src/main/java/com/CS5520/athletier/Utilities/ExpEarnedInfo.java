package com.CS5520.athletier.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class ExpEarnedInfo implements Parcelable {
    private String sportName;
    private int expEarned;
    private int tier;

    public ExpEarnedInfo(String sportName, int expEarned, int tier) {
        this.sportName = sportName;
        this.expEarned = expEarned;
        this.tier = tier;
    }

    protected ExpEarnedInfo(Parcel in) {
        sportName = in.readString();
        expEarned = in.readInt();
        tier = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sportName);
        dest.writeInt(expEarned);
        dest.writeInt(tier);
    }

    public String getSportName() { return this.sportName; }
    public int getExpEarned() { return this.expEarned; }
    public int getTier() { return this.tier; }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExpEarnedInfo> CREATOR = new Creator<ExpEarnedInfo>() {
        @Override
        public ExpEarnedInfo createFromParcel(Parcel in) {
            return new ExpEarnedInfo(in);
        }

        @Override
        public ExpEarnedInfo[] newArray(int size) {
            return new ExpEarnedInfo[size];
        }
    };
}
