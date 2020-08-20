package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class PostBOImpl implements PostBO {

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<PostClass> findAll() {
        return (List<PostClass>) postRepository.findAll();
    }

    @Override
    public List<PostClass> findAllOrderByCreatedAtDesc() {
        return (List<PostClass>) postRepository.findAll(Sort.by("created_at").descending());
    }

    @Override
    public it.univaq.disim.mwt.j2etpapp.business.Page<PostClass> findAllPaginatedOrderByCreatedAtDesc(int page, int size) {
        Page<PostClass> posts = postRepository.findAll(PageRequest.of(page, size, Sort.by("created_at").descending()));
        // TODO: page creation
        return null;
    }

    @Override
    public PostClass findById(String id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public void save(PostClass post) {
        postRepository.save(post);
    }

    @Override
    public void saveAll(List<PostClass> posts) {
        postRepository.saveAll(posts);
    }

    @Override
    public void saveAll(PostClass... posts) {
        postRepository.saveAll(Arrays.asList(posts));
    }

    @Override
    public void deleteById(String id) {
        postRepository.deleteById(id);
    }

    @Override
    public void delete(PostClass post) {
        postRepository.delete(post);
    }

    @Override
    public Long count() {
        return postRepository.count();
    }
}
