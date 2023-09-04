package com.example.pizzapp.utils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class OrariDisponibili {
    private static List<LocalTime> available = new ArrayList<>();

    public static List<LocalTime> getList(final int startHour, final int startMin, List<LocalTime> toRemove) {
        available.clear();
        int m = startMin;
        int h = startHour;
        if (h < 18 || h == 18 && m <= 7) {
            h = 18;
            m = 15;
        } else if (startMin >= 0 && startMin < 15) {
            m = 30;
        } else if (startMin >= 15 && startMin < 30) {
            m = 45;
        } else if (startMin >= 30 && startMin < 45) {
            h++;
            m = 00;
        } else {
            h++;
            m = 15;
        }
        for( ; h <= 21; h++) {
            for( ; m < 60; m += 15) {
                available.add(LocalTime.of(h, m));
            }
            m = 0;
        }
        if(toRemove != null){
            available.removeIf(toRemove::contains);
        }
        return available;
    }

}
