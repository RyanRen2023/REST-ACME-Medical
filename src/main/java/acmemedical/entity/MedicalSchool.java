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
import jakarta.persistence.Entity;
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

/**
 * The persistent class for the medical_school database table.
 */
@Entity(name="MedicalSchool")
@Table(name="medical_school")
@Access(AccessType.FIELD)
@AttributeOverride(name="id", column=@Column(name="medical_school_id"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = MedicalSchool.ALL_MEDICAL_SCHOOLS_QUERY_NAME, query = "SELECT ms FROM MedicalSchool ms")
@NamedQuery(name = MedicalSchool.SPECIFIC_MEDICAL_SCHOOL_QUERY_NAME, query = "SELECT ms FROM MedicalSchool ms WHERE ms.id = :param1")
@NamedQuery(name = MedicalSchool.IS_DUPLICATE_QUERY_NAME, query = "SELECT COUNT(ms) FROM MedicalSchool ms WHERE ms.name = :param1")
public abstract class MedicalSchool extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ALL_MEDICAL_SCHOOLS_QUERY_NAME = "MedicalSchool.findAll";
	public static final String SPECIFIC_MEDICAL_SCHOOL_QUERY_NAME = "MedicalSchool.findById";
	public static final String IS_DUPLICATE_QUERY_NAME = "MedicalSchool.isDuplicate";

	@Column(name = "name")
	private String name;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "medical_school_id")
	private Set<MedicalTraining> medicalTrainings = new HashSet<>();

	@Column(name = "is_public")
	private boolean isPublic;

	public MedicalSchool() {
		super();
	}

	public MedicalSchool(boolean isPublic) {
		this();
		this.isPublic = isPublic;
	}

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
			return Objects.equals(this.getId(), otherMedicalSchool.getId()) &&
					Objects.equals(this.getName(), otherMedicalSchool.getName());
		}
		return false;
	}
}
