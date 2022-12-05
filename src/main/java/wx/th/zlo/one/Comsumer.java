package wx.th.zlo.one;

import com.rabbitmq.client.*;
import wx.th.zlo.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: Comsumer
 * Description: TODO
 * Author: zlo
 * Date: 2022/12/3 11:44
 * Version: 1.0.0
 */
public class Comsumer {
    private final  static  String QUEUE_NAME = "PRODUCION";

    public static void main(String[] args) throws Exception {
         Channel channel = RabbitMqUtils.getChannel();
            DeliverCallback deliverCallback =(deliverTag,delivery)->{
                String s1 = new String(delivery.getBody());
                System.out.println(s1);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            };
            CancelCallback cancelCallback =(deliverTag)->{
                System.out.printf("消息被中断");
            };
            /**
             * 消费者消费消息
             * 1.消费哪个队列
             * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
             * 3.消费者未成功消费的回调
             */
        System.out.println("c1正在等待");
            channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);


        
        
    }
}
