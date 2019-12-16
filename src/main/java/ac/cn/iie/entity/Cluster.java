package ac.cn.iie.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "cluster")
public class Cluster {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  /** 集群编号 */
  private Integer id;

  private String address;
  private String port;
  private String name;
}
