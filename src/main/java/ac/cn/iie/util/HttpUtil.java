package ac.cn.iie.util;

import kong.unirest.Unirest;

public class HttpUtil {
  public static String doGet(String uri) {
    return Unirest.get(uri).asJson().getBody().toString();
  }
}
