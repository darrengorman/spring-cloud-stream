package org.springframework.cloud.stream.binder.activemq;

import org.springframework.cloud.stream.binder.AbstractBinderTests;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;

/**
 * @author Darren Gorman
 */
public class ActiveMqBinderTests extends AbstractBinderTests<ActiveMqTestBinder, ExtendedConsumerProperties<ActiveMqConsumerProperties>, ExtendedProducerProperties<ActiveMqProducerProperties>> {

    @Override
    protected ActiveMqTestBinder getBinder() throws Exception {
        if (testBinder == null) {
            testBinder = new ActiveMqTestBinder();
        }
        return testBinder;
    }

    @Override
    protected ExtendedConsumerProperties<ActiveMqConsumerProperties> createConsumerProperties() {
        return new ExtendedConsumerProperties<>(new ActiveMqConsumerProperties());
    }

    @Override
    protected ExtendedProducerProperties<ActiveMqProducerProperties> createProducerProperties() {
        return new ExtendedProducerProperties<>(new ActiveMqProducerProperties());
    }
}
