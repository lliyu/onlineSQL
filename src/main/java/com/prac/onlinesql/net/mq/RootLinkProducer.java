package com.prac.onlinesql.net.mq;

import com.prac.onlinesql.net.mq.entity.RootPageEntity;
import com.prac.onlinesql.net.mq.util.MQConstant;
import com.prac.onlinesql.net.mq.util.MQFactory;
import com.prac.onlinesql.net.youguo.navigat.ParseNavigator;
import com.prac.onlinesql.net.youguo.utils.Constant;
import com.prac.onlinesql.net.youguo.utils.FileUtils;
import com.prac.onlinesql.net.youguo.utils.URLUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
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

    private final static Logger log = LoggerFactory.getLogger(RootLinkProducer.class);

    public static void main(String[] args) throws IOException, TimeoutException, URISyntaxException {
        RootLinkProducer linkProducer = new RootLinkProducer();
        linkProducer.productRootLinkMessage();
    }

    private void productRootLinkMessage() throws IOException, TimeoutException, URISyntaxException {

        String html = URLUtils.readUrl(Constant.SOURCEURL);
//        FileUtils.readHtmlFromFile("G://html//img.txt");
        ParseNavigator navigator = new ParseNavigator();
        Map<String, String> links = navigator.parseNavigatorLink(html);
        ArrayList<RootPageEntity> pages = convertToEntity(links);
        Connection mqConnection = MQFactory.createMqConnection();
        Channel channel = mqConnection.createChannel();
        channel.exchangeDeclare(MQConstant.ROOT_LINK_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(MQConstant.ROOT_LINK_QUEUE, false, false, false, null);
        channel.queueBind(MQConstant.ROOT_LINK_QUEUE, MQConstant.ROOT_LINK_EXCHANGE, MQConstant.ROOT_LINK_ROUTERKEY);
//        channel.confirmSelect();//设置为confirm模式
        File file = new File("G://html/root.txt");
        if(!file.exists())
            file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file, true);

        pages.stream().forEach(page -> {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(page);
                byte[] bytes = baos.toByteArray();

                fos.write((page.getName() + ":" + page.getPath() + "\r\n").getBytes());
                channel.basicPublish(MQConstant.ROOT_LINK_EXCHANGE, MQConstant.ROOT_LINK_ROUTERKEY, null, bytes);
//                log.info("root link:" + page.getName() + "-" + page.getPath() + " push finished");
                System.out.println("root link:" + page.getName() + "-" + page.getPath() + " push finished");
                //                if(channel.waitForConfirms()){
//                    log.info(page.getName() + ":" + page.getPath() + " push finished");
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        fos.close();
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
