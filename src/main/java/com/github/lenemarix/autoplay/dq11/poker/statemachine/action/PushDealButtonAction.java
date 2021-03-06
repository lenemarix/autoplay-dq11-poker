package com.github.lenemarix.autoplay.dq11.poker.statemachine.action;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;

import com.github.lenemarix.autoplay.dq11.poker.statemachine.event.Events;
import com.github.lenemarix.autoplay.dq11.poker.statemachine.state.States;
import com.github.lenemarix.autoplay.dq11.poker.util.RobotUtil;

/**
 * "くばる"ボタンを押すアクション。 下キー → Enterキー(=○ボタン)の順番でキーを押下する。
 */
public class PushDealButtonAction extends AbstractAutoplayAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            PushDealButtonAction.class);

    private AtomicInteger count = new AtomicInteger(0);

    @Autowired
    RobotUtil robotUtil;

    @Override
    public void doExecute(StateContext<States, Events> context) {
        // 下キーを押して"くばる"ボタンに移動。
        robotUtil.downKeyPress();
        // "くばる"ボタンを押下
        robotUtil.enterKeyPress();

        count.incrementAndGet();
    }

    @PreDestroy
    public void loggingCount() {
        LOGGER.info("PushDealButtonAction count: {}", count.get());
    }
}
