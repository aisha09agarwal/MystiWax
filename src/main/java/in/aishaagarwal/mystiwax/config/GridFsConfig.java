package in.aishaagarwal.mystiwax.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter; // <-- ADD THIS IMPORT
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class GridFsConfig {

    @Bean
    public GridFsTemplate gridFsTemplate(
        MongoDatabaseFactory dbFactory, 
        MongoConverter converter 
    ) {
        // Change "MystiWax" to "mystiwax"
        return new GridFsTemplate(dbFactory, converter, "mystiwax"); 
    }
}