package com.unifun.sigproxy.models.config.m3ua;

public enum TrafficModeType {
    Override(1), Loadshare(2), Broadcast(3);

    private final int type;

    TrafficModeType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}