package acmemedical;

import static acmemedical.utility.MyConstants.APPLICATION_API_VERSION;
import static acmemedical.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static acmemedical.utility.MyConstants.DEFAULT_ADMIN_USER;
import static acmemedical.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static acmemedical.utility.MyConstants.DEFAULT_USER;
import static acmemedical.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static acmemedical.utility.MyConstants.MEDICAL_SCHOOL_RESOURCE_NAME;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import acmemedical.entity.MedicalSchool;
import acmemedical.entity.PrivateSchool;

@SuppressWarnings("unused")

@TestMethodOrder(MethodOrderer.MethodName.class)
class MedicalSchoolResourceTest {

	private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;
    
 // Test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;
    
    protected WebTarget webTarget;
    
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		 logger.debug("oneTimeSetUp");
	        uri = UriBuilder
	            .fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION)
	            .scheme(HTTP_SCHEMA)
	            .host(HOST)
	            .port(PORT)
	            .build();
	        adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
	        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER, DEFAULT_USER_PASSWORD);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		Client client = ClientBuilder.newClient().register(MyObjectMapperProvider.class).register(new LoggingFeature());
        webTarget = client.target(uri);
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
    public void test01_all_medicalschools_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICAL_SCHOOL_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test02_all_medicalschools_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(MEDICAL_SCHOOL_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200)); // User role should not have access, expecting 403 Forbidden
    }

    @Test
    public void test03_create_new_medicalschool_admin() throws JsonProcessingException {
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("entity-type", "private_school");
		requestMap.put("name", "Stanford Medical School");
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICAL_SCHOOL_RESOURCE_NAME)
            .request()
            .post(Entity.json(requestMap));
        
        assertThat(response.getStatus(), is(201)); // Expecting status 201 for creation

    }

    @Test
    public void test04_create_new_medicalschool_user() throws JsonProcessingException {
        // Example data for creating a new medical school
        String jsonInputString = "{\"name\": \"Stanford Medical School\", \"location\": \"Stanford, CA\"}";
        PrivateSchool privateSchool = new PrivateSchool();
        privateSchool.setName("Stanford Medical School");

        Response response = webTarget
            .register(userAuth)
            .path(MEDICAL_SCHOOL_RESOURCE_NAME)
            .request()
            .post(Entity.entity(privateSchool, MediaType.APPLICATION_JSON));

        assertThat(response.getStatus(), is(403)); // User role should not have permission, expecting 403 Forbidden
    }

    @Test
    public void test05_delete_medicalschool_by_id_admin() {
        // Assuming medical school ID 2 exists
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICAL_SCHOOL_RESOURCE_NAME + "/2")
            .request()
            .delete();

        assertThat(response.getStatus(), is(200)); // Expecting status 200 OK for successful deletion
    }

    @Test
    public void test06_delete_medicalschool_by_id_user() {
        // Assuming medical school ID 2 exists
        Response response = webTarget
            .register(userAuth)
            .path(MEDICAL_SCHOOL_RESOURCE_NAME + "/2")
            .request()
            .delete();

        assertThat(response.getStatus(), is(403)); // User role should not have permission, expecting 403 Forbidden
    }

	

}
