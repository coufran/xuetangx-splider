package cn.dotax.splider.xuetangx.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseResult extends Result {
    private Course data;
}
