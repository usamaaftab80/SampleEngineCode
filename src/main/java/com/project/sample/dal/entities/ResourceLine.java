package com.project.sample.dal.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.project.sample.services.resources.ResourceLineMessage;

@Entity
@Cacheable(true)
@Table(name = "CS_RESOURCE_LINES")
@NamedQueries({
	@NamedQuery(name = "ResourceLine.findByResourceUserId", query = "SELECT rl FROM Resource r JOIN r.resourceLines rl WHERE r.userId = :resourceUserId")
})
public class ResourceLine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CSRL_RESOURCE_LINE_ID")
	private Integer id;
	
	@Column(name = "CSRL_NAME")
	private String name;

	@Column(name = "CSRL_KEY")
	private String key;

	@Column(name = "CSRL_VALUE")
	private String value;

	@Column(name = "CSRL_CREATED_DATE")
	private Date createdDate;

	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	@JoinColumn(name="CSRL_RESOURCE_ID")
	private Resource resource;

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

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public static ResourceLine createNewResource(String name, String key, String value) {
		ResourceLine resourceLine = new ResourceLine();
		resourceLine.setName(name);
		resourceLine.setKey(key);
		resourceLine.setValue(value);
		resourceLine.setCreatedDate(new Date());
		return resourceLine;
	}
	
	public static ResourceLine convertFrom(ResourceLineMessage resourceLineMessage) {
		ResourceLine resourceLine = new ResourceLine();
		resourceLine.setName(resourceLineMessage.getName());
		resourceLine.setKey(resourceLineMessage.getKey());
		resourceLine.setValue(resourceLineMessage.getValue());
		resourceLine.setCreatedDate(new Date());
		return resourceLine;
	}
	
	public static List<ResourceLine> convertFrom(List<ResourceLineMessage> resourceLineMessages) {
		List<ResourceLine> resourceLines = new ArrayList<ResourceLine>();
		
		for(ResourceLineMessage resourceLineMessage : resourceLineMessages) {
			ResourceLine resourceLine = new ResourceLine();
			resourceLine.setName(resourceLineMessage.getName());
			resourceLine.setKey(resourceLineMessage.getKey());
			resourceLine.setValue(resourceLineMessage.getValue());
			resourceLine.setCreatedDate(new Date());
			resourceLines.add(resourceLine);
		}
		
		return resourceLines;
	}
}
