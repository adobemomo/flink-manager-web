package ac.cn.iie.service.impl;

import ac.cn.iie.entity.Info;
import ac.cn.iie.repository.InfoRepository;
import ac.cn.iie.service.FlinkRestService;
import ac.cn.iie.service.InfoService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kong.unirest.Unirest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InfoServiceImpl implements InfoService {
  private final InfoRepository infoRepository;
  private final FlinkRestService flinkRestService;

  public InfoServiceImpl(InfoRepository infoRepository, FlinkRestService flinkRestService) {
    this.infoRepository = infoRepository;
    this.flinkRestService = flinkRestService;
  }

  private Info updateInfoWithFlink(int id, String uri) {
      Info info = new Info();

      String status = "dead";
      try {
          if (Unirest.get(uri + "/v1/config").asJson().getStatus() == 200) {
              status = "alive";
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      String taskManagers = Unirest.get(uri + "/v1/taskmanagers").asJson().getBody().toString();
      JSONObject tmObject = JSON.parseObject(taskManagers);
      JSONArray tmList = JSON.parseArray(tmObject.get("taskmanagers").toString());

      info.setId(id);
      info.setUri(uri);
      info.setStatus(status);
      info.setRunningJob(flinkRestService.getJobList(id, "RUNNING").size());
      info.setCompletedJob(flinkRestService.getJobList(id, "FINISHED").size());
      info.setCanceledJob(flinkRestService.getJobList(id, "CANCELED").size());
      info.setFailedJob(flinkRestService.getJobList(id, "FAILED").size());
      info.setRunningTaskmanager(tmList.size());

      return info;
  }

  @Override
  public Info insertInfo(int id, String uri) {
    return infoRepository.save(updateInfoWithFlink(id, uri));
  }

  @Override
  public Boolean deleteInfo(int id) {
    if (infoRepository.findById(id).isPresent()) {
      infoRepository.deleteById(id);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Info updateInfo(int id, String uri) {
    Optional<Info> old = infoRepository.findById(id);
    if (old.isPresent()) {
      return infoRepository.save(updateInfoWithFlink(id, uri));
    } else {
      return null;
    }
  }

  @Override
  public List<Info> updateInfo() {
    List<Info> infoList = selectInfo();
    for (Info info : infoList) {
      updateInfo(info.getId(), info.getUri());
    }
    return selectInfo();
  }

    @Override
    public List<Info> selectInfo() {
        return infoRepository.findAll();
    }

    @Override
    public Optional<Info> selectInfo(int id) {
        return infoRepository.findById(id);
    }

    @Override
    public JSONObject selectOverallCount() {
        JSONObject object = new JSONObject();
        List<Object> overallCountList = infoRepository.selectStatics();


        Object[] objects = ((Object[]) overallCountList.get(0));

        object.put("runningCluster", objects[0]);
        object.put("runningTaskmanager", objects[1]);
        object.put("runningJob", objects[2]);
        object.put("completedJob", objects[3]);
        object.put("canceledJob", objects[4]);
        object.put("failedJob", objects[5]);
        return object;
    }
}
