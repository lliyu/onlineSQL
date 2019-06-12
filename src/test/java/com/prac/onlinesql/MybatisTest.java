package com.prac.onlinesql;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Auther: Administrator
 * @Date: 2019-03-27 09:15
 * @Description:
 */
public class MybatisTest {
    public static void main(String[] args) throws IOException {
        // 加载配置文件 并构建SqlSessionFactory对象
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        // 从SqlSessionFactory对象中获取 SqlSession对象
        SqlSession sqlSession = factory.openSession();
        // 执行操作
//        sqlSession.insert("insert", book);
        // 提交操作
        sqlSession.commit();
        // 关闭SqlSession
        sqlSession.close();
    }
}
