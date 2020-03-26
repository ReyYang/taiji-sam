package com.taiji.boot.common.rocketmq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * Demo TopicBusinessInterface
 *
 * @author ydy
 * @date 2020/3/26 19:54
 */
public interface TopicBusinessInterface {

    ConsumeConcurrentlyStatus doConsume(MessageExt msg);
}
