package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.ImageBO;
import it.univaq.disim.mwt.j2etpapp.domain.ImageClass;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ImageBOImpl implements ImageBO {

    @Autowired
    private ImageRepository imageRepository;


    @Override
    public List<ImageClass> findAll() {
        return (List<ImageClass>) imageRepository.findAll();
    }

    @Override
    public ImageClass findById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    @Override
    public ImageClass findByCaption(String caption) {
        return imageRepository.findByCaption(caption).orElse(null);
    }

    @Override
    public void save(ImageClass image) {
        imageRepository.save(image);
    }

    @Override
    public void saveAll(List<ImageClass> images) {
        imageRepository.saveAll(images);
    }

    @Override
    public void saveAll(ImageClass... images) {
        imageRepository.saveAll(Arrays.asList(images));
    }

    @Override
    public void deleteById(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    public void delete(ImageClass image) {
        imageRepository.delete(image);
    }

    @Override
    public Long count() {
        return imageRepository.count();
    }
}
