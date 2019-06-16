package com.prac.onlinesql.net.mq;

import com.prac.onlinesql.net.mq.entity.DetailPicEntity;
import com.prac.onlinesql.net.mq.entity.SubLinkPageInfo;
import com.prac.onlinesql.net.mq.util.MQConstant;
import com.prac.onlinesql.net.mq.util.MQFactory;
import com.prac.onlinesql.net.youguo.detail.ParsePicDetail;
import com.rabbitmq.client.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * 图片下载consumer
 *
 * @author ly
 * @create 2019-06-14 22:02
 **/
public class PicDownloadConsumer {

    private static Connection mqConnection = null;
    private static Channel channel = null;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    PicDownloadConsumer picDownloadConsumer = new PicDownloadConsumer();
                    picDownloadConsumer.consumerPic();
                }
            });
        }
    }

    public void consumerPic() {
        try {
            mqConnection = MQFactory.createMqConnection();
            channel = mqConnection.createChannel();
            channel.exchangeDeclare(MQConstant.DETAIL_LINK_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(MQConstant.DETAIL_LINK_QUEUE, false, false, false, null);
            channel.queueBind(MQConstant.DETAIL_LINK_QUEUE, MQConstant.DETAIL_LINK_EXCHANGE, MQConstant.DETAIL_LINK_ROUTERKEY);
            channel.basicConsume(MQConstant.DETAIL_LINK_QUEUE, false, picLinkConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void download(DetailPicEntity detailPicEntity) {
        ParsePicDetail parsePicDetail = new ParsePicDetail();
        parsePicDetail.downloadPic(detailPicEntity.getUri(), detailPicEntity.getSubPath(), "G://file//"+detailPicEntity.getRootName());
    }

    private static DefaultConsumer picLinkConsumer = new DefaultConsumer(channel) {
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            try {
//                System.out.println(MQConstant.DETAIL_LINK_QUEUE + "剩余消息数：" + channel.messageCount(MQConstant.DETAIL_LINK_QUEUE));
                ByteArrayInputStream bais = new ByteArrayInputStream(body);
                ObjectInputStream ois = new ObjectInputStream(bais);
                DetailPicEntity detailPicEntity = (DetailPicEntity) ois.readObject();
//                    System.out.println("consumer:detail link - " + detailPicEntity.getUri());
                download(detailPicEntity);
                channel.basicAck(envelope.getDeliveryTag(), false);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    };
}
