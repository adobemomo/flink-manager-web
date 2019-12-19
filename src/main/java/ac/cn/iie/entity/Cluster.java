package ac.cn.iie.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "cluster")
public class Cluster {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String uri;
  private String sysId;
  private String province;
  private String flinkTaskName;
}
