package ac.cn.iie.controller;

import ac.cn.iie.entity.Cluster;
import ac.cn.iie.service.ClusterService;
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

  public ClusterController(ClusterService clusterService) {
    this.clusterService = clusterService;
  }

  @GetMapping("/")
  public String page() {
    log.info("Load dashboard.html.");
    return "dashboard";
  }

  @GetMapping("/cluster")
  @ResponseBody
  public Map<String, Object> getClusterList(Integer page, Integer size) {
    Page<Cluster> clusters = clusterService.selectCluster(page, size);
    Map<String, Object> map = new HashMap<>(2);
    map.put("total", clusters.getTotalElements());
    map.put("rows", clusters.getContent());
    log.info("Return Cluster List.");
    return map;
  }

  @GetMapping("/cluster_name")
  @ResponseBody
  public Map<Integer, String> getClusterNameList() {
    List<Cluster> clusterList = clusterService.selectCluster();
    Map<Integer, String> res = new HashMap<>();
    for (Cluster cluster : clusterList) {
      res.put(
              cluster.getId(),
              cluster.getSysId() + "_" + cluster.getProvince() + "_" + cluster.getFlinkTaskName());
    }
    return res;
  }

  /**
   * .
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
