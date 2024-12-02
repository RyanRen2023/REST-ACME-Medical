/********************************************************************************************************
 * File:  DurationAndStatus.java Course Materials CST 8277
 *
 * @author Teddy Yap
 *
 *  * @author Teddy Yap
 * @author Shaoxian Duan
 * @author Xihai Ren
 * @author Yaozhou Xie
 * @author Huacong Xie
 * @modified_date 2024-12-02
 *
 */
package acmemedical.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@SuppressWarnings("unused")

// TODOo DS01 - This class is not an entity, however, it can be embedded in other entities. Add missing annotation.
@Embeddable
public class DurationAndStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODOo DS02 - Add annotations
	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	// TODOo DS03 - Add annotations
	@Column(name = "end_date", nullable = true)
	private LocalDateTime endDate;

	// TODOo DS04 - Add annotations
	@Column(name = "active", columnDefinition = "BIT(1)", nullable = false)
	private byte active;

	public DurationAndStatus() {
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public void setDurationAndStatus(LocalDateTime startDate, LocalDateTime endDate, String active) {
		setStartDate(startDate);
		setEndDate(endDate);
		byte p = 0b1;
		byte n = 0b0;
		setActive(("+".equals(active) ? p : n));
	}

	public byte getActive() {
		return active;
	}

	public void setActive(byte active) {
		this.active = active;
	}

	/**
	 * Very important: Use getter's for member variables because JPA sometimes needs to intercept those calls
	 * and go to the database to retrieve the value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		return prime * result + Objects.hash(getStartDate(), getEndDate(), getActive());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (obj instanceof DurationAndStatus otherDurationAndStatus) {
			return Objects.equals(this.getStartDate(), otherDurationAndStatus.getStartDate()) &&
					Objects.equals(this.getEndDate(), otherDurationAndStatus.getEndDate()) &&
					Objects.equals(this.getActive(), otherDurationAndStatus.getActive());
		}
		return false;
	}
}