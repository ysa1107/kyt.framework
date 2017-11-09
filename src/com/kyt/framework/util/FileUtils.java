package com.kyt.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileUtils
{
  public static String HOME_PATH = System.getProperty("apppath");
  
  static
  {
    if (HOME_PATH == null) {
      HOME_PATH = "";
    } else {
      HOME_PATH += File.separator;
    }
  }
  
  public static void createFolder(String folder)
  {
    try
    {
      new File(folder).mkdirs();
    }
    catch (Exception localException) {}
  }
  
  public static String readAllString(String filePath)
  {
    try
    {
      File f = new File(filePath);
      return readAllString(f);
    }
    catch (Exception ex) {}
    return "";
  }
  
  public static String readAllString(File f)
  {
    try
    {
      BufferedReader bi = new BufferedReader(new FileReader(f));
      StringBuilder sb = new StringBuilder();
      String str;
      while ((str = bi.readLine()) != null) {
        sb.append("\r\n" + str);
      }
      String result = sb.toString();
      if (result.length() > 0) {}
      return result.substring(2);
    }
    catch (Exception ex) {}
    return "";
  }
  
  public static boolean writeAllString(String filePath, String data)
  {
    try
    {
      File f = new File(filePath);
      return writeAllString(f, data);
    }
    catch (Exception ex) {}
    return false;
  }
  
  public static boolean writeAllString(File f, String data)
  {
    try
    {
      FileWriter wr = new FileWriter(f);Throwable localThrowable2 = null;
      try
      {
        wr.write(data);
      }
      catch (Throwable localThrowable1)
      {
        localThrowable2 = localThrowable1;throw localThrowable1;
      }
      finally
      {
        if (wr != null) {
          if (localThrowable2 != null) {
            try
            {
              wr.close();
            }
            catch (Throwable x2)
            {
              localThrowable2.addSuppressed(x2);
            }
          } else {
            wr.close();
          }
        }
      }
      return true;
    }
    catch (Exception localException) {}
    return false;
  }
  
  public static boolean appendString(String filePath, String data)
  {
    try
    {
      File f = new File(filePath);
      return appendString(f, data);
    }
    catch (Exception ex) {}
    return false;
  }
  
  public static boolean appendString(File f, String data)
  {
    try
    {
      FileWriter wr = new FileWriter(f, true);Throwable localThrowable2 = null;
      try
      {
        wr.append(data);
        wr.flush();
      }
      catch (Throwable localThrowable1)
      {
        localThrowable2 = localThrowable1;throw localThrowable1;
      }
      finally
      {
        if (wr != null) {
          if (localThrowable2 != null) {
            try
            {
              wr.close();
            }
            catch (Throwable x2)
            {
              localThrowable2.addSuppressed(x2);
            }
          } else {
            wr.close();
          }
        }
      }
      return true;
    }
    catch (Exception localException) {}
    return false;
  }
  
  public static boolean deleteFile(String filePath)
  {
    try
    {
      File f = new File(filePath);
      return f.delete();
    }
    catch (Exception localException) {}
    return false;
  }
}
