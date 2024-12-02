/********************************************************************************************************
 * File:  Physician.java Course Materials CST 8277
 *
 * @author Teddy Yap
 *
 * @author Shaoxian Duan
 * @author Xihai Ren
 * @author Yaozhou Xie
 * @author Huacong Xie
 * @modified_date 2024-12-02
 *
 */
package acmemedical.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The persistent class for the physician database table.
 */

// TODOo PH02 - Do we need a mapped super class? If so, which one?
// Answer: Yes, we need a mapped super class. The superclass is PojoBase.
@Entity
@Table(name = "physician")
@NamedQuery(name = Physician.ALL_PHYSICIANS_QUERY_NAME, query = "SELECT p FROM Physician p")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Physician extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ALL_PHYSICIANS_QUERY_NAME = "Physician.findAll";

	public Physician() {
		super();
	}

	// TODOo PH03 - Add annotations.
	@Column(name = "first_name", nullable = false)
	private String firstName;

	// TODOo PH04 - Add annotations.
	@Column(name = "last_name", nullable = false)
	private String lastName;

	// TODOo PH05 - Add annotations for 1:M relation. What should be the cascade and fetch types?
	// Answer: Cascade type should be MERGE to merge changes to MedicalCertificate when changes are made to Physician.
	// Fetch type should be LAZY as we do not always need to load MedicalCertificates.
	@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
	@JsonManagedReference(value="physician-certificate")
	private Set<MedicalCertificate> medicalCertificates = new HashSet<>();

	// TODOo PH06 - Add annotations for 1:M relation. What should be the cascade and fetch types?
	// Answer: Cascade type should be MERGE, and fetch type should be LAZY for similar reasons as above.
	@OneToMany(mappedBy = "physician", fetch = FetchType.LAZY)
	@JsonManagedReference("physician-prescriptions")
	private Set<Prescription> prescriptions = new HashSet<>();

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	// TODOo PH07 - Is an annotation needed here?
	// Answer: No annotation is needed here, this is a regular getter method. 
	public Set<MedicalCertificate> getMedicalCertificates() {
		return medicalCertificates;
	}

	public void setMedicalCertificates(Set<MedicalCertificate> medicalCertificates) {
		this.medicalCertificates = medicalCertificates;
	}

	// TODOo PH08 - Is an annotation needed here?
	// Answer: No annotation is needed here, this is a regular getter method.
	public Set<Prescription> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(Set<Prescription> prescriptions) {
		this.prescriptions = prescriptions;
	}

	public void setFullName(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}

	// Inherited hashCode/equals is sufficient for this entity class
}
