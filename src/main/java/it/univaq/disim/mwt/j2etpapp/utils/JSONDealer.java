package it.univaq.disim.mwt.j2etpapp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.disim.mwt.j2etpapp.business.BusinessException;

public class JSONDealer {
    public static <T> String ObjectToJSON(T object) throws BusinessException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BusinessException("ObjectToJSON", e);
        }
    }
}
