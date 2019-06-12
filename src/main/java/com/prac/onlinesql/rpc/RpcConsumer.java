package com.prac.onlinesql.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: Administrator
 * @Date: 2019-06-12 15:40
 * @Description:
 */
public class RpcConsumer {

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

        channel.queueBind("rpc_queue", "rpc_exchange", "rpc");

        //消费队列消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                AMQP.BasicProperties reply = new AMQP.BasicProperties().builder()
                        .correlationId(properties.getCorrelationId())
                        .build();
                Integer integer = Integer.valueOf(new String(body));
                //计算的结果推入回调队列中 让客户端消费
                channel.basicPublish("", properties.getReplyTo(), reply, String.valueOf(integer * 10).getBytes());
            }
        };

        channel.basicConsume("rpc_queue", true, consumer);
    }
}
