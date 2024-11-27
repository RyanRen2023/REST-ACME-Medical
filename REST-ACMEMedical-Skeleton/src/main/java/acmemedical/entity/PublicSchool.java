/********************************************************************************************************
 * File:  PublicSchool.java Course materials CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 *
 */
package acmemedical.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;

//TODOo PUSC01 - Add missing annotations, please see Week 9 slides page 15.  Value 1 is public and value 0 is private.
//TODOo PUSC02 - Is a JSON annotation needed here?

@Entity
@DiscriminatorValue("1") // value 0 is private and value 1 is public
public class PublicSchool extends MedicalSchool implements Serializable {
	private static final long serialVersionUID = 1L;
	
//
//	public PublicSchool() {
//		super(true);
//	}
	
	

}