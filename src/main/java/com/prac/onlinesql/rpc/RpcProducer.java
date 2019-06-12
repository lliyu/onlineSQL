package com.prac.onlinesql.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: Administrator
 * @Date: 2019-06-12 15:43
 * @Description:
 */
public class RpcProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("39.105.108.154");
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setPort(5672);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("rpc_exchange", "direct");

        channel.queueDeclare("rpc_queue", false, false, true, null);
        //回调队列
        String queue = channel.queueDeclare().getQueue();

        channel.queueBind("rpc_queue", "rpc_exchange", "rpc");

        //做为回调标识  保证调用者得到的消息是自己发送参数对应的结果
        String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .correlationId(corrId)
                .replyTo(queue)
                .build();

        channel.basicPublish("rpc_exchange", "rpc", properties, "30".getBytes());

        //回调消息的消费
        DefaultConsumer replyConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                if(corrId.equals(properties.getCorrelationId()))
                    System.out.println(new String(body));
            }
        };

        channel.basicConsume(queue, true, replyConsumer);

    }

}
