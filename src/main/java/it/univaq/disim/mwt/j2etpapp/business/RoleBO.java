package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.RoleClass;

public interface RoleBO {
    RoleClass findByName(String name);
}
