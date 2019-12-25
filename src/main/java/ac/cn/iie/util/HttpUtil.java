package ac.cn.iie.util;

import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtil {
    public static String doGet(String uri) {
        String response = null;
        try {
            response = Unirest.get(uri).asJson().getBody().toString();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            return response;
        }
    }
}
