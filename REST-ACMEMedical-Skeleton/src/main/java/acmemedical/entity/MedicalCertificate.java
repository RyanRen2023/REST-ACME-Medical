/********************************************************************************************************
 * File:  MedicalCertificate.java Course Materials CST 8277
 *
 * @author Teddy Yap
 *
 */
package acmemedical.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@SuppressWarnings("unused")

/**
 * The persistent class for the medical_certificate database table.
 */
// TODO MC01 - Add the missing annotations.
@Entity
@Table(name = "medical_certificate")
// TODO MC02 - Do we need a mapped super class? If so, which one?
// Answer: Yes, the superclass is PojoBase, which this class already extends.
public class MedicalCertificate extends PojoBase implements Serializable {
	public static final String ID_CARD_QUERY_NAME = "MedicalCertificate.findById";
	private static final long serialVersionUID = 1L;

	// TODO MC03 - Add annotations for 1:1 mapping. What should be the cascade and fetch types?
	// Answer: Assuming that a medical certificate has exactly one training associated with it, we use OneToOne mapping.
	@OneToOne(optional = false)
	@JoinColumn(name = "medical_training_id", referencedColumnName = "id", nullable = false)
	private MedicalTraining medicalTraining;

	// TODO MC04 - Add annotations for M:1 mapping. What should be the cascade and fetch types?
	// Answer: Assuming that a medical certificate can belong to one physician, we use ManyToOne mapping.
	@ManyToOne(optional = false)
	@JoinColumn(name = "physician_id", referencedColumnName = "id", nullable = false)
	private Physician owner;

	// TODO MC05 - Add annotations.
	@Column(name = "signed", columnDefinition = "BIT(1)", nullable = false)
	private byte signed;

	public MedicalCertificate() {
		super();
	}

	public MedicalCertificate(MedicalTraining medicalTraining, Physician owner, byte signed) {
		this();
		this.medicalTraining = medicalTraining;
		this.owner = owner;
		this.signed = signed;
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

	public byte getSigned() {
		return signed;
	}

	public void setSigned(byte signed) {
		this.signed = signed;
	}

	public void setSigned(boolean signed) {
		this.signed = (byte) (signed ? 0b0001 : 0b0000);
	}

	// Inherited hashCode/equals is sufficient for this entity class
}