/**
 * This package contains classes used by the tracing mechanism. The mechanism ensures that current thread has a unique process context
 * represented by {@link com.payu.ratel.context.ProcessContext#getInstance()}.
 * The content of process context is transported via http headers whenever a remote ratel service call takes place.
 * The mechanism assumes that thread management is handled by web container.
 */
package com.payu.ratel.context;
