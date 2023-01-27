# mercadolivro
POC de backend em kotlin
<br><br>
Comando para criar serviço mysql
> docker run --name mysql8 -v /Users/Documents/mysql/8:/var/lib/mysql -p 3306:3306 -d -e MYSQL_ROOT_PASSWORD=root mysql:8.0

onde (/Users/Documents/mysql/8) é a pasta local onde armazenaremos as informações do banco

### iniciando a aplicação

> gradle bootRun