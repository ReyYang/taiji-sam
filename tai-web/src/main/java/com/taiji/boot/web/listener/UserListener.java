package com.taiji.boot.web.listener;

import cn.hutool.core.util.ObjectUtil;
import com.taiji.boot.common.rocketmq.TopicBusinessInterface;
import com.taiji.boot.common.rocketmq.annotation.TopicMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

/**
 * Demo UserListener
 *
 * @author ydy
 * @date 2020/3/26 20:35
 */
@Slf4j
@Component
public class UserListener implements TopicBusinessInterface {

    @Override
    @TopicMethod(topic = "rocket.topic-manager.user.topic")
    public ConsumeConcurrentlyStatus doConsume(MessageExt msg) {
        if (ObjectUtil.isNull(msg)) {
            log.info("接受到的消息为空，不处理，直接返回成功");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        log.info("接受到的消息为：" + msg.toString());
        if (msg.getTopic().equals("TAIJI_USER")) {
            if (msg.getTags().equals("add")) {
                //TODO 判断该消息是否重复消费（RocketMQ不保证消息不重复，如果你的业务需要保证严格的不重复消息，需要你自己在业务端去重）
                //TODO 获取该消息重试次数
                int reconsume = msg.getReconsumeTimes();
                if (reconsume == 3) {//消息已经重试了3次，如果不需要再次消费，则返回成功
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                //TODO 处理对应的业务逻辑
            }
        }
        // 如果没有return success ，consumer会重新消费该消息，直到return success
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
