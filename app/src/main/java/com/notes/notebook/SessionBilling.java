package com.notes.notebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionBilling {

    private SharedPreferences prefs;


    public SessionBilling(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setPremium(boolean isPremium) {
        prefs.edit().putBoolean("Premium", isPremium).commit();
    }

    public boolean isPremium() {
        boolean isPremium = prefs.getBoolean("Premium",false);
        return isPremium;
    }
}
