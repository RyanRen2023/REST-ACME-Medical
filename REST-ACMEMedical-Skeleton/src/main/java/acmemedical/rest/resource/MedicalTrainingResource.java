/********************************************************************************************************
 * File:  MedicalTrainingResource.java Course Materials CST 8277
 *
 * @author Shaoxian Duan
 * @author Xihai Ren
 * @author Yaozhou Xie
 * @author Huacong Xie
 * @modified_date 2024-12-02
 *
 */

package acmemedical.rest.resource;

import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.MEDICAL_TRAINING_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.USER_ROLE;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.MedicalTraining;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path(MEDICAL_TRAINING_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicalTrainingResource {

	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected ACMEMedicalService service;

	@Inject
	protected SecurityContext sc;

	// Get all Medical Trainings
	@GET
	@RolesAllowed({ ADMIN_ROLE, USER_ROLE })
	public Response getMedicalTrainings() {
		LOG.debug("Retrieving all medical trainings...");
		List<MedicalTraining> medicalTrainings = service.getAllMedicalTraining();
		LOG.debug("Medical trainings found = {}", medicalTrainings);
		return Response.ok(medicalTrainings).build();
	}

	// Get a specific Medical Training by ID
	@GET
	@Path("/{medicalTrainingId}")
	@RolesAllowed({ ADMIN_ROLE, USER_ROLE })
	public Response getMedicalTrainingById(@PathParam("medicalTrainingId") int medicalTrainingId) {
		LOG.debug("Retrieving medical training with id = {}", medicalTrainingId);
		MedicalTraining medicalTraining = service.getMedicalTrainingById(medicalTrainingId);
		if (medicalTraining == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Medical Training not found for ID: " + medicalTrainingId).build();
		}
		return Response.ok(medicalTraining).build();
	}

	// Add a new Medical Training
	@POST
	@RolesAllowed({ ADMIN_ROLE })
	public Response addMedicalTraining(MedicalTraining newMedicalTraining) {
		LOG.debug("Adding a new medical training = {}", newMedicalTraining);
		MedicalTraining createdMedicalTraining = service.persistMedicalTraining(newMedicalTraining);
		return Response.status(Response.Status.CREATED).entity(createdMedicalTraining).build();

	}

	// Update an existing Medical Training
	@PUT
	@Path("/{medicalTrainingId}")
	@RolesAllowed({ ADMIN_ROLE })
	public Response updateMedicalTraining(@PathParam("medicalTrainingId") int mtId,
			MedicalTraining updatedMedicalTraining) {
		LOG.debug("Updating medical training with id = {}", mtId);
		MedicalTraining existingMedicalTraining = service.getMedicalTrainingById(mtId);
		if (existingMedicalTraining == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Medical Training not found for ID: " + mtId)
					.build();
		}

		updatedMedicalTraining.setId(existingMedicalTraining.getId()); // Ensure we keep the same ID
		MedicalTraining updated = service.updateMedicalTraining(mtId, updatedMedicalTraining);
		return Response.ok(updated).build();
	}

	// Delete a Medical Training by ID
	@DELETE
    @Path("/{medicalTrainingId}")
    @RolesAllowed({ADMIN_ROLE})
    public Response deleteMedicalTraining(@PathParam("medicalTrainingId") int mtId) {
        LOG.debug("Deleting medical training with id = {}", mtId);
        MedicalTraining deletedMedicalTraining = service.deleteMedicalTraining(mtId);
        if (deletedMedicalTraining == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Medical Training not found for ID: " + mtId)
                    .build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
