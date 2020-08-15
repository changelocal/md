package com.arc.db.util;

import lombok.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:      田秀全
 * Mail:        tianxiuquan@kf.cn
 * Date:        13:22 12/10/2017
 * Version:     1.0
 * Description: 生成实体
 */
public class generateModel {

    private static String url = "jdbc:mysql://47.92.65.35:3306/md";
    private static String user = "root";
    private static String pwd = "tianxiuquan";
    private static String tableName = "brand_order"; //数据库表名

//    private static String url = "jdbc:mysql://rm-2ze483z2gdr20974fuo.mysql.rds.aliyuncs.com:3306/db_ytmd_prod";
//    private static String user = "mdlogo";
//    private static String pwd = "mdlogo123!";
//    private static String tableName = "service"; //数据库表名

    private static String parentPath = "E:\\jf\\md\\md-atom\\md-atom-user-core\\src\\main\\java\\com\\atom\\core\\";
    //model 层的 包名  包路径
    private static String packageModelName = "com.atom.core.model";
    private static String filePathModel = parentPath + "model\\";
    //dao 层的 包名  包路径
    private static String packageDaoName = "com.atom.core.dao";
    private static String filePathDao = parentPath + "dao\\";
    //转换类 model 转dto  的包名 包路径
//    private static String packageConvertName = "kf.kdb.admin.service.convert";
//    private static String packageNameDto = "kf.kdb.admin.service.dto";
//    private static String filePathConvert = parentPath + "convert\\";


    public static void main(String[] args) {
        Gmodel gmodel = new Gmodel();
        gmodel.cpro(url, user, pwd, tableName, packageModelName, filePathModel, packageDaoName, filePathDao);
    }


}


class Gmodel {

    public void cpro(String url, String user, String pwd, String tableName, String packageModelName, String filePathModel
            , String packageDaoName, String filePathDao) {
        List<ColumnTypes> cloumnList = queryColns(url, user, pwd, tableName);
        //生成实体类
        creatModel(cloumnList, filePathModel, packageModelName, tableName);
        //生成实体类查询参数
        creatModelParam(cloumnList, filePathModel, packageModelName, tableName);
        //生成dao
//        cdao(filePathDao, packageDaoName, tableName, packageModelName, cloumnList);
        cdaoimpl(filePathDao, packageDaoName, tableName, packageModelName, cloumnList);
    }


    //获取表信息
    private List<ColumnTypes> queryColns(String url, String user, String password, String tableName) {

        Class<Driver> driverClass = Driver.class;//装载类
        ArrayList<ColumnTypes> list = null;
        try {
            list = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (Statement ps = connection.createStatement()) {
                    try (ResultSet resultSet = ps.executeQuery(String.format("show full columns from `%s`", tableName))) {
                        while (resultSet.next()) {
                            ColumnTypes columnTypes = new ColumnTypes();
                            columnTypes.setField(resultSet.getString("Field"));
                            columnTypes.setType(resultSet.getString("Type"));
                            columnTypes.setAllowNull(resultSet.getString("Null"));
                            columnTypes.setKey(resultSet.getString("Key"));
                            columnTypes.setDefaultVal(resultSet.getString("Default"));
                            columnTypes.setExtra(resultSet.getString("Extra"));
                            columnTypes.setComment(resultSet.getString("Comment"));
                            list.add(columnTypes);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    //根据表信息生成 实体类文件
    private void creatModel(List<ColumnTypes> cloumnList, String filePath, String packgeName, String tableName) {
        File file = new File(filePath + tableName(tableName) + ".java");
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            StringBuilder sb = new StringBuilder();
            sb.append("package " + packgeName + ";\r\n\r\n");
            sb.append("import lombok.Data;\r\nimport com.arc.db.jsd.NameStyle;\r\nimport com.arc.db.jsd.annotation.JsdTable;");
            sb.append("\r\n\r\n");
            if (cloumnList.stream().filter(p -> p.getType().equals("timestamp")).count() > 0) {
                sb.append("import java.time.LocalDateTime;\r\n");
                sb.append("import java.util.Date;\r\n\r\n\r\n");
            }
            if (cloumnList.stream().filter(p -> p.getType().equals("datetime")).count() > 0) {
                sb.append("import java.time.LocalDateTime;\r\n");
                sb.append("import java.util.Date;\r\n\r\n\r\n");
            }

            sb.append(remarkClass("Created by xxx on 2020/07/25."));
            sb.append("@Data\r\n@JsdTable(nameStyle = NameStyle.LOWER)\r\n");
            sb.append("public class " + tableName(tableName) + " {");
            sb.append("\r\n");
            for (int i = 0; i < cloumnList.size(); i++) {
                sb.append("\t/**\r\n");
                sb.append("\t * " + cloumnList.get(i).getComment() + "\r\n");
                sb.append("\t */\r\n");
                sb.append("\tprivate " + fileType(cloumnList.get(i).getType()) + " " + fildName(cloumnList.get(i).getField()) + ";");
                sb.append("\r\n\r\n");
            }
            sb.append("}");

            out.write(sb.toString().getBytes());
            out.close();
        } catch (IOException e) {
            System.out.print(e.toString());
        }
    }

    //根据表信息生成 实体类查询文件
    private void creatModelParam(List<ColumnTypes> cloumnList, String filePath, String packgeName, String tableName) {
        String tableClassName = tableName(tableName);
        File file = new File(filePath + tableClassName + "Param.java");
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            StringBuilder sb = new StringBuilder();
            sb.append("package " + packgeName + ";\r\n\r\n");
            sb.append("import lombok.Data;\r\n");
            sb.append("\r\n\r\n");
            if (cloumnList.stream().filter(p -> p.getType().equals("timestamp")).count() > 0) {
                sb.append("import java.time.LocalDateTime;\r\n\r\n\r\n");
                sb.append("import java.util.Date;\r\n\r\n\r\n");
            }


            sb.append(remarkClass("Created by xxx on 2020/07/25."));
            sb.append("@Data\r\n");
            sb.append("public class " + tableClassName + "Param extends " + tableClassName + "{");
            sb.append("\r\n");
            /*for(int i=0;i<cloumnList.size();i++){
                sb.append("\t*//**\r\n");sb.append("\t * "+cloumnList.get(i).getComment()+"\r\n");sb.append("\t *//*\r\n");
                sb.append("\tprivate "+fileType(cloumnList.get(i).getType())+" "+fildName(cloumnList.get(i).getField())+";");
                sb.append("\r\n\r\n");
            }*/
            sb.append("\t/**\r\n");
            sb.append("\t * 当前页\r\n");
            sb.append("\t */\r\n");
            sb.append("\tprivate int pageIndex;\r\n");
            sb.append("\t/**\r\n");
            sb.append("\t * 每页显示条数\r\n");
            sb.append("\t */\r\n");
            sb.append("\tprivate int pageSize;\r\n");
            sb.append("}");

            out.write(sb.toString().getBytes());
            out.close();
        } catch (IOException e) {
            System.out.print(e.toString());
        }
    }

    //根据表信息生成 dao类文件
    public void cdao(String filePath, String packageName, String tableName, String modelPackage, List<ColumnTypes> cloumnList) {
        String tableClassName = tableName(tableName);
        String tableClassNameLower = tableNameLower(tableName);
        File file = new File(filePath + tableClassName + "Dao.java");
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            StringBuilder sb = new StringBuilder();
            sb.append("package " + packageName + ";\r\n\r\n");
            sb.append("import " + modelPackage + "." + tableClassName + ";\r\n");
            sb.append("import " + modelPackage + "." + tableClassName + "Param;\r\n");
            sb.append("import com.arc.util.data.PageResult;\r\n\r\n");
            sb.append("import java.util.List;");
            sb.append("\r\n\r\n");


            sb.append(remarkClass("Created by sage on 2017/12/7."));
            sb.append("public interface " + tableClassName + "Dao {\r\n\r\n");
            sb.append(remark("按条件分页查询"));
            sb.append("\tPageResult<" + tableClassName + "> query(" + tableClassName + "Param " + tableClassNameLower + "Param);\r\n\r\n");
            sb.append(remark("按条件查询全部"));
            sb.append("\tList<" + tableClassName + "> find(" + tableClassName + "Param " + tableClassNameLower + "Param);\r\n\r\n");
            if (cloumnList.stream().filter(p -> p.getKey().equals("PRI")).count() > 0) {
                ColumnTypes priKey = cloumnList.stream().filter(p -> p.getKey().equals("PRI")).findFirst().get();
                sb.append(remark("按主键查询"));
                sb.append("\t" + tableClassName + " get(" + fileType(priKey.getType()) + " id);\r\n\r\n");
                if (priKey.getExtra().equals("auto_increment")) {
                    sb.append(remark("添加实体"));
                    sb.append("\t" + fileType(priKey.getType()) + " add(" + tableClassName + " " + tableClassNameLower + ");\r\n\r\n");
                } else {
                    sb.append(remark("添加实体"));
                    sb.append("\tvoid add(" + tableClassName + " " + tableClassNameLower + ");\r\n\r\n");
                }
                sb.append(remark("修改实体"));
                sb.append("\t void update(" + tableClassName + " " + tableClassNameLower + ");\r\n\r\n");
            }
            sb.append("}");
            out.write(sb.toString().getBytes());
            out.close();
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    //生成dao类实现
    public void cdaoimpl(String filePath, String packageName, String tableName, String modelPackage, List<ColumnTypes> cloumnList) {
        String tableClassName = tableName(tableName);
        String tableClassNameLower = tableNameLower(tableName);
        File file = new File(filePath +  tableClassName + "Dao.java");
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            StringBuilder sb = new StringBuilder();
            sb.append("package " + packageName + ";\r\n\r\n");
            sb.append("import com.google.common.base.Strings;\r\n");
            sb.append("import " + packageName + ".BaseDao;\r\n");
            sb.append("import " + packageName + "." + tableClassName + "Dao;\r\n");
            sb.append("import " + modelPackage + "." + tableClassName + ";\r\n");
            sb.append("import " + modelPackage + "." + tableClassName + "Param;\r\n");
            sb.append("import org.springframework.stereotype.Repository;\r\n");
            sb.append("import com.arc.db.jsd.Filter;\r\n");
            sb.append("import com.arc.db.jsd.SortType;\r\n");
            sb.append("import com.arc.db.jsd.Sorters;\r\n");
            sb.append("import com.arc.db.jsd.UpdateValues;\r\n");
            sb.append("import com.arc.util.data.PageResult;\r\n\r\n");
            sb.append("import java.util.ArrayList;\r\n");
            sb.append("import java.util.List;\r\n\r\n");
            sb.append("import static com.arc.db.jsd.Shortcut.count;\r\n");
            sb.append("import static com.arc.db.jsd.Shortcut.f;\r\n");
            sb.append("import static com.arc.db.jsd.Shortcut.t;\r\n\r\n");

            sb.append(remarkClass("Created by md on 2020/07/25."));
            sb.append("@Repository\r\n");
            sb.append("public class " + tableClassName + "Dao extends BaseDao  {\r\n\r\n");

            //分页查询方法
//            sb.append("\t@Override\r\n");
            sb.append("\tpublic PageResult<" + tableClassName + "> query(" + tableClassName + "Param " + tableClassNameLower + "Param) {\r\n");
            sb.append("\t\tPageResult<" + tableClassName + "> result = new PageResult<>();\r\n");

            sb.append(condition(tableName, cloumnList));

            sb.append("\t\tSorters sorters = t(\"" + tableName + "\").sorters(SortType.ASC,\"" + cloumnList.get(0).getField() + "\");\r\n");
            sb.append("\t\tlong total=(long)this.DB().select(count()).from(\"" + tableName + "\").where(filter).result().value();\r\n");
            sb.append("\t\tif(total>0){\r\n");
            sb.append("\t\t\tList<" + tableClassName + "> list = DB().select(" + tableClassName + ".class).where(filter).orderBy(sorters)\r\n");
            sb.append("\t\t\t\t.limit((" + tableClassNameLower + "Param.getPageIndex()-1)*" + tableClassNameLower + "Param.getPageSize()," + tableClassNameLower + "Param.getPageSize())\r\n");
            sb.append("\t\t\t\t.result().all(" + tableClassName + ".class);\r\n");
            sb.append("\t\t\tresult.setItems(list);\r\n");
            sb.append("\t\t}\r\n");
            sb.append("\t\tresult.setTotalCount((int)total);\r\n");
            sb.append("\t\treturn result;\r\n");
            sb.append("\t}\r\n\r\n");

            //按条件查询全部
//            sb.append("\t@Override\r\n");
            sb.append("\tpublic List<" + tableClassName + "> find(" + tableClassName + "Param " + tableClassNameLower + "Param) {\r\n");
            sb.append(condition(tableClassNameLower, cloumnList));
            sb.append("\t\tList<" + tableClassName + "> list = DB().select(" + tableClassName + ".class)\r\n");
            sb.append("\t\t\t.where(filter).result().all(" + tableClassName + ".class);\r\n");
            sb.append("\t\treturn list==null?new ArrayList<>():list;\r\n");
            sb.append("\t}\r\n\r\n");

            //按主键查询方法
//            sb.append("\t@Override\r\n");
            sb.append("\tpublic " + tableClassName + " get(" + fileType(cloumnList.get(0).getType()) + " id) {\r\n");
            sb.append("\t\t" + tableClassName + " result = DB().select(" + tableClassName + ".class)\r\n");
            sb.append("\t\t\t.where(f(\"" + cloumnList.get(0).getField() + "\",id)).result().one(" + tableClassName + ".class);\r\n");
            sb.append("\t\treturn result;\r\n");
            sb.append("\t}\r\n\r\n");

            //添加方法
//            sb.append("\t@Override\r\n");
            if (cloumnList.stream().filter(p -> p.getKey().equals("PRI")).count() > 0) {
                ColumnTypes priKey = cloumnList.stream().filter(p -> p.getKey().equals("PRI")).findFirst().get();
                if (priKey.getExtra().equals("auto_increment")) {
                    sb.append("\tpublic " + fileType(priKey.getType()) + " add(" + tableClassName + " " + tableClassNameLower + ") {\r\n");
                    sb.append("\t\tlong id = (long) DB().insert(" + tableClassNameLower + ").result(true).getKeys().get(0);\r\n");
                    sb.append("\t\treturn (int)id;\r\n");
                } else {
                    sb.append("\tpublic void add(" + tableClassName + " " + tableClassNameLower + ") {\r\n");
                    sb.append("\t\tDB().insert(" + tableClassNameLower + ").result();\r\n");
                }
                sb.append("\t}\r\n\r\n");
            }

            //修改方法
//            sb.append("\t@Override\r\n");
            if (cloumnList.stream().filter(p -> p.getKey().equals("PRI")).count() > 0) {
                ColumnTypes priKey = cloumnList.stream().filter(p -> p.getKey().equals("PRI")).findFirst().get();
                sb.append("\tpublic void update(" + tableClassName + " " + tableClassNameLower + ") {\r\n");
                sb.append(updateCondition(tableClassNameLower, cloumnList));
                sb.append("\t\tDB().update(\"" + tableName + "\").set(updateValues).where(f(\"" + cloumnList.get(0).getField() + "\"," + tableClassNameLower + ".get" + fildNameUp(cloumnList.get(0).getField()) + "())).result();\r\n");
                sb.append("\t}\r\n\r\n");
            }

            sb.append("}");
            out.write(sb.toString().getBytes());
            out.close();
        } catch (Exception e) {

        }
    }


    private String tableNameLower(String tableName) {
        String[] part = tableName.split("_");
        String result = "";
        for (int i = 0; i < part.length; i++) {
            if (i == 0) {
                result += part[i];
            } else {
                result += part[i].substring(0, 1).toUpperCase() + part[i].substring(1);
            }

        }
        return result;
    }

    private String tableName(String tableName) {
        String[] part = tableName.split("_");
        String result = "";
        for (int i = 0; i < part.length; i++) {
            result += part[i].substring(0, 1).toUpperCase() + part[i].substring(1);
        }
        return result;
    }

    private String fildNameUp(String filedName) {
        String[] part = filedName.split("_");
        String result = "";
        for (int i = 0; i < part.length; i++) {
            if (i == 0) {
                result += part[i].substring(0, 1).toUpperCase() + part[i].substring(1);
            } else {
                result += part[i].substring(0, 1).toUpperCase() + part[i].substring(1);
            }
        }
        return result;
    }

    private String fildName(String filedName) {
        String[] part = filedName.split("_");
        String result = "";
        for (int i = 0; i < part.length; i++) {
            if (i == 0) {
                result += part[i].substring(0, 1).toLowerCase() + part[i].substring(1);
            } else {
                result += part[i].substring(0, 1).toUpperCase() + part[i].substring(1);
            }
        }
        return result;
    }

    private String fileType(String filedType) {
        String result = "";
        if (filedType.indexOf("bigint") == 0) {
            result = "long";
        } else if (filedType.indexOf("varchar") == 0) {
            result = "String";
        } else if (filedType.indexOf("int") == 0) {
            result = "int";
        } else if (filedType.indexOf("timestamp") == 0) {
            result = "Date";
            //result = "LocalDateTime";
        } else if (filedType.indexOf("smallint") == 0) {
            result = "int";
        } else if (filedType.indexOf("datetime") == 0) {
            result = "Date";
        } else if (filedType.indexOf("tinyint") == 0) {
            result = "int";
        }
        return result;
    }

    private String remark(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append("\t/**\r\n");
        sb.append("\t * " + text + "\r\n");
        sb.append("\t */\r\n");
        return sb.toString();
    }

    private String remarkClass(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append("/**\r\n");
        sb.append(" * " + text + "\r\n");
        sb.append(" */\r\n");
        return sb.toString();
    }

    private String condition(String tableName, List<ColumnTypes> cloumnList) {
        String tableClassNameLower = tableNameLower(tableName);
        StringBuilder sb = new StringBuilder();
        sb.append("\t\tFilter filter= Filter.create();\r\n");
        for (int i = 0; i < cloumnList.size(); i++) {
            String filedUpName = fildNameUp(cloumnList.get(i).getField());
            if (cloumnList.get(i).getType().indexOf("bigint") == 0 || cloumnList.get(i).getType().indexOf("int") == 0) {
                sb.append("\t\tif(" + tableClassNameLower + "Param.get" + filedUpName + "()>0){\r\n");
                sb.append("\t\t\tfilter=filter.and(f(\"" + cloumnList.get(i).getField() + "\"," + tableClassNameLower + "Param.get" + filedUpName + "()));\r\n");
                sb.append("\t\t}\r\n");
            } else if (cloumnList.get(i).getType().indexOf("varchar(") == 0) {
                sb.append("\t\tif(!Strings.isNullOrEmpty(" + tableClassNameLower + "Param.get" + fildNameUp(cloumnList.get(i).getField()) + "())){\r\n");
                sb.append("\t\t\tfilter=filter.and(f(\"" + cloumnList.get(i).getField() + "\"," + tableClassNameLower + "Param.get" + filedUpName + "()));\r\n");
                sb.append("\t\t}\r\n");
            } else {
                //sb.append("if(");
            }
        }
        return sb.toString();
    }

    private String updateCondition(String tableName, List<ColumnTypes> cloumnList) {
        String tableClassNameLower = tableNameLower(tableName);
        StringBuilder sb = new StringBuilder();
        sb.append("\t\tUpdateValues updateValues = new UpdateValues();\r\n");
        for (int i = 0; i < cloumnList.size(); i++) {
            String filedUpName = fildNameUp(cloumnList.get(i).getField());
            if (cloumnList.get(i).getType().indexOf("bigint") == 0 || cloumnList.get(i).getType().indexOf("int") == 0) {
                sb.append("\t\tif(" + tableClassNameLower + ".get" + filedUpName + "()>0){\r\n");
                sb.append("\t\t\tupdateValues.add(\"" + cloumnList.get(i).getField() + "\"," + tableClassNameLower + ".get" + filedUpName + "());\r\n");
                sb.append("\t\t}\r\n");
            } else if (cloumnList.get(i).getType().indexOf("varchar(") == 0) {
                sb.append("\t\tif(!Strings.isNullOrEmpty(" + tableClassNameLower + ".get" + fildNameUp(cloumnList.get(i).getField()) + "())){\r\n");
                sb.append("\t\t\tupdateValues.add(\"" + cloumnList.get(i).getField() + "\"," + tableClassNameLower + ".get" + filedUpName + "());\r\n");
                sb.append("\t\t}\r\n");
            } else {
                //sb.append("if(");
            }
        }
        return sb.toString();
    }


}

@Data
class ColumnTypes {
    private String field;//字段
    private String type;//类型
    private String allowNull;//允许空？
    private String key;//主键
    private String defaultVal;//默认值
    private String extra;//扩展描述
    private String comment;//字段描述
}


