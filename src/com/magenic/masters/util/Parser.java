package com.magenic.masters.util;

@FunctionalInterface
public interface Parser<T, R> {
    R parse (T t);
}
