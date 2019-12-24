package ac.cn.iie.repository;

import ac.cn.iie.entity.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfoRepository extends JpaRepository<Info, Integer>, JpaSpecificationExecutor {
  @Query(
          value =
                  "select count(*) runningCluster, "
                          + "sum(running_taskmanager) runningTaskmanager, "
                          + "sum(running_job) runningJob, "
                          + "sum(completed_job) completedJob, "
                          + "sum(canceled_job) canceledJob, "
                          + "sum(failed_job) failedJob "
                          + "from info "
                          + "where status = 'alive';",
          nativeQuery = true)
  List<Object> selectStatics();
}
