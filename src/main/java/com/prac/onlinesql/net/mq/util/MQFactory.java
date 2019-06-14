package com.prac.onlinesql.net.mq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: Administrator
 * @Date: 2019-06-14 17:55
 * @Description:
 */
public class MQFactory {

    public static Connection createMqConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("39.105.108.154");
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setPort(5672);

        //创建连接
        Connection connection = factory.newConnection();
        return connection;
    }
}
