import express from 'express';

import { createInitialData } from "./src/config/db/initalData.js";
import { connectMongoDb } from "./src/config/db/mongoDbConfig.js";
import Order from "./src/modules/sales/model/Order.js";
import checkToken from "./src/config/auth/checkToken.js";
import { connectRabbitMq } from "./src/config/rabbitmq/rabbitConfig.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

connectMongoDb();
createInitialData();
connectRabbitMq();

app.use(checkToken);

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