package it.univaq.disim.mwt.j2etpapp.repository.mongo;

import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.TagClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends PagingAndSortingRepository<PostClass, String> {
    Optional<List<PostClass>> findByChannelId(Long channelId);
    Page<PostClass> findByChannelId(Long channelId, Pageable pageable);
    Page<PostClass> findByChannelIdAndReported(Long channelId, boolean reported, Pageable pageable);
    Optional<List<PostClass>> findByUserId(Long userId);
    Page<PostClass> findByUserId(Long userId, Pageable pageable);
    Optional<List<PostClass>> findByTagsContains(Set<TagClass> tags);
    Page<PostClass> findByTagsContains(Set<TagClass> tags, Pageable pageable);
    Optional<List<PostClass>> findByTitleContains(String title);
    Page<PostClass> findByTitleContains(String title, Pageable pageable);
    Page<PostClass> findByUsersDownvotedContains(Set<Long> usersDownvoted, Pageable pageable);
    Page<PostClass> findByUsersUpvotedContains(Set<Long> usersUpvoted, Pageable pageable);
    Page<PostClass> findByUsersHiddenContains(Set<Long> usersHidden, Pageable pageable);
    Page<PostClass> findByUsersReportedContains(Set<Long> usersReported, Pageable pageable);
    Page<PostClass> findByUsersSavedContains(Set<Long> usersSaved, Pageable pageable);
}
