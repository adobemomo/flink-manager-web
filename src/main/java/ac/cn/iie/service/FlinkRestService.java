package ac.cn.iie.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface FlinkRestService {
    JSONArray getJobList(Integer id, String status);

}
