package com.gcu.mpd.Model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Mobile Platform Development
 * @author Iyosiyas Workie Mitiku - S1803446
 */
public enum  RegionEnum {
    REGION1("North East","date here"),
    REGION2("South West","date here");

    public String mTitle;
    public String date;

    public static final ArrayList<RegionEnum> regions;

    static {
        regions = new ArrayList<>();
        Collections.addAll(regions, RegionEnum.values());
    }

    RegionEnum(String name, String date) {
        this.mTitle = name;
        this.date = date;
    }
}
