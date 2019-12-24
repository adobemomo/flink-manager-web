package ac.cn.iie.constant;

public class FlinkRestJsonConstant {
  public static final String KEY_TASK_MANAGERS = "taskmanagers";
  public static final String KEY_JOBS = "jobs";

  public static final String JOB_STATUS_RUNNING = "RUNNING";
  public static final String JOB_STATUS_FINISHED = "FINISHED";
  public static final String JOB_STATUS_CANCELED = "CANCELED";
  public static final String JOB_STATUS_FAILED = "FAILED";

  public static final String KEY_JOB_ID = "id";
  public static final String KEY_JOB_STATUS = "status";
  public static final String KEY_JOB_NAME = "name";
  public static final String KEY_JOB_VERTICES = "vertices";
  public static final String KEY_JOB_START_TIME = "start-time";
  public static final String KEY_JOB_END_TIME = "end-time";
  public static final String KEY_JOB_DURATION = "duration";

  public static final String KEY_JOB_VERTICES_TASKS = "tasks";

  public static final String VERTICES_TASKS_STATUS_RUNNING = "RUNNING";
  public static final String VERTICES_TASKS_STATUS_CREATED = "CREATED";
  public static final String VERTICES_TASKS_STATUS_CANCELED = "CANCELED";
  public static final String VERTICES_TASKS_STATUS_RECONCILING = "RECONCILING";
  public static final String VERTICES_TASKS_STATUS_DEPLOYING = "DEPLOYING";
  public static final String VERTICES_TASKS_STATUS_FAILED = "FAILED";
  public static final String VERTICES_TASKS_STATUS_SCHEDULED = "SCHEDULED";
  public static final String VERTICES_TASKS_STATUS_CANCELING = "CANCELING";
  public static final String VERTICES_TASKS_STATUS_FINISHED = "FINISHED";
}
