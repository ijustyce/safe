package com.ijustyce.launcher;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.ijustyce.safe.R;
import com.ijustyce.unit.toast;

public class MenuFragment extends PreferenceFragment 
implements OnPreferenceClickListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
       
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        addPreferencesFromResource(R.xml.menu);

        findPreference("a").setOnPreferenceClickListener(this);
        findPreference("b").setOnPreferenceClickListener(this);
        findPreference("n").setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if("a".equals(key)) {
        	toast.show("haha", this.getActivity());         
        }
  //      ((MainActivity)getActivity()).getSlidingMenu().toggle();
        return false;
    }
}
