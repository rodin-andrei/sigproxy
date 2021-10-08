package com.unifun.sigproxy.service.rabbit.impl;

import com.unifun.sigproxy.service.rabbit.pojo.MapSupplementaryMessageRabbit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Configuration
@EnableRabbit
public class RabbitConfiguration {
    private static final String SEND_TO_ROUTING_QUEUE = "send.to.routing";
    private static final String RECEIVE_BY_GT_QUEUE = "receive.routing.by.gt.#";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitTemplate jsonRabbitTemplate;

    @Autowired
    private AmqpAdmin rabbitAdmin;

    @Deprecated //for the moment
    public void listenForMessagesRoutedByGt(MapSupplementaryMessageRabbit message) {
        //TODO send map
        log.info("message received in queue routed by gt : " + message.toString());
    }

    public String getSendByGtQueue() {
        return SEND_TO_ROUTING_QUEUE;
    }

    @Deprecated // for the moment
    public SimpleMessageListenerContainer legacyPojoListener(ConnectionFactory connectionFactory) { //TODO legacy ?
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(RECEIVE_BY_GT_QUEUE);
        container.setConcurrency("1-3");
        MessageListenerAdapter messageListener = new MessageListenerAdapter(new Object() {

            @SuppressWarnings("unused")
            public void handleMessage(MapSupplementaryMessageRabbit object) {
                log.info("Got a " + object);
            }
        });
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper());
        messageListener.setMessageConverter(jsonConverter);
        container.setMessageListener(messageListener);
        return container;
    }


    //TODO check if we could avoid adding classes of messages
    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("MapMessage", MapSupplementaryMessageRabbit.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonConverter());
        return template;
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     *
     * @return
     */
    @Bean
    public Queue receiveByGt() {
        return new Queue("receive.routing.by.gt.#", true, false, true);
    }
    //TODO add possibility to edit rabbit configuration through beans


}
