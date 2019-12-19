package ac.cn.iie.controller;

import ac.cn.iie.entity.Cluster;
import ac.cn.iie.service.ClusterService;
import ac.cn.iie.service.FlinkRestService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kong.unirest.Unirest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class FlinkRestController {
  private final ClusterService clusterService;
  private final FlinkRestService flinkRestService;

  public FlinkRestController(ClusterService clusterService, FlinkRestService flinkRestService) {
    this.clusterService = clusterService;
    this.flinkRestService = flinkRestService;
  }

  /**
   * 返回值： 正在运行的集群总数 已添加的集群总数 正在运行的TaskManager数量 正在运行的Job数 已完成的Job数 已取消的Job数
   *
   * @return
   */
  @GetMapping("overall_count")
  @ResponseBody
  public Object getOverallCount() {
    Integer totalCluster;
    Integer runningCluster = 0;
    Integer runningTaskManager = 0;
    Integer runningJob = 0;
    Integer completedJob = 0;
    Integer canceledJob = 0;
    Integer failedJob = 0;

    List<Cluster> clusters = clusterService.selectCluster();
    totalCluster = clusters.size();
    for (Cluster c : clusters) {
      if (Unirest.get(c.getUri() + "/v1/jobmanager/config").asJson().getBody().toString() != null)
        runningCluster++;
      else continue;

      String taskManagers =
              Unirest.get(c.getUri() + "/v1/taskmanagers").asJson().getBody().toString();
      JSONObject tmObject = JSON.parseObject(taskManagers);
      JSONArray tmList = JSON.parseArray(tmObject.get("taskmanagers").toString());
      runningTaskManager += tmList.size();

      String jobs = Unirest.get(c.getUri() + "/v1/jobs").asJson().getBody().toString();
      JSONArray jobsList = (JSONArray) JSON.parseObject(jobs).get("jobs");
      for (int i = 0; i < jobsList.size(); i++) {
        switch (jobsList.getJSONObject(i).get("status").toString()) {
          case "RUNNING":
            runningJob++;
            break;
          case "FINISHED":
            completedJob++;
            break;
          case "CANCELED":
            canceledJob++;
            break;
          case "FAILED":
            failedJob++;
            break;
        }
      }
    }

    JSONObject res = new JSONObject();
    res.put("totcalCluster", totalCluster);
    res.put("runningCluster", runningCluster);
    res.put("runningTaskManager", runningTaskManager);
    res.put("runningJob", runningJob);
    res.put("completedJob", completedJob);
    res.put("canceledJob", canceledJob);
    res.put("failedJob", failedJob);

    return res;
  }

  @GetMapping("/jobmanager/config")
  @ResponseBody
  public Object getJmConfig(Integer id) {
    if (id == null) {
      return null;
    }
    Optional<Cluster> cluster = clusterService.selectCluster(id);
    if (cluster.isPresent()) {
      return Unirest.get(cluster.get().getUri() + "/v1/jobmanager/config")
              .asJson()
              .getBody()
              .toString();
    } else {
      return null;
    }
  }

  @GetMapping("/taskmanagers")
  @ResponseBody
  public Object getTmOverview(Integer id) {
    if (id == null) {
      return null;
    } else if (id == -1) {
      List<Cluster> clusterList = clusterService.selectCluster();
      List<Object> taskmanagerInfo = new ArrayList<>();
      for (Cluster cluster : clusterList) {
        JSONObject object =
                JSON.parseObject(
                        Unirest.get(cluster.getUri() + "/v1/taskmanagers").asJson().getBody().toString());
        List<Object> tmList = (List<Object>) object.get("taskmanagers");
        taskmanagerInfo.addAll(tmList);
      }
      return taskmanagerInfo;
    } else {
      Optional<Cluster> cluster = clusterService.selectCluster(id);
      if (cluster.isPresent()) {
        return JSON.parseObject(
                Unirest.get(cluster.get().getUri() + "/v1/taskmanagers")
                        .asJson()
                        .getBody()
                        .toString())
                .get("taskmanagers");
      } else {
        return null;
      }
    }
  }

  @GetMapping("/taskmanagers/detail")
  @ResponseBody
  public Object getTmDetail(Integer cluster_id, String tmId) {
    Optional<Cluster> cluster = clusterService.selectCluster(cluster_id);
    if (cluster.isPresent()) {
      return Unirest.get(cluster.get().getUri() + "/v1/taskmanagers/" + tmId)
              .asJson()
              .getBody()
              .toString();
    } else {
      return null;
    }
  }

  @GetMapping("/jobs/overview")
  @ResponseBody
  public Object getJobOverview(Integer id) {
    return null;
  }

  @GetMapping("/jobs/detail")
  @ResponseBody
  public Object geJobDetail(Integer cluster_id, String jobId) {
    return null;
  }

  @GetMapping("/jobs/running_list")
  @ResponseBody
  public Object getJobRunningList(Integer id) {
    if (id == null) {
      return null;
    } else {
      return flinkRestService.getJobList(id, "RUNNING");
    }
  }

  @GetMapping("/jobs/completed_list")
  @ResponseBody
  public Object getJobCompletedList(Integer id) {
    if (id == null) {
      return null;
    } else {
      return flinkRestService.getJobList(id, "FINISHED");
    }
  }
}
