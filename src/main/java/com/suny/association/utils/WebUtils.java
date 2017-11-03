package com.suny.association.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Objects;

/**
 * Comments:    获取登录用户的信息工具
 * @author :   孙建荣
 * Create Date: 2017/04/20 19:57
 */
public class WebUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);
    /**
     * 未知值
     */
    private static final String UNKNOWN = "unknown";
    /**
     * 可能会出现的本地IP地址
     */
    private static final String LOCALHOST_IP_IPV4="127.0.0.1";
    private static final String LOCALHOST_IP_IPV4_2="127.0.1.1";
    private static final String LOCALHOST_IP_IPV6="0:0:0:0:0:0:0:1";
    private static final String TAOBAO_GET_IP_INFO_URL="http://ip.taobao.com/service/getData.php?ip=";


    /**
     * 获取当前请求的request请求实例
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }


    /**
     * 返回项目的绝对路径
     *
     * @param request 请求对象
     * @return 绝对路径
     */
    private static String getBasePath(HttpServletRequest request) {
        String path = request.getContextPath();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    }

    /**
     * 获取普通精度的位置.
     *
     * @param ip ip地址
     * @return 百度普通定位地址
     */
    public static IpInfo getIpInfo(String ip) {
        if (LOCALHOST_IP_IPV4.equals(ip.trim())||LOCALHOST_IP_IPV6.equals(ip.trim())){
             return localhostIpInfo();
        }
        if (LOCALHOST_IP_IPV4_2.equals(ip.trim())||LOCALHOST_IP_IPV4_2.equals(ip.trim())){
            return localhostIpInfo();
        }
        URL myUrl;
        String ipString = null;
        StringBuilder responseJson = new StringBuilder("");
        try {
            ipString = URLEncoder.encode(ip, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("不支持的编码异常{}", e.getMessage());
        }
        String url = TAOBAO_GET_IP_INFO_URL + ipString;
        URLConnection urlConnection = null;
        try {
            myUrl = new URL(url);
            //　不使用代理进行访问
            urlConnection = myUrl.openConnection();
        } catch (IOException e) {
            logger.error("发生了输入输出流异常",e.getMessage());
        }
        if (urlConnection != null) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
            ) {
                String data;
                while ((data = bufferedReader.readLine()) != null) {
                    responseJson.append(data);
                }
                return parseJsonDate(responseJson.toString());
            } catch (UnknownHostException e) {
                logger.error("无法获取hosts地址，检查是否有网络连接");
            } catch (IOException e1) {
                logger.error("发生输入输出流异常{}", e1.getMessage());
            }
        }
        return null;
    }


    /**
     * 拼接一个固定的本地网络连接的的信息
     * @return  IP等信息
     */
    private static IpInfo localhostIpInfo(){
        return new IpInfo("59.53.207.251","中国","华东","江西省","南昌市","南昌县","电信");
    }

    private static IpInfo parseJsonDate(String jsonInfo){
        ResponseInfo responseInfo = JackJsonUtil.jsonToObject(jsonInfo, ResponseInfo.class);
        if(responseInfo != null){
            if(responseInfo.getCode()== 1){
                logger.error("淘宝返回的JSON状态码为1,查询失败");
                return localhostIpInfo();
            }
            else{
                logger.error("淘宝返回的JSON状态码为0,查询成功");
                return responseInfo.getData();
            }
        }
        logger.warn("由于未知的原因导致返回的IP查询信息为空");
        return localhostIpInfo();
    }







    /***
     * 获取客户端ip地址(可以穿透代理)
     */
    public static String getClientIpAdder(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_VIA");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (LOCALHOST_IP_IPV4.equals(ip) || LOCALHOST_IP_IPV6.equals(ip)){
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException ignored) {
            }
        }
        return ip;
    }

    /**
     * 获取操作系统版本
     *
     * @param userAgent 浏览器userAgent标示
     * @return 模糊匹配操作系统版本
     */
    public static String getClientOS(String userAgent) {
        if (Objects.equals(userAgent, "") || userAgent == null) {
            return UNKNOWN;
        }
        if (userAgent.contains("Windows")) {//主流应用靠前
            if (userAgent.contains("Windows NT 10.0")) {//Windows 10
                return "Windows10";//判断浏览器
            } else if (userAgent.contains("Windows NT 6.2")) {//Windows 8
                return "Windows8";//判断浏览器
            } else if (userAgent.contains("Windows NT 6.1")) {//Windows 7
                return "Windows7";
            } else if (userAgent.contains("Windows NT 6.0")) {//Windows Vista
                return "WindowsVista";
            } else if (userAgent.contains("Windows NT 5.2")) {//Windows XP x64 Edition
                return "WindowsXP x64 Edition";
            } else if (userAgent.contains("Windows NT 5.1")) {//Windows XP
                return "WindowsXP";
            } else if (userAgent.contains("Windows NT 5.01")) {//Windows 2000, Service Pack 1 (SP1)
                return "Windows2000 SP1";
            } else if (userAgent.contains("Windows NT 5.0")) {//Windows 2000
                return "Windows2000";
            } else if (userAgent.contains("Windows NT 4.0")) {//Microsoft Windows NT 4.0
                return "WindowsNT 4.0";
            } else if (userAgent.contains("Windows 98; Win 9x 4.90")) {//Windows Millennium Edition (Windows Me)
                return "WindowsME";
            } else if (userAgent.contains("Windows 98")) {//Windows 98
                return "Windows98";
            } else if (userAgent.contains("Windows 95")) {//Windows 95
                return "Windows95";
            } else if (userAgent.contains("Windows Phone")) {//Windows Phone
                return getOSDetail("Windows", userAgent);
            } else if (userAgent.contains("Windows CE")) {//Windows CE
                return "WindowsCE";
            }
            return "Windows";
        } else if (userAgent.contains("Mac OS X")) {
            if (userAgent.contains("iPhone")) {
                if (userAgent.contains("iPhone 4")) {
                    return "iPhone 4";//判断浏览器
                } else if (userAgent.contains("iPhone 4S")) {
                    return "iPhone 4S";//判断浏览器
                } else if (userAgent.contains("iPhone 5")) {
                    return "iPhone 5";//判断浏览器
                } else if (userAgent.contains("iPhone 5S")) {
                    return "iPhone 5S";//判断浏览器
                } else if (userAgent.contains("iPhone 6")) {
                    return "iPhone 6";//判断浏览器
                } else if (userAgent.contains("iPhone 6S")) {
                    return "iPhone 6S";//判断浏览器
                } else if (userAgent.contains("iPhone ; U ;")) {
                    return "iPhone";//判断浏览器
                }
                return "iPhone";
            }
            if (userAgent.contains("iPod")) {
                return "iPod";//判断浏览器
            } else if (userAgent.contains("iPad")) {
                return "iPad";//判断浏览器
            } else if (userAgent.contains("iPad2")) {
                return "iPad2";//判断浏览器
            }
            return "Mac OS X";
        } else if (userAgent.contains("Linux")) {
            if (userAgent.contains("Android")) {
                return getOSDetail("Android", userAgent);
            }
            return "Linux";
        } else if (userAgent.contains("Ubuntu")) {
            return "Ubuntu";
        } else if (userAgent.contains("x11")) {
            return "Unix";
        }
        return "UnKnown";
    }

    private static String getOSDetail(String simpleName, String userAgent) {
        int simpleNameIndex = userAgent.indexOf(simpleName);
        String simpleNameText = userAgent.substring(simpleNameIndex);
        int osDetailLength = simpleNameText.split(";").length;
        if (osDetailLength > 12) {
            return simpleNameText.split("\\)")[0];
        }
        return simpleNameText.split(";")[0];
    }


    public static String getBrowserInfo(String userAgent) {
        String useragent = userAgent.toLowerCase();
        if (useragent.contains("edge")) {
            return (userAgent.substring(useragent.indexOf("edge")).split(" ")[0]).replace("/", "-");
        } else if (useragent.contains("ucbrowser") || useragent.contains("ubrowser")) {
            if (useragent.contains("ucbrowser")) {
                return (useragent.substring(useragent.indexOf("ucbrowser")).split(" ")[0]).replace("/", "-").replace("ucbrowser", "UC浏览器");
            } else if (useragent.contains("ubrowser")) {
                return (useragent.substring(useragent.indexOf("ubrowser")).split(" ")[0]).replace("/", "-").replace("ubrowser", "UC浏览器");
            }
        } else if (useragent.contains("msie")) {
            if (userAgent.contains("MSIE 11.0")) {//Internet Explorer 10
                return "Internet Explorer11";
            } else if (userAgent.contains("MSIE 10.0")) {//Internet Explorer 10
                return "Internet Explorer10";
            } else if (userAgent.contains("MSIE 9.0")) {//Internet Explorer 9
                return "Internet Explorer9";
            } else if (userAgent.contains("MSIE 8.0")) {//Internet Explorer 8
                return "Internet Explorer8";
            } else if (userAgent.contains("MSIE 7.0")) {//Internet Explorer 7
                return "Internet Explorer7";
            } else if (userAgent.contains("MSIE 6.0")) {//Internet Explorer 6
                return "Internet Explorer6";
            }
//            return "Internet Explorer";
            String substring = userAgent.substring(useragent.indexOf("msie")).split(";")[0];
            return substring.replace("MSIE", "IE").replace(" ", "-");
        } else if (useragent.contains("safari") && useragent.contains("version")) {
            return (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-" + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if (useragent.contains("opr") || useragent.contains("opera")) {
            if (useragent.contains("opera")) {
                return (userAgent.substring(useragent.indexOf("opera")).split(" ")[0]).split("/")[0] + "-" + (userAgent.substring(useragent.indexOf("version")).split(" ")[0]).split("/")[1];
            } else if (useragent.contains("opr")) {
                return ((userAgent.substring(useragent.indexOf("opr")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
            }
        } else if (useragent.contains("chrome")) {
            return (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if (useragent.contains("firefox")) {
            return (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if (useragent.contains("360SE")) {
            return "360安全浏览器";
        } else if (useragent.contains("QIHU 360EE")) {
            return "360急速浏览器";
        } else if (useragent.contains("Maxthon")) {
            return "傲游浏览器";
        } else if (useragent.contains("rv")) {
            String ieversion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
            return "IE" + ieversion.substring(0, ieversion.length() - 1);
        }
        return UNKNOWN;

    }

    public static class ResponseInfo {
        /**
         * 返回的状态码
         */
        private int code;
        /**
         * 返回的查询数据
         */
        private IpInfo data;

        public ResponseInfo(int code, IpInfo data) {
            this.code = code;
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public IpInfo getData() {
            return data;
        }

        public void setData(IpInfo data) {
            this.data = data;
        }
    }

    /**************************************
     *  Description   查询IP地址信息
     *  @author 孙建荣
     *  @date 17-11-1.下午8:37
     *  @version 1.0
     **************************************/
    public static class IpInfo {

        private String ip;
        /**
         * 国家
         */
        private String country;
        private String area;
        /**
         *省
         */
        private String region;
        /**
         * 市
         */
        private String city;
        /**
         * 县
         */
        private String county;
        /**
         * 电信商
         */
        private String isp;

        @JsonProperty("country_id")
        private String countryId;
        @JsonProperty("area_id")
        private String areaId;
        @JsonProperty("region_id")
        private String regionId;
        @JsonProperty("city_id")
        private String cityId;
        @JsonProperty("county_id")
        private String countyId;
        @JsonProperty("isp_id")
        private String ispId;

        /**
         * 私有的构造方法,不允许空的构造方法
         */
        private IpInfo(){

        }

        /**
         * 最基本的构造方法,包含常用的
         * @param ip  ip地址
         * @param country  国家
         * @param area  地区
         * @param region  省
         * @param city   市
         * @param county  县
         * @param isp   宽带服务商
         */
        public IpInfo(String ip, String country, String area, String region, String city, String county, String isp) {
            this.ip = ip;
            this.country = country;
            this.area = area;
            this.region = region;
            this.city = city;
            this.county = county;
            this.isp = isp;
        }


        /**
         * 完整的构造函数,用于网络查询返回封装的构造函数
         * @param ip  ip地址
         * @param country  国家
         * @param area  地区
         * @param region  省
         * @param city   市
         * @param county  县
         * @param isp   宽带服务商
         * @param countryId  国家ID
         * @param areaId   地区ID
         * @param regionId  省ID
         * @param cityId   市ID
         * @param countyId  县ID
         * @param ispId  宽带商ID
         */
        public IpInfo(String ip, String country, String area, String region, String city, String county, String isp, String countryId, String areaId, String regionId, String cityId, String countyId, String ispId) {
            this.ip = ip;
            this.country = country;
            this.area = area;
            this.region = region;
            this.city = city;
            this.county = county;
            this.isp = isp;
            this.countryId = countryId;
            this.areaId = areaId;
            this.regionId = regionId;
            this.cityId = cityId;
            this.countyId = countyId;
            this.ispId = ispId;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getIsp() {
            return isp;
        }

        public void setIsp(String isp) {
            this.isp = isp;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getRegionId() {
            return regionId;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getCountyId() {
            return countyId;
        }

        public void setCountyId(String countyId) {
            this.countyId = countyId;
        }

        public String getIspId() {
            return ispId;
        }

        public void setIspId(String ispId) {
            this.ispId = ispId;
        }
    }





}
