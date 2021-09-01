package by.petrovlad.injector.impl;

import by.petrovlad.injector.Injector;
import by.petrovlad.injector.Provider;
import by.petrovlad.injector.examples.constructors.notfound.ConstructorNotFoundImplA;
import by.petrovlad.injector.examples.constructors.notfound.ConstructorNotFoundImplB;
import by.petrovlad.injector.examples.constructors.notfound.ConstructorNotFound;
import by.petrovlad.injector.examples.constructors.toomany.TooManyConstructors;
import by.petrovlad.injector.examples.constructors.toomany.TooManyConstructorsImplA;
import by.petrovlad.injector.examples.constructors.toomany.TooManyConstructorsImplB;
import by.petrovlad.injector.examples.correct.*;
import by.petrovlad.injector.examples.cyclic.*;
import by.petrovlad.injector.exception.ConstructorNotFoundException;
import by.petrovlad.injector.exception.CyclicInjectionException;
import by.petrovlad.injector.exception.TooManyConstructorsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class InjectorImplTest {

    private static Stream<Arguments> twoClassesWithException() {
        return Stream.of(
                arguments(IllegalArgumentException.class, null, ConstructorNotFoundImplA.class),
                arguments(IllegalArgumentException.class, ConstructorNotFound.class, null),
                arguments(IllegalArgumentException.class, null, null),
                arguments(TooManyConstructorsException.class, TooManyConstructors.class, TooManyConstructorsImplA.class),
                arguments(TooManyConstructorsException.class, TooManyConstructors.class, TooManyConstructorsImplB.class),
                arguments(ConstructorNotFoundException.class, ConstructorNotFound.class, ConstructorNotFoundImplA.class),
                arguments(ConstructorNotFoundException.class, ConstructorNotFound.class, ConstructorNotFoundImplB.class)
        );
    }

    @ParameterizedTest
    @MethodSource("twoClassesWithException")
    public <T> void bindWithException(Class<? extends Exception> e, Class<T> intf, Class<? extends T> impl) {
        assertThrows(e, () -> new InjectorImpl().bind(intf, impl));
    }

    @ParameterizedTest
    @MethodSource("twoClassesWithException")
    public <T> void bindSingletonWithException(Class<? extends Exception> e, Class<T> intf, Class<? extends T> impl) {
        assertThrows(e, () -> new InjectorImpl().bindSingleton(intf, impl));
    }

    @Test
    public void getProviderWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new InjectorImpl().getProvider(null));
    }

    @Test
    public void getProviderForPrototypes() {
        Injector injector = new InjectorImpl();

        injector.bind(EventDao.class, InMemoryEventDAOImpl.class);
        injector.bind(EventService.class, EventServiceImpl.class);
        injector.bind(SomeEventController.class, SomeEventController.class);

        Provider<?> controllerProvider = injector.getProvider(SomeEventController.class);
        assertNotNull(controllerProvider);
        assertNotNull(controllerProvider.getInstance());
        assertNotSame(controllerProvider.getInstance(), controllerProvider.getInstance());
    }

    @Test
    public void getProviderForSingletons() {
        Injector injector = new InjectorImpl();

        injector.bindSingleton(EventDao.class, InMemoryEventDAOImpl.class);
        injector.bindSingleton(EventService.class, EventServiceImpl.class);
        injector.bindSingleton(SomeEventController.class, SomeEventController.class);

        Provider<?> controllerProvider = injector.getProvider(SomeEventController.class);
        assertNotNull(controllerProvider);
        assertNotNull(controllerProvider.getInstance());
        assertSame(controllerProvider.getInstance(), controllerProvider.getInstance());
    }

    @ParameterizedTest
    @ValueSource(classes = {CycleOfOne.class, CycleOfTwoA.class, CycleOfTwoB.class, ComplexCycleA.class, ComplexCycleC.class})
    public <T> void getProviderForSingletonsWithCyclicDependencies(Class<T> intf) {
        Injector injector = new InjectorImpl();

        // register all candidates
        injector.bindSingleton(CycleOfOne.class, CycleOfOne.class);
        injector.bindSingleton(CycleOfTwoA.class, CycleOfTwoA.class);
        injector.bindSingleton(CycleOfTwoB.class, CycleOfTwoB.class);
        injector.bindSingleton(ComplexCycleA.class, ComplexCycleA.class);
        injector.bindSingleton(ComplexCycleB.class, ComplexCycleB.class);
        injector.bindSingleton(ComplexCycleC.class, ComplexCycleC.class);

        // and then try get their providers
        assertThrows(CyclicInjectionException.class, () -> injector.getProvider(intf));
    }

    @ParameterizedTest
    @ValueSource(classes = {CycleOfOne.class, CycleOfTwoA.class, CycleOfTwoB.class, ComplexCycleA.class, ComplexCycleC.class})
    public <T> void getProviderForPrototypesWithCyclicDependencies(Class<T> intf) {
        Injector injector = new InjectorImpl();

        // register all candidates
        injector.bind(CycleOfOne.class, CycleOfOne.class);
        injector.bind(CycleOfTwoA.class, CycleOfTwoA.class);
        injector.bind(CycleOfTwoB.class, CycleOfTwoB.class);
        injector.bind(ComplexCycleA.class, ComplexCycleA.class);
        injector.bind(ComplexCycleB.class, ComplexCycleB.class);
        injector.bind(ComplexCycleC.class, ComplexCycleC.class);

        // and then try get their providers
        assertThrows(CyclicInjectionException.class, () -> injector.getProvider(intf));
    }

}