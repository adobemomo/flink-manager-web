package ac.cn.iie.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "info")
public class Info {
  @Id
  private int id;

  private String uri;
  private String status;
  private int runningTaskmanager;
  private int runningJob;
  private int completedJob;
  private int canceledJob;
  private int failedJob;
}
