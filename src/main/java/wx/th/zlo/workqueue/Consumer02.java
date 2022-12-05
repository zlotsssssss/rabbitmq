package wx.th.zlo.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import wx.th.zlo.utils.RabbitMqUtils;
import wx.th.zlo.utils.SleepUtils;

/**
 * ClassName: Consumer01
 * Description: TODO
 * Author: zlo
 * Date: 2022/12/4 12:31
 * Version: 1.0.0
 */
public class Consumer02 {
    private final static String ACK_QUEUE = "ack_queue";

    public static void main(String[] args) throws  Exception{
        Channel channel = RabbitMqUtils.getChannel();
        DeliverCallback deliverCallback = (deliverTag,deliver)->{
            String s = new String(deliver.getBody(), "UTF-8");
            SleepUtils.sleep(10);
            System.out.println(s);
            channel.basicNack(deliver.getEnvelope().getDeliveryTag(),false,false);
            //channel.basicAck(deliver.getEnvelope().getDeliveryTag(),false);
        };
        //该值定义通道上允许的未确认消息的最大数量
        int perfetchCount = 5;
        channel.basicQos(perfetchCount);
        channel.basicConsume(ACK_QUEUE,false,deliverCallback,(deliverTag)->{
            System.out.println("消息终中断");
        });
    }
}
