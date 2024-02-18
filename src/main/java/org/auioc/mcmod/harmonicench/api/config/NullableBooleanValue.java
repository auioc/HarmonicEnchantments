package org.auioc.mcmod.harmonicench.api.config;

import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import org.auioc.mcmod.arnicalib.base.holder.ObjectHolder;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class NullableBooleanValue extends ObjectHolder<BooleanValue> {

    private final boolean defaultValue;
    private final Supplier<BooleanValue> valueSupplier;
    private boolean alwaysNull = false;

    public NullableBooleanValue(boolean defaultValue, Supplier<BooleanValue> valueSupplier) {
        super(null);
        this.defaultValue = defaultValue;
        this.valueSupplier = valueSupplier;
    }

    public NullableBooleanValue(Supplier<BooleanValue> valueSupplier) {
        this(true, valueSupplier);
    }

    @Override
    public void set(@Nullable BooleanValue newValue) {
        if (newValue == null) {
            alwaysNull = true;
        } else {
            value = newValue;
            alwaysNull = false;
        }
    }

    public void set() {
        if (!alwaysNull && value == null) {
            set(valueSupplier.get());
        }
    }

    public boolean getValue() {
        set();
        return (alwaysNull) ? defaultValue : value.get();
    }

}
