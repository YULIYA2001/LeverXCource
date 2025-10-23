package com.subtask3;

import com.simple.logger.ConsoleLogger;
import com.simple.logger.Logger;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;

public class MessageSender {
    public static final Logger LOGGER = ConsoleLogger.getLogger(MessageSender.class);
    public static final String SEPARATOR = "\n---------------------------------------------";

    private final String sender;

    public MessageSender(String sender) {
        this.sender = sender;
    }

    public void send(String recipient, String content) {
        BiPredicate<String, String> bothNotEmpty =
                (a, b) -> a != null && !a.isBlank() && b != null && !b.isBlank();
        if (!bothNotEmpty.test(recipient, content)) {
            LOGGER.error("Missing recipient or content. Recipient: \"" + recipient
                    + "\" Content: \"" + content + "\"");
            return;
        }

        Message message = new Message() {
            private static final AtomicInteger COUNTER = new AtomicInteger(0);

            private final int id;
            private final String recipient_;
            private final String content_;
            {
                this.id = COUNTER.incrementAndGet();
                this.recipient_ = recipient;
                this.content_ = content;
            }

            @Override
            public int getId() {
                return id;
            }

            @Override
            public String format() {
                return "[" + LocalDateTime.now() + "] #" + this.id + " From: " + sender
                        + " To: " + this.recipient_ + "\n" + this.content_;
            }
        };

        ((Channel) data ->
                LOGGER.info("Send data via channel:" + SEPARATOR + "\n" + data + SEPARATOR)
        ).sending(message.format());

        new Thread(() -> {
            int seconds = ThreadLocalRandom.current().nextInt(1, 4);
            try {
                Thread.sleep(seconds * 600L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("#" + message.getId() + " Delivered to: " + recipient);
        }).start();
    }
}
