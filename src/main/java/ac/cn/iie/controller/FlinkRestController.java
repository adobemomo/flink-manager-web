package ac.cn.iie.controller;

import ac.cn.iie.entity.Cluster;
import ac.cn.iie.service.ClusterService;
import ac.cn.iie.service.FlinkRestService;
import ac.cn.iie.service.InfoService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

import static ac.cn.iie.constant.FlinkRestApiConstant.JOB_MANAGER_CONFIG;
import static ac.cn.iie.constant.FlinkRestApiConstant.TASK_MANAGER_OVERVIEW;
import static ac.cn.iie.constant.FlinkRestJsonConstant.JOB_STATUS_RUNNING;
import static ac.cn.iie.constant.FlinkRestJsonConstant.KEY_TASK_MANAGERS;
import static ac.cn.iie.constant.OverallCountConstant.OVERALL_TOTAL_CLUSTER;
import static ac.cn.iie.util.HttpUtil.doGet;

@Controller
@Slf4j
public class FlinkRestController {
  private final ClusterService clusterService;
  private final FlinkRestService flinkRestService;
  private final InfoService infoService;

  /**
   * Constructor.
   *
   * @param clusterService   .
   * @param flinkRestService .
   * @param infoService      .
   */
  public FlinkRestController(
          ClusterService clusterService, FlinkRestService flinkRestService, InfoService infoService) {
    this.clusterService = clusterService;
    this.flinkRestService = flinkRestService;
    this.infoService = infoService;
  }

  /**
   * 返回值： 正在运行的集群总数 已添加的集群总数 正在运行的TaskManager数量 正在运行的Job数 已完成的Job数 已取消的Job数.
   *
   * @return
   */
  @GetMapping("overall_count")
  @ResponseBody
  public Object getOverallCount() {
    int totalCluster;

    List<Cluster> clusters = clusterService.selectCluster();
    totalCluster = clusters.size();
    JSONObject res = infoService.selectOverallCount();
    res.put(OVERALL_TOTAL_CLUSTER, totalCluster);

    log.info("Return overall count.");
    return res;
  }

  /**
   * 获得job manager配置.
   *
   * @param id id
   * @return
   */
  @GetMapping("/jobmanager/config")
  @ResponseBody
  public Object getJmConfig(Integer id) {
    if (id == null) {
      return null;
    }
    Optional<Cluster> cluster = clusterService.selectCluster(id);
    if (cluster.isPresent()) {
      log.info("Return Job Manager configuration of cluster " + id + ".");
      return doGet(cluster.get().getUri() + JOB_MANAGER_CONFIG);
    } else {
      return null;
    }
  }

  /**
   * 获取taskmanager列表.
   *
   * @param id id
   * @return
   */
  @GetMapping("/taskmanagers")
  @ResponseBody
  public Object getTmOverview(Integer id) {
    if (id == null) {
      return null;
    } else {
      Optional<Cluster> cluster = clusterService.selectCluster(id);
      if (cluster.isPresent()) {
        log.info("Return Task Manager list in cluster " + id + ".");
        return JSON.parseObject(doGet(cluster.get().getUri() + TASK_MANAGER_OVERVIEW))
                .get(KEY_TASK_MANAGERS);
      } else {
        return null;
      }
    }
  }

  /**
   * 获得task manager详情.
   *
   * @param clusterId cluster id
   * @param tmId task manager id
   * @return
   */
  @GetMapping("/taskmanagers/detail")
  @ResponseBody
  public Object getTmDetail(Integer clusterId, String tmId) {
    Optional<Cluster> cluster = clusterService.selectCluster(clusterId);
    if (cluster.isPresent()) {
      log.info("Return Task Manager detail of " + tmId + " in cluster " + clusterId + ".");
      return doGet(cluster.get().getUri() + TASK_MANAGER_OVERVIEW + tmId);
    } else {
      return null;
    }
  }

  /**
   * 获取正在运行的job列表.
   *
   * @param id id
   * @return
   */
  @GetMapping("/jobs/running_list")
  @ResponseBody
  public Object getJobRunningList(Integer id) {
    if (id == null) {
      return null;
    } else {
      log.info("Return running job list of cluster " + id + ".");
      return flinkRestService.getJobList(id, JOB_STATUS_RUNNING);
    }
  }
}
