/********************************************************************************************************
 * File:  PrivateSchool.java Course Materials CST 8277
 *
 * @author Teddy Yap
 *
 */
package acmemedical.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;

//TODO0 PRSC01 - Add missing annotations, please see Week 9 slides page 15.  Value 1 is public and value 0 is private.
//TODO0 PRSC02 - Is a JSON annotation needed here?
//TODOo confused by the value, check later
@Entity
@DiscriminatorValue("0") // value false is private and value true is public
public class PrivateSchool extends MedicalSchool implements Serializable {
	private static final long serialVersionUID = 1L;

	public PrivateSchool(String name) {
		super(name);
	}

	public PrivateSchool() {
		super();
	}
	
	
	

}