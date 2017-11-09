package com.kyt.framework.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

public class NetworkUtils {

    public static String getClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public static String ipAddressToBinaryString(String ipAddress) {
        try {
            String result = "";
            String[] arr = ipAddress.split("\\.");
            if (arr.length != 4) {
                return "";
            }
            for (int i = 0; i < arr.length; i++) {
                int val = ConvertUtils.toInt(arr[i], -1);
                if ((val > 255) || (val < 0)) {
                    return "";
                }
                result = result + StringUtils.leftPad(Integer.toBinaryString(val), 8, "0");
            }
            return result;
        } catch (Exception localException) {
        }
        return "";
    }

    public static String ipRangeToBinaryString(String ipRange) {
        try {
            String[] arr = ipRange.split("/");
            if (arr.length == 1) {
                return ipAddressToBinaryString(arr[0].trim());
            }
            if (arr.length == 2) {
                int len = ConvertUtils.toInt(arr[1]);
                return ipAddressToBinaryString(arr[0].trim()).substring(0, len);
            }
        } catch (Exception localException) {
        }
        return "";
    }

    public static long ipToLong(InetAddress ip) {
        byte[] octets = ip.getAddress();
        long result = 0L;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xFF;
        }
        return result;
    }

    public static long ipToLong(String ipAddress) {
        try {
            String[] arr = ipAddress.split(",");
            return ipToLong(InetAddress.getByName(arr[0].trim()));
        } catch (Exception localException) {
        }
        return -1L;
    }

    public static long[] ipRangeToRangeNumber(String ipRange) {
        try {
            String[] arr = ipRange.split("/");
            if (arr.length == 1) {
                long value = ipToLong(arr[0]);
                return new long[]{value, value};
            }
            if (arr.length == 2) {
                int len = ConvertUtils.toInt(arr[1]);
                String bin = ipAddressToBinaryString(arr[0].trim()).substring(0, len);
                long from = Long.parseLong(StringUtils.rightPad(bin, 32, "0"), 2);
                long to = Long.parseLong(StringUtils.rightPad(bin, 32, "1"), 2);
                return new long[]{from, to};
            }
        } catch (Exception localException) {
        }
        return new long[]{-1L, -1L};
    }

    public static boolean isIPAddressInRange(String ipAddress, String ipRange) {
        String bIP = ipAddressToBinaryString(ipRange);
        String bRange = ipRangeToBinaryString(ipRange);
        return bIP.startsWith(bRange);
    }

    public static int getContentLength(String url)
            throws Exception {
        try {
            if ((url == null) || (url.length() <= 0)) {
                return 0;
            }
            URL sourceURL = new URL(url);

            HttpURLConnection sourceConnection = (HttpURLConnection) sourceURL.openConnection();

            sourceConnection.connect();

            System.out.println(sourceConnection.getHeaderFields());
            int result = 0;
            if ((sourceConnection.getResponseCode() >= 200) && (sourceConnection.getResponseCode() < 300)) {
                result = ConvertUtils.toInt(sourceConnection.getHeaderField("Content-Length"));
            }
            sourceConnection.getInputStream().close();
            sourceConnection.disconnect();

            return result;
        } catch (Exception ex) {
            throw ex;
        }
    }

   

    public static boolean sendRequest(String url) {
        try {
            URL sourceURL = new URL(url);
            HttpURLConnection sourceConnection = (HttpURLConnection) sourceURL.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            sourceConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

            sourceConnection.connect();

            String encoding = sourceConnection.getContentEncoding();

            int code = sourceConnection.getResponseCode();

            boolean result = (code >= 200) && (code < 500);
            if (!result) {
                System.out.println(code);
            }
            return (code >= 200) && (code < 500);
        } catch (Exception localException) {
        }
        return false;
    }

    
}
