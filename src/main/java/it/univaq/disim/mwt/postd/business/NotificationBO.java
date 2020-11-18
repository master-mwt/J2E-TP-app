package it.univaq.disim.mwt.postd.business;

import it.univaq.disim.mwt.postd.domain.NotificationClass;

import java.util.List;

public interface NotificationBO {

    List<NotificationClass> findAll();
    NotificationClass findById(String id);
    List<NotificationClass> findByUserTargetId(Long userId);
    void save(NotificationClass notification);
    void saveAll(List<NotificationClass> notifications);
    void saveAll(NotificationClass... notifications);
    void deleteById(String id);
    void delete(NotificationClass notification);
    void deleteAllByUserTargetId(Long userId);
    Long count();

}
