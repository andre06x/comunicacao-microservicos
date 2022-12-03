import OrderException from "../exception/OrderExecpetion.js";
import { sendMessageToProductStockUpdateQueue } from "../model/product/rabbitmq/productStockUpdateSender.js";
import OrderRepository from "../repository/OrderRepository.js";
import { ACCEPTED, PENDING, REJECTED } from '../status/OrderStatus.js';
import { BAD_REQUEST } from '../../../config/constants/httpStatus.js';
import ProductClient from "../model/product/client/ProductClient.js";

import * as httpStatus from '../../../config/constants/httpStatus.js';

class OrderService {
  async createOrder(req) {
    try {
      const orderData = req.body;
      this.validateOrderData(orderData);

      const { authUser } = req;
      const { authorization } = req.headers;

      let order = this.createInitialOrderData(orderData, authUser);
      await this.validateProductStock(order, authorization);

      const createdOrder = await OrderRepository.save(order);

      this.sendMessage(createdOrder);
      return {
        status: httpStatus.SUCCESS,
        createdOrder
      }
    } catch (err) {
      return {
        status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: err.message
      }
    }
  }
  createInitialOrderData(orderData, authUser) {
    return {
      status: PENDING,
      user: authUser,
      createdAt: new Date(),
      updatedAt: new Date(),
      products: orderData.products
    };

  }
  async updateOrder(orderMessage) {
    try {
      const order = JSON.parse(orderMessage);

      console.log(order);
      if (order.saledId && order.status) {
        let existingOrder = await OrderRepository.findById(order.saledId);

        if (existingOrder && order.status !== existingOrder.status) {
          existingOrder.status = order.status;
          existingOrder.updatedAt = new Date();
          await OrderRepository.save(existingOrder);
        }

      } else {
        console.warn("The order message was not complete.");
      }



    } catch (err) {
      console.error("Could not parse order message from queue")
    }
  }

  validateOrderData(data) {
    if (!data || !data.products) {
      throw new OrderException(BAD_REQUEST, 'The products must be informed.');
    }
  }

  async validateProductStock(order, token) {
    // console.log(order);
    let stockIsOk = await ProductClient.checkProductStock(order, token);
    console.log(order);
    console.log(stockIsOk);
    if (!stockIsOk) {
      throw new OrderException(BAD_REQUEST, "The stock is out for the products.");
    }
  }

  sendMessage(createdOrder) {
    const message = {
      salesId: createdOrder.id,
      products: createdOrder.products
    }
    sendMessageToProductStockUpdateQueue(message);
  }

  async findById(req) {

    try {
      const { id } = req.params;
      this.validateInformedId(id);

      const existingOrder = await OrderRepository.findById(id);
      if (!existingOrder) {
        throw new OrderException(BAD_REQUEST, "The order was not found.");
      }

      return {
        status: httpStatus.SUCCESS,
        existingOrder
      }
    } catch (err) {
      return {
        status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: err.message
      }
    }

  }

  async findAll() {
    try {

      const orders = await OrderRepository.findAll();
      if (!orders) {
        throw new OrderException(BAD_REQUEST, "No orders were found.");
      }

      return {
        status: httpStatus.SUCCESS,
        orders
      }

    } catch (err) {
      return {
        status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: err.message
      }
    }

  }

  async findByProductId(req) {
    try {

      const { id } = req.params;
      this.validateInformedProductId(id);

      const orders = await OrderRepository.findByProductId(id);
      if (!orders) {
        throw new OrderException(BAD_REQUEST, "No orders were found.");
      }

      return {
        status: httpStatus.SUCCESS,
        salesId: orders.map((order) => {
          return order.id;
        })
      }

    } catch (err) {
      return {
        status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: err.message
      }
    }

  }

  validateInformedId(id) {
    if (!id) {
      throw new OrderException(BAD_REQUEST, "The order ID must be informed.");
    }
  }

  validateInformedProductId(id){
    if(!id){
      throw new OrderException(BAD_REQUEST, "The order's productId must be informed");
    }
  }
}

export default new OrderService();

