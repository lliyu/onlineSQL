package com.prac.onlinesql;

import com.prac.onlinesql.mq.entity.AcademicWorksEntity;
import com.prac.onlinesql.qo.DBsQO;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.springframework.util.ObjectUtils;
import org.xml.sax.EntityResolver;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 对象装换成对应的xml格式表示
 *
 * @author ly
 * @create 2019-04-06 15:04
 **/
public class ObjectToXml {

    public static String generatorXml(Object source) throws IllegalAccessException, IOException, DocumentException {

        StringBuilder sb = new StringBuilder();
        sb.append("<data>\n" +
                "<operation>insert</operation>\n");
        sb.append("<sourceTable>academic_works</sourceTable>");

        Field[] fields = source.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            if(!ObjectUtils.isEmpty(field.get(source))){
                sb.append("<field>\n");
                sb.append("<name>" + field.getName() + "</name>\n");
                sb.append("<type>" + field.getType().getName() + "</type>\n");
                sb.append("<value>" + field.get(source) + "</value>\n");
                sb.append("</field>\n");
            }
        }
        sb.append("</data>\n");
        Document document = DocumentHelper.parseText(sb.toString());
        Iterator<Element> opertation = document.getRootElement().elementIterator();
        while (opertation.hasNext()){
            Element next = opertation.next();
            System.out.println(next.getStringValue());
        }

        return "";
    }

    public static void main(String[] args) throws IllegalAccessException, IOException, DocumentException {
        DBsQO dBsQO = new DBsQO();
        dBsQO.setTableName("test");
        AcademicWorksEntity worksEntity = new AcademicWorksEntity();
        worksEntity.setAuthor("liyu");
        worksEntity.setClickNum(1002);
        worksEntity.setUserId(10000l);
        worksEntity.setAccessUrl("http://www.baidu.com");
        ObjectToXml xml = new ObjectToXml();
        xml.generatorXml(worksEntity);
    }
}
