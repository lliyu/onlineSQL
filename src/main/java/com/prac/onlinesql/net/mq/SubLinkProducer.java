package com.prac.onlinesql.net.mq;

import com.prac.onlinesql.net.mq.entity.RootPageEntity;
import com.prac.onlinesql.net.mq.util.MQConstant;
import com.prac.onlinesql.net.mq.util.MQFactory;
import com.prac.onlinesql.net.mq.entity.SubLinkPageInfo;
import com.prac.onlinesql.net.youguo.sublink.ParseSubLink;
import com.prac.onlinesql.net.youguo.utils.Constant;
import com.prac.onlinesql.net.youguo.utils.URLUtils;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 子页面解析mq版
 *
 * @author ly
 * @create 2019-06-14 20:24
 **/
public class SubLinkProducer {

    private final static Logger log = LoggerFactory.getLogger(SubLinkProducer.class);
    private static List<String> skipLinks = new ArrayList<>();

    static {
        skipLinks.add("糖果画报");
        skipLinks.add("爱丝");
        skipLinks.add("魅妍社");
        skipLinks.add("首页");
    }

    public SubLinkProducer() throws IOException, TimeoutException {
    }

    public static void main(String[] args) throws IOException, TimeoutException, URISyntaxException {
        SubLinkProducer linkProducer = new SubLinkProducer();
        linkProducer.consumerRootLink();
//        Connection mqConnection = MQFactory.createMqConnection();
//        Channel channel = mqConnection.createChannel();
//        RootPageEntity pageEntity = new RootPageEntity();
//        pageEntity.setName("薄荷叶a href=\"/albums/XiuRen.html\" \ttitle=");
//        pageEntity.setPath("/albums/MintYe.html");
//        pushSubLink(pageEntity, channel);
//        System.out.println("2222");
    }

    public void consumerRootLink() throws IOException, TimeoutException {
        Connection mqConnection = MQFactory.createMqConnection();
        Channel channel = mqConnection.createChannel();
        channel.exchangeDeclare(MQConstant.ROOT_LINK_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(MQConstant.ROOT_LINK_QUEUE, false, false, false, null);
        channel.queueBind(MQConstant.ROOT_LINK_QUEUE, MQConstant.ROOT_LINK_EXCHANGE, MQConstant.ROOT_LINK_ROUTERKEY);

        RootPageEntity pageEntity = null;
        //从mq取出一条根链接
        DefaultConsumer rootLinkConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(body);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    RootPageEntity pageEntity = (RootPageEntity) ois.readObject();
                    if(skipLinks.contains(pageEntity.getName())){
                        System.out.println("skip " + pageEntity.getName());
                        return;
                    }
                    System.out.println(MQConstant.ROOT_LINK_QUEUE + "剩余消息数：" + channel.messageCount(MQConstant.ROOT_LINK_QUEUE));
                    System.out.println("consumer:sub link - " + pageEntity.getName());
                    //赞助�><a href="/albums/IShow.html" 	title="爱秀
                    pushSubLink(pageEntity, channel);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        channel.basicConsume(MQConstant.ROOT_LINK_QUEUE, false, rootLinkConsumer);
    }

    private static void pushSubLink(RootPageEntity pageEntity, Channel channel) throws IOException {
        ParseSubLink subLink = new ParseSubLink();
        File file = null;
        try {
            String html = URLUtils.readUrl(Constant.SOURCEURL + pageEntity.getPath());
            int count = subLink.parseDetailPageCount(html);
            channel.exchangeDeclare(MQConstant.SUB_LINK_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(MQConstant.SUB_LINK_QUEUE, false, false, false, null);
            channel.queueBind(MQConstant.SUB_LINK_QUEUE, MQConstant.SUB_LINK_EXCHANGE, MQConstant.SUB_LINK_ROUTERKEY);

            File dirc = new File("G://html/" + pageEntity.getName());
            if(!dirc.exists())
                dirc.mkdirs();
            file = new File("G://html/" + pageEntity.getName() + "/" + pageEntity.getName() +".txt");
            if(!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, true);

            for (int i = 1; i <= count; i++) {
                html = URLUtils.readUrl(URLUtils.parseURL(Constant.SOURCEURL + pageEntity.getPath(), i));
                ArrayList<SubLinkPageInfo> pageInfos = subLink.parseHtmlToPage(html);
                pageInfos.stream().forEach(pageInfo -> {
                    try {
                        pageInfo.setRootPath(pageEntity.getPath());
                        pageInfo.setRootName(pageEntity.getName());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(pageInfo);
                        byte[] bytes = baos.toByteArray();
                        fos.write((pageInfo.getUri() + "\r\n").getBytes());
                        channel.basicPublish(MQConstant.SUB_LINK_EXCHANGE, MQConstant.SUB_LINK_ROUTERKEY, null, bytes);

                        log.info("sub link:" + pageInfo.getName() + "-" +pageInfo.getRootPath() +  pageInfo.getUri() + " push finished");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            fos.close();
        } catch (IOException e) {
            System.out.println("create " + file.getName() + " error!");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
