package com.payu.ratel.context;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * A context of a process that is always available via {@link ProcessContext#getInstance()}.
 * It is automatically transported over ratel remote service calls.
 */
public class ProcessContext {

    private static final ThreadLocal<ProcessContext> INSTANCE = ThreadLocal.withInitial(new Supplier<ProcessContext>() {

        @Override
        public ProcessContext get() {
            return new ProcessContext();
        }

    });

    private String processIdentifier;

    public static ProcessContext getInstance() {
        return INSTANCE.get();
    }

    /**
     * Get unique identifier of this process.
     */
    public String getProcessIdentifier() {
        return processIdentifier;
    }


    public void setProcessIdentifier(String processId) {
        this.processIdentifier = processId;
    }

    /**
     * Generate new random process identifier, given that it is not presently set.
     *
     * @throws IllegalStateException when currently set process id is not null.
     */
    public void generateProcessIdentifier() {
        if (getProcessIdentifier() != null) {
            throw new IllegalStateException("Cannot generate new process identifier when present is not cleared");
        }
        setProcessIdentifier(UUID.randomUUID().toString());
    }

    public void clearProcessIdentifier() {
        setProcessIdentifier(null);
    }


}
