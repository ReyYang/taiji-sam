package com.taiji.boot.common.rocketmq;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * Demo TopicBusinessInterface
 *
 * @author ydy
 * @date 2020/3/26 19:54
 */
public interface TopicBusinessInterface {

    ConsumeConcurrentlyStatus doConsume(MessageExt msg);
}
