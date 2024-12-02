/********************************************************************************************************
 * File:  CustomIdentityStoreJPAHelper.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Mike Norman
 *
 * @author Shaoxian Duan
 * @author Xihai Ren
 * @author Yaozhou Xie
 * @author Huacong Xie
 * @modified_date 2024-12-02
 */
package acmemedical.security;

import static acmemedical.utility.MyConstants.PARAM1;
import static acmemedical.utility.MyConstants.PU_NAME;

import static java.util.Collections.emptySet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmemedical.entity.SecurityRole;
import acmemedical.entity.SecurityUser;
import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@SuppressWarnings("unused")

@Singleton
public class CustomIdentityStoreJPAHelper {

    private static final Logger LOG = LogManager.getLogger();

    @PersistenceContext(name = PU_NAME)
    protected EntityManager em;

    public SecurityUser findUserByName(String username) {
        LOG.debug("find a SecurityUser by name = {}", username);
        SecurityUser user = null;
        /* TODOo CISJPAH01 -
         *  Call the entity manager's createNamedQuery() method to call a named query on SecurityUser
         *  The named query should be labeled "SecurityUser.userByName" and accepts a parameter called "param1"
         *
         *  Call getSingleResult() inside a try-catch statement (NoResultException)
         *
         *  Note:  Until this method is complete, the Basic Authentication for all HTTP
         *         requests will fail, none of the REST'ful endpoints will work.
         *
         */
        // @ThisAnnotationDoesNotExistSoYouMustFixThisBeforeTheProjectCanDeploy
        try{
            TypedQuery<SecurityUser> query = em.createNamedQuery(SecurityUser.SECURITY_USER_BY_NAME_QUERY, SecurityUser.class);
            query.setParameter(PARAM1, username);
            List<SecurityUser> users = query.getResultList();
            if(users != null && users.size() > 0)
            	user = users.get(0);
            else {
            	throw new NoResultException();
            }
        }catch(NoResultException e){
            LOG.warn("No user found with username={}", username);
        }
        return user;
    }

    public Set<String> findRoleNamesForUser(String username) {
        LOG.debug("find Roles For Username={}", username);
        Set<String> roleNames = new HashSet<>();
        SecurityUser securityUser = findUserByName(username);
        if (securityUser != null) {
        	Set<SecurityRole> roles = securityUser.getRoles();
        	for(SecurityRole role : roles) {
        		roleNames.add(role.getRoleName());
        	}
        }
        return roleNames;
    }

    @Transactional
    public void saveSecurityUser(SecurityUser user) {
        LOG.debug("adding new user={}", user);
        em.persist(user);
    }

    @Transactional
    public void saveSecurityRole(SecurityRole role) {
        LOG.debug("adding new role={}", role);
        em.persist(role);
    }
}