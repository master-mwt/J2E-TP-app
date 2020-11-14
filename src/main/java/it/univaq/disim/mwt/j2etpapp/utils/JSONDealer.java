package it.univaq.disim.mwt.j2etpapp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSONDealer {
    public static <T> String ObjectToJSON(T object) throws BusinessException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.info("ObjectToJSON: Error in converting object to JSON");
            throw new BusinessException("Object to JSON conversion error", e);
        }
    }
}
