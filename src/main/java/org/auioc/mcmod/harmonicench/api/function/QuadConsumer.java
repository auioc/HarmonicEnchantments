package org.auioc.mcmod.harmonicench.api.function;

@FunctionalInterface
public interface QuadConsumer<S, T, U, V> {

    void accept(S s, T t, U u, V v);

}
