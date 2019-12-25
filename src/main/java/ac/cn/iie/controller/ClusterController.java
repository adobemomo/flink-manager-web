package ac.cn.iie.controller;

import ac.cn.iie.entity.Cluster;
import ac.cn.iie.entity.Info;
import ac.cn.iie.service.ClusterService;
import ac.cn.iie.service.FlinkRestService;
import ac.cn.iie.service.InfoService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ac.cn.iie.constant.JavaScriptConstant.*;

@Controller
@Slf4j
public class ClusterController {
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
  public ClusterController(
          ClusterService clusterService, FlinkRestService flinkRestService, InfoService infoService) {
    this.clusterService = clusterService;
    this.flinkRestService = flinkRestService;
    this.infoService = infoService;
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
      Info info;
      try {
        Optional<Info> infoOptional = infoService.selectInfo(c.getId());
        if (infoOptional.isPresent()) {
          info = infoOptional.get();
        } else {
          info = infoService.insertInfo(c.getId(), c.getUri());
        }
        obj.put(CLUSTER_INFO_RUNNING_JOB_CNT, info.getRunningJob());
        obj.put(CLUSTER_INFO_STATUS, info.getStatus());
        rows.add(obj);
      } catch (Exception e) {
        log.error(e.getMessage());
        rows.add(obj);
        map.put(BS_TABLE_PAGE_TOTAL, clustersPage.getTotalElements());
        map.put(BS_TABLE_PAGE_ROWS, rows);
        log.info("Return Cluster List.(No Info)");
        return map;
      }
    }
    map.put(BS_TABLE_PAGE_TOTAL, clustersPage.getTotalElements());
    map.put(BS_TABLE_PAGE_ROWS, rows);
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
    List<Cluster> clusterList = clusterService.selectAliveCluster();
    JSONObject res = new JSONObject();
    for (Cluster cluster : clusterList) {
      System.out.println(cluster.toString());
      JSONObject attr = new JSONObject();
      attr.put(
              "name",
              cluster.getSysId() + "_" + cluster.getProvince() + "_" + cluster.getFlinkTaskName());
      res.put(String.valueOf(cluster.getId()), attr);
    }
    return res;
  }

  /** 添加集群. */
  @PostMapping("/cluster")
  @ResponseBody
  public Cluster addClusterInformation(Cluster cluster) {
    Cluster c = clusterService.insertCluster(cluster);
    try {
      infoService.insertInfo(c.getId(), c.getUri());
      log.info("Add info of " + cluster + " to database.");
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    log.info("Add cluster " + cluster + " to database.");
    return c;
  }

  /**
   * Update cluster information with given cluster.
   *
   * @param cluster cluster to be updated.
   * @return updated cluster.
   */
  @PutMapping("/cluster")
  @ResponseBody
  public Cluster updateClusterInformation(Cluster cluster) {
    Cluster c = clusterService.updateCluster(cluster);
    log.info("Update info of cluster " + cluster + " in database.");
    infoService.updateInfo(c.getId(), c.getUri());
    log.info("Update cluster " + cluster + " in database.");
    return c;
  }

  /**
   * Delete cluster record with given id.
   *
   * @param id id of cluster to be deleted.
   * @return
   */
  @DeleteMapping("/cluster/{id}")
  @ResponseBody
  public Boolean deletePlayerInformation(@PathVariable Integer id) {
    log.info("Delete info of cluster " + id + " in database.");
    log.info("Delete cluster by id " + id);
    return clusterService.deleteCluster(id) && infoService.deleteInfo(id);
  }
}
