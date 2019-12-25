package ac.cn.iie.service;

import ac.cn.iie.entity.Cluster;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ClusterService {
    Cluster insertCluster(Cluster cluster);

    Boolean deleteCluster(Integer number);

    Cluster updateCluster(Cluster cluster);

    List<Cluster> selectCluster();

    List<Cluster> selectAliveCluster();

    Optional<Cluster> selectCluster(Integer number);

    Page<Cluster> selectCluster(Integer page, Integer size);
}
