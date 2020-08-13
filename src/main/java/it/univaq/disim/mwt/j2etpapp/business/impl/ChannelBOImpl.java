package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ChannelBOImpl implements ChannelBO {

    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public List<ChannelClass> findAll() {
        return (List<ChannelClass>) channelRepository.findAll();
    }

    @Override
    public ChannelClass findById(Long id) {
        return channelRepository.findById(id).orElse(null);
    }

    @Override
    public ChannelClass findByName(String name) {
        return channelRepository.findByName(name).orElse(null);
    }

    @Override
    public void save(ChannelClass channel) {
        channelRepository.save(channel);
    }

    @Override
    public void saveAll(List<ChannelClass> channels) {
        channelRepository.saveAll(channels);
    }

    @Override
    public void saveAll(ChannelClass... channels) {
        channelRepository.saveAll(Arrays.asList(channels));
    }

    @Override
    public void deleteById(Long id) {
        channelRepository.deleteById(id);
    }

    @Override
    public void delete(ChannelClass channel) {
        channelRepository.delete(channel);
    }

    @Override
    public Long count() {
        return channelRepository.count();
    }
}
