package cn.dotax.splider.xuetangx.model;

import lombok.Data;

import java.util.List;

@Data
public class Course {
    private String courseId;
    private List<Chapter> courseChapter;
    private String courseName;
}
