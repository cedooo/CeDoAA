package top.eeeqxxtg;

import cn.org.rapid_framework.generator2.provider.db.table.model.Table;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Table t = new Table();

        //printBeanDescprtions(t.getClass());


        String tR = ".*:((.*);{0,1})+";
        System.out.println("平台类型:1(IOS);2(安卓);3(H5)".matches(tR));
    }

    private static void printCon(){
        Properties p = System.getProperties();
        p.list(System.out);
        System.out.println("=============");
        Map<String,String> me = System.getenv();
        for (String key:
                me.keySet()) {
            System.out.println(key + " = " + me.get(key));
        }
    }

    public static  void printBeanDescprtions(Class clazz){

        //PropertyDescriptor[] pda = BeanHelper.getPropertyDescriptors(t.getClass());
        PropertyDescriptor[] pda = null;
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            pda = (new PropertyDescriptor[0]);
        }
        pda = beanInfo.getPropertyDescriptors();
        if (pda == null) {
            pda = new PropertyDescriptor[0];
        }


        for (int i = 0; i < pda.length; i++) {
            PropertyDescriptor pd = pda[i];
            System.out.println(pd.getName());
        }

    }

}
