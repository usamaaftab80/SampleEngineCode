package com.project.sample.dal.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.project.sample.helpers.EntityManagerHelper;
import com.project.sample.services.resources.ResourceMessage;

@Entity
@Cacheable(true)
@Table(name = "CS_RESOURCES")
@NamedQueries({
		@NamedQuery(name = "Resource.findByUserId", query = "SELECT r FROM Resource r WHERE r.userId = :userId"),
		@NamedQuery(name = "Resource.findByResourceId", query = "SELECT r FROM Resource r LEFT JOIN FETCH r.resourceLines WHERE r.id = :id"),
		@NamedQuery(name = "Resource.findByResourceLineValue", query = "SELECT r FROM Resource r JOIN r.resourceLines rl WHERE rl.key = :resourceLineKey AND rl.value = :resourceLineValue"),
		@NamedQuery(name = "Resource.checkIfDatabaseIsAlive", query = "SELECT r FROM Resource r WHERE 1 = 0")
	})
public class Resource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CSR_RESOURCE_ID")
	private Integer id;

	@Column(name = "CSR_USER_ID")
	private Integer userId;

	@Column(name = "CSR_NAME")
	private String name;
	
	@Column(name = "CSR_DESCRIPTION")
	private String description;	

	@Column(name = "CSR_CREATED_DATE")
	private Date createdDate;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "resource")
    private List<ResourceLine> resourceLines = new ArrayList<ResourceLine>();

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
	
	public boolean isResourceLinesLoaded() {
		return EntityManagerHelper.isLoaded(this, "resourceLines");
	}

	public List<ResourceLine> getResourceLines() {
		return resourceLines;
	}

	public void setResourceLines(List<ResourceLine> resourceLines) {
		this.resourceLines = resourceLines;
	}

	public static Resource createNewResource(int userId, String name, String description, List<ResourceLine> resourceLines) {
		Resource resource = new Resource();
		resource.setUserId(userId);
		resource.setName(name);
		resource.setCreatedDate(new Date());
		resource.setDescription(description);
		resource.setResourceLines(resourceLines);
		return resource;
	}
	
	public static Resource convertFromResourceMessage(ResourceMessage resourceMessage) {
		
		List<ResourceLine> resourceLines = ResourceLine.convertFrom(resourceMessage.getResourceLines());
		
		Resource resource = new Resource();
		resource.setUserId(resourceMessage.getId());
		resource.setName(resourceMessage.getName());
		resource.setCreatedDate(new Date());
		resource.setDescription(resourceMessage.getDescription());
		resource.setResourceLines(resourceLines);
		return resource;
	}
}
