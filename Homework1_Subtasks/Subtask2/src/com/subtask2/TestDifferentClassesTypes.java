package com.subtask2;

public class TestDifferentClassesTypes {

    public static void main(String[] args) {
        MessageSender sender = new MessageSender("Sender");
        sender.send("Duke", "Hello Duke");
        sender.send("", "* Empty recipient test *");
        sender.send("Tux", "Hello Tux");
        sender.send("* Empty content test *", "");
        sender.send("Moby Dock", "Hello Moby Dock");
    }
}
