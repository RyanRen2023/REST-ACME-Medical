/********************************************************************************************************
 * File:  MedicalTraining.java Course Materials CST 8277
 *
 * @author Teddy Yap
 *
 */
package acmemedical.entity;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Embedded;

@SuppressWarnings("unused")

/**
 * The persistent class for the medical_training database table.
 */
//TODO MT01 - Add the missing annotations.
@Entity
@Table(name = "medical_training")
//TODO MT02 - Do we need a mapped super class?  If so, which one?
public class MedicalTraining extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String FIND_BY_ID = "MedicalTraining.findById";

	// Primary Key Mapping
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "training_id")
	private int trainingId;

	// TODO MT03 - Add annotations for M:1. What should be the cascade and fetch types?
	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "school_id", referencedColumnName = "school_id", nullable = false)
	private MedicalSchool school;

	// TODO MT04 - Add annotations for 1:1. What should be the cascade and fetch types?
	@OneToOne(mappedBy = "medicalTraining", cascade = CascadeType.ALL, optional = true)
	private MedicalCertificate certificate;

	@Embedded
	private DurationAndStatus durationAndStatus;

	public MedicalTraining() {
		durationAndStatus = new DurationAndStatus();
	}

	public int getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(int trainingId) {
		this.trainingId = trainingId;
	}

	public MedicalSchool getMedicalSchool() {
		return school;
	}

	public void setMedicalSchool(MedicalSchool school) {
		this.school = school;
	}

	public MedicalCertificate getCertificate() {
		return certificate;
	}

	public void setCertificate(MedicalCertificate certificate) {
		this.certificate = certificate;
	}

	public DurationAndStatus getDurationAndStatus() {
		return durationAndStatus;
	}

	public void setDurationAndStatus(DurationAndStatus durationAndStatus) {
		this.durationAndStatus = durationAndStatus;
	}

	// Inherited hashCode/equals NOT sufficient for this Entity class
	/**
	 * Very important:  Use getter's for member variables because JPA sometimes needs to intercept those calls<br/>
	 * and go to the database to retrieve the value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		// Only include member variables that really contribute to an object's identity
		// i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
		// they shouldn't be part of the hashCode calculation

		// include DurationAndStatus in identity
		return prime * result + java.util.Objects.hash(getTrainingId(), getDurationAndStatus());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof MedicalTraining otherMedicalTraining) {
			// See comment (above) in hashCode():  Compare using only member variables that are
			// truly part of an object's identity
			return java.util.Objects.equals(this.getTrainingId(), otherMedicalTraining.getTrainingId()) &&
					java.util.Objects.equals(this.getDurationAndStatus(), otherMedicalTraining.getDurationAndStatus());
		}
		return false;
	}
}
