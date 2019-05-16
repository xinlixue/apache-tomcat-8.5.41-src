package org.apache.catalina.loader;

import org.apache.tomcat.util.digester.Digester;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

/**
 * @program: apache-tomcat-8.5.41-src
 * @description: Xml解析测试
 * @author: Mr.Feng
 * @create: 2019-05-17 00:59
 **/
public class TestDigester {


  @Test
  public void test01() {
    Digester digester = new Digester();
    digester.setValidating(false);
    digester.setRulesValidation(true);
    //匹配节点，创建对象
    digester.addObjectCreate("department", Department.class.getName());
    //匹配到department,设置对象属性
    digester.addSetProperties("department");
    digester.addObjectCreate("department/user", User.class.getName());
    digester.addSetProperties("department/user");
    //匹配到department/user，调用Department对象的addUser方法
    digester.addSetNext("department/user", "addUser", User.class.getName());
    //匹配到department/extension，调用Department对象的putExtension方法
    digester.addCallMethod("department/extension", "putExtension", 2);
    //调用方法的第一个参数为节点propert-name
    digester.addCallParam("department/extension/property-name", 0);
    digester.addCallParam("department/extension/property-value", 1);

    try {
      Department department = (Department) digester.parse(new File("G:\\JAVA_WORK\\apache-tomcat-8.5.41-src\\test\\org\\apache\\catalina\\loader\\test.xml"));
      System.out.println(department);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }
  }

}
