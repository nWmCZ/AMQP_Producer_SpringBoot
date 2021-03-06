package eu.sn.model;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.UUID;

@Service
public class MessagingService {

    @Value("${amqpUrl:tcp://localhost:61616}")
    String amqpUrl;

    @Value("${queue:scheduledQueue}")
    String queue;

    @Value("${topic:scheduledTopic}")
    String topic;

    @Value("${toQueue:true}")
    boolean toQueue;

    @Value("${messageText:jmsMessage}")
    String messageText;

    Logger logger = LoggerFactory.getLogger(MessagingService.class);

    public void sendMessage(int count) {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(amqpUrl);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination;
            // Create the destination (Topic or Queue)
            if (toQueue) {
                destination = session.createQueue(queue);
            } else {
                destination = session.createTopic(topic);
            }

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            for (int i = 0; i < count; i++) {
                // Create a messages
                String messageId = UUID.randomUUID().toString();
                TextMessage message = session.createTextMessage(messageText + " MessageId: " + messageId);

                // Tell the producer to send the message
                producer.send(message);

                if (toQueue) {
                    logger.info("Message sent with MessageId {}, to queue {}", messageId, queue);
                } else {
                    logger.info("Message sent with MessageId {}, to topic {}", messageId, topic);
                }
            }

            // Clean up
            session.close();
            connection.close();
        }
        catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }
}
