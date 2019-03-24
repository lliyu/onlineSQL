package com.prac.onlinesql.mq.consumer;

import com.prac.onlinesql.mq.db.RemoteDBOperation;
import com.prac.onlinesql.mq.entity.AcademicWorksEntity;
import com.prac.onlinesql.mq.jedis.JedisClientPool;
import com.rabbitmq.client.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

/**
 * 消费端
 *
 * @author ly
 * @create 2019-02-07 11:08
 **/
public class DataConsumer {
    private static String DATA_SYNC_EXCHANGE = "data_sync_exchange";
    private static String DATA_QUEUE = "data_queue";
    private static String DATA_STRUCTURE_QUEUE = "data_structure_queue";
    private static String DATA_ROUTE_KEY = "data";
    private static String STRUCTURE_ROUTE_KEY = "structure";

    private static JedisClientPool jedisClientPool = new JedisClientPool();

    public void consumer() throws IOException, TimeoutException, SQLException, ClassNotFoundException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("39.105.108.154");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        //创建连接
        Connection connection = factory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();
        //由消费者来创建交换机和队列  因为一般是先打开消费者
        //数据队列
        channel.exchangeDeclare(DATA_SYNC_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(DATA_QUEUE, false, false, true, null);

        channel.queueBind(DATA_QUEUE, DATA_SYNC_EXCHANGE, DATA_ROUTE_KEY);
        //结构队列
        channel.queueDeclare(DATA_STRUCTURE_QUEUE, false, false, true, null);

        channel.queueBind(DATA_STRUCTURE_QUEUE, DATA_SYNC_EXCHANGE, STRUCTURE_ROUTE_KEY);


        //判断数据表是否存在，不存在则将数据结构进行同步
        if(!RemoteDBOperation.isTableExist("academic_works")){
            DefaultConsumer consumerStructure = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        String message = new String(body, "utf-8");
                        RemoteDBOperation.createTable(message);
                        System.out.println(consumerTag);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            };
            channel.basicConsume(DATA_STRUCTURE_QUEUE, false, consumerStructure);
        }

        //消费数据
        if(RemoteDBOperation.isTableExist("academic_works")){
            DefaultConsumer consumerData = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                    ByteArrayInputStream stream = new ByteArrayInputStream(body);
                    ObjectInputStream ois = new ObjectInputStream(stream);
                    try {
                        AcademicWorksEntity academicWorksEntity = (AcademicWorksEntity) ois.readObject();
                        //使用类名+id作为全局唯一ID 保证消息不会重复消费
                        String key = academicWorksEntity.getClass().getName() + ":" + academicWorksEntity.getId();
                        if(jedisClientPool.get(key)==null){
                            RemoteDBOperation.insertData(academicWorksEntity);
                            jedisClientPool.set(key, String.valueOf(academicWorksEntity.getId()));
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            };
            channel.basicConsume(DATA_QUEUE, false, consumerData);
        }

    }
}
