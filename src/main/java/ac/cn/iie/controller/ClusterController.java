package ac.cn.iie.controller;

import ac.cn.iie.entity.Cluster;
import ac.cn.iie.service.ClusterService;
import ac.cn.iie.service.FlinkRestService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Slf4j
public class ClusterController {
  private final ClusterService clusterService;
  private final FlinkRestService flinkRestService;

  public ClusterController(ClusterService clusterService, FlinkRestService flinkRestService) {
    this.clusterService = clusterService;
    this.flinkRestService = flinkRestService;
  }

  /**
   * .
   *
   * @return
   */
  @GetMapping("/")
  public String page() {
    log.info("Load dashboard.html.");
    return "dashboard";
  }

  /**
   * 获取集群列表.
   *
   * @param page 页码
   * @param size 每页容量
   * @return 单页的数据
   */
  @GetMapping("/cluster")
  @ResponseBody
  public Map<String, Object> getClusterList(Integer page, Integer size) {
    Page<Cluster> clustersPage = clusterService.selectCluster(page, size);
    Map<String, Object> map = new HashMap<>(2);

    List<Cluster> clusters = clustersPage.getContent();
    JSONArray rows = new JSONArray();
    for (Cluster c : clusters) {
      JSONObject obj = (JSONObject) JSON.toJSON(c);
      obj.put("runningJobCnt", flinkRestService.getJobList(c.getId(), "RUNNING").size());
      String status;
      if (Unirest.get(c.getUri() + "/v1/config").asJson().getStatus() == 200) {
        status = "alive";
      } else {
        status = "dead";
      }
      obj.put("status", status);
      rows.add(obj);
    }
    map.put("total", clustersPage.getTotalElements());
    map.put("rows", rows);
    log.info("Return Cluster List.");
    return map;
  }

  /**
   * 获取集群名字列表.
   *
   * @return
   */
  @GetMapping("/cluster_name")
  @ResponseBody
  public JSONObject getClusterNameList() {
    List<Cluster> clusterList = clusterService.selectCluster();
    JSONObject res = new JSONObject();
    for (Cluster cluster : clusterList) {
      JSONObject attr = new JSONObject();
      attr.put(
              "name",
              cluster.getSysId() + "_" + cluster.getProvince() + "_" + cluster.getFlinkTaskName());
      //      attr.put(
      //          "runningJob",
      //          String.valueOf(
      //              ((JSONArray)
      //                      JSON.parseObject(
      //                              Unirest.get(cluster.getUri() + "/v1/jobs")
      //                                  .asJson()
      //                                  .getBody()
      //                                  .toString())
      //                          .get("jobs"))
      //                  .size()));
      res.put(String.valueOf(cluster.getId()), attr);
    }
    return res;
  }

  /**
   * 添加集群.
   */
  @PostMapping("/cluster")
  @ResponseBody
  public Cluster addClusterInformation(Cluster cluster) {
    log.info("Add cluster " + cluster + " to database.");
    return clusterService.insertCluster(cluster);
  }

  @PutMapping("/cluster")
  @ResponseBody
  public Cluster updatePlayerInformation(Cluster cluster) {
    log.info("Update cluster " + cluster + " in database.");
    return clusterService.updateCluster(cluster);
  }

  @DeleteMapping("/cluster/{id}")
  @ResponseBody
  public Boolean deletePlayerInformation(@PathVariable Integer id) {
    log.info("Delete cluster by id " + id);
    return clusterService.deleteCluster(id);
  }
}
