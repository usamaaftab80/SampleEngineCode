package com.project.sample.services;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.project.sample.errors.WebServiceExceptionHandler;
import com.project.sample.services.filters.CORSFilter;
import com.project.sample.services.filters.CleanupFilter;
import com.project.sample.services.filters.TokenAuthenticationRequestFilter;
import com.project.sample.services.info.InfoService;
import com.project.sample.services.resources.ResourceService;

@ApplicationPath("services")
public class WebServicesApplication extends ResourceConfig {

	/**
	 * Register all web service here
	 */
	public WebServicesApplication() {
		register(JacksonFeature.class);
		register(RolesAllowedDynamicFeature.class);
		register(TokenAuthenticationRequestFilter.class);
		register(CORSFilter.class);
		register(CleanupFilter.class);
		register(WebServiceExceptionHandler.class);
		register(InfoService.class);
		register(ResourceService.class);
		property(ServerProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
	}

}
