package com.project.sample.services.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.project.sample.dal.entities.ResourceLine;

public class ResourceLineMessage {
	private Integer id;
	private String name;
	private String key;
	private String value;
	private Date createdDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public static ResourceLineMessage convertFrom(ResourceLine resourceLine) {
		ResourceLineMessage message = new ResourceLineMessage();
		message.setId(resourceLine.getId());
		message.setName(resourceLine.getName());
		message.setKey(resourceLine.getKey());
		message.setValue(resourceLine.getValue());
		message.setCreatedDate(resourceLine.getCreatedDate());
		return message;
	}

	public static List<ResourceLineMessage> convertFrom(List<ResourceLine> resourceLines) {
		List<ResourceLineMessage> resourceLineMessages = new ArrayList<ResourceLineMessage>();
		for (ResourceLine resourceLine : resourceLines) {
			ResourceLineMessage resourceLineMessage = convertFrom(resourceLine);
			resourceLineMessages.add(resourceLineMessage);
		}
		return resourceLineMessages;
	}

}
