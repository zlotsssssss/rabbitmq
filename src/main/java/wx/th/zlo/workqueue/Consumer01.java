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
public class Consumer01 {
    private final static String ACK_QUEUE = "ack_queue";

    public static void main(String[] args) throws  Exception{
        Channel channel = RabbitMqUtils.getChannel();
        DeliverCallback deliverCallback = (deliverTag,deliver)->{
            String s = new String(deliver.getBody(), "UTF-8");
            SleepUtils.sleep(1);
            System.out.println(s);
            channel.basicAck(deliver.getEnvelope().getDeliveryTag(),false);
        };
        int perfetchCount = 2;
        channel.basicQos(perfetchCount);
        //通过信道消费队列
        channel.basicConsume(ACK_QUEUE,false,deliverCallback,(deliverTag)->{
            System.out.println("消息终中断");
        });
    }
}
