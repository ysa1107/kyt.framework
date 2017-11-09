package com.kyt.framework.util;

import java.util.ArrayList;
import java.util.List;

public class PagingUtils
{
  public static List<String> pagingListString(List<String> list, int offset, int count)
  {
    List<String> result = new ArrayList();
    if (list != null)
    {
      int lsCount = list.size();
      if (offset > lsCount) {
        return result;
      }
      int fromIndex = offset < 0 ? 0 : offset;
      int toIndex = offset + count;
      toIndex = toIndex > lsCount ? lsCount : toIndex;
      result = list.subList(fromIndex, toIndex);
    }
    return result;
  }
  
  public static List<Long> pagingListLong(List<Long> list, int offset, int count)
  {
    List<Long> result = new ArrayList();
    if (list != null)
    {
      int lsCount = list.size();
      if (offset > lsCount) {
        return result;
      }
      int fromIndex = offset < 0 ? 0 : offset;
      int toIndex = offset + count;
      toIndex = toIndex > lsCount ? lsCount : toIndex;
      result = list.subList(fromIndex, toIndex);
    }
    return result;
  }
}
