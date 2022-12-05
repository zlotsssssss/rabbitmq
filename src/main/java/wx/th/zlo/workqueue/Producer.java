package wx.th.zlo.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import wx.th.zlo.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * ClassName: Producer
 * Description: TODO
 * Author: zlo
 * Date: 2022/12/4 10:58
 * Version: 1.0.0
 */
public class Producer  {
    private final static String ACK_QUEUE = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //创建一个队列
        channel.queueDeclare(ACK_QUEUE,true,false,false,null);
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入");
        while (scanner.hasNext()){
            System.out.println("请输入");
            String next = scanner.next();
            //向队列发送消息
            channel.basicPublish("",ACK_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN,next.getBytes("UTF-8"));
        }
    }
}
