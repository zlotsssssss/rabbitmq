package wx.th.zlo.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: Comsumer
 * Description: TODO
 * Author: zlo
 * Date: 2022/12/3 11:44
 * Version: 1.0.0
 */
public class Comsumer1 {
    private final  static  String QUEUE_NAME = "PRODUCION";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");
        
//        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()){
//            DeliverCallback deliverCallback =(consumerTag,delivery)->{
//                String s1 = new String(delivery.getBody());
//                System.out.printf(s1);
//            };
//            CancelCallback cancelCallback =(consumerTag)->{
//                System.out.printf("消息被中断");
//            };
//            /**
//             * 消费者消费消息
//             * 1.消费哪个队列
//             * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
//             * 3.消费者未成功消费的回调
//             */
//
//            channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
//        }
        Connection connection = factory.newConnection(); Channel channel = connection.createChannel();
            DeliverCallback deliverCallback =(consumerTag,delivery)->{
                String s1 = new String(delivery.getBody());
                System.out.println(s1);
            };
            CancelCallback cancelCallback =(consumerTag)->{
                System.out.printf("消息被中断");
            };
            /**
             * 消费者消费消息
             * 1.消费哪个队列
             * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
             * 3.消费者未成功消费的回调
             */

            channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

        
        
    }
}
