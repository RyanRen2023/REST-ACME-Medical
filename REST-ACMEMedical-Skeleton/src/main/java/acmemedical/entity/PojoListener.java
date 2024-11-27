/********************************************************************************************************
 * File:  PojoListener.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 *
 */
package acmemedical.entity;

import java.time.LocalDateTime;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@SuppressWarnings("unused")

public class PojoListener {

	// TODOo PL01 - What annotation is used when we want to do something just before object is INSERT'd in the database?
	@PrePersist
	public void setCreatedOnDate(PojoBase pojoBase) {
		LocalDateTime now = LocalDateTime.now();
		// TODOo PL02 - What member field(s) do we wish to alter just before object is INSERT'd in the database?
		pojoBase.setCreated(now);
		pojoBase.setUpdated(now);
	}

	// TODOo PL03 - What annotation is used when we want to do something just before object is UPDATE'd in the database?
	@PreUpdate
	public void setUpdatedDate(PojoBase pojoBase) {

		// TODOo PL04 - What member field(s) do we wish to alter just before object is UPDATE'd in the database?
		LocalDateTime now = LocalDateTime.now();
		pojoBase.setUpdated(now);
	}

}