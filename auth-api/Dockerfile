FROM node:14
WORKDIR .
COPY packge*.json ./
RUN npm run
COPY . .
EXPOSE 8080
CMD ["node", "app.js"]