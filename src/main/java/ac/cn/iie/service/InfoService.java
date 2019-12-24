package ac.cn.iie.service;

import ac.cn.iie.entity.Info;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Optional;

public interface InfoService {

  Info insertInfo(int id, String uri);

  Boolean deleteInfo(int id);

  Info updateInfo(int id, String uri);

  List<Info> updateInfo();

  List<Info> selectInfo();

  Optional<Info> selectInfo(int id);

  JSONObject selectOverallCount();
}
