package com.inobitec.inobitecjavatest.repo;

import com.inobitec.inobitecjavatest.entity.CommentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<CommentEntity, Long> {

    // FIND methods
    @Transactional
    Optional<CommentEntity> findCommentEntityById(Long id);

    @Transactional
    @Query(value = "SELECT * FROM info WHERE path=CAST(:path AS ltree)", nativeQuery = true)
    Optional<CommentEntity> findCommentEntityByPath(@Param("path") String path);

    @Transactional
    @Query(value = "SELECT * FROM info WHERE path ~ lquery(:path) ORDER BY id", nativeQuery = true)
    List<CommentEntity> findCommentEntitiesByPathOrderById(@Param("path") String path);

    @Transactional
    @Query(value = "SELECT coalesce(max(id), 0) FROM info;", nativeQuery = true)
    long findLastId();


    // ADD methods
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO info (id, comment, path) VALUES (:id, :comment, CAST(:path AS ltree))", nativeQuery = true)
    void addNewComment(@Param("id") Long id, @Param("comment") String comment, @Param("path") String path);


    // DELETE methods
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM info WHERE path ~ lquery(:path)", nativeQuery = true)
    void deleteCommentEntitiesByPath(@Param("path") String path);

    @Modifying
    @Transactional
    void deleteCommentEntityById(Long id);
}