package com.project.sample.services.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.project.sample.dal.entities.Resource;

public class ResourceMessage {

	private Integer id;
	private Integer userId;
	private String name;
	private String description;
	private Date createdDate;
	private List<ResourceLineMessage> resourceLines = new ArrayList<ResourceLineMessage>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<ResourceLineMessage> getResourceLines() {
		return resourceLines;
	}

	public void setResourceLines(List<ResourceLineMessage> resourceLines) {
		this.resourceLines = resourceLines;
	}

	public static ResourceMessage convertFrom(Resource resource) {
		ResourceMessage resourceMessage = new ResourceMessage();
		resourceMessage.setId(resource.getId());
		resourceMessage.setUserId(resource.getUserId());
		resourceMessage.setName(resource.getName());
		resourceMessage.setDescription(resource.getDescription());
		resourceMessage.setCreatedDate(resource.getCreatedDate());
		if (resource.isResourceLinesLoaded()) {
			resourceMessage.setResourceLines(ResourceLineMessage.convertFrom(resource.getResourceLines()));
		}
		
		return resourceMessage;
	}

	public static List<ResourceMessage> convertFrom(List<Resource> resources) {
		List<ResourceMessage> messages = new ArrayList<ResourceMessage>();
		for (Resource resource : resources) {
			messages.add(convertFrom(resource));
		}
		return messages;
	}

}
