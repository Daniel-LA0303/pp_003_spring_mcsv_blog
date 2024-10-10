package com.mx.mcsv.gateway.config;

import java.nio.charset.StandardCharsets;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.mcsv.gateway.dto.ApiResponse;
import com.mx.mcsv.gateway.dto.TokenDto;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

	public static class Config {
	}

	private WebClient.Builder webClient;

	public AuthFilter(WebClient.Builder webClient) {
		super(Config.class);
		this.webClient = webClient;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (((exchange, chain) -> {
			if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "Missing Authorization Header", HttpStatus.BAD_REQUEST);
			}
			String tokenHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String[] chunks = tokenHeader.split(" ");
			if (chunks.length != 2 || !chunks[0].equals("Bearer")) {
				return onError(exchange, "Invalid Authorization Header", HttpStatus.BAD_REQUEST);
			}
			return webClient.build().post().uri("http://service-auth/auth/validate?token=" + chunks[1]).retrieve()
					.bodyToMono(TokenDto.class).map(t -> {
						t.getToken();
						return exchange;
					}).flatMap(chain::filter);
		}));
	}

	public Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus status) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);

		ApiResponse<Object, String> apiResponse = new ApiResponse<>(status.value(), null, error);
		String errorJson = toJson(apiResponse);
		byte[] bytes = errorJson.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = response.bufferFactory().wrap(bytes);

		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		return response.writeWith(Mono.just(buffer));
	}

	private String toJson(ApiResponse<Object, String> apiResponse) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(apiResponse);
		} catch (JsonProcessingException e) {
			return "{\"error\": \"Internal Server Error\"}";
		}
	}
}