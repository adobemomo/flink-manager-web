package ac.cn.iie.controller;

import ac.cn.iie.entity.Cluster;
import ac.cn.iie.service.ClusterService;
import ac.cn.iie.service.FlinkRestService;
import ac.cn.iie.util.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class FlinkRestController {
  @Autowired private ClusterService clusterService;
  @Autowired private FlinkRestService flinkRestService;
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
      if (HttpClientUtil.doGet(c.getUri() + "/v1/jobmanager/config") != null) runningCluster++;
      else continue;

      String taskmanaers = HttpClientUtil.doGet(c.getUri() + "/v1/taskmanagers");
      JSONObject tmObject = JSON.parseObject(taskmanaers);
      JSONArray tmList = JSON.parseArray(tmObject.get("taskmanagers").toString());
      runningTaskManager += tmList.size();

      String jobs = HttpClientUtil.doGet(c.getUri() + "/v1/jobs");
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
      Cluster c = cluster.get();
      return HttpClientUtil.doGet(c.getUri() + "/v1/jobmanager/config");
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
            JSON.parseObject(HttpClientUtil.doGet(cluster.getUri() + "/v1/taskmanagers"));
        List<Object> tmList = (List<Object>) object.get("taskmanagers");
        for (Object tm : tmList) {
          taskmanagerInfo.add(tm);
        }
      }
      return taskmanagerInfo;
    } else {
      Optional<Cluster> cluster = clusterService.selectCluster(id);
      if (cluster.isPresent()) {
        Cluster c = cluster.get();
        JSONObject object = JSON.parseObject(HttpClientUtil.doGet(c.getUri() + "/v1/taskmanagers"));
        return object.get("taskmanagers");
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
      Cluster c = cluster.get();
      return HttpClientUtil.doGet(c.getUri() + "/v1/taskmanagers/" + tmId);
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
    return flinkRestService.getJobList(id, "RUNNING");
  }

  @GetMapping("/jobs/completed_list")
  @ResponseBody
  public Object getJobCompletedList(Integer id) {
    return flinkRestService.getJobList(id, "FINISHED");
  }
}
