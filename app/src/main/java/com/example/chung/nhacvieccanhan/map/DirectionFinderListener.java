package com.example.chung.nhacvieccanhan.map;

import java.util.List;

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
