import express from 'express';

import { createInitialData } from "./src/config/db/initalData.js";
import { connectMongoDb } from "./src/config/db/mongoDbConfig.js";
import Order from "./src/modules/sales/model/Order.js";
import checkToken from "./src/config/auth/checkToken.js";
import { connectRabbitMq } from "./src/config/rabbitmq/rabbitConfig.js";
import { sendMessageToProductStockUpdateQueue } from "./src/modules/sales/model/product/rabbitmq/productStockUpdateSender.js";

import orderRoutes from "./src/modules/sales/routes/OrderRoutes.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

console.log("Iniciando App");
  connectMongoDb();
  connectRabbitMq();


app.use(express.json());
app.use(checkToken);
app.use(orderRoutes);


app.get('/teste', (req, res) => {
  try {
    sendMessageToProductStockUpdateQueue([
      {
        productId: 1001,
        quantity: 3
      },
      {
        productId: 1002,
        quantity: 2
      },
      {
        productId: 1003,
        quantity: 1
      }
    ])
    return res.status(200).json({status: 200});
  }catch(err){
    console.log(err);
    return res.status(500).json({error: true});
  }
});

app.get('/api/status', async (req, res) => {
    
    return res.status(200).json({
        service: "Sales-API",
        status: "up",
        httpStatus: 200
    })
});



app.listen(PORT, () => {
    console.info(`Server started at port ${PORT}`);
})