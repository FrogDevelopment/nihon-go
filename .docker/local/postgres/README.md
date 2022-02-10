When need to create a new database after the entrypoint already executed

```shell script
docker exec -it postgres psql -U postgres -c "CREATE DATABASE XXXXX OWNER YYYY;"
```
