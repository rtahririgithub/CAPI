package com.telus.ait.integration.kb.info;

public class SubscriberInfo {
	private int ban;
    private long subscriptionId;
    private String subscriberNumber;

    public SubscriberInfo(int ban, String subscriberNumber, long subscriptionId) {
        this.ban = ban;
        this.subscriberNumber = subscriberNumber;
        this.subscriptionId = subscriptionId;
    }

    public int getBan() {
        return ban;
    }

    public String getSubscriberNumber() {
        return subscriberNumber;
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }
}
