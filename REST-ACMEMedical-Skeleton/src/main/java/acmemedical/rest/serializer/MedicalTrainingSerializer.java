/********************************************************************************************************
 * File:  SecurityRoleSerializer.java
 * Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Mike Norman
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
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import acmemedical.entity.MedicalTraining;
import acmemedical.entity.SecurityRole;

public class MedicalTrainingSerializer extends StdSerializer<Set<MedicalTraining>> implements Serializable {
    private static final long serialVersionUID = 1L;

    public MedicalTrainingSerializer() {
        this(null);
    }

    public MedicalTrainingSerializer(Class<Set<MedicalTraining>> t) {
        super(t);
    }

    /**
     * This is to prevent back and forth serialization between many-to-many relations.<br>
     * This is done by setting the relation to null.
     */
    @Override
    public void serialize(Set<MedicalTraining> original, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
    	int size = original.size();
    	generator.writeStartObject();
    	generator.writeNumberField("medicalTrainingsCount", size);

        generator.writeEndObject();
    }
}