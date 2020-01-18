package cn.dotax.splider.xuetangx.model;

import lombok.Data;

import java.util.List;

@Data
public class Chapter {
    private String id;
    private Integer order;
    private String name;
    private List<Section> sectionLeafList;
}
