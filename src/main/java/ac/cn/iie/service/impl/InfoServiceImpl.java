package ac.cn.iie.service.impl;

import ac.cn.iie.entity.Info;
import ac.cn.iie.repository.InfoRepository;
import ac.cn.iie.service.FlinkRestService;
import ac.cn.iie.service.InfoService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static ac.cn.iie.constant.FlinkRestApiConstant.TASK_MANAGER_OVERVIEW;
import static ac.cn.iie.constant.FlinkRestApiConstant.WEB_CONFIG;
import static ac.cn.iie.constant.FlinkRestJsonConstant.*;
import static ac.cn.iie.constant.OverallCountConstant.*;
import static ac.cn.iie.util.HttpUtil.doGet;

@Slf4j
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

        try {
            String status = "dead";
            if (Unirest.get(uri + WEB_CONFIG).asJson().getStatus() == 200) {
                status = "alive";
            }
            String taskManagers = doGet(uri + TASK_MANAGER_OVERVIEW);
            JSONObject tmObject = JSON.parseObject(taskManagers);
            JSONArray tmList = JSON.parseArray(tmObject.get(KEY_TASK_MANAGERS).toString());

            info.setId(id);
            info.setUri(uri);
            info.setStatus(status);
            info.setRunningJob(flinkRestService.getJobList(id, JOB_STATUS_RUNNING).size());
            info.setCompletedJob(flinkRestService.getJobList(id, JOB_STATUS_FINISHED).size());
            info.setCanceledJob(flinkRestService.getJobList(id, JOB_STATUS_CANCELED).size());
            info.setFailedJob(flinkRestService.getJobList(id, JOB_STATUS_FAILED).size());
            info.setRunningTaskmanager(tmList.size());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

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
            Info info = new Info();
            try {
                info = infoRepository.save(updateInfoWithFlink(id, uri));
            } catch (Exception e) {
                log.warn(e.getMessage());
            } finally {
                return info;
            }
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

        if (infoRepository.count() == 0) {
            object.put(OVERALL_RUNNING_CLUSTER, 0);
            object.put(OVERALL_RUNNING_TASK_MANAGER, 0);
            object.put(OVERALL_RUNNING_JOB, 0);
            object.put(OVERALL_COMPLETED_JOB, 0);
            object.put(OVERALL_CANCELED_JOB, 0);
            object.put(OVERALL_FAILED_JOB, 0);
        } else {
            List<Object> overallCountList = infoRepository.selectStatics();

            Object[] objects = ((Object[]) overallCountList.get(0));

            object.put(OVERALL_RUNNING_CLUSTER, objects[0]);
            object.put(OVERALL_RUNNING_TASK_MANAGER, objects[1]);
            object.put(OVERALL_RUNNING_JOB, objects[2]);
            object.put(OVERALL_COMPLETED_JOB, objects[3]);
            object.put(OVERALL_CANCELED_JOB, objects[4]);
            object.put(OVERALL_FAILED_JOB, objects[5]);
        }

        return object;
    }
}
