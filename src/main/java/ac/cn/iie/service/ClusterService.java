package ac.cn.iie.service;

import ac.cn.iie.entity.Cluster;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ClusterService {
  Cluster insertCluster(Cluster cluster);

  Boolean deleteCluster(Integer number);

  Cluster updateCluster(Cluster cluster);

  List<Cluster> selectCluster();

  Optional<Cluster> selectCluster(Integer number);

  Page<Cluster> selectCluster(Integer page, Integer size);
}
