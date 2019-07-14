package com.project.sample.services.info;

import static org.junit.Assert.*;
import javax.ws.rs.core.GenericType;
import org.junit.Test;
import com.project.sample.services.BaseServiceTest;

public class InfoServiceTest extends BaseServiceTest {


	@Test
	public void testGetInfo() {
		ServiceInfoMessage infoMessageResponse = target("info").request().get(new GenericType<ServiceInfoMessage>() {
		});

		assertNotNull(infoMessageResponse.getTimeStamp());
		assertNotNull(infoMessageResponse.getBaseDataBaseURL());
		assertNotNull(infoMessageResponse.getBaseDataBaseURL());
	}

	@Test
	public void testGetSpecificDBStatus() {
		DatabaseInfoMessage response = target("info/[DATABASE NAME]").request().get(new GenericType<DatabaseInfoMessage>() {
		});

		assertNotNull(response.getMessage());
	}
	
}
