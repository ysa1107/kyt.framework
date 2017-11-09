package com.kyt.framework.dbconn;

import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.log4j.Logger;

public class ClientObjectFactory implements PoolableObjectFactory
{
  private static final Logger log = Logger.getLogger(ClientObjectFactory.class);
  private String driver;
  private String url;
  private String user;
  private String password;
  
  public ClientObjectFactory(String driver, String url, String user, String password)
  {
     this.driver = driver;
     this.url = url;
    this.user = user;
  this.password = password;
  }
  

  public void activateObject(Object arg0)
    throws Exception
  {}
  

  public void destroyObject(Object obj)
    throws Exception
  {
    if (obj == null) {
      return;
    }
    Connection client = (Connection)obj;
    client.close();
  }
  
  public Object makeObject()
    throws Exception
  {
    Class.forName(this.driver);
    Connection client = DriverManager.getConnection(this.url + "&user=" + this.user + "&password=" + this.password);
   return client;
  }
  

  public void passivateObject(Object arg0)
    throws Exception
  {}
  
  public boolean validateObject(Object obj)
  {
     boolean result = true;
    try {
      if (obj == null) {
        return false;
      }
      
      Connection client = (Connection)obj;
      result = (result) && (client.isValid(1));
     result = (result) && (!client.isClosed());
    }
    catch (Exception ex) {
     result = false;
    }
    return result;
  }
}
