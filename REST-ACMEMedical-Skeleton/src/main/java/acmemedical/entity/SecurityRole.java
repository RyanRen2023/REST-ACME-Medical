/********************************************************************************************************
 * File:  SecurityRole.java Course Materials CST 8277
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
import jakarta.persistence.metamodel.StaticMetamodel;

import javax.annotation.processing.Generated;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("unused")

/**
 * Role class used for (JSR-375) Jakarta EE Security authorization/authentication
 */
// TODOo SR01 - Make this into JPA entity and add all necessary annotations inside the class.
@Entity
@Table(name = "security_role")
@NamedQuery(name = SecurityRole.ROLE_BY_NAME_QUERY, query = "select sr from SecurityRole sr where sr.roleName = :param1")
public class SecurityRole implements Serializable {
    /**
     * Explicit set serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    public static final String ROLE_BY_NAME_QUERY = "SecurityRole.findByRoleName";


    // TODOo SR02 - Add annotations.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    protected int id;

    // TODOo SR03 - Add annotations.
    @Column(name= "name")
    protected String roleName;

    // TODOo SR04 - Add annotations.
    @ManyToMany(mappedBy = "roles")
    protected Set<SecurityUser> users = new HashSet<SecurityUser>();

    public SecurityRole() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<SecurityUser> getUsers() {
        return users;
    }

    public void setUsers(Set<SecurityUser> users) {
        this.users = users;
    }

    public void addUserToRole(SecurityUser user) {
        getUsers().add(user);
    }

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
        if (obj instanceof SecurityRole otherSecurityRole) {
            // See comment (above) in hashCode(): Compare using only member variables that are
            // truly part of an object's identity
            return Objects.equals(this.getId(), otherSecurityRole.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SecurityRole [id = ").append(id).append(", ");
        if (roleName != null)
            builder.append("roleName = ").append(roleName);
        builder.append("]");
        return builder.toString();
    }
}