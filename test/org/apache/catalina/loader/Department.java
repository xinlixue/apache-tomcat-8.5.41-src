package org.apache.catalina.loader;

import lombok.Data;

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
@Data
public class Department {


  private String name;
  private String code;
  private Map<String, String> extension = new HashMap<>();
  private List<User> users = new ArrayList<>();


  public void addUser(User user) {
    this.users.add(user);
  }

  public void putExtension(String name, String value) {
    this.extension.put(name, value);
  }

}
