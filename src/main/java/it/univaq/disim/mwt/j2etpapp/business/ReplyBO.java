package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.ReplyClass;

import java.util.List;

public interface ReplyBO {

    List<ReplyClass> findAll();
    ReplyClass findById(String id);
    void save(ReplyClass reply);
    void saveAll(List<ReplyClass> replies);
    void saveAll(ReplyClass... replies);
    void deleteById(String id);
    void delete(ReplyClass reply);
    Long count();

}
