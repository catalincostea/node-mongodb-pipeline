FROM node:12.18.1
WORKDIR /app
COPY ["package.json", "package-lock.json*", "./"]
RUN npm install
COPY models/ models/
COPY views/ views/
COPY index.js .
#COPY . .
CMD ["npm", "start"]
#CMD ["/bin/ping","localhost"]
