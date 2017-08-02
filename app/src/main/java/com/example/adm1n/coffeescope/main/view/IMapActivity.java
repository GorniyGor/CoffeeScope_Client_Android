package com.example.adm1n.coffeescope.main.view;

import com.example.adm1n.coffeescope.model.Place;

import java.util.ArrayList;

/**
 * Created by adm1n on 01.08.2017.
 */

public interface IMapActivity {
    void setMarkers(ArrayList<Place> list);

    void showError(String s);

    void showPeakView(Place place);
}
