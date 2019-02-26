package com.prac.onlinesql.mq;

import com.prac.onlinesql.mq.producer.DataProducer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

/**
 * @author ly
 * @create 2019-02-06 22:52
 **/
public class Server {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, TimeoutException {

        DataProducer producer = new DataProducer();
        producer.syncData("academic_works");
//        System.out.println(RemoteDBOperation.isTableExist("sys_config"));
    }
}
