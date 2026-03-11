package com.heurescreuses;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class PrefsManager {
    private static final String PREFS = "hc_prefs";
    private static final String KEY_PLAGES = "plages";
    private static final String DEFAULT = "22:30-06:30";

    public static List<PlageHoraire> loadPlages(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String raw = sp.getString(KEY_PLAGES, DEFAULT);
        List<PlageHoraire> list = new ArrayList<>();
        if (raw == null || raw.isEmpty()) return list;
        for (String part : raw.split("\\|")) {
            PlageHoraire p = PlageHoraire.deserialize(part.trim());
            if (p != null) list.add(p);
        }
        return list;
    }

    public static void savePlages(Context ctx, List<PlageHoraire> plages) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plages.size(); i++) {
            if (i > 0) sb.append("|");
            sb.append(plages.get(i).serialize());
        }
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
           .edit().putString(KEY_PLAGES, sb.toString()).apply();
    }

    public static boolean isCreuse(Context ctx) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int nowMin = cal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + cal.get(java.util.Calendar.MINUTE);
        for (PlageHoraire p : loadPlages(ctx)) {
            if (p.contains(nowMin)) return true;
        }
        return false;
    }

    /** Returns the minute-of-day of the next transition, or -1 if no plages */
    public static int nextChange(Context ctx) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int nowMin = cal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + cal.get(java.util.Calendar.MINUTE);
        boolean creuse = isCreuse(ctx);
        List<PlageHoraire> plages = loadPlages(ctx);
        if (plages.isEmpty()) return -1;
        int best = Integer.MAX_VALUE;
        for (PlageHoraire p : plages) {
            int target = creuse ? p.endTotal() : p.startTotal();
            int diff = (target - nowMin + 1440) % 1440;
            if (diff == 0) diff = 1440;
            if (diff < best) best = diff;
        }
        return (nowMin + best) % 1440;
    }
}
