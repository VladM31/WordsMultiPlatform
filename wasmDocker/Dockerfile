FROM nginx:1.27-alpine

# Копируем всё содержимое текущей папки в html-каталог Nginx
COPY ../composeApp/build/dist/wasmJs/productionExecutable /usr/share/nginx/html

# Заменяем дефолтный конфиг
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
