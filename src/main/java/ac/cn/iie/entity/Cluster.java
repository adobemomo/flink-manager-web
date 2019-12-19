package ac.cn.iie.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "cluster")
public class Cluster {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  /** 集群编号 */
  private Integer id;

  private String uri;
  private String name;
}
