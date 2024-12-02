/********************************************************************************************************
 * File:  MedicalSchool.java Course Materials CST 8277
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

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import acmemedical.rest.serializer.MedicalTrainingSerializer;

/**
 * The persistent class for the medical_school database table.
 */
@Entity(name = "MedicalSchool")
@Table(name = "medical_school")
@Access(AccessType.FIELD)
@AttributeOverride(name = "id", column = @Column(name = "school_id"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "public", discriminatorType = DiscriminatorType.INTEGER) // Adding discriminator column
@NamedQuery(name = MedicalSchool.ALL_MEDICAL_SCHOOLS_QUERY_NAME, query = "SELECT ms FROM MedicalSchool ms")
@NamedQuery(name = MedicalSchool.SPECIFIC_MEDICAL_SCHOOL_QUERY_NAME, query = "SELECT ms FROM MedicalSchool ms where ms.id = :param1")
@NamedQuery(name = MedicalSchool.IS_DUPLICATE_QUERY_NAME, query = "SELECT COUNT(ms) FROM MedicalSchool ms WHERE ms.name = :param1")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "entityType")
@JsonSubTypes({ @Type(value = PrivateSchool.class, name = "private_school"),
		@Type(value = PublicSchool.class, name = "public_school") })
//@JsonSerialize(using = MedicalSchoolSerializer.class)
public abstract class MedicalSchool extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ALL_MEDICAL_SCHOOLS_QUERY_NAME = "MedicalSchool.findAll";
	public static final String SPECIFIC_MEDICAL_SCHOOL_QUERY_NAME = "MedicalSchool.findById";
	public static final String IS_DUPLICATE_QUERY_NAME = "MedicalSchool.isDuplicate";
//	public static final String SPECIFIC_MEDICAL_SCHOOL_QUERY = "MedicalSchool.findById";

	@Column(name = "name")
	private String name;

//	@JsonBackReference(value = "training-school")
	@OneToMany(mappedBy = "school", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<MedicalTraining> medicalTrainings = new HashSet<>();

//	@Column(name = "public")  // This column is used for the discriminator
//    private boolean isPublic;  // The field corresponding to the "public" column, which indicates if it's a public school.

	public MedicalSchool() {
		super();
	}

//	public MedicalSchool(boolean isPublic) {
//		super();
//		this.isPublic = isPublic;
//	}

	public MedicalSchool(String name) {
		super();
		this.name = name;
	}
    @JsonInclude(Include.NON_NULL)
	@JsonSerialize(using = MedicalTrainingSerializer.class)
	public Set<MedicalTraining> getMedicalTrainings() {
		return medicalTrainings;
	}

	public void setMedicalTrainings(Set<MedicalTraining> medicalTrainings) {
		this.medicalTrainings = medicalTrainings;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

//	public boolean isPublic() {
//		return isPublic;
//	}
//
//
//
//	public void setPublic(boolean isPublic) {
//		this.isPublic = isPublic;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		return prime * result + Objects.hash(getId(), getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (obj instanceof MedicalSchool otherMedicalSchool) {
			return Objects.equals(this.getId(), otherMedicalSchool.getId())
					&& Objects.equals(this.getName(), otherMedicalSchool.getName());
		}
		return false;
	}
}
