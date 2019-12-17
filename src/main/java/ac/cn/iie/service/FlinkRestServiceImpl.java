package ac.cn.iie.service;

import ac.cn.iie.entity.Cluster;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static ac.cn.iie.util.HttpClientUtil.doGet;

@Service
public class FlinkRestServiceImpl implements FlinkRestService {
  @Autowired private ClusterService clusterService;

  private List<JSONObject> getJobList(String host, String port, String clusterName, String status) {
    List<JSONObject> result = new ArrayList<>();
    String uri = "http://" + host + ":" + port + "/v1/jobs";
    JSONArray jobOverview = JSON.parseArray(JSON.parseObject(doGet(uri)).get("jobs").toString());
    for (Object obj : jobOverview) {
      JSONObject job = (JSONObject) obj;
      if (!job.get("status").equals(status)) continue;

      JSONObject jobInfo = new JSONObject();

      jobInfo.put("cluster", clusterName);
      jobInfo.put("id", job.get("id"));
      jobInfo.put("status", job.get("status"));

      JSONObject jobDetail =
          JSON.parseObject(doGet("http://" + host + ":" + port + "/v1/jobs/" + job.get("id")));

      jobInfo.put("name", jobDetail.get("name"));
      jobInfo.put("tasks", jobDetail.getJSONArray("vertices").size());

      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      jobInfo.put(
          "start-time",
          format.format(new Date(Long.parseLong(String.valueOf(jobDetail.get("start-time"))))));
      if (jobDetail.containsKey("end-time")) {
        System.out.println(status + " end-time: " + jobDetail.get("end-time"));
        jobInfo.put(
            "end-time",
            format.format(new Date(Long.parseLong(String.valueOf(jobDetail.get("end-time"))))));
      }

      SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
      jobInfo.put(
          "duration",
          ft.format(new Date(Long.parseLong(String.valueOf(jobDetail.get("duration"))))));

      result.add(jobInfo);
    }

    return result;
  }

  @Override
  public JSONArray getJobList(Integer id, String status) {
    JSONArray result = new JSONArray();
    if (id == null) {
      return null;
    } else if (id == -1) { // jobs of all clusters
      List<Cluster> clusters = clusterService.selectCluster();
      for (Cluster c : clusters) {
        result.addAll(getJobList(c.getAddress(), c.getPort(), c.getName(), status));
      }
    } else { // jobs of cluster with given id
      Optional<Cluster> cluster = clusterService.selectCluster(id);
      if (cluster.isPresent()) {
        Cluster c = cluster.get();
        result.addAll(getJobList(c.getAddress(), c.getPort(), c.getName(), status));
      } else {
        return null;
      }
    }
    return result;
  }
}
