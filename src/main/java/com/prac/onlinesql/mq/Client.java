package com.prac.onlinesql.mq;

import mq.consumer.DataConsumer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

/**
 * ¿Í»§¶Ë
 *
 * @author ly
 * @create 2019-02-07 11:18
 **/
public class Client {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, TimeoutException, IOException {
        DataConsumer consumer = new DataConsumer();
        consumer.consumer();
    }
}
