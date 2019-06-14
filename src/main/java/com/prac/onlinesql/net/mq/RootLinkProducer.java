package com.prac.onlinesql.net.mq;

import com.prac.onlinesql.net.mq.entity.RootPageEntity;
import com.prac.onlinesql.net.mq.util.MQFactory;
import com.prac.onlinesql.net.youguo.navigat.ParseNavigator;
import com.prac.onlinesql.net.youguo.utils.FileUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: Administrator
 * @Date: 2019-06-14 17:53
 * @Description: 根链接生产者 比如http://www.94img.com这种
 */
public class RootLinkProducer {

    private static final String ROOT_LINK_EXCHANGE = "root_link_exchange";
    private static final String ROOT_LINK_QUEUE = "root_link_queue";
    private static final String ROOT_LINK_ROUTERKEY = "root_link";

    public static void main(String[] args) throws IOException, TimeoutException {
        String html = FileUtils.readHtmlFromFile("G://html//img.txt");
        ParseNavigator navigator = new ParseNavigator();
        Map<String, String> links = navigator.parseNavigatorLink(html);
        ArrayList<RootPageEntity> pages = convertToEntity(links);
        Connection mqConnection = MQFactory.createMqConnection();
        Channel channel = mqConnection.createChannel();
        channel.exchangeDeclare(ROOT_LINK_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(ROOT_LINK_QUEUE, false, false, true, null);
        channel.queueBind(ROOT_LINK_QUEUE, ROOT_LINK_EXCHANGE, ROOT_LINK_ROUTERKEY);
        channel.confirmSelect();//设置为confirm模式

        pages.stream().forEach(page -> {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(page);
                byte[] bytes = baos.toByteArray();

                channel.basicPublish(ROOT_LINK_EXCHANGE, ROOT_LINK_ROUTERKEY, null, bytes);
                if(channel.waitForConfirms()){
                    
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static ArrayList<RootPageEntity> convertToEntity(Map<String, String> links) {
        Iterator<Map.Entry<String, String>> iterator = links.entrySet().iterator();
        ArrayList<RootPageEntity> pages = new ArrayList<>();
        while(iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            RootPageEntity entity = new RootPageEntity();
            entity.setName(next.getKey());
            entity.setPath(next.getValue());
            pages.add(entity);
        }
        return pages;
    }
}
