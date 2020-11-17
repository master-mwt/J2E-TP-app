package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.UserClass;

public interface AuthBO {
    void registerUser(UserClass user) throws BusinessException;
}
