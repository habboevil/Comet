package com.cometproject.api.events;

public abstract class EventArgs {

    private boolean cancelled = false;

    public boolean isCancelled() {
        return false;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
