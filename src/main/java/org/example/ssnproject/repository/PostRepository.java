package org.example.ssnproject.repository;

import org.example.ssnproject.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findAll(Pageable pageable);

    @Modifying
    @Query(value = "update PostEntity p set p.views = p.views +1 where p.id = :id")
    void updateViews(@Param("id") Long id);

    @Modifying
    @Query(value = "update PostEntity p set p.title = :title , p.contents = :contents where p.id = :id")
    void updatePost(@Param("id") Long id , @Param("title") String title , @Param("contents") String contents);
}
