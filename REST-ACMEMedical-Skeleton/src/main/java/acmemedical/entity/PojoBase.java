/********************************************************************************************************
 * File:  PojoBase.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 *
 */
package acmemedical.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Abstract class that is base of (class) hierarchy for all @Entity classes
 */

@MappedSuperclass // Define this class as the superclass of all entities.
@Access(AccessType.FIELD) // Place all JPA annotations on fields.
@EntityListeners(PojoListener.class) // Add annotation for listener class.
public abstract class PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id // Define this field as the primary key.
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Use an auto-incremented primary key.
	@Column(name = "id")
	protected int id;

	@Version // Define this field to handle optimistic locking.
	protected int version;

	@Column(name = "created", nullable = false) // Map this field to the "created" column, cannot be null, and not updatable after creation.
	protected LocalDateTime created;

	@Column(name = "updated", nullable = false) // Map this field to the "updated" column, cannot be null.
	protected LocalDateTime updated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		return prime * result + Objects.hash(getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (obj instanceof PojoBase otherPojoBase) {
			return Objects.equals(this.getId(), otherPojoBase.getId());
		}
		return false;
	}
}