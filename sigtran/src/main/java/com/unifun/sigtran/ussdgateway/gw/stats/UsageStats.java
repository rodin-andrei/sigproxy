// 
// Decompiled by Procyon v0.5.36
// 

package com.unifun.sigtran.ussdgateway.gw.stats;

import org.springframework.stereotype.Component;

@Component
public class UsageStats {
    private final DialogStats incDialogs;
    private final DialogStats outDialogs;

    public UsageStats() {
        this.incDialogs = new DialogStats();
        this.outDialogs = new DialogStats();
    }

    public DialogStats getIncDialogs() {
        return this.incDialogs;
    }

    public DialogStats getOutDialogs() {
        return this.outDialogs;
    }
}
