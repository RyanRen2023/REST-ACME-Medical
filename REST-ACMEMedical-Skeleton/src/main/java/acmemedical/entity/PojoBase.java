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

// TODOo PB01 - Define this class as the superclass of all entities.
@MappedSuperclass
// TODOo PB02 - Place all JPA annotations on fields.
@Access(AccessType.FIELD)
// TODOo PB03 - Add annotation for listener class.
@EntityListeners(PojoListener.class)
public abstract class PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODOo PB04 - Define this field as the primary key.
	@Id
	// TODOo PB05 - Use an auto-incremented primary key.
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected int id;

	// TODOo PB06 - Define this field to handle optimistic locking.
	@Version
	protected int version;

	// TODOo PB07 - Map this field to the "created" column, cannot be null, and not updatable after creation.
	@Column(name = "created", nullable = false, updatable = false)
	protected LocalDateTime created;

	// TODOo PB08 - Map this field to the "updated" column, cannot be null.
	@Column(name = "updated", nullable = false)
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
