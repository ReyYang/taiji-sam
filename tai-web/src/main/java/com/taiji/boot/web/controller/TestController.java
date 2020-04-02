package com.taiji.boot.web.controller;

import com.taiji.boot.biz.bo.user.UserBO;
import com.taiji.boot.biz.business.user.UserBusiness;
import com.taiji.boot.biz.vo.user.UserVO;
import com.taiji.boot.common.beans.page.PaginationQuery;
import com.taiji.boot.common.beans.page.PaginationResult;
import com.taiji.boot.common.beans.result.Result;
import com.taiji.boot.common.beans.result.ResultUtil;
import com.taiji.boot.web.config.rocketmq.topic.TopicProperties;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Demo TestController
 *
 * @author ydy
 * @date 2020/1/4 21:06
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Resource
    private UserBusiness userBusiness;

    @Resource
    private DefaultMQProducer producer;

    @Resource
    @Qualifier("userTopicProperties")
    private TopicProperties properties;

    @GetMapping("/detail/{id}")
    public Result<UserVO> getUser(@PathVariable Integer id) {
        Result<UserVO> userVOResult = ResultUtil.buildSuccessResult(userBusiness.getUser(id));
        String s = userVOResult.toString();
        System.out.println(s);
        return userVOResult;
    }

    @GetMapping("/mq/{msg}")
    public Result<Object> testMQ(@PathVariable("msg") String msg) throws Exception {
        Message message = new Message();
        message.setTopic(properties.getTopic());
        message.setTags(properties.getTags().get(0));
        message.setBody(msg.getBytes());
        producer.send(message);
        return ResultUtil.buildSuccessResult("success");
    }
}
