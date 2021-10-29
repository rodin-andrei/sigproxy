// 
// Decompiled by Procyon v0.5.36
// 

package com.unifun.sigtran.ussdgateway.gw.stats;

import java.util.concurrent.atomic.AtomicInteger;

public class DialogStats {
    private final AtomicInteger active;
    private final AtomicInteger started;
    private final AtomicInteger rejected;
    private final AtomicInteger userAborted;
    private final AtomicInteger providerAborted;
    private final AtomicInteger released;
    private final AtomicInteger timedOut;
    private final AtomicInteger closed;

    public DialogStats() {
        this.active = new AtomicInteger(0);
        this.started = new AtomicInteger(0);
        this.rejected = new AtomicInteger(0);
        this.userAborted = new AtomicInteger(0);
        this.providerAborted = new AtomicInteger(0);
        this.released = new AtomicInteger(0);
        this.timedOut = new AtomicInteger(0);
        this.closed = new AtomicInteger(0);
    }

    public void reset() {
        this.active.set(0);
        this.started.set(0);
        this.rejected.set(0);
        this.userAborted.set(0);
        this.providerAborted.set(0);
        this.released.set(0);
        this.timedOut.set(0);
        this.closed.set(0);
    }

    public int getActive() {
        return this.active.get();
    }

    public int getStarted() {
        return this.started.get();
    }

    public void updateStarted() {
        this.started.incrementAndGet();
        this.active.incrementAndGet();
    }

    public int getRejected() {
        return this.rejected.get();
    }

    public void updateRejected() {
        this.rejected.incrementAndGet();
        this.active.decrementAndGet();
    }

    public int getUserAborted() {
        return this.userAborted.get();
    }

    public void updateUserAborted() {
        this.userAborted.incrementAndGet();
        this.active.decrementAndGet();
    }

    public int getProviderAborted() {
        return this.providerAborted.get();
    }

    public void updateProviderAborted() {
        this.providerAborted.incrementAndGet();
        this.active.decrementAndGet();
    }

    public int getReleased() {
        return this.released.get();
    }

    public void updateReleased() {
        this.released.incrementAndGet();
        this.active.decrementAndGet();
    }

    public int getTimeout() {
        return this.timedOut.get();
    }

    public void updateTimedOut() {
        this.timedOut.incrementAndGet();
        this.active.decrementAndGet();
    }

    public int getClosed() {
        return this.closed.get();
    }

    public void updateClosed() {
        this.closed.incrementAndGet();
        this.active.decrementAndGet();
    }
}
