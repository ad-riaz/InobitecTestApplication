package com.inobitec.inobitecjavatest.service;

import com.inobitec.inobitecjavatest.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    //  GET methods
    Optional<CommentEntity> getCommentById(Long id) throws Exception;

    Optional<CommentEntity> getCommentByPath(String path);

    List<CommentEntity> getCommentsByPath(String path);

    boolean commentExistsById(Long id) throws Exception;

    boolean commentsExistByPath(String path);

    long getLastIdFromDatabase();


    //  ADD methods
    Optional<CommentEntity> addNewComment(CommentEntity commentEntity) throws Exception;


    //  DELETE methods
    void deleteCommentById(Long id);

    void deleteCommentsByPath(String path);

}
