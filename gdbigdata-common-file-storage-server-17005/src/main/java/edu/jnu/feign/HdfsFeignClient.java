package edu.jnu.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月01日 19:57
 */
@FeignClient(name = "HdfsService", url = "http://localhost:8899")
public interface HdfsFeignClient {

    @PostMapping(value = "/api/v1/da/hdfs/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map upload();
}
