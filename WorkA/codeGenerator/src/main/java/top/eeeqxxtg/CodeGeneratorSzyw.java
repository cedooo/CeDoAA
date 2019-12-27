package top.eeeqxxtg;

import cn.org.rapid_framework.generator.GeneratorFacade;

/**
 * @author cedoo cedoo(@)qq.com
 */
public class CodeGeneratorSzyw {

    public static void main(String[] args) throws Exception {

        GeneratorFacade g = new GeneratorFacade();
        //模板路径
        String templatePath = "classpath:/generator/template/com.fjszyw.gamesdk";
        g.getGenerator().addTemplateRootDir(templatePath);
        //删除生成器的输出目录//
        g.deleteOutRootDir();
        //通过数据库表生成文件

    /*    g.generateByTable(
                "tbl_sdk_device_whitelist",
                "tbl_sdk_game_certification",
                "tbl_sdk_game_customer_service_qa",
                "tbl_sdk_game_notice",
                "tbl_sdk_game_gift",
                "tbl_sdk_game_gift_seria_no",
                "tbl_sdk_game_gift_getter");*/

        g.generateByTable(
                "tbl_sdk_device_whitelist");

//        自动搜索数据库中的所有表并生成文件,template为模板的根目录
//        g.generateByAllTable();
//        按table名字删除文件
//        g.deleteByTable("table_name", "template");
    }

}
