package acmemedical.rest.resource;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.Medicine;
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

import java.util.List;

@Path(MyConstants.MEDICINE_SUBRESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicineResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    @Inject
    protected SecurityContext sc;

    // Get all medicines
    @GET
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"}) // Allow both admins and users
    public Response getAllMedicines() {
        LOG.debug("Retrieving all medicines...");
        try {
            List<Medicine> medicines = service.getAllMedicines();
            LOG.debug("Found {} medicines", medicines.size());
            return Response.ok(medicines).build();
        } catch (Exception e) {
            LOG.error("Error retrieving medicines", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get medicine by ID
    @GET
    @Path("/{medicineId}")
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"}) // Allow both admins and users
    public Response getMedicineById(@PathParam("medicineId") int medicineId) {
        LOG.debug("Retrieving medicine with id = {}", medicineId);
        try {
            Medicine medicine = service.getMedicineById(medicineId);
            if (medicine != null) {
                return Response.ok(medicine).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error("Error retrieving medicine with id = {}", medicineId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Add new medicine
    @POST
    @RolesAllowed("ADMIN_ROLE") // Only admins are allowed to add a new medicine
    public Response addMedicine(Medicine newMedicine) {
        LOG.debug("Adding a new medicine = {}", newMedicine);
        try {
            Medicine createdMedicine = service.persistMedicine(newMedicine);
            return Response.status(Response.Status.CREATED).entity(createdMedicine).build();
        } catch (Exception e) {
            LOG.error("Error adding medicine", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update medicine
    @PUT
    @Path("/{medicineId}")
    @RolesAllowed("ADMIN_ROLE") // Only admins can update a medicine
    public Response updateMedicine(@PathParam("medicineId") int medicineId, Medicine updatedMedicine) {
        LOG.debug("Updating medicine with id = {}", medicineId);
        try {
            Medicine existingMedicine = service.getMedicineById(medicineId);
            if (existingMedicine == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            updatedMedicine.setId(medicineId);
            Medicine updated = service.updateMedicine(medicineId, updatedMedicine);
            return Response.ok(updated).build();
        } catch (Exception e) {
            LOG.error("Error updating medicine with id = {}", medicineId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete medicine by ID
    @DELETE
    @Path("/{medicineId}")
    @RolesAllowed("ADMIN_ROLE") // Only admins can delete a medicine
    public Response deleteMedicine(@PathParam("medicineId") int medicineId) {
        LOG.debug("Deleting medicine with id = {}", medicineId);
        try {
            Medicine medicine = service.getMedicineById(medicineId);
            if (medicine == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            service.deleteMedicineById(medicineId);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            LOG.error("Error deleting medicine with id = {}", medicineId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}