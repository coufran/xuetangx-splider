package cn.dotax.splider.xuetangx.model;

import lombok.Data;

import java.util.List;

@Data
public class Section {
    private Integer order;
    private String chapterId;
    private String id;
    private String name;
    private List<Leaf> leafList;
}
