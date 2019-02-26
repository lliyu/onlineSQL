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
 * ����mysql�е����ݵ�mq��
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

        //��������
        Connection connection = factory.newConnection();
        //�����ŵ�
        Channel channel = connection.createChannel();
        //���������������������Ͷ���  ��Ϊһ�����ȴ�������
        channel.exchangeDeclare(DATA_SYNC_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare("data_queue", false, false, true, null);

        channel.queueBind("data_queue", DATA_SYNC_EXCHANGE, ROUTE_KEY);

        channel.confirmSelect();

        //�ж����ݱ��Ƿ���ڣ������������ݽṹ����ͬ��
        if(!RemoteDBOperation.isTableExist(tableName)){
            channel.queueDeclare("data_structure_queue", false, false, true, null);

            channel.queueBind("data_structure_queue", DATA_SYNC_EXCHANGE, "structure");
            String tableSQL = DBData.getCreateTableSQL(tableName);
            channel.basicPublish(DATA_SYNC_EXCHANGE, "structure", null, tableSQL.getBytes("utf-8"));
        }

        //��������
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
