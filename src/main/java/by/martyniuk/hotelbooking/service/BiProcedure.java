package by.martyniuk.hotelbooking.service;

public interface BiProcedure<T,K> {
    void accept(T t, K k);
}