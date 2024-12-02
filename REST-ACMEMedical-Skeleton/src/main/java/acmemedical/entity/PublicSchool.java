/********************************************************************************************************
 * File:  PublicSchool.java Course materials CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 *
 * @author Shaoxian Duan
 * @author Xihai Ren
 * @author Yaozhou Xie
 * @author Huacong Xie
 * @modified_date 2024-12-02
 *
 */
package acmemedical.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

//TODOo PUSC01 - Add missing annotations, please see Week 9 slides page 15.  Value 1 is public and value 0 is private.
//TODOo PUSC02 - Is a JSON annotation needed here?

@Entity
@DiscriminatorValue("1") // value 0 is private and value 1 is public
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicSchool extends MedicalSchool implements Serializable {
	private static final long serialVersionUID = 1L;
	

	public PublicSchool(String name) {
		super(name);
	}


	public PublicSchool() {
		super();
	}
	
	
	

}