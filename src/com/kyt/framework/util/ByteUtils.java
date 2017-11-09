package com.kyt.framework.util;

import java.nio.ByteBuffer;

public class ByteUtils
{
  private static final ByteBuffer buffer = ByteBuffer.allocate(64);
  
  public static synchronized byte[] longToBytes(long x)
  {
    buffer.putLong(0, x);
    return buffer.array();
  }
  
  public static synchronized long bytesToLong(byte[] bytes)
  {
    buffer.put(bytes, 0, bytes.length);
    buffer.flip();
    return buffer.getLong();
  }
}