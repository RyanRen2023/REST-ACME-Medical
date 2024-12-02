/********************************************************************************************************
 * File:  MedicalTraining.java Course Materials CST 8277
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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@SuppressWarnings("unused")

/**
 * The persistent class for the medical_training database table.
 */
//TODOo MT01 - Add the missing annotations.
@Entity
@Table(name = "medical_training")
@NamedQuery(name = MedicalTraining.FIND_ALL, query = "SELECT mt FROM MedicalTraining mt")
@NamedQuery(name = MedicalTraining.FIND_BY_ID, query = "SELECT mt FROM MedicalTraining mt WHERE mt.id = :param1")
@AttributeOverride(name = "id", column = @Column(name = "training_id"))
//TODOo MT02 - Do we need a mapped super class?  If so, which one? PojoBase
public class MedicalTraining extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String FIND_ALL = "MedicalTraining.findAll";
	public static final String FIND_BY_ID = "MedicalTraining.findById";

	// TODOo MT03 - Add annotations for M:1. What should be the cascade and fetch
	// types?
//	@JsonBackReference(value="training-school")
//	@JsonManagedReference(value = "training-school")
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id")
	private MedicalSchool school;

	// TODOo MT04 - Add annotations for 1:1. What should be the cascade and fetch
	// types?
//	@OneToOne(cascade=CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
//	@JoinColumn( name = "certificate_id", referencedColumnName = "certificate_id", nullable = true, insertable = false, updatable = false)
//	private MedicalCertificate certificate;

	// @OneToOne(cascade=CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
	// @JoinColumn(name = "certificate_id", referencedColumnName = "certificate_id",
	// nullable = true)
	@OneToOne(mappedBy = "medicalTraining", fetch = FetchType.LAZY)
	@JsonManagedReference("training-certificate")
	private MedicalCertificate certificate;

	@Embedded
	private DurationAndStatus durationAndStatus;

	public MedicalTraining() {
		durationAndStatus = new DurationAndStatus();
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
	 * Very important: Use getter's for member variables because JPA sometimes needs
	 * to intercept those calls<br/>
	 * and go to the database to retrieve the value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		// Only include member variables that really contribute to an object's identity
		// i.e. if variables like version/updated/name/etc. change throughout an
		// object's lifecycle,
		// they shouldn't be part of the hashCode calculation

		// include DurationAndStatus in identity
		return prime * result + Objects.hash(getId(), getDurationAndStatus());
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
			// See comment (above) in hashCode(): Compare using only member variables that
			// are
			// truly part of an object's identity
			return Objects.equals(this.getId(), otherMedicalTraining.getId())
					&& Objects.equals(this.getDurationAndStatus(), otherMedicalTraining.getDurationAndStatus());
		}
		return false;
	}

//	// 类的成员变量
//	private int trainingId;
//
//	// Getter 方法：返回 trainingId 作为 String
//	public String getTrainingId() {
//		return String.valueOf(trainingId);
//	}
//
//	// Setter 方法：接受一个 String 并将其转换为 int
//	public void setTrainingId(String trainingId) {
//		try {
//			this.trainingId = Integer.parseInt(trainingId);
//		} catch (NumberFormatException e) {
//			// 在输入的字符串无法转换为整数时处理错误（可以根据需求调整这里的逻辑）
//			System.err.println("Invalid training ID: " + trainingId);
//		}
//	}

}
