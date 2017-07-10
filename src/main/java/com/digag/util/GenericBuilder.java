package com.digag.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * Created by Yuicon on 2017/7/10.
 * https://github.com/Yuicon
 */
public class GenericBuilder<T> {
    private final Supplier<T> instantiator;
    private List<Consumer<T>> instantiatorModifiers = new ArrayList<>();
    private List<Consumer<T>> keyValueModifiers = new ArrayList<>();

    public GenericBuilder(Supplier<T> instantiator) {
        this.instantiator = instantiator;
    }

    public static <T> GenericBuilder<T> of(Supplier<T> instantiator) {
        return new GenericBuilder<T>(instantiator);
    }

    public <U> GenericBuilder<T> with(BiConsumer<T, U> consumer, U value) {
        Consumer<T> c = instance -> consumer.accept(instance, value);
        instantiatorModifiers.add(c);
        return this;
    }

//    public <K, V> GenericBuilder<T> with(KeyValueConsumer<T, K, V> consumer, K key, V value) {
//        Consumer<T> c = instance -> consumer.accept(instance, key, value);
//        keyValueModifiers.add(c);
//        return this;
//    }

    public T build() {
        T value = instantiator.get();
        instantiatorModifiers.forEach(modifier -> modifier.accept(value));
        keyValueModifiers.forEach(keyValueModifier -> keyValueModifier.accept(value));
        instantiatorModifiers.clear();
        keyValueModifiers.clear();
        return value;
    }
}
