package wx.th.zlo.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * ClassName: RabbitMqUtils
 * Description: TODO
 * Author: zlo
 * Date: 2022/12/3 14:39
 * Version: 1.0.0
 */
public class RabbitMqUtils {
    public static Channel getChannel() throws Exception{
        //连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");
        //建立连接
        Connection connection = factory.newConnection();
        //创建一个信道
        return  connection.createChannel();
    }
}
