package com.github.lovept.initializer;

import com.github.lovept.config.TelegramBotConfig;
import com.github.lovept.handler.TelegramUpdateHandler;
import com.github.lovept.mapper.MemoMapper;
import com.github.lovept.mapper.ResourceMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lovept
 * @date 2024/7/23 14:29
 * @description telegram 初始化
 */

@Slf4j
@Component
public class TelegramBotInitializer {
    @Resource
    private TelegramBotConfig config;

    private TelegramBot bot;

    @Resource
    private MemoMapper memoMapper;

    @Resource
    ResourceMapper resourceMapper;

    @PostConstruct
    private void init() {
        bot = new TelegramBot(config.getToken());
        bot.setUpdatesListener(this::handleUpdates, this::handleError);
    }

    private int handleUpdates(List<Update> updates) {
        updates.forEach(update -> new TelegramUpdateHandler(bot, update, memoMapper, resourceMapper).handler());
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void handleError(TelegramException e) {
        if (e.response() != null) {
            log.error("Error code: {}, Description: {}", e.response().errorCode(), e.response().description());
        } else {
            log.error(e.getMessage());
        }
    }
}
