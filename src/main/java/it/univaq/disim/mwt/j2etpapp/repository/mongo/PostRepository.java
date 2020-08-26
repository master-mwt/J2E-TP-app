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
    Optional<List<PostClass>> findByUserId(Long userId);
    Optional<List<PostClass>> findByTagsContains(Set<TagClass> tags);
    Optional<List<PostClass>> findByTitleContains(String title);
    Page<PostClass> findByTitleContains(String title, Pageable pageable);
    // TODO: these queries are ok ?
    Optional<List<PostClass>> findByUsersDownvotedContains(Set<Long> usersDownvoted);
    Optional<List<PostClass>> findByUsersUpvotedContains(Set<Long> usersUpvoted);
    Optional<List<PostClass>> findByUsersHiddenContains(Set<Long> usersHidden);
    Optional<List<PostClass>> findByUsersReportedContains(Set<Long> usersReported);
    Optional<List<PostClass>> findByUsersSavedContains(Set<Long> usersSaved);
}
