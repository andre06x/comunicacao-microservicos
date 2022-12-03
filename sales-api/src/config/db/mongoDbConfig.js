import mongoose from 'mongoose';

import { MONGO_DB_URL } from "../constants/secrets.js";

export function connectMongoDb(){
    mongoose.connect(MONGO_DB_URL, {
        useNewUrlParser: true,
        serverSelectionTimeoutMS: 180000
    });
    mongoose.connection.on('connected', () => {
        console.info('The application connected to MongoDB succssfully!');
    });
    mongoose.connection.on('error', () => {
        console.error('Error to a connect MongoDB');
    });

}