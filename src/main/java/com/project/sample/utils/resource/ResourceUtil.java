package com.project.sample.utils.resource;

import java.util.logging.Logger;
import com.project.sample.dal.entities.Resource;

public class ResourceUtil {
	
	private static Logger LOGGER = Logger.getLogger(ResourceUtil.class.getName());
	
	public static Resource someUtility(Resource resourceToBeProcessed) throws Exception {		
		
		LOGGER.info("some Utility is being triggered on resource: " + resourceToBeProcessed.getId());
		
		//Do something here to manipulate the resource.
		
		return resourceToBeProcessed;
	}
	
}
