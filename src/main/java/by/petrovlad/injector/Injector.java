package by.petrovlad.injector;

public interface Injector {
    <T> Provider<T> getProvider(Class<T> type);
    <T> void bing(Class<T> intf, Class<? extends T> impl);
    <T> void bindSingleton(Class<T> intf, Class<? extends T> impl);
}
