package com.github.lenemarix.autoplay.dq11.poker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.statemachine.StateMachine;

import com.github.lenemarix.autoplay.dq11.poker.model.Card;
import com.github.lenemarix.autoplay.dq11.poker.model.Mode;
import com.github.lenemarix.autoplay.dq11.poker.prepare.CaptureBetCoinInputRunner;
import com.github.lenemarix.autoplay.dq11.poker.prepare.CaptureCardRunner;
import com.github.lenemarix.autoplay.dq11.poker.prepare.CaptureDealCardsButtonRunner;
import com.github.lenemarix.autoplay.dq11.poker.statemachine.event.Events;
import com.github.lenemarix.autoplay.dq11.poker.statemachine.state.States;

/**
 * DQ11のポーカーを自動実行し、ロイヤルストレートスライムが出るまで続ける。
 * 準備として"くばる"ボタンおよびロイヤルストレートスライムを構成するカードの画像キャプチャを取る必要がある。
 */
@SpringBootApplication
@ConfigurationProperties(prefix = "")
public class AutoplayApplication implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoplayApplication.class);

    @Autowired
    CaptureCardRunner captureCardRunner;

    @Autowired
    CaptureDealCardsButtonRunner captureDealCardsButtonRunner;

    @Autowired
    CaptureBetCoinInputRunner captureBetCoinInputRunner;

    @Autowired(required = false)
    StateMachine<States, Events> stateMachine;

    /** アプリケーションの動作モード。 */
    private Mode mode;
    /** CAPTURE_CARDモードで、何番目のカードをキャプチャするか(左側から1番目)。 */
    private int captureCardNumber;
    /** CAPTURE_CARDモードで、キャプチャしたカードの種別は何か。 */
    private Card captureCardType;

    public static void main(String[] args) {
        SpringApplication.run(AutoplayApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("mode: {}", mode);

        switch (mode) {
        case AUTOPLAY:
            stateMachine.start();
            break;
        case CAPTURE_CARD:
            captureCardRunner.capture(captureCardNumber, captureCardType);
            break;
        case CAPTURE_DEAL_CARDS_BUTTON:
            captureDealCardsButtonRunner.capture();
            break;
        case CAPTURE_BET_COIN_INPUT:
            captureBetCoinInputRunner.capture();
            break;
        default:
            // do nothing in some particular cases (e.g. for test)
            break;
        }
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public int getCaptureCardNumber() {
        return captureCardNumber;
    }

    public void setCaptureCardNumber(int captureCardNumber) {
        this.captureCardNumber = captureCardNumber;
    }

    public Card getCaptureCardType() {
        return captureCardType;
    }

    public void setCaptureCardType(Card captureCardType) {
        this.captureCardType = captureCardType;
    }
}