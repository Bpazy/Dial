package bpazy.dial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CRouter {
    public CRouter() {
    }

    public final static int SUCCESS = 0;
    public final static int INIT_URL_ERROR = 100;
    public final static int IO_EXCEPTION = 101;
    public final static int UNKNOWN_ERROR = 102;

    public int connect(String password, String Cookie, String userName) {
        try {
            URL url = new URL("http://192.168.1.1/userRpm/PPPoECfgRpm.htm?wan=0&wantype=2&acc=" + userName + "&psw=" + password + "&confirm=" + password + "&specialDial=100&SecType=0&sta_ip=0.0.0.0&sta_mask=0.0.0.0&linktype=2&Save=%B1%A3+%B4%E6");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Accept", "text/html, application/xhtml+xml, */*");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cookie", Cookie);
            connection.setRequestProperty("Host", "192.168.1.1");
            connection.setRequestProperty("Referer", "http://192.168.1.1/userRpm/PPPoECfgRpm.htm");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko");
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "gbk"));
            String result = null, line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            if (result != null && result.matches("(.+)自动断线等待时间(.+)")) {
                return SUCCESS;
            }
        } catch (MalformedURLException e) {
            return INIT_URL_ERROR;
        } catch (IOException e) {
            return IO_EXCEPTION;
        }
        return UNKNOWN_ERROR;
    }
}
