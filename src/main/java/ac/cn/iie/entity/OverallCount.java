package ac.cn.iie.entity;

import lombok.Data;


@Data
public class OverallCount {
    private int runningCluster;
    private int runningTaskmanager;
    private int runningJob;
    private int completedJob;
    private int canceledJob;
    private int failedJob;
}
