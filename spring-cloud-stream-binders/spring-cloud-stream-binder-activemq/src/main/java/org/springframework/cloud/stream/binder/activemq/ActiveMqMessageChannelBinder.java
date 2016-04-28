package org.springframework.cloud.stream.binder.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.cloud.stream.binder.*;
import org.springframework.integration.endpoint.AbstractEndpoint;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.jms.ChannelPublishingJmsMessageListener;
import org.springframework.integration.jms.JmsMessageDrivenEndpoint;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

import javax.jms.ConnectionFactory;

/**
 * A {@link org.springframework.cloud.stream.binder.Binder} implementation backed by ActiveMQ.
 *
 * @author Darren Gorman
 */
public class ActiveMqMessageChannelBinder extends AbstractBinder<MessageChannel, ExtendedConsumerProperties<ActiveMqConsumerProperties>,
        ExtendedProducerProperties<ActiveMqProducerProperties>> implements ExtendedPropertiesBinder<MessageChannel, ActiveMqConsumerProperties, ActiveMqProducerProperties> {

    private ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

    @Override
    protected Binding<MessageChannel> doBindConsumer(String name, String group, MessageChannel inputTarget, ExtendedConsumerProperties<ActiveMqConsumerProperties> properties) {
        DefaultMessageListenerContainer mlc = new DefaultMessageListenerContainer();
        mlc.setConnectionFactory(connectionFactory);
        mlc.setDestinationName(name);

        ChannelPublishingJmsMessageListener ml = new ChannelPublishingJmsMessageListener();
        ml.setRequestChannel(inputTarget);

        JmsMessageDrivenEndpoint endpoint = new JmsMessageDrivenEndpoint(mlc, ml);
        endpoint.afterPropertiesSet();
        endpoint.start();

        return new DefaultBinding<>(name, group, inputTarget, endpoint);
    }

    @Override
    protected Binding<MessageChannel> doBindProducer(String name, MessageChannel outboundBindTarget, ExtendedProducerProperties<ActiveMqProducerProperties> properties) {
        JmsSendingMessageHandler handler = new JmsSendingMessageHandler(new JmsTemplate(connectionFactory));
        handler.setDestinationName(name);

        AbstractEndpoint endpoint = new EventDrivenConsumer((SubscribableChannel) outboundBindTarget, handler);
        endpoint.afterPropertiesSet();
        endpoint.start();

        return new DefaultBinding<>(name, null, outboundBindTarget, endpoint);
    }

    @Override
    public ActiveMqConsumerProperties getExtendedConsumerProperties(String channelName) {
        return new ActiveMqConsumerProperties();
    }

    @Override
    public ActiveMqProducerProperties getExtendedProducerProperties(String channelName) {
        return new ActiveMqProducerProperties();
    }
}
