package com.inobitec.inobitecjavatest.controller;

import com.inobitec.inobitecjavatest.entity.CommentEntity;
import com.inobitec.inobitecjavatest.model.CommentModel;
import com.inobitec.inobitecjavatest.service.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    // GET end points
    @GetMapping("comments/comment/{id}")
    public ResponseEntity getCommentById(@PathVariable Long id) {
        try {
            Optional<CommentEntity> comment = commentService.getCommentById(id);

            if (comment.isPresent()) {
                return new ResponseEntity(comment, HttpStatus.OK);
            }
            return new ResponseEntity("There is no comment with id: " + id, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("comments/comment")
    public ResponseEntity getCommentsByPath(@RequestParam String path) throws IllegalArgumentException {
        try {
            return new ResponseEntity<>(commentService.getCommentsByPath(path), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }


    // DELETE end points
    @DeleteMapping("comments/comment/{id}")
    public ResponseEntity deleteCommentById(@PathVariable Long id) {
        try {
            commentService.deleteCommentById(id);
            return new ResponseEntity("Comment with id: " + id + " was deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("comments")
    public ResponseEntity deleteCommentsByPath(@RequestParam String path) {
        try {
            commentService.deleteCommentsByPath(path);
            return new ResponseEntity("Comments were deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // ADD end points
    @PostMapping(path = "comments",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addNewComment(@RequestBody CommentModel commentModel) {
        try {
            CommentEntity commentEntity = CommentModel.toEntity(commentModel, commentService);

            Optional<CommentEntity> newComment = commentService.addNewComment(commentEntity);

            if (newComment.isPresent()) {
                return new ResponseEntity(commentModel, HttpStatus.OK);
            } else {
                return new ResponseEntity("Comment id or path is already taken", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}