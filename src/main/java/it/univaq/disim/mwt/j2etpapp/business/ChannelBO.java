package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;

import java.util.List;

public interface ChannelBO {
    List<ChannelClass> findAll();
}
