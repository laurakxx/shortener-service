# URL Shortener

URL Shortener — backend-сервис для создания коротких ссылок, перенаправления пользователей на исходные URL и сбора статистики переходов.

Проект состоит из основного сервиса сокращения ссылок и отдельного analytics-сервиса.

## Возможности

- создание коротких ссылок;
- redirect по short code;
- подсчёт переходов;
- просмотр статистики ссылки;
- хранение ссылок в PostgreSQL;
- кэширование через Redis;
- передача событий переходов через Kafka;
- отдельный analytics-service;
- мониторинг через Prometheus и Grafana;
- health check через Spring Boot Actuator;
- Swagger UI;
- Docker Compose;
- Kubernetes manifests;
- deployment на VPS через Nginx.

## Технологии

### Backend

- Java 21
- Spring Boot 4.1.1
- Spring Web
- Spring Data JPA
- PostgreSQL
- Redis
- Apache Kafka
- Gradle

### Monitoring

- Spring Boot Actuator
- Micrometer
- Prometheus
- Grafana

### Infrastructure

- Docker
- Docker Compose
- Kubernetes
- Nginx
- Ubuntu VPS

## Архитектура

Основной сервис отвечает за:

- создание коротких ссылок;
- redirect;
- работу с Redis;
- хранение ссылок в PostgreSQL;
- отправку событий переходов в Kafka.

Analytics Service:

- получает события переходов из Kafka;
- хранит данные аналитики в отдельной PostgreSQL;
- предоставляет статистику по ссылкам.

Сервисы используют отдельные базы данных.

## Структура проекта

```text
shortener-service/
├── analytics-service/
├── frontend/
├── k8s/
├── prometheus/
├── src/
├── Dockerfile
├── docker-compose.yml
├── build.gradle
└── settings.gradle
```
## Запуск через Docker Compose

Создать `.env` на основе файла:

```text
.env.example
```

Запустить приложение:

```bash
docker compose up -d --build
```

Проверить состояние контейнеров:

```bash
docker compose ps
```

## API

Swagger UI при локальном запуске:

```text
http://localhost:8080/swagger-ui/index.html
```

Health check:

```text
http://localhost:8080/actuator/health
```

## Frontend

Простой frontend находится в:

```text
frontend/index.html
```

Он позволяет:

- создать короткую ссылку;
- перейти по короткой ссылке;
- посмотреть количество переходов.

Frontend использует адрес текущего сервера через `window.location.origin`, поэтому может работать как локально, так и на VPS без жёстко заданного IP-адреса.

## Kubernetes

Kubernetes manifests находятся в директории:

```text
k8s/
```

В Kubernetes используются:

- Deployment;
- Service;
- ConfigMap;
- Secret;
- NodePort.

Во время тестирования проверялись:

- self-healing — Kubernetes автоматически создаёт новый Pod после удаления старого;
- scaling — количество реплик приложения можно изменять через Deployment.

## VPS Deployment

Проект развёрнут на Ubuntu VPS.

Внешний HTTP-трафик проходит через Nginx:

```text
Internet
   |
   v
Nginx :80
   |
   v
127.0.0.1:8080
   |
   v
Spring Boot in Docker
```

Spring Boot напрямую в интернет на порту `8080` не публикуется. Порт доступен только через localhost VPS:

```text
127.0.0.1:8080
```

Docker Compose запускает следующие сервисы:

- shortener-service;
- analytics-service;
- PostgreSQL для shortener-service;
- PostgreSQL для analytics-service;
- Redis;
- Kafka;
- ZooKeeper;
- Prometheus;
- Grafana.

### Публичные endpoints

Frontend:

```text
http://161.104.54.16/app/
```

Swagger UI:

```text
http://161.104.54.16/swagger-ui/index.html
```

Health check:

```text
http://161.104.54.16/actuator/health
```

## Environment Variables

Секретные значения хранятся в файле `.env`.

Файл `.env` не добавляется в Git.

Пример необходимых переменных находится в:

```text
.env.example
```

Используются отдельные настройки для:

- основной PostgreSQL;
- PostgreSQL analytics-service;
- Grafana.

## Monitoring

Prometheus собирает метрики приложения через Spring Boot Actuator и Micrometer.

Grafana используется для визуализации метрик.

В проекте отслеживаются, в частности:

- количество созданных коротких ссылок;
- количество переходов;
- HTTP-метрики;
- JVM-метрики.

## Проверка работы

После запуска можно проверить health endpoint:

```bash
curl http://localhost:8080/actuator/health
```

При успешном запуске приложение возвращает статус:

```json
{
  "status": "UP"
}
```

Для VPS запрос проходит через Nginx:

```text
http://161.104.54.16/actuator/health
```
