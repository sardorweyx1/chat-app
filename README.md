## Задача

---

Требуется написать чат-сервер.  Сервис должен предоставлять REST API. UI не нужен.****

## Требования

---

- Язык программирования - Java 11 или Java 17
- Фреймворк использовать Spring Boot
- Сервер должен быть доступен на порту 8080
- Для сборки проекта использовать Maven
- В качестве хранилища данных нужно использовать Postgres
- Визуализация данных в виде пользовательского интерфейса (веб-приложение, мобильное приложение) не требуется – достаточно только обозначенного ниже API, доступного из командной строки. Однако простор фантазии не ограничиваем, покуда соблюдаются основные требования
- Финальную версию нужно выложить на [github.com](http://github.com). Просьба не делать форк этого репозитория.
- Readme файл должен  содержать простую инструкцию для запуска. (в идеале — с возможностью запустить через `docker-compose up`, но это необязательно)
- Добавить валидация полей: размер изображений не должно превышать 100 мб

## ****Основные сущности****

---

Ниже перечислены основные сущности, которыми должен оперировать сервер.****

### ****User****

Пользователь приложения. Имеет следующие свойства:

- **id** - уникальный идентификатор пользователя (может быть как числом, так и строковым – как удобнее)
- **username** - уникальное имя пользователя
- **created_at** - время создания пользователя

### ****Chat****

Отдельный чат. Имеет следующие свойства:

- **id** - уникальный идентификатор чата
- **name** - уникальное имя чата
- **users** - список пользователей в чате, отношение многие-ко-многим
- **created_at** - время создания

### ****Message****

Сообщение в чате. Имеет следующие свойства:

- **id** - уникальный идентификатор сообщения
- **chat** - ссылка на идентификатор чата, в который было отправлено сообщение
- **author** - ссылка на идентификатор отправителя сообщения, отношение многие-к-одному
- **content_type** - тип сообщения
- **content** - текст отправленного сообщения или ссылка на файл
- **created_at** - время создания

### MessageType

Тип сообщения в чате. Ниже перечислено все типы сообщений

- IMAGE
- TEXT
## ****Основные API методы****

---

Методы обрабатывают HTTP POST запросы c телом, содержащим все необходимые параметры в JSON.****

### ****Добавить нового пользователя****

Запрос:

```jsx
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"username": "user_1"}' \
  http://localhost:8080/users/add
```

Ответ: `id` созданного пользователя или HTTP-код ошибки + описание ошибки.****

### ****Создать новый чат между пользователями****

Запрос:

```jsx
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"name": "chat_1", "users": ["<USER_ID_1>", "<USER_ID_2>"]}' \
  http://localhost:8080/chats/add
```

Ответ: `id` созданного чата или HTTP-код ошибки или HTTP-код ошибки + описание ошибки.

Количество пользователей в чате не ограничено.****

### ****Отправить сообщение в чат от лица пользователя****

Запрос:

```jsx
curl --request POST \
  --url http://localhost:8080/messages/add \
  --header 'Content-Type: application/json' \
  --data '{"chat": "<CHAT_ID>", "author": "<USER_ID>", "type": "text", "content": "hi"}'

curl --request POST \
  --url http://localhost:8080/messages/add \
  --header 'Content-Type: application/json' \
  --data '{"chat": "<CHAT_ID>", "author": "<USER_ID>", "type": "images", 
"content": "AGQAbwBtAHAAZABmACAAPAA5AGUAYQA4ADUAMgBjADQAPgAgACsAIABDAFAARABGKQovQ3JlYXRpb25EYXRlIChEOjIwMjAwMjA2MDk1OTI3KzA1JzAwJykKL01vZERhdGUgKEQ6MjAyMDAyMDYwOTU5MjcrMDUnMDAnKQo+PgplbmRvYmoKNiAwIG9iago8PCAvVHlwZSAvUGFnZQovTWVkaWFCb3ggWzAuMDAwIDAuMDAwIDYxMi4wMDAgNzkyLjAwMF0KL1BhcmVudCAzIDAgUgovQ29udGVudHMgNyAwIFIKPj4KZW5kb2JqCjcgMCBvYmoKPDwgL0ZpbHRlciAvRmxhdGVEZWNvZGUKL0xlbmd0aCAxMDEyID4+CnN0cmVhbQp4nK1Xz2/bNhjlgScfhx6GAV259tICA8OfIuUkjpOmyToEDbr61u4wrGgvK4p126H/Tf/UvY+SLFn+7AiBYYiSP5GPj+Tj46fZ3zNbW+2iUfQLURtjlA/a2EolU+k6BvXnJ3X00qnLz7PXM1MqDMsvH2cXq65JVUeda6dW79XRlVW1Nmr1Qam3T+UjuZDH8rm8llGeyiyjULgvpMPl5QL/ltLIhDLKX9o6t6WOQXlWWmfUpFqXeCKcczkH4iu8D/h3Ky9QHsubgnuOfzfPflerX9WL1ezo+o1VH/9h6P92TS8dvezGkJM2daU+qZiSrnNYR/4qEecpUmkfKIJGqaoGAfWBAD0B4h6aXm1F/fUlJm07CCprClHHEYUmMpXCmPo0pO0IoNglv9+EjobHUJ8ynSQ3ABBQlXTKnpHbQ0jgBHJYQhwJkrm",
	"ext": "png"}'
```

Ответ: `id` созданного сообщения или HTTP-код ошибки + описание ошибки.****

### ****Получить список чатов конкретного пользователя****

Запрос:

```jsx
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"user": "<USER_ID>"}' \
  http://localhost:8080/chats/get
```

Ответ: cписок всех чатов со всеми полями, отсортированный по времени создания последнего сообщения в чате (от позднего к раннему). Или HTTP-код ошибки + описание ошибки.****

### ****Получить список сообщений в конкретном чате****

Запрос:

```jsx
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"chat": "<CHAT_ID>"}' \
  http://localhost:8080/messages/get
```

Ответ: список всех сообщений чата со всеми полями, отсортированный по времени создания сообщения (от раннего к позднему). Или HTTP-код ошибки + описание ошибки.

Использование объектных хранилищ для хранения изображений (AWS S3, MinIO, Yandex S3)# chat-app
