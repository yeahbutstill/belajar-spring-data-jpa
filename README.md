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

## Crud Repository
- JpaRepository adalah turunan dari interface CrudRepository dan ListCrudRepository, dimana di interface tersebut banyak method yang bisa digunakan untuk melakukan operasi CRUD 
- Kita tidak perlu lagi menggunakan Entity Manager untuk melakukan operasi CRUD, cukup gunakan JpaRepository 
- Ada yang perlu diperhatikan di JpaRepository, method untuk CREATE dan UPDATE digabung dalam satu method save(), yang artinya method save() adalah CREATE or UPDATE
- https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html
- https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/ListCrudRepository.html 

## Declarative Transaction
- Saat kita menggunakan JPA secara manual, kita harus melakukan management transaction secara manual menggunakan EntityManager 
- Spring menyediakan fitur Declarative Transaction, yaitu management transaction secara declarative, yaitu dengan menggunakan annotation @Transactional 
- Annotation ini secara otomatis dibaca oleh Spring AOP, dan akan menjalankan transaction secara otomatis ketika memanggil method yang terdapat annotation @Transactional nya
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/Transactional.html 

## Yang Perlu Diperhatikan
- Saat membuat method dengan annotation @Transactional, karena dia dibungkus oleh Spring AOP, jadi untuk menjalankannya, kita harus memanggil method tersebut dari luar object 
- Misal kita memiliki CategoryService.create() dengan annotation @Transactional, jika kita panggil dari CategoryController, maka Spring AOP akan berjalan, namun jika dipanggil di CategoryService.test() misalnya, maka Spring AOP tidak akan berjalan

## Transaction Propagation
- Saat kita membuat method dengan annotation @Transactional, kita mungkin didalamnya memanggil method @Transactional lainnya 
- Pada kasus seperti itu, ada baiknya kita mengerti tentang attribute propagation pada @Transactional 
- Kita bisa memilih nilai apa yang ingin kita gunakan
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/Propagation.html 

## Programmatic Transaction
- Fitur Declarative Transaction sangat mudah untuk digunakan, karena hanya butuh menggunakan annotation @Transaction
- Namun pada beberapa kasus, misal kode yang kita buat butuh jalan secara async misal nya, maka Declarative Transaction tidak akan berjalan, mau tidak mau biasanya kita akan melakukan manual transaction management lagi
- Kita bisa gunakan cara lama menggunakan Entity Manager, atau kita bisa menggunakan fitur Spring untuk melakukan management transaction secara manual
- Ada beberapa cara untuk melakukan programmatic transaction di Spring

## Transaction Operations
- Pada kasus yang sederhana, kita bisa menggunakan TransactionOperations
- Kita bisa menggunakan bean TransactionOperations yang sudah secara otomatis dibuat oleh Spring Boot
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/support/TransactionOperations.html 

## Platform Transaction Manager
- Jika kita butuh melakukan management transaction secara low level, maka sebenarnya kita bisa menggunakan Entity Manager, namun hal itu tidak disarankan 
- Kita bisa menggunakan Platform Transaction Manager yang sudah disediakan oleh Spring
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html
- Penggunaan ini sangat manual, sehingga kita bisa atur semuanya secara manual

## Query Method
- Saat kita menggunakan EntityManager, kita bisa membuat query menggunakan JPA QL, namun bagaimana jika menggunakan Repository?
- Spring Data menyediakan fitur Query Method, yaitu membuat query menggunakan nama method secara otomatis
- Spring Data akan melakukan penerjemahan secara otomatis dari nama method menjadi JPA QL

## Format Query Method
- Untuk melakukan query yang mengembalikan data lebih dari satu, kita bisa gunakan prefix findAll...
- Untuk melakukan query yang mengembalikan data pertama, kita bisa gunakan prefix findFirst...
- Selanjutnya diikuti dengan kata By dan diikuti dengan operator query nya
- Untuk operator query yang didukung, kita bisa lihat di halaman ini
- https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation 

## Query Relation
- Saat kita belajar JPA, kita bisa melakukan query ke relasi Entity atau Embedded field secara otomatis menggunakan tanda . (titik)
- Di Spring Data Repository, kita bisa gunakan _ (garis bawah) untuk menyebutkan bahwa itu adalah tanda . (titik) nya 
- Misal ProductRepository.findAllByCategory_Name(String)

## Sorting
- Spring Data Repository juga memiliki fitur untuk melakukan Sorting, caranya kita bisa tambahkan parameter Sort pada posisi parameter terakhir
- https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Sort.html 

## Paging
- Selain Sort, Spring Data Repository juga mendukung paging seperti di EntityManager 
- Caranya kita bisa tambahkan parameter Pageable di posisi terakhir parameter 
- Pageable adalah sebuah interface, biasanya kita akan menggunakan PageRequest sebagai class implementasinya 
- Dan jika sudah menggunakan Pageable, kita tidak perlu lagi menggunakan Sort, karena sudah bisa dihandle oleh Pageable
- https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/PageRequest.html 

## Page Result
- Saat kita menggunakan Paging, kadang kita ingin tahu seperti jumlah total data hasil query, dan juga total page nya 
- Hal ini biasanya kita akan lakukan dengan cara manual dengan cara menghitung count dari hasil total hasil query tanpa paging 
- Untungnya, Spring Data JPA menyediakan return value berupa Page<T>, dimana secara otomatis akan diambil informasi total data dan total page nya
- https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Page.html 

## Count Query Method
- JPA Repository juga bisa digunakan untuk membuat count query method
- Cukup gunakan prefix method countBy...
- Selebihnya kita bisa membuat format seperti Query Method biasanya