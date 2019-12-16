package ac.cn.iie.repository;

import ac.cn.iie.entity.Cluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClusterRepository
    extends JpaRepository<Cluster, Integer>, JpaSpecificationExecutor {}
