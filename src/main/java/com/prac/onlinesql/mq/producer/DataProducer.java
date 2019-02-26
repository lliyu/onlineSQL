package com.prac.onlinesql.mq.producer;

import com.prac.onlinesql.mq.db.DBData;
import com.prac.onlinesql.mq.db.RemoteDBOperation;
import com.prac.onlinesql.mq.entity.AcademicWorksEntity;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 推送mysql中的数据到mq中
 *
 * @author ly
 * @create 2019-02-06 22:58
 **/
public class DataProducer {

    private static String DATA_SYNC_EXCHANGE = "data_sync_exchange";
    private static String ROUTE_KEY = "data";

    public void syncData(String tableName) throws IOException, TimeoutException, SQLException, ClassNotFoundException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("39.105.108.154");
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setPort(5672);

        //创建连接
        Connection connection = factory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();
        //由消费者来创建交换机和队列  因为一般是先打开消费者
        channel.exchangeDeclare(DATA_SYNC_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare("data_queue", false, false, true, null);

        channel.queueBind("data_queue", DATA_SYNC_EXCHANGE, ROUTE_KEY);

        channel.confirmSelect();

        //判断数据表是否存在，不存在则将数据结构进行同步
        if(!RemoteDBOperation.isTableExist(tableName)){
            channel.queueDeclare("data_structure_queue", false, false, true, null);

            channel.queueBind("data_structure_queue", DATA_SYNC_EXCHANGE, "structure");
            String tableSQL = DBData.getCreateTableSQL(tableName);
            channel.basicPublish(DATA_SYNC_EXCHANGE, "structure", null, tableSQL.getBytes("utf-8"));
        }

        //推送数据
        List<AcademicWorksEntity> data = DBData.getData(tableName);
        data.stream().forEach(entity -> {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(stream);
                oos.writeObject(entity);
                byte[] bytes = stream.toByteArray();


                channel.basicPublish(DATA_SYNC_EXCHANGE, ROUTE_KEY, null, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        channel.close();
        connection.close();

    }


}
