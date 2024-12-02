/********************************************************************************************************
 * File:  TestACMEMedicalSystem.java
 * Course Materials CST 8277
 * Teddy Yap
 * (Original Author) Mike Norman
 *
 */
package acmemedical;
import java.time.LocalDateTime;
import java.util.*;

import acmemedical.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.*;

import java.lang.invoke.MethodHandles;
import java.net.URI;

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
    // Define variables to hold entity data for delete tests
    private static List<Physician> physicianList = new ArrayList<>();
    private static List<Patient> patientList = new ArrayList<>();
    private static List<Medicine> medicineList = new ArrayList<>();
    private static List<Prescription> prescriptionList = new ArrayList<>();
    private static List<MedicalSchool> medicalSchoolList = new ArrayList<>();
    private static List<MedicalCertificate> medicalCertificateList = new ArrayList<>();
    private static List<MedicalTraining> medicalTrainingList = new ArrayList<>();


    private static WebTarget getTempTarget() {
        Client client = ClientBuilder.newClient()
                .register(MyObjectMapperProvider.class)
                .register(new LoggingFeature());
        WebTarget tempTarget = client.target(uri);
        return tempTarget;
    }


    private Physician dataPhysician(){
        WebTarget tempTarget = getTempTarget();
        Response response = tempTarget
            .register(adminAuth)
            .path(PHYSICIAN_RESOURCE_NAME)
            .request()
            .get();
        List<Physician> physicians = response.readEntity(new GenericType<List<Physician>>(){});
        physicianList = physicians;
        return physicianList.get(physicianList.size()-1);

    }


    private Patient dataPatient(){
        WebTarget tempTarget = getTempTarget();
        Response response = tempTarget
                 .register(adminAuth)
                 .path(PATIENT_RESOURCE_NAME)
                 .request()
                 .get();
         List<Patient> patients = response.readEntity(new GenericType<List<Patient>>(){});
         patientList = patients;
        return patientList.get(0);
    }
     private Medicine dataMedicine(){
         WebTarget tempTarget = getTempTarget();
         Response response = tempTarget
                 .register(adminAuth)
                 .path(MEDICINE_RESOURCE_NAME)
                 .request()
                 .get();
         List<Medicine> medicines = response.readEntity(new GenericType<List<Medicine>>(){});
         medicineList = medicines;
        return medicineList.get(0);
    }
     private Prescription dataPrescription(){
         WebTarget tempTarget = getTempTarget();
         Response response = tempTarget
                 .register(adminAuth)
                 .path(PRESCRIPTION_RESOURCE_NAME)
                 .request()
                 .get();
         List<Prescription> prescriptions = response.readEntity(new GenericType<List<Prescription>>(){});
        return prescriptionList.get(0);
    }

     private MedicalSchool dataMedicalSchool() throws JsonProcessingException {
         WebTarget tempTarget = getTempTarget();
         Response response = tempTarget
                 .register(adminAuth)
                 .path(MEDICAL_SCHOOL_RESOURCE_NAME)
                 .request(MediaType.APPLICATION_JSON)
                 .get();
         // read as String
         String jsonResponse = response.readEntity(String.class);
         // Configure ObjectMapper
         ObjectMapper mapper = new ObjectMapper();
         mapper.registerModule(new JavaTimeModule());
         mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
         mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
         // Create TypeReference for List<Map>
         List<Map<String, Object>> medicalSchoolMapList = mapper.readValue(jsonResponse,
                 new TypeReference<List<Map<String, Object>>>() {});
         assertThat(medicalSchoolMapList, is(not(empty())));
         for (Map<String, Object> school : medicalSchoolMapList) {
             assertThat(school.get(FIELD_ID), notNullValue());
             assertThat(school.get(FIELD_NAME), notNullValue());
         }
         getMedicalSchoolList(medicalSchoolMapList);
        return medicalSchoolList.get(0);
    }


//
//     private MedicalCertificate dataMedicalCertificate(){
//         WebTarget tempTarget = getTempTarget();
//         Response response = tempTarget
//                 .register(adminAuth)
//                 .path(MEDICAL_CERTIFICATE_RESOURCE_NAME)
//                 .request(MediaType.APPLICATION_JSON)
//                 .get();
//
//        return medicalCertificateList.get(0);
//    }
//     private MedicalTraining dataMedicalTraining(){
//         WebTarget tempTarget = getTempTarget();
//         Response response = tempTarget
//                 .register(adminAuth)
//                 .path(MEDICAL_TRAINING_RESOURCE_NAME)
//                 .request(MediaType.APPLICATION_JSON)
//                 .get();
//         List<MedicalTraining> medicalTrainings = response.readEntity(new GenericType<List<MedicalTraining>>(){});
//         return medicalTrainingList.get(0);
//    }
//



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
    private Medicine getMedicines(List<Medicine> medicines) {
        return medicines.get(0);
    }


    private void getMedicalSchoolList(List<Map<String, Object>> medicalSchools) {
        for (Map<String, Object> schoolMap : medicalSchools) {
            MedicalSchool school;
            if (SCHOOL_TYPE_PUBLIC.equals(schoolMap.get(ENTITY_TYPE))) {
                school = new PublicSchool();
            } else {
                school = new PrivateSchool();
            }
            // Set common properties
            school.setId((Integer) schoolMap.get(FIELD_ID));
            school.setName((String) schoolMap.get(FIELD_NAME));
            school.setVersion(((Number) schoolMap.get(FIELD_VERSION)).intValue());

            // Handle dates
            String created = (String) schoolMap.get(FIELD_CREATED);
            String updated = (String) schoolMap.get(FIELD_UPDATED);
            school.setCreated(LocalDateTime.parse(created));
            school.setUpdated(LocalDateTime.parse(updated));

            medicalSchoolList.add(school);
        }
    }

    //test cases for physician ---------------------------------------------------------------------------------
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
        physicianList = physicians;
    }

    @Test
    public void test02_all_physicians_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(PHYSICIAN_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(403));
    }




    @Test
    public void test03_get_physician_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        int id = dataPhysician().getId();
        Response response = webTarget
                .register(adminAuth)
                .path(PHYSICIAN_RESOURCE_NAME + SLASH+id)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        Physician physician = response.readEntity(Physician.class);
        assertThat(physician, is(notNullValue()));
        assertThat(physician.getId(), is(1));
    }

    @Test
    public void test04_get_physician_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        int id = dataPhysician().getId();
        Response response = webTarget
                .register(userAuth)
                .path(PHYSICIAN_RESOURCE_NAME + SLASH + id)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test05_create_physician_with_adminrole() throws JsonMappingException, JsonProcessingException {
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
    public void test06_create_physician_with_userrole() throws JsonMappingException, JsonProcessingException {
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
    public void test07_update_physician_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Physician updatePhysician = dataPhysician();
        int id = updatePhysician.getId();
        int patientId = 1;
//{physician_id}/patient/{patient_id}/medicine";
        Response response = webTarget
                .register(adminAuth)
                .path(PHYSICIAN_RESOURCE_NAME+SLASH + id+ SLASH+"patient"+SLASH+patientId+SLASH+"medicine")
                .request()
                .put(Entity.json(updatePhysician));
        assertThat(response.getStatus(), is(200));
        Physician physician = response.readEntity(Physician.class);
        assertThat(physician, is(notNullValue()));
        assertThat(physician.getId(), is(not(0)));
    }


    @Test
    public void test08_update_physician_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Medicine newMedicine = new Medicine();
        newMedicine.setDrugName("Ibuprofen");
        newMedicine.setManufacturerName("Johnson & Johnson");
        newMedicine.setDosageInformation("Take 2 tablets every 6 hours");
        int id = dataMedicine().getId();

        Response response = webTarget
                .register(userAuth)
                .path(MEDICINE_RESOURCE_NAME+SLASH + id)
                .request()
                .put(Entity.json(newMedicine));
        assertThat(response.getStatus(), is(403));
    }



    @Test
    public void test09_update_Not_Existed_physician_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Medicine newMedicine = new Medicine();
        newMedicine.setDrugName("Ibuprofen");
        newMedicine.setManufacturerName("Johnson & Johnson");
        newMedicine.setDosageInformation("Take 2 tablets every 6 hours");

        Response response = webTarget
                .register(adminAuth)
                .path(MEDICINE_RESOURCE_NAME+"/999999")
                .request()
                .put(Entity.json(newMedicine));
        assertThat(response.getStatus(), is(404));
    }





    //test cases for patients ---------------------------------------------------------------------------------
    @Test
    public void test10_all_patients_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(PATIENT_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<Patient> patients = response.readEntity(new GenericType<List<Patient>>(){});
        assertThat(patients, is(not(empty())));
        patientList = patients;
    }

    @Test
    public void test11_all_patients_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(PATIENT_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test12_get_patient_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        int id = dataPatient().getId();

        Response response = webTarget
                .register(adminAuth)
                .path(PATIENT_RESOURCE_NAME + SLASH + id)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        Patient patient = response.readEntity(Patient.class);
        assertThat(patient, is(notNullValue()));
        assertThat(patient.getId(), is(id));
    }

    @Test
    public void test13_get_patient_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        int id = dataPatient().getId();

        Response response = webTarget
                .register(userAuth)
                .path(PATIENT_RESOURCE_NAME + SLASH + id)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test14_create_patient_with_adminrole() throws JsonMappingException, JsonProcessingException {
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
    public void test15_create_patient_with_userrole() throws JsonMappingException, JsonProcessingException {
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


    @Test
    public void test16_update_patient_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        int id = dataPatient().getId();
        int version = dataPatient().getVersion();
        Patient existedPatient = dataPatient();

        Response response = webTarget
                .register(adminAuth)
                .path(PATIENT_RESOURCE_NAME+SLASH+id)
                .request()
                .put(Entity.json(existedPatient));
        assertThat(response.getStatus(), is(200));
        Patient createdPatient = response.readEntity(Patient.class);
        assertThat(createdPatient, is(notNullValue()));
        assertThat(createdPatient.getId(), is(not(0)));
    }

    @Test
    public void test17_update_patient_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        int id = dataPatient().getId();

        Patient newPatient = new Patient();
        newPatient.setFirstName("John");
        newPatient.setLastName("Smith");
        newPatient.setYear(1985);
        newPatient.setAddress("456 Oak St");
        newPatient.setHeight(180);
        newPatient.setWeight(75);
        newPatient.setSmoker((byte) 1);
        newPatient.setPrescription(null);

        Response response = webTarget
                .register(userAuth)
                .path(PATIENT_RESOURCE_NAME+SLASH + id)
                .request()
                .put(Entity.json(newPatient));
        assertThat(response.getStatus(), is(403));
    }




    //test cases for Medicine ---------------------------------------------------------------------------------

    @Test
    public void test18_all_medicines_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICINE_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<Medicine> medicines = response.readEntity(new GenericType<List<Medicine>>(){});
        assertThat(medicines, is(not(empty())));
        medicineList = medicines;
    }

    @Test
    public void test19_all_medicines_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(MEDICINE_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test20_get_medicine_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        int id = dataMedicine().getId();

        Medicine existedMedicine = dataMedicine();
         Response response = webTarget
                .register(adminAuth)
                .path(MEDICINE_RESOURCE_NAME + SLASH+id)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        Medicine medicine = response.readEntity(Medicine.class);
        assertThat(medicine, is(notNullValue()));
        assertThat(medicine.getId(), is(id));
    }



    @Test
    public void test21_get_medicine_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        int id = dataMedicine().getId();


        Response response = webTarget
                .register(userAuth)
                .path(MEDICINE_RESOURCE_NAME + SLASH + id)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test22_create_medicine_with_adminrole() throws JsonMappingException, JsonProcessingException {
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
    public void test23_create_medicine_with_userrole() throws JsonMappingException, JsonProcessingException {
        Medicine newMedicine = new Medicine();
        newMedicine.setDrugName("Ibuprofen");
        newMedicine.setManufacturerName("Johnson & Johnson");
        newMedicine.setDosageInformation("Take 2 tablets every 6 hours");
        newMedicine.setPrescriptions(null);
        Response response = webTarget
                .register(userAuth)
                .path(MEDICINE_RESOURCE_NAME)
                .request()
                .post(Entity.json(newMedicine));
        assertThat(response.getStatus(), is(403));
    }

    @Test
    public void test24_update_medicine_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Medicine newMedicine = new Medicine();
        newMedicine.setDrugName("Aspirin");
        newMedicine.setManufacturerName("Bayer");
        newMedicine.setDosageInformation("Take 1 tablet daily");
        newMedicine.setPrescriptions(null);
        int id = dataMedicine().getId();

        Response response = webTarget
                .register(adminAuth)
                .path(MEDICINE_RESOURCE_NAME+SLASH + id)
                .request()
                .put(Entity.json(newMedicine));
        assertThat(response.getStatus(), is(200));
        Medicine createdMedicine = response.readEntity(Medicine.class);
        assertThat(createdMedicine, is(notNullValue()));
        assertThat(createdMedicine.getId(), is(not(0)));
    }

    @Test
    public void test25_update_Not_Existed_medicine_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Medicine newMedicine = new Medicine();
        newMedicine.setDrugName("Ibuprofen");
        newMedicine.setManufacturerName("Johnson & Johnson");
        newMedicine.setDosageInformation("Take 2 tablets every 6 hours");

        Response response = webTarget
                .register(adminAuth)
                .path(MEDICINE_RESOURCE_NAME+"/999999")
                .request()
                .put(Entity.json(newMedicine));
        assertThat(response.getStatus(), is(404));
    }



    @Test
    public void test26_update_medicine_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        Medicine newMedicine = new Medicine();
        newMedicine.setDrugName("Ibuprofen");
        newMedicine.setManufacturerName("Johnson & Johnson");
        newMedicine.setDosageInformation("Take 2 tablets every 6 hours");
        int id = dataMedicine().getId();

        Response response = webTarget
                .register(userAuth)
                .path(MEDICINE_RESOURCE_NAME+SLASH + id)
                .request()
                .put(Entity.json(newMedicine));
        assertThat(response.getStatus(), is(403));
    }





    //test cases for MedicalSchool ---------------------------------------------------------------------------------
    @Test
    public void test27_all_medicalschools_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus(), is(200));

//        List<MedicalSchool> patients = response.readEntity(new GenericType<List<MedicalSchool>>(){});

        // read as String
        String jsonResponse = response.readEntity(String.class);

        // Configure ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Create TypeReference for List<Map>
        List<Map<String, Object>> medicalSchoolMapList = mapper.readValue(jsonResponse,
                new TypeReference<List<Map<String, Object>>>() {});

        assertThat(medicalSchoolMapList, is(not(empty())));

        for (Map<String, Object> school : medicalSchoolMapList) {
            assertThat(school.get(FIELD_ID), notNullValue());
            assertThat(school.get("name"), notNullValue());
        }
        getMedicalSchoolList(medicalSchoolMapList);
    }


    @Test
    public void test28_all_medicalschools_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<Map<String, Object>> medicalSchools = response.readEntity(
                new GenericType<List<Map<String, Object>>>() {}
        );
        assertThat(medicalSchools, is(not(empty())));
    }

    @Test
    public void test29_get_medicalschool_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        int id = dataMedicalSchool().getId();

        Response response = webTarget
                .register(adminAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME + SLASH + id)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus(), is(200));

//        MedicalSchool medicalSchool = response.readEntity(MedicalSchool.class);

        Map<String, Object> medicalSchoolMap = response.readEntity(
                new GenericType<Map<String, Object>>() {}
        );

        assertThat(medicalSchoolMap, is(notNullValue()));
        assertThat(medicalSchoolMap.get(FIELD_ID), is(id));

    }

    @Test
    public void test30_get_medicalschool_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        int id = dataMedicalSchool().getId();

        Response response = webTarget
                .register(userAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME + SLASH + id)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));

        Map<String, Object> medicalSchoolMap = response.readEntity(
                new GenericType<Map<String, Object>>() {}
        );

        assertThat(medicalSchoolMap, is(notNullValue()));
        assertThat(medicalSchoolMap.get(FIELD_ID), is(id));
    }

    @Test
    public void test31_create_medicalschool_with_adminrole() throws JsonMappingException, JsonProcessingException {
        MedicalSchoolDTO newMedicalSchoolDTO = new MedicalSchoolDTO();
        newMedicalSchoolDTO.setEntityType("private_school");
        newMedicalSchoolDTO.setName("Harvard Medical School");

        Response response = webTarget
                .register(adminAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME)
                .request()
                .post(Entity.json(newMedicalSchoolDTO));
        assertThat(response.getStatus(), is(200));
        Map<String, Object> medicalSchoolMap = response.readEntity(
                new GenericType<Map<String, Object>>() {}
        );

        assertThat(medicalSchoolMap, is(notNullValue()));
        assertThat(medicalSchoolMap.get("name"), is("Harvard Medical School"));
    }

    @Test
    public void test32_create_medicalschool_with_userrole() throws JsonMappingException, JsonProcessingException {
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
    public void test33_update_medicalschool_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        int id = dataMedicalSchool().getId();

        PrivateSchool updateMedicalSchool = new PrivateSchool();
        updateMedicalSchool.setName("State University Medical School");
        updateMedicalSchool.setId(id);
        updateMedicalSchool.setMedicalTrainings(null);


        Response response = webTarget
                .register(adminAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME + SLASH + id)
                .request()
                .put(Entity.json(updateMedicalSchool));
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test34_update_medicalschool_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        int id = dataMedicalSchool().getId();

        PrivateSchool updateMedicalSchool = new PrivateSchool();
        updateMedicalSchool.setName("State University Medical School");
        updateMedicalSchool.setMedicalTrainings(null);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(updateMedicalSchool);
        System.out.println("Sending JSON: " + json);

        Response response = webTarget
                .register(userAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME + SLASH + id)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(updateMedicalSchool));

        assertThat(response.getStatus(), is(200));
    }








    //test cases for MedicalTraining ---------------------------------------------------------------------------------

    @Test
    public void test35_getAllMedicalTrainings_withAdminRole() {
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICAL_TRAINING_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
//        List<MedicalTraining> trainings = response.readEntity(new GenericType<List<MedicalTraining>>() {});
//        assertThat(trainings, is(not(empty())));
    }


    @Test
    public void test36_getMedicalTrainingById_withAdminRole() {
//        int id = dataMedicalTraining().getId();
//        MedicalTraining updatedTraining = dataMedicalTraining();
        int id = 1;
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICAL_TRAINING_RESOURCE_NAME + SLASH + id)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
//        MedicalTraining training = response.readEntity(MedicalTraining.class);
//        assertThat(training.getId(), is(id));
    }


    @Test
    public void test37getAllMedicalTrainings_withUserRole() {
        Response response = webTarget
                .register(userAuth)
                .path(MEDICAL_TRAINING_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }




    //test cases for MedicalCertificate ---------------------------------------------------------------------------------

    @Test
    public void test38_getAllMedicalCertificate_withAdminRole() {
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICAL_CERTIFICATE_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<MedicalCertificate> prescriptions = response.readEntity(new GenericType<List<MedicalCertificate>>() {});
        assertThat(prescriptions, is(not(empty())));
    }

    @Test
    public void test39_getAllMedicalCertificate_withUserRole() {
        Response response = webTarget
                .register(userAuth)
                .path(MEDICAL_CERTIFICATE_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<MedicalCertificate> prescriptions = response.readEntity(new GenericType<List<MedicalCertificate>>() {});
        assertThat(prescriptions, is(not(empty())));
    }




    //test cases for Prescription ---------------------------------------------------------------------------------
    @Test
    public void test40_getAllPrescriptions_withAdminRole() {
        Response response = webTarget
                .register(adminAuth)
                .path(PRESCRIPTION_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<Prescription> prescriptions = response.readEntity(new GenericType<List<Prescription>>() {});
        assertThat(prescriptions, is(not(empty())));
    }

    @Test
    public void test41_getAllPrescriptions_withUserRole() {
        Response response = webTarget
                .register(userAuth)
                .path(PRESCRIPTION_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<Prescription> prescriptions = response.readEntity(new GenericType<List<Prescription>>() {});
        assertThat(prescriptions, is(not(empty())));
    }



    //test cases for delete ---------------------------------------------------------------------------------
    @Test
    public void test42_delete_physician_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        int id = dataPhysician().getId();

        Response response = webTarget
                .register(adminAuth)
                .path(PHYSICIAN_RESOURCE_NAME + SLASH + id)
                .request(MediaType.APPLICATION_JSON)
                .delete();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test43_delete_physician_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        int id = dataPhysician().getId();

        Response response = webTarget
                .register(userAuth)
                .path(PHYSICIAN_RESOURCE_NAME + SLASH + id)
                .request(MediaType.APPLICATION_JSON)
                .delete();
        assertThat(response.getStatus(), is(403));
    }




    @Test
    public void test44_delete_patient_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        int id = dataPatient().getId();

        Response response = webTarget
                .register(adminAuth)
                .path(PATIENT_RESOURCE_NAME + SLASH+id)
                .request()
                .delete();
        assertThat(response.getStatus(), is(204));
    }

    //status=403, reason="Forbidden"; only adminAuth can delete
    @Test
    public void test45_delete_patient_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        int id = dataPatient().getId();
        Response response = webTarget
                .register(userAuth)
                .path(PATIENT_RESOURCE_NAME + SLASH+id)
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
    }




   @Test
    public void test46_delete_medicalschool_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        int id = dataMedicalSchool().getId();

        Response response = webTarget
                .register(adminAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME + SLASH+id)
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
    }

    //status=403, reason="Forbidden"; only adminAuth can delete
    @Test
    public void test47_delete_medicalschool_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        int id = dataMedicalSchool().getId();

        Response response = webTarget
                .register(userAuth)
                .path(MEDICAL_SCHOOL_RESOURCE_NAME + SLASH+id)
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
    }



    // id not exist in database
    @Test
    public void test48_delete_medicine_by_notExisted_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        int id = 99999;
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICINE_RESOURCE_NAME + SLASH + id)
                .request()
                .delete();
//        NOT_FOUND(404, "Not Found"),
        assertThat(response.getStatus(), is(404));
    }


    //status=403, reason="Forbidden"; only adminAuth can delete
    @Test
    public void test49_delete_medicine_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        int id = dataMedicine().getId();

        Response response = webTarget
                .register(userAuth)
                .path(MEDICINE_RESOURCE_NAME + SLASH+id)
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
    }



    @Test
    public void test50_deleteMedicalTraining_withAdminRole() {
        int id = 1;
        Response response = webTarget
                .register(adminAuth)
                .path(MEDICAL_TRAINING_RESOURCE_NAME + SLASH+id)
                .request()
                .delete();
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void test51_deleteMedicalTraining_withUserRole() {
        int id = 1;
        Response response = webTarget
                .register(userAuth)
                .path(MEDICAL_TRAINING_RESOURCE_NAME+ SLASH+id)
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
    }





}














































