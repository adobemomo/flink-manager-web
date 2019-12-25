package ac.cn.iie.service.impl;

import ac.cn.iie.entity.Cluster;
import ac.cn.iie.repository.ClusterRepository;
import ac.cn.iie.service.ClusterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClusterServiceImpl implements ClusterService {

  private final ClusterRepository clusterRepository;

  public ClusterServiceImpl(ClusterRepository clusterRepository) {
    this.clusterRepository = clusterRepository;
  }

  @Override
  public Cluster insertCluster(Cluster cluster) {
    return clusterRepository.save(cluster);
  }

  @Override
  public Boolean deleteCluster(Integer id) {
    if (clusterRepository.findById(id).isPresent()) {
      clusterRepository.deleteById(id);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Cluster updateCluster(Cluster cluster) {
    Optional<Cluster> old = clusterRepository.findById(cluster.getId());
    if (old.isPresent()) {
      return clusterRepository.save(cluster);
    } else {
      return null;
    }
  }

  @Override
  public List<Cluster> selectCluster() {
    return clusterRepository.findAll();
  }

  @Override
  public List<Cluster> selectAliveCluster() {
    List<Object> objectList = clusterRepository.selectAvliceCluster();
    List<Cluster> res = new ArrayList<>();
    for (Object obj : objectList) {
      Object[] objArray = (Object[]) obj;
      Cluster cluster = new Cluster();
      cluster.setId((int) objArray[0]);
      cluster.setUri((String) objArray[1]);
      cluster.setSysId((String) objArray[2]);
      cluster.setProvince((String) objArray[3]);
      cluster.setFlinkTaskName((String) objArray[4]);
      res.add(cluster);
    }
    return res;
  }

  @Override
  public Optional<Cluster> selectCluster(Integer number) {
    return clusterRepository.findById(number);
  }

  @Override
  public Page<Cluster> selectCluster(Integer page, Integer size) {
    /// Sort.Direction是个枚举有ASC(升序)和DESC(降序)
    Sort.Direction sort = Sort.Direction.DESC;
    /// 获取PageRequest对象 page:页码 size每页容量 sort排序方式 "number" 以谁为准排序
    Pageable pageable = PageRequest.of(page - 1, size, sort, "id");
    return clusterRepository.findAll(pageable);
  }
}
