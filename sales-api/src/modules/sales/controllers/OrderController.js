import OrderService from '../services/OrderService.js';

class OrderController {
  async createOrder(req, res){
    let order = await OrderService.createOrder(req);
    return res.status(order.status).json(order);
  }

  async findById(req, res){
    let user = await OrderService.findById(req);
    return res.status(user.status).json(user);
  }

  async findAll(req, res){
    let user = await OrderService.findAll();
    return res.status(user.status).json(user);
  }

  async findByProductId(req, res){
    let user = await OrderService.findByProductId(req);
    return res.status(user.status).json(user);
  }
}

export default new OrderController();