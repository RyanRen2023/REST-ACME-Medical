/********************************************************************************************************
 * File:  SecurityUser.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 *
 */
package acmemedical.entity;

import acmemedical.rest.serializer.SecurityRoleSerializer;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("unused")

/**
 * User class used for (JSR-375) Jakarta EE Security authorization/authentication
 */
// TODOo SU01 - Make this into JPA entity and add all the necessary annotations inside the class.
@Entity
@Access(AccessType.FIELD)
@Table(name = "security_user")
@NamedQuery(name = SecurityUser.SECURITY_USER_BY_NAME_QUERY, query = "SELECT u FROM SecurityUser u WHERE u.username = :param1")
@NamedQuery(name = SecurityUser.USER_FOR_OWNING_PHYSICIAN_QUERY, query = "SELECT u FROM SecurityUser u WHERE u.physician.id = :param1")
public class SecurityUser implements Serializable, Principal {

    public static final String USER_FOR_OWNING_PHYSICIAN_QUERY = "SecurityUser.findUserForOwningPhysician";
    /**
     * Explicit set serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    public static final String SECURITY_USER_BY_NAME_QUERY = "SecurityUser.userByName";

    // TODOo SU02 - Add annotations.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    protected int id;

    // TODOo SU03 - Add annotations.
    @Basic(optional = false)
    @Column(name = "username", nullable = false)
    protected String username;

    // TODOo SU04 - Add annotations.
    @Basic(optional = false)
    @Column(name = "password_hash", nullable = false)
    protected String pwHash;

    // TODOo SU05 - Add annotations.
    // @OneToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "physician_id", referencedColumnName = "physician_id", insertable = false, updatable = false)
    // protected Physician physician;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "physician_id", referencedColumnName = "id")
    protected Physician physician;

    // TODOo SU06 - Add annotations.
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_has_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    protected Set<SecurityRole> roles = new HashSet<SecurityRole>();

    public SecurityUser() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPwHash() {
        return pwHash;
    }

    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    // TODOo SU07 - Setup custom JSON serializer
    @JsonInclude(Include.NON_NULL)
    @JsonSerialize(using = SecurityRoleSerializer.class)
    public Set<SecurityRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SecurityRole> roles) {
        this.roles = roles;
    }

    public Physician getPhysician() {
        return physician;
    }

    public void setPhysician(Physician physician) {
        this.physician = physician;
    }

    // Principal
    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        // Only include member variables that really contribute to an object's identity
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
        if (obj instanceof SecurityUser otherSecurityUser) {
            // Compare using only member variables that are truly part of an object's identity
            return Objects.equals(this.getId(), otherSecurityUser.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SecurityUser [id = ").append(id).append(", username = ").append(username).append("]");
        return builder.toString();
    }
}