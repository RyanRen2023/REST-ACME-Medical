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
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

@SuppressWarnings("unused")

/**
 * The persistent class for the medical_certificate database table.
 */
@Entity
@Table(name = "medical_certificate")
public class MedicalCertificate extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String FIND_BY_ID = "MedicalCertificate.findById";

    // TODO MC01 - Add missing annotations. Bidirectional mapping to MedicalTraining
    @OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name = "training_id", referencedColumnName = "training_id")
    private MedicalTraining medicalTraining;

    // TODO MC02 - Add missing annotations. Reference to Physician entity, required
    @ManyToOne(cascade=CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "physician_id")
    private Physician owner;

    // TODO MC03 - Add missing annotations. Signed column to track the status of the certificate
    @Column(name = "signed", columnDefinition = "BIT(1)", nullable = false)
    private byte signed;

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

    public byte getSigned() {
        return signed;
    }

    public void setSigned(byte signed) {
        this.signed = signed;
    }

    public void setSigned(boolean signed) {
        this.signed = (byte) (signed ? 0b0001 : 0b0000);
    }
}