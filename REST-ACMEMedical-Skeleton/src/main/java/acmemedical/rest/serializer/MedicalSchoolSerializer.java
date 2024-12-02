/********************************************************************************************************
 * File:  MedicalSchoolSerializer.java Course Materials CST 8277
 *
 * @author Shaoxian Duan
 * @author Xihai Ren
 * @author Yaozhou Xie
 * @author Huacong Xie
 * @modified_date 2024-12-02
 *
 */

package acmemedical.rest.serializer;

import java.io.IOException;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import acmemedical.entity.MedicalSchool;
import acmemedical.entity.PrivateSchool;
import acmemedical.entity.PublicSchool;

public class MedicalSchoolSerializer extends StdSerializer<MedicalSchool> implements Serializable {

	private static final long serialVersionUID = 1L;

	public MedicalSchoolSerializer() {
		this(null);
	}

	public MedicalSchoolSerializer(Class<MedicalSchool> t) {
		super(t);
	}

	@Override
	public void serialize(MedicalSchool school, JsonGenerator gen, SerializerProvider provider) throws IOException {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		gen.writeStartObject();
		if (school instanceof PrivateSchool) {
			gen.writeStringField("entityType", "private_school");
		} else if (school instanceof PublicSchool){
			gen.writeStringField("entityType", "public_school");
		}
		gen.writeNumberField("id", school.getId());
		gen.writeNumberField("version", school.getVersion());
		gen.writeStringField("created", school.getCreated().format(formatter));
		gen.writeStringField("updated", school.getUpdated().format(formatter));
		gen.writeStringField("name", school.getName());
		gen.writeNumberField("medicalTrainingsCount", school.getMedicalTrainings().size());
		gen.writeEndObject();

	}

}
