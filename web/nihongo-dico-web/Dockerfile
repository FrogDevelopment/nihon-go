FROM nginx:stable-alpine

## Remove default nginx website
RUN rm -rf /usr/share/nginx/html/*
RUN rm -rf /etc/nginx/conf.d
COPY ./nginx /etc/nginx

## Copy website to default nginx public folder
COPY ./dist/nihongo-dico /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
