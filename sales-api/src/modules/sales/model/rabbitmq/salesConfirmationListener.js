import amqp from "amqplib/callback_api.js";
import { RABBIT_MQ_URL } from "../../../../config/constants/secrets.js";

import {
  SALES_CONFIRMATION_QUEUE,
  SALES_CONFIRMATION_ROUTING_KEY
} from '../../../../config/rabbitmq/queue.js';
import OrderService from "../../services/OrderService.js";

export function listenToSalesConfirmationQueue() {
  amqp.connect(RABBIT_MQ_URL, (error, connection) => {
    if (error) {
      throw error;
    }
    console.info("Listening to Sales Confirmation Queue...");
    connection.createChannel((error, channel) => {
      if (error) {
        throw error;
      }
      channel.consume(
        SALES_CONFIRMATION_QUEUE,
        (message) => {
          console.info(
            `Recieving message from queue: ${message.content.toString()}`
          );
          OrderService.updateOrder(message.content.toString());
        },
        {
          noAck: true,
        }
      );
    });
  });
}