# talks-scanner
Для запуска фронтенда необходимо из каталога talks-scanner-frontend  сперва прописать npm install,
а после успешной установки зависимостей npm start для локального запуска

Для запуска бекенда необходимо открыть проект talks-scanner-backend помощьб
Intellij Idea. Для работы бэкенда необходима база данных PostgreSQL, запущенная локально.
Версия Java 18 - 19

1. Создать базу данных с названием talks-scanner-dev.
2. Создать конфигурацию для запуска при попощи Intellij Idea.
3. Указать ru.smartup.talksscanner.TalksScannerApplication - main class.
4. Тут же создать переменные окружения перечисленные ниже с примерами.

PG_HOST=localhost;
PG_PASSWORD=123;
PG_PORT=5432;
PG_USER=postgres
