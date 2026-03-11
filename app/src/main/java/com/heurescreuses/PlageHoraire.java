package com.heurescreuses;

public class PlageHoraire {
    public int startHour, startMin, endHour, endMin;

    public PlageHoraire(int sh, int sm, int eh, int em) {
        startHour = sh; startMin = sm; endHour = eh; endMin = em;
    }

    public int startTotal() { return startHour * 60 + startMin; }
    public int endTotal()   { return endHour * 60 + endMin; }

    public boolean contains(int nowMin) {
        int s = startTotal(), e = endTotal();
        if (s < e) return nowMin >= s && nowMin < e;
        else return nowMin >= s || nowMin < e; // crosses midnight
    }

    public String startStr() { return String.format("%02d:%02d", startHour, startMin); }
    public String endStr()   { return String.format("%02d:%02d", endHour, endMin); }

    // Serialization: "HH:MM-HH:MM"
    public String serialize() { return startStr() + "-" + endStr(); }

    public static PlageHoraire deserialize(String s) {
        try {
            String[] parts = s.split("-");
            String[] st = parts[0].split(":");
            String[] en = parts[1].split(":");
            return new PlageHoraire(
                Integer.parseInt(st[0]), Integer.parseInt(st[1]),
                Integer.parseInt(en[0]), Integer.parseInt(en[1])
            );
        } catch (Exception e) { return null; }
    }
}
