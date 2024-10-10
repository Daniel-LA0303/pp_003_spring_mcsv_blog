package com.mx.mcsv.user.config.resttemplateconfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-blog")
public interface BlogOpenFeign {

	@GetMapping("/api/blogs/{id}")
	public ResponseEntity<?> getBlog(@PathVariable Long id);

}
