package acmemedical.rest.resource;
import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.Patient;
import acmemedical.utility.MyConstants;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.USER_ROLE;

import java.util.List;

@Path(MyConstants.PATIENT_RESOURCE_NAME)  // API endpoint for patients
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PatientResouce {
	
	 private static final Logger LOG = LogManager.getLogger();

	    @EJB
	    protected ACMEMedicalService service;

	    @Inject
	    protected SecurityContext sc;

	    // Get all patients
	    @GET
	    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
	    public Response getAllPatients() {
	        LOG.debug("Retrieving all patients...");
	        List<Patient> patients = service.getAllPatients();  // Assuming this service method exists
	        LOG.debug("Patients found = {}", patients);
	        return Response.ok(patients).build();
	    }

	    // Get a patient by ID
	    @GET
	    @Path("/{patientId}")
	    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
	    public Response getPatientById(@PathParam("patientId") int patientId) {
	        LOG.debug("Retrieving patient with id = {}", patientId);
	        Patient patient = service.getPatientById(patientId);  // Assuming this service method exists
	        if (patient == null) {
	            return Response.status(Response.Status.NOT_FOUND).entity("Patient not found").build();
	        }
	        return Response.ok(patient).build();
	    }

	    // Add a new patient
	    @POST
	    @RolesAllowed({ADMIN_ROLE})
	    public Response addPatient(Patient newPatient) {
	        LOG.debug("Adding a new patient = {}", newPatient);
	        
	        Patient createdPatient = service.persistPatient(newPatient);  // Assuming this service method exists
	        return Response.status(Response.Status.CREATED).entity(createdPatient).build();
	    }

	    // Update an existing patient
	    @PUT
	    @Path("/{patientId}")
	    @RolesAllowed({ADMIN_ROLE})
	    public Response updatePatient(@PathParam("patientId") int patientId, Patient updatedPatient) {
	        LOG.debug("Updating patient with id = {}", patientId);
	        Patient existingPatient = service.getPatientById(patientId);  // Assuming this service method exists
	        if (existingPatient == null) {
	            return Response.status(Response.Status.NOT_FOUND).entity("Patient not found").build();
	        }
	        updatedPatient.setId(patientId);  // Ensure the patient ID is set correctly
	        Patient updated = service.updatePatient(patientId, updatedPatient);  // Assuming this service method exists
	        return Response.ok(updated).build();
	    }

	    // Delete a patient
	    @DELETE
	    @Path("/{patientId}")
	    @RolesAllowed({ADMIN_ROLE})
	    public Response deletePatient(@PathParam("patientId") int patientId) {
	        LOG.debug("Deleting patient with id = {}", patientId);
	        Patient patient = service.deletePatientById(patientId);  // Assuming this service method exists
	        if (patient == null) {
	            return Response.status(Status.NOT_FOUND).entity("Patient not found").build();
	        }
	        return Response.status(Status.OK).entity("Patient was deleted successfully!").build();
	    }

}
