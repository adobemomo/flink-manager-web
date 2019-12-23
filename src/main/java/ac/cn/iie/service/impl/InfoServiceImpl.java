package ac.cn.iie.service.impl;

import ac.cn.iie.entity.Info;
import ac.cn.iie.repository.InfoRepository;
import ac.cn.iie.service.FlinkRestService;
import ac.cn.iie.service.InfoService;
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
    info.setId(id);
    info.setUri(uri);
    info.setStatus(status);
    info.setRunningJob(flinkRestService.getJobList(id, "RUNNING").size());
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
}
