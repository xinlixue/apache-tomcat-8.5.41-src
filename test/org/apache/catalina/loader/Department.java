package org.apache.catalina.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: apache-tomcat-8.5.41-src
 * @description: 测试
 * @author: Mr.Feng
 * @create: 2019-05-16 23:34
 **/
public class Department {


  private String name;
  private String code;
  private Map<String, String> extension = new HashMap<>();
  private List<User> users = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Map<String, String> getExtension() {
    return extension;
  }

  public void setExtension(Map<String, String> extension) {
    this.extension = extension;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }
}
