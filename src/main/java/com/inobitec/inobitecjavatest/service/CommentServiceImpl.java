package com.inobitec.inobitecjavatest.service;

import com.inobitec.inobitecjavatest.entity.CommentEntity;
import com.inobitec.inobitecjavatest.repo.CommentRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepo commentRepo;

    private Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);


    //  GET methods
    @Override
    @NotNull
    public Optional<CommentEntity> getCommentById(Long id) throws IllegalArgumentException {
        if (id == null || id == 0) {
            throw new IllegalArgumentException("Id can't be equal to zero or null");
        }
        logger.info("Getting a comment by id: " + id);
        return commentRepo.findById(id);
    }

    @Override
    @NotNull
    public Optional<CommentEntity> getCommentByPath(String path) throws IllegalArgumentException {
        if (path == null || path.equals("")) {
            throw new IllegalArgumentException("The path can't be null or empty");
        }
        logger.info("Getting a comment by path: " + path);
        return commentRepo.findCommentEntityByPath(path);
    }

    @Override
    @NotNull
    public List<CommentEntity> getCommentsByPath(String path) throws IllegalArgumentException {
        if (path == null || path.equals("")) {
            throw new IllegalArgumentException("The path can't be null or empty");
        }
        logger.info("Getting comments by path: " + path);
        return commentRepo.findCommentEntitiesByPathOrderById(path);
    }

    @Override
    public boolean commentExistsById(Long id) throws IllegalArgumentException {
        if (getCommentById(id).isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean commentsExistByPath(String path) throws IllegalArgumentException {
        if (!getCommentsByPath(path).isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public long getLastIdFromDatabase() {
        logger.info("Getting last id from the database.");
        return commentRepo.findLastId();
    }

    ;


    //  ADD methods
    @Override
    public Optional<CommentEntity> addNewComment(CommentEntity commentEntity) throws Exception {
        if (commentEntity == null) {
            logger.info("Adding a new comment: The comment is null. Comment wasn't added to database.");
            return Optional.empty();
        }

        Long id = commentEntity.getId();
        String comment = commentEntity.getComment();
        String path = commentEntity.getPath();

        if (id == 0 || comment == null || path == null) {
            logger.info("Adding a new comment: The comment fields are zero or null. Comment wasn't added to database.");
            return Optional.empty();
        }

        if (commentExistsById(id)) {
            logger.info("Adding a new comment: A comment with such id already exists.");
            return Optional.empty();
        }

        if (commentsExistByPath(path)) {
            logger.info("Adding a new comment: A comment with such tree already exists.");
            return Optional.empty();
        }

        try {
            logger.info("Adding a new comment to database...");
            commentRepo.addNewComment(id, comment, path);
            if (commentExistsById(id)) {
                logger.info("Comment was added to database successfully.");
                return commentRepo.findCommentEntityById(id);
            } else
                throw new Exception("Adding a new comment: ERROR. Something went wrong when adding a comment to the database.\n");

        } catch (Exception e) {
            throw new Exception("Adding a new comment: ERROR. The comment wasn't added to database. Reason:\n" + e.getMessage());
        }
    }


    //  DELETE methods
    @Override
    public void deleteCommentById(Long id) throws IllegalArgumentException {
        if (commentExistsById(id)) {
            commentRepo.deleteCommentEntityById(id);
            logger.info("Comment with id: " + id + " was deleted.");
        } else throw new IllegalArgumentException("There is no comment with id: " + id);
    }

    @Override
    public void deleteCommentsByPath(String path) throws IllegalArgumentException {
        if (commentsExistByPath(path)) {
            commentRepo.deleteCommentEntitiesByPath(path);
            logger.info("Comments with path: " + path + " was deleted.");
        } else throw new IllegalArgumentException("There are no comments by path: " + path);
    }
}