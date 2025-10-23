package com.subtask2;

import com.simple.logger.ConsoleLogger;
import com.simple.logger.Logger;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageSender {
    public static final Logger LOGGER = ConsoleLogger.getLogger(MessageSender.class);
    public static final String SEPARATOR = "\n---------------------------------------------";

    private final String sender;

    public MessageSender(String sender) {
        this.sender = sender;
    }

    public class Message {
        private static final AtomicInteger COUNTER = new AtomicInteger(0);

        private final int id;
        private final String recipient;
        private final String content;

        public Message(String recipient, String content) {
            this.id = COUNTER.incrementAndGet();
            this.recipient = recipient;
            this.content = content;
        }

        public String format() {
            return "[" + LocalDateTime.now() + "] #" + id + " From: " + sender + " To: " + recipient + "\n" + content;
        }
    }

    public static class Validator {
        public static boolean isValid(String recipient, String content) {
            return isNotEmpty(recipient) && isNotEmpty(content);
        }

        private static boolean isNotEmpty(String str) {
            return str != null && !str.isBlank();
        }
    }

    public void send(String recipient, String content) {
        class Channel {
            void send(String data) {
                LOGGER.info("Send data via channel:" + SEPARATOR + "\n" + data + SEPARATOR);
            }
        }

        if (!Validator.isValid(recipient, content)) {
            LOGGER.error("Missing recipient or content. Recipient: \"" + recipient
                    + "\" Content: \"" + content + "\"");
            return;
        }

        Message message = new Message(recipient, content);
        Channel channel = new Channel();

        channel.send(message.format());

        new Thread(new Runnable() {
            @Override
            public void run() {
                deliver();
                LOGGER.info("#" + message.id + " Delivered to: " + recipient);
            }

            void deliver() {
                int seconds = ThreadLocalRandom.current().nextInt(1, 4);
                try {
                    Thread.sleep(seconds * 600L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
