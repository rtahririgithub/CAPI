package com.telus.api.util;

import java.util.Map;

public interface ComponentEndpointConfigurationManager {

	Map getComponentEndpointConfigurations();

	ComponentEndpointConfiguration getComponentEndpointConfiguration(String name);

	ComponentEndpointConfiguration getComponentEndpointConfiguration(Class type);

}