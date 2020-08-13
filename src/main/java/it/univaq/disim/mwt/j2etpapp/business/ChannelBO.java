package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;

import java.util.List;

public interface ChannelBO {

    List<ChannelClass> findAll();
    ChannelClass findById(Long id);
    ChannelClass findByName(String name);
    void save(ChannelClass channel);
    void saveAll(List<ChannelClass> channels);
    void saveAll(ChannelClass... channels);
    void deleteById(Long id);
    void delete(ChannelClass channel);
    Long count();

}
