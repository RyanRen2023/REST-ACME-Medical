/********************************************************************************************************
 * File:  MedicalSchoolDTO.java Course Materials CST 8277
 *
 * @author Shaoxian Duan
 * @author Xihai Ren
 * @author Yaozhou Xie
 * @author Huacong Xie
 * @modified_date 2024-12-02
 *
 */
package acmemedical.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MedicalSchoolDTO {
	private String entityType;
	private String name;
	
	
	
	 public MedicalSchoolDTO() {
		super();
	}
	// Constructor for deserialization
    @JsonCreator
    public MedicalSchoolDTO(
        @JsonProperty("entityType") String entityType, 
        @JsonProperty("name") String name) {
        this.entityType = entityType;
        this.name = name;
    }
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
