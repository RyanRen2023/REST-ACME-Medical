/********************************************************************************************************
 * File:  MyObjectMapperProvider.java
 * Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Mike Norman
 *
 * @author Shaoxian Duan
 * @author Xihai Ren
 * @author Yaozhou Xie
 * @author Huacong Xie
 * @modified_date 2024-12-02
 *
 *
 * Note:  Students do NOT need to change anything in this class.
 */
package acmemedical;

import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;

@Provider
public class MyObjectMapperProvider extends JacksonJsonProvider implements ContextResolver<ObjectMapper> {
    
    static ObjectMapper defaultObjectMapper;
    static {
        defaultObjectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    public MyObjectMapperProvider() {
        super(defaultObjectMapper);
    }
    
    public ObjectMapper getContext(Class<?> type) {
            return defaultObjectMapper;
    }
    
}