package com.micro.order_service.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.micro.common.models.OrderEvent;
import com.micro.order_service.service.OrderManagementService;

@Configuration
@EnableKafkaStreams
public class KafkaStreamConfig {

        @Value(value = "${spring.kafka.bootstrap-servers}")
        private String bootstrapAddress;

        @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
        KafkaStreamsConfiguration kStreamsConfig() {
                Map<String, Object> props = new HashMap<>();
                props.put(StreamsConfig.APPLICATION_ID_CONFIG, "order-processing-app");
                props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
                props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
                props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
                props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 4); // Sử dụng nhiều threads hơn cho xử lý song song
                props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 10 * 1024 * 1024L); // Tối ưu bộ đệm
                props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100); // Thay đổi tần suất commit để tối ưu hiệu suất

                return new KafkaStreamsConfiguration(props);
        }

        // @Bean
        // public KStream<String, OrderEvent> kstreamOrder(StreamsBuilder builder,
        // OrderManagementService orderManagementService) {
        // Serde<String> stringSerde = Serdes.String();
        // JsonSerde<OrderEvent> orderJsonSerde = new JsonSerde<>(OrderEvent.class);

        // KStream<String, OrderEvent> orderStockStream =
        // builder.stream("product_topic",
        // Consumed.with(stringSerde, orderJsonSerde));
        // KStream<String, OrderEvent> orderPaymentStream =
        // builder.stream("payment_topic",
        // Consumed.with(stringSerde, orderJsonSerde));

        // orderStockStream.join(orderPaymentStream,
        // orderManagementService::processOrder,
        // JoinWindows.of(Duration.ofMillis(120000)),
        // StreamJoined.with(stringSerde, orderJsonSerde, orderJsonSerde))
        // .peek((key, value) -> System.out.println("Joined result: " + value))
        // .to("order_topic");

        // return orderStockStream;
        // }
        @Bean
        public KStream<String, OrderEvent> kstreamOrder(StreamsBuilder builder,
                        OrderManagementService orderManagementService) {
                Serde<String> stringSerde = Serdes.String();
                JsonSerde<OrderEvent> orderJsonSerde = new JsonSerde<>(OrderEvent.class);

                KStream<String, OrderEvent> orderStockStream = builder.stream("product_topic",
                                Consumed.with(stringSerde, orderJsonSerde));

                orderStockStream.peek((key, value) -> System.out.println("Received event: " + value))
                                .foreach((key, value) -> orderManagementService.procesOrder(value));

                return orderStockStream;
        }
}
