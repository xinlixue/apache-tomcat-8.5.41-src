/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.catalina.startup;


import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.RuleSetBase;


/**
 * <p><strong>RuleSet</strong> for processing the contents of a
 * Context definition element.</p>
 *
 * @author Craig R. McClanahan
 */
@SuppressWarnings("deprecation")
public class ContextRuleSet extends RuleSetBase {


    // ----------------------------------------------------- Instance Variables


    /**
     * The matching pattern prefix to use for recognizing our elements.
     */
    protected final String prefix;


    /**
     * Should the context be created.
     */
    protected final boolean create;


    // ------------------------------------------------------------ Constructor


    /**
     * Construct an instance of this <code>RuleSet</code> with the default
     * matching pattern prefix.
     */
    public ContextRuleSet() {
        this("");
    }


    /**
     * Construct an instance of this <code>RuleSet</code> with the specified
     * matching pattern prefix.
     *
     * @param prefix Prefix for matching pattern rules (including the
     *  trailing slash character)
     */
    public ContextRuleSet(String prefix) {
        this(prefix, true);
    }


    /**
     * Construct an instance of this <code>RuleSet</code> with the specified
     * matching pattern prefix.
     *
     * @param prefix Prefix for matching pattern rules (including the
     *  trailing slash character)
     * @param create <code>true</code> if the main context instance should be
     *  created
     */
    public ContextRuleSet(String prefix, boolean create) {
        this.prefix = prefix;
        this.create = create;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Add the set of Rule instances defined in this RuleSet to the
     * specified <code>Digester</code> instance, associating them with
     * our namespace URI (if any).  This method should only be called
     * by a Digester instance.</p>
     *
     * Context的配置有多处，大多数情况不需要在server.xml中配置Context，由HostConfig自动扫描部署目录,以context.xml文件
     * 为基础进行解析创建，如果使用IDE工具启动Tomcat部署web应用，其Context配置会动态更新到server.xml中
     *
     * @param digester Digester instance to which the new Rule instances
     *  should be added.
     */
    @Override
    public void addRuleInstances(Digester digester) {
        /**
         *  通过server.xml配置Context时,create为true,因此需要创建Context实例
         *  通过HostConfig自动创建Context时,create为false，此时仅需要解析子节点接口
         */
        if (create) {
            digester.addObjectCreate(prefix + "Context",
                    "org.apache.catalina.core.StandardContext", "className");
            digester.addSetProperties(prefix + "Context");
        } else {
            digester.addRule(prefix + "Context", new SetContextPropertiesRule());
        }

        if (create) {
            digester.addRule(prefix + "Context",
                             new LifecycleListenerRule
                                 ("org.apache.catalina.startup.ContextConfig",
                                  "configClass"));
            digester.addSetNext(prefix + "Context",
                                "addChild",
                                "org.apache.catalina.Container");
        }
        //为Context添加生命周期监听器
        digester.addObjectCreate(prefix + "Context/Listener",
                                 null, // MUST be specified in the element
                                 "className");
        digester.addSetProperties(prefix + "Context/Listener");
        digester.addSetNext(prefix + "Context/Listener",
                            "addLifecycleListener",
                            "org.apache.catalina.LifecycleListener");
        //为Context指定类加载器
        digester.addObjectCreate(prefix + "Context/Loader",
                            "org.apache.catalina.loader.WebappLoader",
                            "className");
        digester.addSetProperties(prefix + "Context/Loader");
        digester.addSetNext(prefix + "Context/Loader",
                            "setLoader",
                            "org.apache.catalina.Loader");
        //为Context添加会话管理器
        digester.addObjectCreate(prefix + "Context/Manager",
                                 "org.apache.catalina.session.StandardManager",
                                 "className");
        digester.addSetProperties(prefix + "Context/Manager");
        digester.addSetNext(prefix + "Context/Manager",
                            "setManager",
                            "org.apache.catalina.Manager");

        digester.addObjectCreate(prefix + "Context/Manager/Store",
                                 null, // MUST be specified in the element
                                 "className");
        digester.addSetProperties(prefix + "Context/Manager/Store");
        digester.addSetNext(prefix + "Context/Manager/Store",
                            "setStore",
                            "org.apache.catalina.Store");

        digester.addObjectCreate(prefix + "Context/Manager/SessionIdGenerator",
                                 "org.apache.catalina.util.StandardSessionIdGenerator",
                                 "className");
        digester.addSetProperties(prefix + "Context/Manager/SessionIdGenerator");
        digester.addSetNext(prefix + "Context/Manager/SessionIdGenerator",
                            "setSessionIdGenerator",
                            "org.apache.catalina.SessionIdGenerator");
        //为Context添加初始化参数
        digester.addObjectCreate(prefix + "Context/Parameter",
                                 "org.apache.tomcat.util.descriptor.web.ApplicationParameter");
        digester.addSetProperties(prefix + "Context/Parameter");
        digester.addSetNext(prefix + "Context/Parameter",
                            "addApplicationParameter",
                            "org.apache.tomcat.util.descriptor.web.ApplicationParameter");

        digester.addRuleSet(new RealmRuleSet(prefix + "Context/"));
        //为Context添加安全配置以及Web资源配置
        digester.addObjectCreate(prefix + "Context/Resources",
                                 "org.apache.catalina.webresources.StandardRoot",
                                 "className");
        digester.addSetProperties(prefix + "Context/Resources");
        digester.addSetNext(prefix + "Context/Resources",
                            "setResources",
                            "org.apache.catalina.WebResourceRoot");

        digester.addObjectCreate(prefix + "Context/Resources/PreResources",
                                 null, // MUST be specified in the element
                                 "className");
        digester.addSetProperties(prefix + "Context/Resources/PreResources");
        digester.addSetNext(prefix + "Context/Resources/PreResources",
                            "addPreResources",
                            "org.apache.catalina.WebResourceSet");

        digester.addObjectCreate(prefix + "Context/Resources/JarResources",
                                 null, // MUST be specified in the element
                                 "className");
        digester.addSetProperties(prefix + "Context/Resources/JarResources");
        digester.addSetNext(prefix + "Context/Resources/JarResources",
                            "addJarResources",
                            "org.apache.catalina.WebResourceSet");

        digester.addObjectCreate(prefix + "Context/Resources/PostResources",
                                 null, // MUST be specified in the element
                                 "className");
        digester.addSetProperties(prefix + "Context/Resources/PostResources");
        digester.addSetNext(prefix + "Context/Resources/PostResources",
                            "addPostResources",
                            "org.apache.catalina.WebResourceSet");

        //为Context添加资源链接
        digester.addObjectCreate(prefix + "Context/ResourceLink",
                "org.apache.tomcat.util.descriptor.web.ContextResourceLink");
        digester.addSetProperties(prefix + "Context/ResourceLink");
        digester.addRule(prefix + "Context/ResourceLink",
                new SetNextNamingRule("addResourceLink",
                        "org.apache.tomcat.util.descriptor.web.ContextResourceLink"));
        //为Context添加Valve
        digester.addObjectCreate(prefix + "Context/Valve",
                                 null, // MUST be specified in the element
                                 "className");
        digester.addSetProperties(prefix + "Context/Valve");
        digester.addSetNext(prefix + "Context/Valve",
                            "addValve",
                            "org.apache.catalina.Valve");
        /**
         * 为Context添加守护资源配置
         * WatchedResource: 用于为Context添加监视资源，当这些资源发生变更时，这些应用将重新加载，具体见conf/context.xml
         */
        digester.addCallMethod(prefix + "Context/WatchedResource",
                               "addWatchedResource", 0);

        digester.addCallMethod(prefix + "Context/WrapperLifecycle",
                               "addWrapperLifecycle", 0);

        digester.addCallMethod(prefix + "Context/WrapperListener",
                               "addWrapperListener", 0);

        digester.addObjectCreate(prefix + "Context/JarScanner",
                                 "org.apache.tomcat.util.scan.StandardJarScanner",
                                 "className");
        digester.addSetProperties(prefix + "Context/JarScanner");
        digester.addSetNext(prefix + "Context/JarScanner",
                            "setJarScanner",
                            "org.apache.tomcat.JarScanner");

        digester.addObjectCreate(prefix + "Context/JarScanner/JarScanFilter",
                                 "org.apache.tomcat.util.scan.StandardJarScanFilter",
                                 "className");
        digester.addSetProperties(prefix + "Context/JarScanner/JarScanFilter");
        digester.addSetNext(prefix + "Context/JarScanner/JarScanFilter",
                            "setJarScanFilter",
                            "org.apache.tomcat.JarScanFilter");

        digester.addObjectCreate(prefix + "Context/CookieProcessor",
                                 "org.apache.tomcat.util.http.Rfc6265CookieProcessor",
                                 "className");
        digester.addSetProperties(prefix + "Context/CookieProcessor");
        digester.addSetNext(prefix + "Context/CookieProcessor",
                            "setCookieProcessor",
                            "org.apache.tomcat.util.http.CookieProcessor");
    }

}
