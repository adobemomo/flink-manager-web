package ac.cn.iie.service;

import ac.cn.iie.entity.Cluster;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kong.unirest.Unirest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class FlinkRestServiceImpl implements FlinkRestService {
  private final ClusterService clusterService;

  public FlinkRestServiceImpl(ClusterService clusterService) {
    this.clusterService = clusterService;
  }

  private JSONObject getVerticesTasks(JSONArray vertices) {
    Integer created = 0;
    Integer canceled = 0;
    Integer running = 0;
    Integer reconciling = 0;
    Integer deploying = 0;
    Integer failed = 0;
    Integer scheduled = 0;
    Integer canceling = 0;
    Integer finished = 0;

    for (Object obj : vertices) {
      JSONObject count = (JSONObject) ((JSONObject) obj).get("tasks");
      created += count.getInteger("CREATED");
      canceled += count.getInteger("CANCELED");
      running += count.getInteger("RUNNING");
      reconciling += count.getInteger("RECONCILING");
      deploying += count.getInteger("DEPLOYING");
      failed += count.getInteger("FAILED");
      scheduled += count.getInteger("SCHEDULED");
      canceling += count.getInteger("CANCELING");
      finished += count.getInteger("FINISHED");
    }

    JSONObject tasks = new JSONObject();
    tasks.put("CREATED", created);
    tasks.put("CANCELED", canceled);
    tasks.put("RUNNING", running);
    tasks.put("RECONCILING", reconciling);
    tasks.put("DEPLOYING", deploying);
    tasks.put("FAILED", failed);
    tasks.put("SCHEDULED", scheduled);
    tasks.put("CANCELING", canceling);
    tasks.put("FINISHED", finished);
    tasks.put(
        "TOTAL",
        created
            + canceled
            + running
            + reconciling
            + deploying
            + failed
            + scheduled
            + canceling
            + finished);

    return tasks;
  }

  private List<JSONObject> getJobList(String clusterUri, String clusterName, String status) {
    List<JSONObject> result = new ArrayList<>();
    try {
      String uri = clusterUri + "/v1/jobs";
      JSONArray jobOverview =
              JSON.parseArray(
                      JSON.parseObject(Unirest.get(uri).asJson().getBody().toString())
                              .get("jobs")
                              .toString());
      for (Object obj : jobOverview) {
        JSONObject job = (JSONObject) obj;
        if (!job.get("status").equals(status)) {
          continue;
        }

        JSONObject jobInfo = new JSONObject();

        jobInfo.put("cluster", clusterName);
        jobInfo.put("id", job.get("id"));
        jobInfo.put("status", job.get("status"));

        JSONObject jobDetail =
                JSON.parseObject(
                        Unirest.get(clusterUri + "/v1/jobs/"
                                + job.get("id")).asJson().getBody().toString());

        jobInfo.put("name", jobDetail.get("name"));
        jobInfo.put("tasks", getVerticesTasks(jobDetail.getJSONArray("vertices")));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        jobInfo.put(
                "start-time",
                format.format(new Date(Long.parseLong(String.valueOf(jobDetail.get("start-time"))))));
        if (!jobDetail.get("end-time").toString().equals("-1")) {
          jobInfo.put(
                  "end-time",
                  format.format(new Date(Long.parseLong(String.valueOf(jobDetail.get("end-time"))))));
        } else {
          jobInfo.put("end-time", "-");
        }

        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
        jobInfo.put(
                "duration",
                ft.format(new Date(Long.parseLong(String.valueOf(jobDetail.get("duration"))))));

        result.add(jobInfo);
      }
    } catch (Exception e) {
      e.printStackTrace();
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
        result.addAll(
                getJobList(
                        c.getUri(),
                        c.getSysId() + "_" + c.getProvince() + "_" + c.getFlinkTaskName(),
                        status));
      }
    } else { // jobs of cluster with given id
      Optional<Cluster> cluster = clusterService.selectCluster(id);
      if (cluster.isPresent()) {
        Cluster c = cluster.get();
        result.addAll(
                getJobList(
                        c.getUri(),
                        c.getSysId() + "_" + c.getProvince() + "_" + c.getFlinkTaskName(),
                        status));
      } else {
        return null;
      }
    }
    return result;
  }
}
