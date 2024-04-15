Spring boot java 17 kullanarak RabbitMQ exchange yapılarıyla basit işlemler gerçekleştirildi. 3 farklı exchange ve dead-letter non-durable kuyruklar üzerinden her 3 saniye de bir veri olusturacak sekilde bir islem baslatildi.
resources-> devops altında cluster olarak rabbitmq ve factory-builder yaml dosyaları hazırlandı. bu islemler kubernates podları olarak servis olarak hazirlandi.
factory servisi ayağa kalktığında 3 sn de bir otomatik alarm olusturur isterseniz http://127.0.0.1:8080/create post metoduyla 3 sn de bir olusan fonksiyonu manuel tetikleyebilirsiniz. 
