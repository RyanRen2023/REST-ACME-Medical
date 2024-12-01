/********************************************************************************************************
 * File:  MedicalSchool.java Course Materials CST 8277
 *
 * @author Teddy Yap
 *
 */
package acmemedical.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "entity-type")
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

	@OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
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
