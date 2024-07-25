package com.github.lovept.handler;

import com.github.lovept.downloader.TelegramImageDownloader;
import com.github.lovept.entity.Memo;
import com.github.lovept.entity.Resource;
import com.github.lovept.mapper.MemoMapper;
import com.github.lovept.mapper.ResourceMapper;
import com.github.lovept.service.BotCommand;
import com.github.lovept.utils.UUIDGenerator;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.message.origin.MessageOrigin;
import com.pengrad.telegrambot.model.message.origin.MessageOriginHiddenUser;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lovept
 * @date 2024/7/23 19:47
 * @description 对话构建器
 */
public class TelegramUpdateHandler {

    private static final Logger log = LoggerFactory.getLogger(TelegramUpdateHandler.class);
    private final TelegramBot bot;
    private final Update update;
    private final Map<String, BotCommand> commandMap = new HashMap<>();

    private final MemoMapper memoMapper;

    private final ResourceMapper resourceMapper;

    public TelegramUpdateHandler(TelegramBot bot, Update update, MemoMapper memoMapper, ResourceMapper resourceMapper) {
        this.bot = bot;
        this.update = update;
        this.memoMapper = memoMapper;
        this.resourceMapper = resourceMapper;
        initializeCommands();
    }

    private void initializeCommands() {
        commandMap.put("/start", (bot, update) -> startCommand());
        //commandMap.put("/list", (bot, update) -> listCommand());
        //commandMap.put("/sub", (bot, update) -> subCommand());
        commandMap.put("default", (bot, update) -> defaultCommand());
    }

    public void handler() {
        String commandText = update.message().text();
        BotCommand command = commandMap.getOrDefault(commandText, commandMap.get("default"));
        command.accept(bot, update);
    }

    private void startCommand() {
        long chatId = update.message().chat().id();
        String firstName = update.message().from().firstName();
        String lastName = update.message().from().lastName();
        String username = (firstName == null ? "" : firstName) + (lastName == null ? "" : lastName);

        SendMessage sendMessage = new SendMessage(chatId, "hello " + username) ;
        SendResponse response = bot.execute(sendMessage);
    }


    private void defaultCommand() {

        long chatId = update.message().chat().id();
        String text = update.message().text();
        String caption = update.message().caption();
        MessageOrigin messageOrigin = update.message().forwardOrigin();

        if (messageOrigin instanceof MessageOriginHiddenUser) {
            MessageOriginHiddenUser origin = (MessageOriginHiddenUser) update.message().forwardOrigin();
            if (origin != null) {
                String forwardedSenderName = origin.senderUserName();
                text = "Forwarded from: " + forwardedSenderName + "\n" + (text == null ? "" : text) + (caption == null ? "" : caption);
            }
        }

        // 接收消息 todo 视频
        Memo memo = new Memo();
        memo.setUid(UUIDGenerator.newEncodedUUID());
        memo.setCreatorId(1);
        memo.setRowStatus("NORMAL");
        memo.setContent(text == null ? "" : text);
        memo.setVisibility("PRIVATE");
        memo.setTags("[]");
        memo.setPayload("{\"property\": {}}");
        memoMapper.insert(memo);
        log.info("Text saved to database successfully.");

        Resource resource = new Resource();
        resource.setUid(UUIDGenerator.newEncodedUUID());
        resource.setCreatorId(1);

        resource.setMemoId(memo.getId());
        resource.setPayload("{}");

        PhotoSize[] photos = update.message().photo();

        if (photos != null) {
            PhotoSize largestPhoto = Arrays.stream(photos)
                    .max(Comparator.comparingLong(PhotoSize::fileSize))
                    .orElse(null);

            // 4.返回成功的消息
            if (largestPhoto != null) {
                TelegramImageDownloader imageDownloader = new TelegramImageDownloader(bot);
                resource.setSize(Math.toIntExact(largestPhoto.fileSize()));
                imageDownloader.download(resource, largestPhoto.fileId());
                resourceMapper.insert(resource);
                log.info("Image saved to database successfully.");
            }
        }


        SendMessage sendMessage = new SendMessage(chatId, "\uD83C\uDF89 memos/" + memo.getId())
                .parseMode(ParseMode.Markdown)
                .replyToMessageId(update.message().messageId());

        SendResponse response = bot.execute(sendMessage);
    }


}
