FROM nginx

COPY dist/assets/img/ /usr/share/nginx/html/assets/img/
COPY dist/assets/css/ /usr/share/nginx/html/assets/css/
COPY dist/assets/lib/ /usr/share/nginx/html/assets/lib/
COPY dist/assets/templates/ /usr/share/nginx/html/assets/templates/
COPY dist/assets/fonts/ /usr/share/nginx/html/assets/fonts/
COPY dist/index.html /usr/share/nginx/html/
COPY dist/main.bundle.js /usr/share/nginx/html/
COPY dist/polyfills.bundle.js /usr/share/nginx/html/
COPY dist/style.bundle.css /usr/share/nginx/html/

#COPY javadoc/ /usr/share/nginx/html/javadoc

COPY nginx_config/nginx.conf /etc/nginx/nginx.conf
COPY nginx_config/default.conf /etc/nginx/conf.d/default.conf
COPY nginx_config/ssl.conf /app/nginx-confs/ssl.conf

COPY docker-entrypoint.sh /

RUN chown -R nginx:nginx /usr/share/nginx/html/

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]