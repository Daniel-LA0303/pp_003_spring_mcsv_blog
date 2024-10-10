package com.mx.mcsv.user.config.resttemplateconfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-blog", url = "http://localhost:8082/api/blogs")
public interface BlogOpenFeign {

	@GetMapping("/{id}")
	public ResponseEntity<?> getBlog(@PathVariable Long id);

}
