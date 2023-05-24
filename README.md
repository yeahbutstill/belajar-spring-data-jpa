# belajar-spring-data-jpa

## Pengenalan Spring Data JPA
- Spring Data JPA adalah fitur di Spring yang digunakan untuk mempermudah kita membuat aplikasi menggunakan JPA 
- Dengan menggunakan Spring Data JPA, kita tidak perlu membuat banyak hal secara manual dilakukan seperti jika menggunakan JPA secara manual
- https://spring.io/projects/spring-data-jpa 

## Spring Data
- Spring Data JPA sendiri merupakan bagian dari Spring Data Project 
- Spring Data Project adalah project di Spring yang digunakan untuk mempermudah kita melakukan manipulasi data di database 
- Konsep yang digunakan di Spring Data hampir sama di semua tipe project, oleh karena itu jika nanti kita sudah mengerti tentang Spring Data JPA, ketika akan menggunakan fitur Spring Data lainnya, maka tidak akan kesulitan, seperti Spring Data MongoDB, Spring Data Elasticsearch, Spring Data Redis, dan lain-lain
- https://spring.io/projects/spring-data 

## Run PostgreSQL with Docker
```shell
docker run --rm --name belajar-spring-data-jpa-db -e POSTGRES_DB=belajar_spring_data_jpa \
-e POSTGRES_USER=dani \
-e POSTGRES_PASSWORD=dani \
-e PGDATA=/var/lib/postgresql/data/pgdata \
-v "$PWD/belajar-spring-data-jpa-db-data:/var/lib/postgresql/data" \
-p 5432:5432 \
postgres:15
```