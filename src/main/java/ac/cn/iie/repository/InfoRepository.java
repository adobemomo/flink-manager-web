package ac.cn.iie.repository;

import ac.cn.iie.entity.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoRepository
        extends JpaRepository<Info, Integer>, JpaSpecificationExecutor {
}
