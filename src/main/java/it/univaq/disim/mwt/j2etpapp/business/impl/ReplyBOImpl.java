package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.ReplyBO;
import it.univaq.disim.mwt.j2etpapp.domain.ReplyClass;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ReplyBOImpl implements ReplyBO {

    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public List<ReplyClass> findAll() {
        return (List<ReplyClass>) replyRepository.findAll();
    }

    @Override
    public ReplyClass findById(String id) {
        return replyRepository.findById(id).orElse(null);
    }

    @Override
    public void save(ReplyClass reply) {
        replyRepository.save(reply);
    }

    @Override
    public void saveAll(List<ReplyClass> replies) {
        replyRepository.saveAll(replies);
    }

    @Override
    public void saveAll(ReplyClass... replies) {
        replyRepository.saveAll(Arrays.asList(replies));
    }

    @Override
    public void deleteById(String id) {
        replyRepository.deleteById(id);
    }

    @Override
    public void delete(ReplyClass reply) {
        replyRepository.delete(reply);
    }

    @Override
    public Long count() {
        return replyRepository.count();
    }
}
