package it.univaq.disim.mwt.postd.business.impl;

import it.univaq.disim.mwt.postd.business.NotificationBO;
import it.univaq.disim.mwt.postd.domain.NotificationClass;
import it.univaq.disim.mwt.postd.repository.mongo.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class NotificationBOImpl implements NotificationBO {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<NotificationClass> findAll() {
        return (List<NotificationClass>) notificationRepository.findAll();
    }

    @Override
    public NotificationClass findById(String id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public List<NotificationClass> findByUserTargetId(Long userId) {
        return notificationRepository.findByUserTargetId(userId).orElse(new ArrayList<>());
    }

    @Override
    public void save(NotificationClass notification) {
        notificationRepository.save(notification);
    }

    @Override
    public void saveAll(List<NotificationClass> notifications) {
        notificationRepository.saveAll(notifications);
    }

    @Override
    public void saveAll(NotificationClass... notifications) {
        notificationRepository.saveAll(Arrays.asList(notifications));
    }

    @Override
    public void deleteById(String id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public void delete(NotificationClass notification) {
        notificationRepository.delete(notification);
    }

    @Override
    public void deleteAllByUserTargetId(Long userId) {
        List<NotificationClass> notifications = notificationRepository.findByUserTargetId(userId).orElse(new ArrayList<>());
        if(!notifications.isEmpty()) {
            notificationRepository.deleteAll(notifications);
        }
    }

    @Override
    public Long count() {
        return notificationRepository.count();
    }
}
