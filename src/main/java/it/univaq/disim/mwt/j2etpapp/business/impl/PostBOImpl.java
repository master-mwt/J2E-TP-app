package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.Page;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.TagClass;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public Page<PostClass> findAllOrderByCreatedAtDescPaginated(int page, int size) {
        return new Page<>(postRepository.findAll(PageRequest.of(page, size, Sort.by("created_at").descending())));
    }

    @Override
    public List<PostClass> findByChannelId(Long channelId) {
        return postRepository.findByChannelId(channelId).orElse(new ArrayList<>());
    }

    @Override
    public Page<PostClass> findByChannelIdReportedOrderByCreatedAtDescPaginated(Long channelId, int page, int size) {
        return new Page<>(postRepository.findByChannelIdAndReported(channelId, true, PageRequest.of(page, size, Sort.by("created_at").descending())));
    }

    @Override
    public Page<PostClass> findByChannelIdOrderByCreatedAtDescPaginated(Long channelId, int page, int size) {
        return new Page<>(postRepository.findByChannelId(channelId, PageRequest.of(page, size, Sort.by("created_at").descending())));
    }

    @Override
    public List<PostClass> findByUserId(Long userId) {
        return postRepository.findByUserId(userId).orElse(new ArrayList<>());
    }

    @Override
    public Page<PostClass> findByUserIdOrderByCreatedAtDescPaginated(Long userId, int page, int size) {
        return new Page<>(postRepository.findByUserId(userId, PageRequest.of(page, size, Sort.by("created_at").descending())));
    }

    @Override
    public List<PostClass> findByTagsContains(Set<TagClass> tags) {
        return postRepository.findByTagsContains(tags).orElse(new ArrayList<>());
    }

    @Override
    public Page<PostClass> findByTagsContainsPaginated(Set<TagClass> tags, int page, int size) {
        return new Page<>(postRepository.findByTagsContains(tags, PageRequest.of(page, size)));
    }

    @Override
    public List<PostClass> findByTitleContains(String title) {
        return postRepository.findByTitleContains(title).orElse(new ArrayList<>());
    }

    @Override
    public Page<PostClass> findByTitleContainsPaginated(String title, int page, int size) {
        return new Page<>(postRepository.findByTitleContains(title, PageRequest.of(page, size)));
    }

    @Override
    public Page<PostClass> findByUserDownvotedPaginated(Long userId, int page, int size) {
        return new Page<>(postRepository.findByUsersDownvotedContains(new HashSet<>(Arrays.asList(userId)), PageRequest.of(page, size)));
    }

    @Override
    public Page<PostClass> findByUserUpvotedPaginated(Long userId, int page, int size) {
        return new Page<>(postRepository.findByUsersUpvotedContains(new HashSet<>(Arrays.asList(userId)), PageRequest.of(page, size)));
    }

    @Override
    public Page<PostClass> findByUserHiddenPaginated(Long userId, int page, int size) {
        return new Page<>(postRepository.findByUsersHiddenContains(new HashSet<>(Arrays.asList(userId)), PageRequest.of(page, size)));
    }

    @Override
    public Page<PostClass> findByUserReportedPaginated(Long userId, int page, int size) {
        return new Page<>(postRepository.findByUsersReportedContains(new HashSet<>(Arrays.asList(userId)), PageRequest.of(page, size)));
    }

    @Override
    public Page<PostClass> findByUserSavedPaginated(Long userId, int page, int size) {
        return new Page<>(postRepository.findByUsersSavedContains(new HashSet<>(Arrays.asList(userId)), PageRequest.of(page, size)));
    }

    @Override
    public List<PostClass> findByUserDownvoted(Long userId) {
        return postRepository.findByUsersDownvotedContains(new HashSet<>(Arrays.asList(userId))).orElse(new ArrayList<>());
    }

    @Override
    public List<PostClass> findByUserUpvoted(Long userId) {
        return postRepository.findByUsersUpvotedContains(new HashSet<>(Arrays.asList(userId))).orElse(new ArrayList<>());
    }

    @Override
    public List<PostClass> findByUserHidden(Long userId) {
        return postRepository.findByUsersHiddenContains(new HashSet<>(Arrays.asList(userId))).orElse(new ArrayList<>());
    }

    @Override
    public List<PostClass> findByUserReported(Long userId) {
        return postRepository.findByUsersReportedContains(new HashSet<>(Arrays.asList(userId))).orElse(new ArrayList<>());
    }

    @Override
    public List<PostClass> findByUserSaved(Long userId) {
        return postRepository.findByUsersSavedContains(new HashSet<>(Arrays.asList(userId))).orElse(new ArrayList<>());
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
