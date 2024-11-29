package acmemedical.rest.resource;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.MedicalCertificate;
import acmemedical.utility.MyConstants;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path(MyConstants.MEDICAL_CERTIFICATE_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicalCertificateResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    @Inject
    protected SecurityContext sc;

    // Get all medical certificates
    @GET
    public Response getAllMedicalCertificates() {
        LOG.debug("Retrieving all medical certificates...");
        try {
            // Retrieve all certificates
            List<MedicalCertificate> certificates = service.getAllMedicalCertificates();
            LOG.debug("Found {} certificates", certificates.size());
            return Response.ok(certificates).build();
        } catch (Exception e) {
            LOG.error("Error retrieving medical certificates", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get a specific medical certificate by ID
    @GET
    @Path("/{certificateId}")
    public Response getMedicalCertificateById(@PathParam("certificateId") int certificateId) {
        LOG.debug("Retrieving medical certificate with ID = {}", certificateId);
        try {
            MedicalCertificate certificate = service.getMedicalCertificateById(certificateId);
            if (certificate != null) {
                return Response.ok(certificate).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Medical certificate not found").build();
            }
        } catch (Exception e) {
            LOG.error("Error retrieving medical certificate by ID", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create a new medical certificate
    @POST
    public Response addMedicalCertificate(MedicalCertificate newCertificate) {
        LOG.debug("Adding a new medical certificate: {}", newCertificate);
        try {
            // Create the new medical certificate
            MedicalCertificate createdCertificate = service.persistMedicalCertificate(newCertificate);
            return Response.status(Response.Status.CREATED).entity(createdCertificate).build();
        } catch (Exception e) {
            LOG.error("Error adding medical certificate", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update an existing medical certificate
    @PUT
    @Path("/{certificateId}")
    public Response updateMedicalCertificate(@PathParam("certificateId") int certificateId, MedicalCertificate updatedCertificate) {
        LOG.debug("Updating medical certificate with ID = {}", certificateId);
        try {
            MedicalCertificate existingCertificate = service.getMedicalCertificateById(certificateId);
            if (existingCertificate != null) {
                updatedCertificate.setId(certificateId);
                MedicalCertificate updated = service.updateMedicalCertificate(certificateId, updatedCertificate);
                return Response.ok(updated).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Medical certificate not found").build();
            }
        } catch (Exception e) {
            LOG.error("Error updating medical certificate", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete a medical certificate by ID
    @DELETE
    @Path("/{certificateId}")
    public Response deleteMedicalCertificate(@PathParam("certificateId") int certificateId) {
        LOG.debug("Deleting medical certificate with ID = {}", certificateId);
        try {
            MedicalCertificate certificate = service.getMedicalCertificateById(certificateId);
            if (certificate != null) {
                service.deleteMedicalCertificateById(certificateId);
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Medical certificate not found").build();
            }
        } catch (Exception e) {
            LOG.error("Error deleting medical certificate", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}