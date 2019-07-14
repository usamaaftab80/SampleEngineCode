package com.project.sample;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.project.sample.helpers.PropertyHelper;

public class PropertyHelperTest {

	@Test
	public void testPropertyHelper(){
		PropertyHelper propertyHelper = PropertyHelper.getInstance();	
		assertNotNull(propertyHelper.getProperty("db.host"));
	}
}
