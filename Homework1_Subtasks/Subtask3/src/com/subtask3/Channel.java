package com.subtask3;

import java.util.function.Consumer;

@FunctionalInterface
interface Channel extends Consumer<String> {
    @Override
    void accept(String s);

    default void sending(String s) {
        accept(s);
    }
}
