package ac.cn.iie.service;

import com.alibaba.fastjson.JSONArray;

public interface FlinkRestService {
    JSONArray getJobList(Integer id, String status);
}
