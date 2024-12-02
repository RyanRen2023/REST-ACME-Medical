/********************************************************************************************************
 * File:  PojoBaseCompositeKey.java Course Materials CST 8277
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

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@SuppressWarnings("unused")

/**
 * Abstract class that is base of (class) hierarchy for all @Entity classes
 * @param <ID> - type of composite key used
 */
//TODOo PC01 - Add annotation to define this class as superclass of all entities.  Please see Week 9 lecture slides.
//TODOo PC02 - Add annotation to place all JPA annotations on fields.
//TODOo PC03 - Add annotation for listener class.
@MappedSuperclass
@Access(AccessType.FIELD)
@EntityListeners(PojoCompositeListener.class)
public abstract class PojoBaseCompositeKey<ID extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;

    // TODOo PC04 - Add missing annotations.
    @Version
    protected int version;

    // TODOo PC05 - Add missing annotations (hint, is this column on DB?).
    @Column(name = "created")
    protected LocalDateTime created;

    // TODOo PC06 - Add missing annotations (hint, is this column on DB?).
    @Column(name = "updated")
    protected LocalDateTime updated;

    public abstract ID getId();

    public abstract void setId(ID id);

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

    /**
     * Very important:  Use getter's for member variables because JPA sometimes needs to intercept those calls<br/>
     * and go to the database to retrieve the value
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        // Only include member variables that really contribute to an object's identity
        // i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
        // they shouldn't be part of the hashCode calculation
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

        if (obj instanceof PojoBaseCompositeKey otherPojoBaseComposite) {
            // See comment (above) in hashCode():  Compare using only member variables that are
            // truly part of an object's identity
            return Objects.equals(this.getId(), otherPojoBaseComposite.getId());
        }
        return false;
    }
}