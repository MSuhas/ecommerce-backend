package com.suhasm.ecommerce.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_QUEUE = "ecommerce.order.queue";
    public static final String ORDER_DLQ =
            "ecommerce.order.dlq";
    public static final String ORDER_EXCHANGE =
            "ecommerce.order.exchange";
    public static final String ORDER_CREATED_ROUTING_KEY =
            "order.created";
    public static final String ORDER_DLX =
            "ecommerce.order.dlx";

    public static final String ORDER_DLX_ROUTING_KEY =
            "order.dead";

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public DirectExchange orderDlqExchange() {
        return new DirectExchange(ORDER_DLX);
    }

    @Bean
    public Binding orderBinding(
            Queue orderQueue,
            TopicExchange orderExchange) {

        return BindingBuilder
                .bind(orderQueue)
                .to(orderExchange)
                .with(ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding orderDlqBinding(
            Queue orderDlq,
            DirectExchange orderDlqExchange) {

        return BindingBuilder
                .bind(orderDlq)
                .to(orderDlqExchange)
                .with(ORDER_DLX_ROUTING_KEY);
    }


    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }

    @Bean
    public Queue orderDlq() {
        return new Queue(ORDER_DLQ, true);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
             RabbitTemplate rabbitTemplate,
             MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);

        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(messageConverter);

        factory.setAdviceChain(
                RetryInterceptorBuilder.stateless()
                        .maxAttempts(3)
                        .backOffOptions(
                                1000,
                                2.0,
                                5000
                        )
                        .recoverer(
                                new RepublishMessageRecoverer(
                                        rabbitTemplate,
                                        ORDER_DLX,
                                        ORDER_DLX_ROUTING_KEY
                                )
                        )
                        .build()
        );

        return factory;
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}