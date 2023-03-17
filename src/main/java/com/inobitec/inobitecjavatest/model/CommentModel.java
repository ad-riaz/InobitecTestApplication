package com.inobitec.inobitecjavatest.model;

import com.inobitec.inobitecjavatest.entity.CommentEntity;
import com.inobitec.inobitecjavatest.service.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentModel {

    private String path;
    private String comment;


    public static CommentEntity toEntity(CommentModel commentModel, CommentServiceImpl commentService) {
        CommentEntity commentEntity = new CommentEntity();

        Long lastDbId = commentService.getLastIdFromDatabase();
        if (lastDbId.equals(null)) lastDbId = 0L;
        long id = lastDbId + 1;


        commentEntity.setComment(commentModel.getComment());
        commentEntity.setId(id);

        String modelPath = commentModel.getPath();

        if (modelPath.equals("") || modelPath.equals(null)) {
            commentEntity.setPath(Long.toString(id));
        } else {
            commentEntity.setPath(modelPath + "." + Long.toString(id));
        }

        return commentEntity;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentModel that)) return false;

        return getPath().equals(that.getPath());
    }

    @Override
    public int hashCode() {
        return getPath().hashCode();
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "path='" + path + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
