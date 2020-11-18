package it.univaq.disim.mwt.postd.business;

import it.univaq.disim.mwt.postd.domain.UserClass;

public interface AuthBO {
    void registerUser(UserClass user) throws BusinessException;
}
