package ac.cn.iie.service.impl;

import ac.cn.iie.constant.JavaScriptConstant;
import ac.cn.iie.entity.Cluster;
import ac.cn.iie.service.ClusterService;
import ac.cn.iie.service.FlinkRestService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static ac.cn.iie.constant.FlinkRestApiConstant.JOBS_OVERVIEW;
import static ac.cn.iie.constant.FlinkRestJsonConstant.*;
import static ac.cn.iie.util.HttpUtil.doGet;

@Service
public class FlinkRestServiceImpl implements FlinkRestService {
  private final ClusterService clusterService;

  public FlinkRestServiceImpl(ClusterService clusterService) {
    this.clusterService = clusterService;
  }

  private JSONObject getVerticesTasks(JSONArray vertices) {
    int created = 0;
    int canceled = 0;
    int running = 0;
    int reconciling = 0;
    int deploying = 0;
    int failed = 0;
    int scheduled = 0;
    int canceling = 0;
    int finished = 0;

    for (Object obj : vertices) {
      JSONObject count = (JSONObject) ((JSONObject) obj).get(KEY_JOB_VERTICES_TASKS);
      created += count.getInteger(VERTICES_TASKS_STATUS_CREATED);
      canceled += count.getInteger(VERTICES_TASKS_STATUS_CANCELED);
      running += count.getInteger(VERTICES_TASKS_STATUS_RUNNING);
      reconciling += count.getInteger(VERTICES_TASKS_STATUS_RECONCILING);
      deploying += count.getInteger(VERTICES_TASKS_STATUS_DEPLOYING);
      failed += count.getInteger(VERTICES_TASKS_STATUS_FAILED);
      scheduled += count.getInteger(VERTICES_TASKS_STATUS_SCHEDULED);
      canceling += count.getInteger(VERTICES_TASKS_STATUS_CANCELING);
      finished += count.getInteger(VERTICES_TASKS_STATUS_FINISHED);
    }

    JSONObject tasks = new JSONObject();
    tasks.put(VERTICES_TASKS_STATUS_CREATED, created);
    tasks.put(VERTICES_TASKS_STATUS_CANCELED, canceled);
    tasks.put(VERTICES_TASKS_STATUS_RUNNING, running);
    tasks.put(VERTICES_TASKS_STATUS_RECONCILING, reconciling);
    tasks.put(VERTICES_TASKS_STATUS_DEPLOYING, deploying);
    tasks.put(VERTICES_TASKS_STATUS_FAILED, failed);
    tasks.put(VERTICES_TASKS_STATUS_SCHEDULED, scheduled);
    tasks.put(VERTICES_TASKS_STATUS_CANCELING, canceling);
    tasks.put(VERTICES_TASKS_STATUS_FINISHED, finished);
    tasks.put(
            JavaScriptConstant.JOB_TASKS_TOTAL,
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
      String uri = clusterUri + JOBS_OVERVIEW;
      JSONArray jobOverview =
              JSON.parseArray(JSON.parseObject(doGet(uri)).get(KEY_JOBS).toString());
      for (Object obj : jobOverview) {
        JSONObject job = (JSONObject) obj;
        if (!job.get(KEY_JOB_STATUS).equals(status)) {
          continue;
        }

        JSONObject jobInfo = new JSONObject();

        jobInfo.put(JavaScriptConstant.JOB_INFO_CLUSTER, clusterName);
        jobInfo.put(JavaScriptConstant.JOB_INFO_ID, job.get(KEY_JOB_ID));
        jobInfo.put(JavaScriptConstant.JOB_INFO_STATUS, job.get(KEY_JOB_STATUS));

        JSONObject jobDetail =
                JSON.parseObject(doGet(clusterUri + JOBS_OVERVIEW + job.get(KEY_JOB_ID)));

        jobInfo.put(JavaScriptConstant.JOB_INFO_NAME, jobDetail.get(KEY_JOB_NAME));
        jobInfo.put(JavaScriptConstant.JOB_INFO_TASKS,
                getVerticesTasks(jobDetail.getJSONArray(KEY_JOB_VERTICES)));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        jobInfo.put(
                JavaScriptConstant.JOB_INFO_START_TIME,
                format.format(
                        new Date(Long.parseLong(String.valueOf(jobDetail.get(KEY_JOB_START_TIME))))));
        if (!jobDetail.get(KEY_JOB_END_TIME).toString().equals("-1")) {
          jobInfo.put(
                  JavaScriptConstant.JOB_INFO_END_TIME,
                  format.format(
                          new Date(Long.parseLong(String.valueOf(jobDetail.get(KEY_JOB_END_TIME))))));
        } else {
          jobInfo.put(JavaScriptConstant.JOB_INFO_END_TIME, "-");
        }

        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
        jobInfo.put(
                JavaScriptConstant.JOB_INFO_DURATION,
                ft.format(new Date(Long.parseLong(String.valueOf(jobDetail.get(KEY_JOB_DURATION))))));

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
