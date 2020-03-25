package cn.edu.swpu.wlzx.consumer.common.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gongsir
 * @date 2020/3/25 15:14
 * 编码不要畏惧变化，要拥抱变化
 */
public class IpAddrUtil {
    private static final String APP_CODE = "e9e249da68794023bf9929df37f3fed2";

    private static final String HOST = "https://ips.market.alicloudapi.com";

    private static final String path = "/iplocaltion";

    private static final String SUCCESS_CODE = "100";

    public static String getIpInfo(String ip) {
        String url = HOST + path;
        //ip参数
        Map<String,String> params = new HashMap<>();
        params.put("ip",ip);

        //请求头header
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","APPCODE "+APP_CODE);
        String rs = HttpClientUtil.doGet(url, params, headers);
        JSONObject jsonObject = JSONObject.parseObject(rs);
        if (SUCCESS_CODE.equals(jsonObject.get("code").toString())){
            //ip解析成功
            JSONObject result = JSONObject.parseObject(jsonObject.get("result").toString());
            return result.get("province") + "-" + result.get("city");
        }
        return jsonObject.get("message").toString();
    }

    /**
     * 获取客户端IP地址
     * @param request 请求信息
     * @return ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length () == 0 || "unknown".equalsIgnoreCase (ip)) {
            ip = request.getHeader ("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length () == 0 || "unknown".equalsIgnoreCase (ip)) {
            ip = request.getRemoteAddr ();
            if ("127.0.0.1".equals(ip)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost ();
                } catch (Exception e) {
                    e.printStackTrace ();
                }
                ip = inet != null ? inet.getHostAddress() : null;
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length () > 15) {
            if (ip.indexOf (",") > 0) {
                ip = ip.substring (0, ip.indexOf (","));
            }
        }
        return ip;
    }
}
