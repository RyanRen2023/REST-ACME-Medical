/********************************************************************************************************
 * File:  MedicalSchoolResource.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * 
 */
package acmemedical.rest.resource;

import java.util.List;

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
import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.USER_ROLE;
import jakarta.ws.rs.core.Response.Status;
import static acmemedical.utility.MyConstants.MEDICAL_SCHOOL_RESOURCE_NAME;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.MedicalTraining;
import acmemedical.entity.PrivateSchool;
import acmemedical.entity.PublicSchool;
import acmemedical.entity.MedicalSchool;
import acmemedical.entity.MedicalSchoolDTO;

@Path(MEDICAL_SCHOOL_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicalSchoolResource {
    
    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    @Inject
    protected SecurityContext sc;
    
    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response getMedicalSchools() {
        LOG.debug("Retrieving all medical schools...");
        List<MedicalSchool> medicalSchools = service.getAllMedicalSchools();
        LOG.debug("Medical schools found = {}", medicalSchools);
        Response response = Response.ok(medicalSchools).build();
        return response;
    }
    
    @GET
    // TODOo MSR01 - Specify the roles allowed for this method
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path("/{medicalSchoolId}")
    public Response getMedicalSchoolById(@PathParam("medicalSchoolId") int medicalSchoolId) {
        LOG.debug("Retrieving medical school with id = {}", medicalSchoolId);
        MedicalSchool medicalSchool = service.getMedicalSchoolById(medicalSchoolId);
        Response response;
        if(medicalSchool != null) {
        	response = Response.ok(medicalSchool).build();
        }else {
        	response = Response.status(Status.OK).entity("No School found").build();
        }        	
        return response;
    }

    @DELETE
    // TODOo MSR02 - Specify the roles allowed for this method
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{medicalSchoolId}")
    public Response deleteMedicalSchool(@PathParam("medicalSchoolId") int msId) {
        LOG.debug("Deleting medical school with id = {}", msId);
        MedicalSchool medicalSchool = service.deleteMedicalSchool(msId);
        Response response;
        if(medicalSchool != null) {
        	response = Response.ok(medicalSchool).build();
        }else {
        	response = Response.status(Status.OK).entity("No School found").build();
        }        	
        return response;
    }
    
    // Please try to understand and test the below methods:
    @RolesAllowed({ADMIN_ROLE})
    @POST
    public Response addMedicalSchool(MedicalSchoolDTO newMedicalSchoolDTO) {
    	MedicalSchool medicalSchool = null;
    	if(newMedicalSchoolDTO.getEntityType().equals("private_school")) {
    		medicalSchool = new PrivateSchool(newMedicalSchoolDTO.getName());
    		
    	}else if(newMedicalSchoolDTO.getEntityType().equals("public_school")) {
    		medicalSchool = new PublicSchool(newMedicalSchoolDTO.getName());
    	}else {
    	    return Response.status(Status.BAD_REQUEST).entity("Invalid entity type").build();
    	}
        LOG.debug("Adding a new medical school = {}", medicalSchool);
        MedicalSchool tempMedicalSchool = service.persistMedicalSchool(medicalSchool);
        return Response.ok(tempMedicalSchool).build();
    }

    @RolesAllowed({ADMIN_ROLE})
    @POST
    @Path("/{medicalSchoolId}/medicaltraining")
    public Response addMedicalTrainingToMedicalSchool(@PathParam("medicalSchoolId") int msId, MedicalTraining newMedicalTraining) {
        LOG.debug("Adding a new MedicalTraining to medical school with id = {}", msId);
        
        MedicalSchool ms = service.getMedicalSchoolById(msId);
        newMedicalTraining.setMedicalSchool(ms);
        ms.getMedicalTrainings().add(newMedicalTraining);
        service.updateMedicalSchool(msId, ms);
        
        return Response.ok(sc).build();
    }

    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @PUT
    @Path("/{medicalSchoolId}")
    public Response updateMedicalSchool(@PathParam("medicalSchoolId") int msId, MedicalSchool updatingMedicalSchool) {
        LOG.debug("Updating a specific medical school with id = {}", msId);
        Response response = null;
        MedicalSchool updatedMedicalSchool = service.updateMedicalSchool(msId, updatingMedicalSchool);
        response = Response.ok(updatedMedicalSchool).build();
        return response;
    }
    
}
