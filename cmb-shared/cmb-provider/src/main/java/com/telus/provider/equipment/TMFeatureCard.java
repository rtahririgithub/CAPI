package com.telus.provider.equipment;

import com.telus.api.equipment.FeatureCard;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.provider.TMProvider;

public class TMFeatureCard extends TMCard implements FeatureCard {

	private static final long serialVersionUID = 1L;

	public TMFeatureCard(TMProvider provider, CardInfo delegate) {
		super(provider, delegate);
	}

}
