package com.prac.onlinesql.net.mq;

import com.prac.onlinesql.net.mq.entity.DetailPicEntity;
import com.prac.onlinesql.net.mq.entity.RootPageEntity;
import com.prac.onlinesql.net.mq.util.MQConstant;
import com.prac.onlinesql.net.mq.util.MQFactory;
import com.prac.onlinesql.net.youguo.detail.ParsePicDetail;
import com.prac.onlinesql.net.mq.entity.SubLinkPageInfo;
import com.prac.onlinesql.net.youguo.utils.Constant;
import com.prac.onlinesql.net.youguo.utils.RegexUtils;
import com.prac.onlinesql.net.youguo.utils.URLUtils;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * 详情页面解析mq
 *
 * @author ly
 * @create 2019-06-14 21:24
 **/
public class DetailsPageLinkProducer {

    private final static Logger log = LoggerFactory.getLogger(DetailsPageLinkProducer.class);
    private static Connection mqConnection = null;
    private static Channel channel = null;

    public static void main(String[] args) {
        DetailsPageLinkProducer linkProducer = new DetailsPageLinkProducer();
        linkProducer.consumerSubLink();
    }

    public void consumerSubLink() {
        try {
            mqConnection = MQFactory.createMqConnection();
            channel = mqConnection.createChannel();
            channel.exchangeDeclare(MQConstant.SUB_LINK_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(MQConstant.SUB_LINK_QUEUE, false, false, false, null);
            channel.queueBind(MQConstant.SUB_LINK_QUEUE, MQConstant.SUB_LINK_EXCHANGE, MQConstant.SUB_LINK_ROUTERKEY);
            channel.basicConsume(MQConstant.SUB_LINK_QUEUE, false, subLinkConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    //从mq取出一条根链接
    private static DefaultConsumer subLinkConsumer = new DefaultConsumer(channel) {
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(body);
                ObjectInputStream ois = new ObjectInputStream(bais);
                SubLinkPageInfo pageInfo = (SubLinkPageInfo) ois.readObject();
                System.out.println(MQConstant.SUB_LINK_QUEUE + "剩余消息数：" + channel.messageCount(MQConstant.SUB_LINK_QUEUE));
                System.out.println("consumer:detail link - " + pageInfo.getName());
                pushDetailLink(pageInfo, channel);
                channel.basicAck(envelope.getDeliveryTag(), false);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    };

    private static void pushDetailLink(SubLinkPageInfo pageInfo, Channel channel) throws URISyntaxException {
        ParsePicDetail detail = new ParsePicDetail();
        String url = Constant.SOURCEURL + pageInfo.getUri();
        String html = null;
        int count = 0;
        File file = null;
        try {
            html = URLUtils.readUrl(url);
            count = detail.parseDetailPageCount(html);
            channel.exchangeDeclare(MQConstant.DETAIL_LINK_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(MQConstant.DETAIL_LINK_QUEUE, false, false, false, null);
            channel.queueBind(MQConstant.DETAIL_LINK_QUEUE, MQConstant.DETAIL_LINK_EXCHANGE, MQConstant.DETAIL_LINK_ROUTERKEY);

            File dirc = new File("G://html/" + pageInfo.getRootName() + "/" + pageInfo.getName());
            if(!dirc.exists())
                dirc.mkdirs();
            file = new File("G://html/" + pageInfo.getRootName() + "/" + pageInfo.getName() + ".txt");
            if(!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, true);

            for (int i = 1; i <= count; i++) {
                ArrayList<String> imgs = detail.parseDetailPageHtml(URLUtils.readUrl(URLUtils.parseURL(url, i)));

                imgs.stream().filter(img -> RegexUtils.matchURL(img))
                        .forEach(img -> {
                            try {
                                DetailPicEntity entity = new DetailPicEntity();
                                entity.setRootPath(pageInfo.getRootPath());
                                entity.setSubPath(pageInfo.getName());
                                entity.setRootName(pageInfo.getRootName());
                                entity.setUri(img);

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ObjectOutputStream oos = new ObjectOutputStream(baos);
                                oos.writeObject(entity);
                                byte[] bytes = baos.toByteArray();

                                fos.write((img + "\r\n").getBytes());

                                channel.basicPublish(MQConstant.DETAIL_LINK_EXCHANGE, MQConstant.DETAIL_LINK_ROUTERKEY, null, bytes);
                                log.info("detail link:" + entity.getRootPath()+ "-" + entity.getSubPath() + "-" + entity.getUri() + " push finished");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            }

            fos.close();
        } catch (IOException e) {
            System.out.println("create " + file.getName() + " error!");
            e.printStackTrace();
        }
    }
}
