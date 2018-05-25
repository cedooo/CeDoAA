#模板

- 模板使用freemarker作为解析引擎
- 模板默认属性包括java环境变量
- 除了配置文件中自定义的模板，针对不同的"生成模型"模板，还包含根据相关类生成扩展的property。

    例如： 
    利用Table生成代码时，其扩展property来自```cn.org.rapid_framework.generator.provider.db.table.model.Table```
    
    具体原理参考 
    ``` 
    java.beans.Introspector.getBeanInfo(Class<?> beanClass);
    java.beans.BeanInfo.getPropertyDescriptors();
    ```
