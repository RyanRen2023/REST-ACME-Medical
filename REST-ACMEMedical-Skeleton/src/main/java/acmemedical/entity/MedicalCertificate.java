/********************************************************************************************************
 * File:  MedicalCertificate.java Course Materials CST 8277
 *
 * @author Teddy Yap
 *
 */
package acmemedical.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

@SuppressWarnings("unused")

/**
 * The persistent class for the medical_certificate database table.
 */
@Entity
@Table(name = "medical_certificate")
@AttributeOverride(name = "id", column = @Column(name = "certificate_id"))
@NamedQuery(name = MedicalCertificate.FIND_BY_ID, query = "select mc from MedicalCertificate mc where mc.id = :param1")
public class MedicalCertificate extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_ID = "MedicalCertificate.findById";

	// TODOo MC01 - Add missing annotations. Bidirectional mapping to
	// MedicalTraining
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "training_id", referencedColumnName = "training_id")
	@JsonBackReference("training-certificate")
	private MedicalTraining medicalTraining;

	// TODOo MC02 - Add missing annotations. Reference to Physician entity, required
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "physician_id", nullable = false)
	@JsonBackReference(value="physician-certificate")
	private Physician owner;

	// TODOo MC03 - Add missing annotations. Signed column to track the status of the
	// certificate
	@Column(name = "signed", nullable = false)
	private boolean signed;

	public MedicalCertificate() {
		super();
	}

	public MedicalTraining getMedicalTraining() {
		return medicalTraining;
	}

	public void setMedicalTraining(MedicalTraining medicalTraining) {
		this.medicalTraining = medicalTraining;
	}

	public Physician getOwner() {
		return owner;
	}

	public void setOwner(Physician owner) {
		this.owner = owner;
	}

	public boolean getSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

//	public void setSigned(boolean signed) {
//		this.signed = (byte) (signed ? 0b0001 : 0b0000);
//	}
}