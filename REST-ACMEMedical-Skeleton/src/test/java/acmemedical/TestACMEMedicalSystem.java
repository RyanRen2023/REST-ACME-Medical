/********************************************************************************************************
 * File:  TestACMEMedicalSystem.java
 * Course Materials CST 8277
 * Teddy Yap
 * (Original Author) Mike Norman
 *
 */
package acmemedical;

import acmemedical.entity.MedicalSchoolDTO;
import acmemedical.entity.Medicine;
import acmemedical.entity.Patient;
import acmemedical.entity.Physician;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.*;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import static acmemedical.utility.MyConstants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
@SuppressWarnings("unused")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestACMEMedicalSystem {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;

    // Test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;

    @BeforeAll
    public static void oneTimeSetUp() throws Exception {
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

    protected WebTarget webTarget;
    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient().register(MyObjectMapperProvider.class).register(new LoggingFeature());
        webTarget = client.target(uri);
    }

    @Test
    public void test01_all_physicians_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(PHYSICIAN_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<Physician> physicians = response.readEntity(new GenericType<List<Physician>>(){});
        assertThat(physicians, is(not(empty())));
        assertThat(physicians, hasSize(greaterThan(0)));
    }

    @Test
    public void test01_all_physicians_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(PHYSICIAN_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(403));
    }




    @Test
    public void test02_get_physician_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(PHYSICIAN_RESOURCE_NAME + "/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        Physician physician = response.readEntity(Physician.class);
        assertThat(physician, is(notNullValue()));
        assertThat(physician.getId(), is(1));
    }

    @Test
    public void test02_get_physician_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(PHYSICIAN_RESOURCE_NAME + "/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test03_create_physician_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Physician newPhysician = new Physician();
        newPhysician.setFirstName("John");
        newPhysician.setLastName("Doe");

        Response response = webTarget
                .register(adminAuth)
                .path(PHYSICIAN_RESOURCE_NAME)
                .request()
                .post(Entity.json(newPhysician));
        assertThat(response.getStatus(), is(200));
        Physician createdPhysician = response.readEntity(Physician.class);
        assertThat(createdPhysician, is(notNullValue()));
        assertThat(createdPhysician.getId(), is(not(0)));
    }

    @Test
    public void test03_create_physician_with_userrole() throws JsonMappingException, JsonProcessingException {
        Physician newPhysician = new Physician();
        newPhysician.setFirstName("Jane");
        newPhysician.setLastName("Smith");

        Response response = webTarget
                .register(userAuth)
                .path(PHYSICIAN_RESOURCE_NAME)
                .request()
                .post(Entity.json(newPhysician));
        assertThat(response.getStatus(), is(403));
    }

    @Test
    public void test04_delete_physician_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(PHYSICIAN_RESOURCE_NAME + "/2")
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test04_delete_physician_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(PHYSICIAN_RESOURCE_NAME + "/2")
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
    }


    @Test
    public void test05_all_patients_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(PATIENT_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<Patient> patients = response.readEntity(new GenericType<List<Patient>>(){});
        assertThat(patients, is(not(empty())));
    }

    @Test
    public void test5_all_patients_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(PATIENT_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test6_get_patient_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(PATIENT_RESOURCE_NAME + "/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        Patient patient = response.readEntity(Patient.class);
        assertThat(patient, is(notNullValue()));
        assertThat(patient.getId(), is(1));
    }

    @Test
    public void test6_get_patient_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(PATIENT_RESOURCE_NAME + "/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test7_create_patient_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Patient newPatient = new Patient();
        newPatient.setFirstName("Jane");
        newPatient.setLastName("Doe");
        newPatient.setYear(1990);
        newPatient.setAddress("123 Main St");
        newPatient.setHeight(170);
        newPatient.setWeight(60);
        newPatient.setSmoker((byte) 0);

        Response response = webTarget
                .register(adminAuth)
                .path(PATIENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(newPatient));
        assertThat(response.getStatus(), is(201));
        Patient createdPatient = response.readEntity(Patient.class);
        assertThat(createdPatient, is(notNullValue()));
        assertThat(createdPatient.getId(), is(not(0)));
    }

    @Test
    public void test7_create_patient_with_userrole() throws JsonMappingException, JsonProcessingException {
        Patient newPatient = new Patient();
        newPatient.setFirstName("John");
        newPatient.setLastName("Smith");
        newPatient.setYear(1985);
        newPatient.setAddress("456 Oak St");
        newPatient.setHeight(180);
        newPatient.setWeight(75);
        newPatient.setSmoker((byte) 1);

        Response response = webTarget
                .register(userAuth)
                .path(PATIENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(newPatient));
        assertThat(response.getStatus(), is(403));
    }

/*
    @Test
    public void test8_delete_patient_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(PATIENT_RESOURCE_NAME + "/2")
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
    }
*/

    @Test
    public void test8_delete_patient_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(PATIENT_RESOURCE_NAME + "/3")
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
    }

    // Medicine test methods
    @Test
    public void test9_all_medicines_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICINE_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<Medicine> medicines = response.readEntity(new GenericType<List<Medicine>>(){});
        assertThat(medicines, is(not(empty())));
    }

    @Test
    public void test9_all_medicines_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(MEDICINE_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test10_get_medicine_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICINE_RESOURCE_NAME + "/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        Medicine medicine = response.readEntity(Medicine.class);
        assertThat(medicine, is(notNullValue()));
        assertThat(medicine.getId(), is(1));
    }

    @Test
    public void test10_get_medicine_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(MEDICINE_RESOURCE_NAME + "/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test11_create_medicine_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Medicine newMedicine = new Medicine();
        newMedicine.setDrugName("Aspirin");
        newMedicine.setManufacturerName("Bayer");
        newMedicine.setDosageInformation("Take 1 tablet daily");

        Response response = webTarget
                .register(adminAuth)
                .path(MEDICINE_RESOURCE_NAME)
                .request()
                .post(Entity.json(newMedicine));
        assertThat(response.getStatus(), is(201));
        Medicine createdMedicine = response.readEntity(Medicine.class);
        assertThat(createdMedicine, is(notNullValue()));
        assertThat(createdMedicine.getId(), is(not(0)));
    }

    @Test
    public void test11_create_medicine_with_userrole() throws JsonMappingException, JsonProcessingException {
        Medicine newMedicine = new Medicine();
        newMedicine.setDrugName("Ibuprofen");
        newMedicine.setManufacturerName("Johnson & Johnson");
        newMedicine.setDosageInformation("Take 2 tablets every 6 hours");

        Response response = webTarget
                .register(userAuth)
                .path(MEDICINE_RESOURCE_NAME)
                .request()
                .post(Entity.json(newMedicine));
        assertThat(response.getStatus(), is(403));
    }

/*
    @Test
    public void test12_delete_medicine_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICINE_RESOURCE_NAME + "/1")
                .request()
                .delete();
        assertThat(response.getStatus(), is(204));
    }
*/

    @Test
    public void test12_delete_medicine_by_notExisted_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICINE_RESOURCE_NAME + "/9999999")
                .request()
                .delete();
//        NOT_FOUND(404, "Not Found"),
        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void test12_delete_medicine_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(MEDICINE_RESOURCE_NAME + "/3")
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
    }

    
    
    // MedicalSchool test methods
    @Test
    public void test13_all_medicalschools_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(200));
//        List<MedicalSchool> medicalSchools = response.readEntity(new GenericType<List<MedicalSchool>>(){});
//        assertThat(medicalSchools, is(not(empty())));
    }

    @Test
    public void test13_all_medicalschools_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test14_get_medicalschool_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME + "/2")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
//        MedicalSchool medicalSchool = response.readEntity(MedicalSchool.class);
//        assertThat(medicalSchool, is(notNullValue()));
//        assertThat(medicalSchool.getId(), is(2));
    }

    @Test
    public void test14_get_medicalschool_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME + "/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test15_create_medicalschool_with_adminrole() throws JsonMappingException, JsonProcessingException {
        MedicalSchoolDTO newMedicalSchoolDTO = new MedicalSchoolDTO();
        newMedicalSchoolDTO.setEntityType("private_school");
        newMedicalSchoolDTO.setName("Harvard Medical School");

        Response response = webTarget
                .register(adminAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME)
                .request()
                .post(Entity.json(newMedicalSchoolDTO));
        assertThat(response.getStatus(), is(200));
//        MedicalSchool createdMedicalSchool = response.readEntity(MedicalSchool.class);
//        assertThat(createdMedicalSchool, is(notNullValue()));
//        assertThat(createdMedicalSchool.getId(), is(not(0)));
    }

    @Test
    public void test15_create_medicalschool_with_userrole() throws JsonMappingException, JsonProcessingException {
        MedicalSchoolDTO newMedicalSchoolDTO = new MedicalSchoolDTO();
        newMedicalSchoolDTO.setEntityType("private_school");
        newMedicalSchoolDTO.setName("Yale School of Medicine");

        Response response = webTarget
                .register(userAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME)
                .request()
                .post(Entity.json(newMedicalSchoolDTO));
        assertThat(response.getStatus(), is(403));
    }

    @Test
    public void test16_delete_medicalschool_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME + "/3")
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test16_delete_medicalschool_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME + "/2")
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
    }













}














































