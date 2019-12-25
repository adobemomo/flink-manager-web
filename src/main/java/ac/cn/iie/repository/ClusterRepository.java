package ac.cn.iie.repository;

import ac.cn.iie.entity.Cluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClusterRepository
        extends JpaRepository<Cluster, Integer>, JpaSpecificationExecutor {

  @Query(
          value =
                  "select c.id, c.uri, c.sys_id, c.province,  c.flink_task_name from cluster c join info i "
                          + "where i.status = 'alive'; ",
          nativeQuery = true)
  List<Object> selectAvliceCluster();
}
