package wx.th.zlo.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import wx.th.zlo.utils.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * ClassName: MessageConfirm
 * Description: TODO
 * Author: zlo
 * Date: 2022/12/5 18:31
 * Version: 1.0.0
 */
public class MessageConfirm {
    public static void main(String[] args) throws Exception {
        //单个确认发布 210ms
        // publishMessageIndividually();

        //批量确认  65ms
       // publishMessageBatch();

        //异步确认 28ms
        publishMessageAsync();

    }

    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare("test", false, false, false, null);
        //开启消息确认
        channel.confirmSelect();
        int messageCount = 1000;
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < messageCount; i++) {
            String msg = i + "";
            channel.basicPublish("", "test", null, msg.getBytes());
            //服务端返回 false 或超时时间内未返回，生产者可以消息重发
            channel.waitForConfirms();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("花费" + (endTime - beginTime) + "ms");

    }

    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare("test1", false, false, false, null);
        //开启消息确认
        channel.confirmSelect();
        int messageCount = 1000;
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < messageCount; i++) {
            String msg = i + "";
            channel.basicPublish("", "test1", null, msg.getBytes());
            //服务端返回 false 或超时时间内未返回，生产者可以消息重发
            if (i % 100 == 0)
                channel.waitForConfirms();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("花费" + (endTime - beginTime) + "ms");

    }

    public static void publishMessageAsync() throws Exception {
        int MESSAGE_COUNT = 1000;
        try (Channel channel = RabbitMqUtils.getChannel()) {
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, false, false, false, null);
            //开启发布确认
            channel.confirmSelect();
            /**
             * 线程安全有序的一个哈希表，适用于高并发的情况
             * 1.轻松的将序号与消息进行关联
             * 2.轻松批量删除条目 只要给到序列号
             * 3.支持并发访问
             */
            ConcurrentSkipListMap<Long, String> outstandingConfirms = new
                    ConcurrentSkipListMap<>();
            /**
             * 确认收到消息的一个回调
             * 1.消息序列号
             * 2.true 可以确认小于等于当前序列号的消息
             * false 确认当前序列号消息
             */
            ConfirmCallback ackCallback = (sequenceNumber, multiple) -> {
                if (multiple) {
                    //返回的是小于等于当前序列号的未确认消息 是一个 map
                    ConcurrentNavigableMap<Long, String> confirmed =
                            outstandingConfirms.headMap(sequenceNumber, true);
                    //清除该部分未确认消息
                    confirmed.clear();
                } else {
                    //只清除当前序列号的消息
                    outstandingConfirms.remove(sequenceNumber);
                }
            };
            ConfirmCallback nackCallback = (sequenceNumber, multiple) ->
            {
                String message = outstandingConfirms.get(sequenceNumber);
                System.out.println("发布的消息" + message + "未被确认，序列号" + sequenceNumber);
            };
            /**
             * 添加一个异步确认的监听器
             * 1.确认收到消息的回调
             * 2.未收到消息的回调
             */
            channel.addConfirmListener(ackCallback, nackCallback);
            long begin = System.currentTimeMillis();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = "消息" + i;
                /**
                 * channel.getNextPublishSeqNo()获取下一个消息的序列号
                 * 通过序列号与消息体进行一个关联
                 * 全部都是未确认的消息体
                 */
                outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
                channel.basicPublish("", queueName, null, message.getBytes());

            }
            long end = System.currentTimeMillis();
            System.out.println("花费" + (end - begin) + "ms");

        }
    }
}
