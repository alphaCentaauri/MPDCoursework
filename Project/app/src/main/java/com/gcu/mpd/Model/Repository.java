package com.gcu.mpd.Model;

import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Mobile Platform Development
 * @author Iyosiyas Workie Mitiku - S1803446
 */
public class Repository{
    static public List<Region> regions;

    public Repository() {
        this.regions = new ArrayList<>();
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public void addRegion(Region region) {
        this.regions.add(region);
    }

    public void listRegions(){
        for(Region region: regions){
            System.out.println(region.title.toString());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repository that = (Repository) o;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.equals(regions, that.regions);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(regions);
        }
        return -1;
    }
}
