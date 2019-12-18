package ac.cn.iie.controller;

import ac.cn.iie.entity.Cluster;
import ac.cn.iie.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClusterController {
  @Autowired private ClusterService clusterService;

  @GetMapping("/")
  public String page() {
    return "dashboard";
  }

  //    @GetMapping("/detail")
  //    public String detail(@RequestParam(name = "id")String id, Model model){
  //        model.addAttribute("id",id);
  //        return "detail";
  //    }

  @GetMapping("/cluster")
  @ResponseBody
  public Map<String, Object> getClusterList(Integer page, Integer size) {
    Page<Cluster> clusters = clusterService.selectCluster(page, size);
    Map map = new HashMap(2);
    map.put("total", clusters.getTotalElements());
    map.put("rows", clusters.getContent());
    return map;
  }

  @GetMapping("/cluster_name")
  @ResponseBody
  public Map<Integer, String> getClusterNameList() {
    List<Cluster> clusterList = clusterService.selectCluster();
    Map<Integer, String> res = new HashMap<>();
    for (Cluster cluster : clusterList) {
      res.put(cluster.getId(), cluster.getName());
    }
    return res;
  }

  @PostMapping("/cluster")
  @ResponseBody
  public Cluster addClusterInformation(Cluster cluster) {
    cluster.setUri("http://"+cluster.getUri());
    return clusterService.insertCluster(cluster);
  }

  @PutMapping("/cluster")
  @ResponseBody
  public Cluster updatePlayerInformation(Cluster cluster){
    return clusterService.updateCluster(cluster);
  }

  @DeleteMapping("/cluster/{id}")
  @ResponseBody
  public Boolean deletePlayerInformation(@PathVariable Integer id) {
    return clusterService.deleteCluster(id);
  }
}
