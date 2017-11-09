package com.kyt.framework.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringEscapeUtils;
import org.cliffc.high_scale_lib.NonBlockingHashtable;

public class StringUtils {

    private static NonBlockingHashtable _hashTableBadWord = null;
    private static String _patternBadWord = "";
    private static final String GEN_KEY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String removeNonPrintableCharactor(String data) {
        return data.replaceAll("\\p{C}", "");
    }

    public static String urlEncode(String url) {
        if (isEmpty(url)) {
            return "";
        }
        String result = "";
        try {
            result = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
        }
        return result;
    }

    public static String urlDecode(String url) {
        if (isEmpty(url)) {
            return "";
        }
        String result = "";
        try {
            result = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
        }
        return result;
    }

    private static char getRandomChar() {
        Random rd = new Random();
        Integer number = Integer.valueOf(rd.nextInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length()));
        return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(number.intValue());
    }

    public static String getRandomString(Integer length) {
        String result = "";
        for (int i = 0; i < length.intValue(); i++) {
            result = result + getRandomChar();
        }
        return result;
    }

    public static String removeScriptTag(String data) {
        if ((data != null) && (data.length() > 0)) {
            return data.replaceAll("<\\s*script[^>]*>.*?</\\s*script[^>]*>", "");
        }
        return "";
    }

    public static boolean haveScriptTag(String data) {
        if (data == null) {
            return false;
        }
        return data.length() != removeScriptTag(data).length();
    }

    public static String removeAllTag(String data) {
        if ((data != null) && (data.length() > 0)) {
            return data.replaceAll("<(.|\n)*?>", "");
        }
        return "";
    }

    public static boolean haveTag(String data) {
        if (data == null) {
            return false;
        }
        return data.length() != removeAllTag(data).length();
    }

    public static String removeUnicode(String data) {
        if ((data != null) && (data.length() > 0)) {
            return data.replaceAll("[^\\p{ASCII}]", "");
        }
        return "";
    }

    public static String killUnicode(String data) {
        if ((data != null) && (data.length() > 0)) {
            data = data.replaceAll("��", "D");
            data = data.replaceAll("��", "d");
            return Normalizer.normalize(data, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        }
        return "";
    }

    public static boolean haveUnicode(String data) {
        if (data == null) {
            return false;
        }
        return data.length() != removeUnicode(data).length();
    }

    public static String normalize(String data) {
        if ((data != null) && (data.length() > 0)) {
            return Normalizer.normalize(data, Normalizer.Form.NFKC);
        }
        return "";
    }

    public static String removeBadWord(String data) {
        if ((data != null) && (data.length() > 0) && (_patternBadWord.length() > 0) && (_hashTableBadWord != null) && (_hashTableBadWord.size() > 0)) {
            Matcher matcher = Pattern.compile(_patternBadWord).matcher(data);
            int i = 0;
            while (matcher.find()) {
                if (_hashTableBadWord.containsKey(matcher.group(i))) {
                    data = data.replaceAll(matcher.group(i), (String) _hashTableBadWord.get(matcher.group(i)));
                }
                i++;
            }
            return data;
        }
        return "";
    }

    public static boolean haveBadWord(String data) {
        if ((data != null) && (data.length() > 0)) {
            Matcher matcher = Pattern.compile(_patternBadWord).matcher(data);
            return matcher.find();
        }
        return false;
    }

    private static boolean loadBadWord(String filePath) {
        try {
            if (_hashTableBadWord == null) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream inputData = classLoader.getResourceAsStream(filePath);
                if (inputData == null) {
                    return false;
                }
                _hashTableBadWord = new NonBlockingHashtable();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputData, "UTF-8"));
                Throwable localThrowable2 = null;
                try {
                    _patternBadWord = "(";
                    String streamLine;
                    while ((streamLine = bufferedReader.readLine()) != null) {
                        String[] strArr = streamLine.split(",");
                        if (strArr.length >= 2) {
                            String key = strArr[0].trim().toLowerCase();
                            if (!_hashTableBadWord.containsKey(key)) {
                                _hashTableBadWord.put(key, strArr[1]);
                                _patternBadWord = _patternBadWord + key + "|";
                            }
                        }
                    }
                    _patternBadWord = _patternBadWord.replaceAll(".$", "");
                    _patternBadWord += ")";
                } catch (Throwable localThrowable1) {
                    localThrowable2 = localThrowable1;
                    throw localThrowable1;
                } finally {
                    if (bufferedReader != null) {
                        if (localThrowable2 != null) {
                            try {
                                bufferedReader.close();
                            } catch (Throwable x2) {
                                localThrowable2.addSuppressed(x2);
                            }
                        } else {
                            bufferedReader.close();
                        }
                    }
                }
            }
        } catch (Exception localException) {
        }
        return false;
    }

    public static String slug(String data) {
        data = killUnicode(data).trim();
        if (data.length() > 0) {
            data = data.replaceAll("[^a-zA-Z0-9- ]*", "");
            data = data.replaceAll("[ ]{1,}", "-");
        }
        return data;
    }

    public static String slug(String data, int length) {
        String slugString = slug(data);
        return slug(data).substring(0, slugString.length() > length ? length : slugString.length());
    }

    public static String digitFormat(long value) {
        NumberFormat formatter = new DecimalFormat("#,##0");
        return formatter.format(value);
    }

    public static String doMD5(String source) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        digest.update(source.getBytes());
        byte[] hash = digest.digest();
        return byteArrayToHexString(hash);
    }

    public static String doSHA256(String source) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        digest.update(source.getBytes());
        byte[] hash = digest.digest();
        return byteArrayToHexString(hash);
    }

    public static String byteToHexString(byte aByte) {
        String hex = Integer.toHexString(0xFF & aByte);
        return (hex.length() == 1 ? "0" : "") + hex;
    }

    public static String byteArrayToHexString(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            hexString.append(byteToHexString(hash[i]));
        }
        return hexString.toString();
    }

    public static String stripStart(String str, String stripChars) {
        int strLen;
        if ((str == null) || ((strLen = str.length()) == 0)) {
            return str;
        }
        int start = 0;
        if (stripChars == null) {
            while ((start != strLen) && (Character.isWhitespace(str.charAt(start)))) {
                start++;
            }
        }
        if (stripChars.length() == 0) {
            return str;
        }
        while ((start != strLen) && (stripChars.indexOf(str.charAt(start)) != -1)) {
            start++;
        }
        return str.substring(start);
    }

    public static String stripEnd(String str, String stripChars) {
        int end;
        if ((str == null) || ((end = str.length()) == 0)) {
            return str;
        }
        if (stripChars == null) {
            while ((end != 0) && (Character.isWhitespace(str.charAt(end - 1)))) {
                end--;
            }
        }
        if (stripChars.length() == 0) {
            return str;
        }
        while ((end != 0) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
            end--;
        }
        return str.substring(0, end);
    }

    public static boolean isEmpty(String str) {
        return (str == null) || (str.length() == 0);
    }

    public static String strip(String str, String stripChars) {
        if (isEmpty(str)) {
            return str;
        }
        str = stripStart(str, stripChars);
        return stripEnd(str, stripChars);
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }
        int bufSize = endIndex - startIndex;
        if (bufSize <= 0) {
            return "";
        }
        if (endIndex > array.length) {
            endIndex = array.length;
        }
        StringBuilder buf = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                if (array[i].toString().contains(separator)) {
                    buf.append("\"");
                    buf.append(array[i]);
                    buf.append("\"");
                } else {
                    buf.append(array[i]);
                }
            }
        }
        return buf.toString();
    }

    public static String join(Iterator iterator, String separator) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return first == null ? "" : first.toString();
        }
        StringBuilder buf = new StringBuilder();
        if (first != null) {
            buf.append(first);
        }
        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    public static String join(Collection collection, String separator) {
        if (collection == null) {
            return null;
        }
        return join(collection.iterator(), separator);
    }

    public static List<String> toList(String str, String regex) {
        ArrayList<String> list = new ArrayList();
        if (str != null) {
            boolean quoted = false;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '"') {
                    quoted = !quoted;
                    sb.append(str.charAt(i));
                } else if ((!quoted) && (regex.indexOf(str.charAt(i)) >= 0)) {
                    list.add(sb.toString().trim().replaceAll("\"", ""));
                    sb.setLength(0);
                } else {
                    sb.append(str.charAt(i));
                }
            }
            if (sb.length() > 0) {
                list.add(sb.toString().trim().replaceAll("\"", ""));
            }
        }
        return list;
    }

    public static <T extends Number> List<T> toList(String str, String regex, Class<T> clazz)
  {
    List<T> list = new ArrayList();
    if (str != null) {
      boolean quoted = false;
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < str.length(); i++) {
        if (str.charAt(i) == '"') {
          quoted = !quoted;
          sb.append(str.charAt(i));
        } else if ((!quoted) && (regex.indexOf(str.charAt(i)) >= 0)) {
          try {
//            if (clazz.equals(Integer.class)) {
//              list.add(Integer.valueOf(sb.toString().trim().replaceAll("\"", "")));
//            } else if (clazz.equals(Long.class)) {
//              list.add(Long.valueOf(sb.toString().trim().replaceAll("\"", "")));
//            } else if (clazz.equals(Float.class)) {
//              list.add(Float.valueOf(sb.toString().trim().replaceAll("\"", "")));
//            } else if (clazz.equals(Double.class)) {
//              list.add(Double.valueOf(sb.toString().trim().replaceAll("\"", "")));
//            } else if (clazz.equals(Short.class)) {
//              list.add(Short.valueOf(sb.toString().trim().replaceAll("\"", "")));
//            }
          }
          catch (NumberFormatException localNumberFormatException) {}
          

          sb.setLength(0);
        } else {
          sb.append(str.charAt(i));
        }
      }
      if (sb.length() > 0) {
        try {
//          if (clazz.equals(Integer.class)) {
//            list.add(Integer.valueOf(sb.toString().trim().replaceAll("\"", "")));
//          } else if (clazz.equals(Long.class)) {
//            list.add(Long.valueOf(sb.toString().trim().replaceAll("\"", "")));
//          } else if (clazz.equals(Float.class)) {
//            list.add(Float.valueOf(sb.toString().trim().replaceAll("\"", "")));
//          } else if (clazz.equals(Double.class)) {
//            list.add(Double.valueOf(sb.toString().trim().replaceAll("\"", "")));
//          } else if (clazz.equals(Short.class)) {
//            list.add(Short.valueOf(sb.toString().trim().replaceAll("\"", "")));
//          }
        }
        catch (NumberFormatException localNumberFormatException1) {}
      }
    }
    return list;
  }
}
