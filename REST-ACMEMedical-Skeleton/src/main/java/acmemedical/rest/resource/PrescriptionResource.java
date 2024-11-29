package acmemedical.rest.resource;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.Prescription;
import acmemedical.entity.PrescriptionPK;
import acmemedical.utility.MyConstants;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.USER_ROLE;

import java.util.List;

@Path(MyConstants.PRESCRIPTION_RESOURCE_NAME)  // API endpoint for prescriptions
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PrescriptionResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    @Inject
    protected SecurityContext sc;

    // Get all prescriptions
    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response getAllPrescriptions() {
        LOG.debug("Retrieving all prescriptions...");
        List<Prescription> prescriptions = service.getAllPrescriptions();  // Assuming this service method exists
        LOG.debug("Prescriptions found = {}", prescriptions);
        return Response.ok(prescriptions).build();
    }

    // Get a prescription by ID
    @GET
    @Path("/{prescriptionId}")
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response getPrescriptionById(@PathParam("physicianId") int physicianId, @PathParam("patientId") int patientId) {
    	PrescriptionPK prescriptionId = new PrescriptionPK(physicianId,patientId);
    	LOG.debug("Retrieving prescription with id = {}", prescriptionId);
        Prescription prescription = service.getPrescriptionById(prescriptionId);  // Assuming this service method exists
        if (prescription == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Prescription not found").build();
        }
        return Response.ok(prescription).build();
    }

    // Add a new prescription
    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addPrescription(Prescription newPrescription) {
        LOG.debug("Adding a new prescription = {}", newPrescription);
       
        Prescription createdPrescription = service.persistPrescription(newPrescription);  // Assuming this service method exists
        return Response.status(Response.Status.CREATED).entity(createdPrescription).build();
    }

    // Update an existing prescription
    @PUT
    @Path("/{prescriptionId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response updatePrescription(@PathParam("physicianId") int physicianId, @PathParam("patientId") int patientId, Prescription updatedPrescription) {
        
    	PrescriptionPK prescriptionId = new PrescriptionPK(physicianId,patientId);
    	LOG.debug("Updating prescription with id = {}", prescriptionId);
        Prescription existingPrescription = service.getPrescriptionById(prescriptionId);  // Assuming this service method exists
        if (existingPrescription == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Prescription not found").build();
        }
        updatedPrescription.setId(prescriptionId);  // Ensure the prescription ID is set correctly
        Prescription updated = service.updatePrescription(prescriptionId, updatedPrescription);  // Assuming this service method exists
        return Response.ok(updated).build();
    }

    // Delete a prescription
    @DELETE
    @Path("/{prescriptionId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response deletePrescription(@PathParam("physicianId") int physicianId, @PathParam("patientId") int patientId) {
    	PrescriptionPK prescriptionId = new PrescriptionPK(physicianId,patientId);
        LOG.debug("Deleting prescription with id = {}", prescriptionId);
        Prescription prescription = service.deletePrescriptionById(prescriptionId);  // Assuming this service method exists
        if (prescription == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Prescription not found").build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}