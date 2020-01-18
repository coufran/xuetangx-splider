package cn.dotax.splider.xuetangx.model;

import cn.dotax.splider.xuetangx.VideoNodeRunner;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class VideoNode {
    private String id;
    private Integer order;
    private String name;
    private List<VideoNode> nodes;

    private String chapterId;
    private String videoId;
    private String videoUrl;

    public VideoNode(Course course) {
        this.id = course.getCourseId();
        this.name = course.getCourseName();
        if(course.getCourseChapter() == null) {
            return;
        }
        this.nodes = course.getCourseChapter().stream()
                .map(chapter -> new VideoNode(chapter))
                .collect(Collectors.toList());
    }

    public VideoNode(Chapter chapter) {
        this.id = chapter.getId();
        this.order = chapter.getOrder();
        this.name = chapter.getName();
        if(chapter.getSectionLeafList() == null) {
            return;
        }
        this.nodes = chapter.getSectionLeafList().stream()
                .map(section -> new VideoNode(section))
                .collect(Collectors.toList());
    }

    public VideoNode(Section section) {
        this.id = section.getId();
        this.order = section.getOrder();
        this.name = section.getName();
        if(section.getLeafList() == null) {
            return;
        }
        this.nodes = section.getLeafList().stream()
                .map(leaf -> new VideoNode(leaf))
                .collect(Collectors.toList());
    }

    public VideoNode(Leaf leaf) {
        this.id = leaf.getId();
        this.order = leaf.getOrder();
        this.name = leaf.getName();
        this.chapterId = leaf.getId();
    }

    public boolean isRoot() {
        return this.name == null;
    }

    public boolean isLeaf() {
        return this.chapterId != null;
    }

    public void foreach(VideoNodeRunner runner) {
        this.foreach("", runner);
    }

    private void foreach(String path, VideoNodeRunner runner) {
        runner.run(path, this);
        if(this.nodes == null) {
            return;
        }
        final String finalPath = path + "/" + this.getFileName();
        this.nodes.stream()
                .forEach(node -> {
                    node.foreach(finalPath, runner);
                });
    }

    public String getFileName() {
        String name = this.name.replaceAll("[\\\\/:\\*\\?\"<>\\|]", "");
        return name;
    }

}
