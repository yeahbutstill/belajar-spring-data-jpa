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

## Data Source
- Salah satu keuntungan menggunakan Spring Data JPA dan Spring Boot adalah, semua upacara yang biasa kita lakukan ketika menggunakan JPA, sudah dilakukan oleh Spring Boot 
- Jadi kita tidak perlu membuat DataSource secara manual, karena sudah otomatis dibuat oleh Spring Boot
- https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/jdbc/DataSourceAutoConfiguration.java
- Untuk mengubah konfigurasi DataSource, kita cukup menggunakan application properties saja 
- Kita bisa lihat semua konfigurasinya dengan prefix spring.datasource.*
- https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#appendix.application-properties.data 

## Konfigurasi JPA
- Untuk melakukan konfigurasi JPA, kita juga tidak perlu melakukannya secara manual lagi di file persistance.xml
- Secara otomatis JPA akan menggunakan DataSource di Spring, dan jika kita butuh mengubah konfigurasi, kita bisa menggunakan properties dengan prefix spring.jpa.*
- https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html

## Entity Manager Factory
- Selain DataSource, Spring Boot juga secara otomatis membuatkan bean EntityMangerFactory, sehingga kita tidak perlu membuatnya secara manual 
- Itu semua secara otomatis dibuat oleh Spring Boot
- https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaAutoConfiguration.java

## Tanpa Entity Manager
- Saat kita menggunakan Spring Data JPA, kita akan jarang sekali membuat Entity Manager lagi 
- Bahkan mungkin jarang menggunakan Entity Manager Factory 
- Spring Data membawa konsep Repository (diambil dari buku Domain Driven Design)
- Dimana Repository merupakan layer yang digunakan untuk mengelola data (contohnya di database)

## Repository
- Setiap Entity yang kita buat di JPA, maka kita biasanya akan buatkan Repository nya 
- Repository berbentuk Interface, yang secara otomatis diimplementasikan oleh Spring menggunakan AOP 
- Untuk membuat Repository, kita cukup membuat interface turunan dari JPARepository<T, ID>
- https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html
- Dan kita juga bisa tambahkan annotation @Repository (walaupun tidak wajib)
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Repository.html 

