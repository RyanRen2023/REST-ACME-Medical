/********************************************************************************************************
 * File:  PojoCompositeListener.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 *
 */
package acmemedical.entity;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@SuppressWarnings("unused")

public class PojoCompositeListener {

	// TODOo PCL01 - What annotation is used when we want to do something just before object is INSERT'd into database?
	@PrePersist
	public void setCreatedOnDate(PojoBaseCompositeKey<?> pojoBaseComposite) {
		LocalDateTime now = LocalDateTime.now();
		// TODOo PCL02 - What member field(s) do we wish to alter just before object is INSERT'd in the database?
		pojoBaseComposite.setCreated( now);
		pojoBaseComposite.setUpdated( now);
	}

	// TODOo PCL03 - What annotation is used when we want to do something just before object is UPDATE'd into database?
	@PreUpdate
	public void setUpdatedDate(PojoBaseCompositeKey<?> pojoBaseComposite) {
		// TODOo PCL04 - What member field(s) do we wish to alter just before object is UPDATE'd in the database?
		LocalDateTime now = LocalDateTime.now();
		pojoBaseComposite.setUpdated(now);
	}

}