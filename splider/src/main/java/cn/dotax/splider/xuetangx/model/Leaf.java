package cn.dotax.splider.xuetangx.model;

import lombok.Data;

@Data
public class Leaf {
    private String id;
    private String name;
    private Boolean isLocked;
    private String startTime;
    private String chapterId;
    private String sectionId;
    private String leafType;
    private String isShow;
    private String endTime;
    private String scoreDeadline;
    private String isScore;
    private String isAssessed;
    private Integer order;
    private String leafinfoId;
}
