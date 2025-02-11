package com.telus.cmb.subscriber.kafka.json.mapper.v2;

import com.telus.cmb.subscriber.kafka.domain.ChangeEventInfo;

/**
 * Responsible for composing/mapping between source obj and target obj.
 * Once finished its own composing/mapping, pass the target obj over to the 'next' EventComposer if exists.
 *
 */
public abstract class EventComposer {

    protected final String eventSubType;
    
    private EventComposer next;
    
    public EventComposer(String eventSubType) {
        this.eventSubType = eventSubType;
    }

    public EventComposer next(EventComposer next) {
        this.next = next;
        return next;
    }

    public final ChangeEventInfo compose(ChangeEventInfo target) {
        
        composeEvent(target);
        
        if (next != null) {
            return next.compose(target);
        }
        
        return target;
    }

    protected abstract void composeEvent(ChangeEventInfo target);
    
}
