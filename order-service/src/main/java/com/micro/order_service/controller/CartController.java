package com.micro.order_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.micro.order_service.config.JwtProvider;
import com.micro.order_service.dto.ProductDTO;
import com.micro.order_service.models.Cart;
import com.micro.order_service.models.CartItem;
import com.micro.order_service.request.CartItemRequest;
import com.micro.order_service.service.CartItemService;
import com.micro.order_service.service.CartService;
import com.micro.order_service.service.client.ProductClient;

import reactor.core.publisher.Mono;

@RestController
public class CartController {
    private final Logger log = LoggerFactory.getLogger(CartController.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ProductClient productClient;

    @PostMapping("/private/order/cart/addtocart")
    public ResponseEntity<CartItem> addToCart(@RequestHeader("Authorization") String jwt,
            @RequestBody CartItemRequest cartItemRequest) throws Exception {
        Long userId = JwtProvider.getUserIdFromJwtToken(jwt);

        CartItem cartItem = cartItemService.addToCart(userId, cartItemRequest);

        log.info("ADD TO CART ----------------------------------"); 

        return new ResponseEntity<CartItem>(cartItem, HttpStatus.OK);
    }

    @GetMapping("/private/order/cart")
    public ResponseEntity<Cart> getUserCart(@RequestHeader("Authorization") String jwt){
        Long userId = JwtProvider.getUserIdFromJwtToken(jwt);
        return new ResponseEntity<Cart>(cartService.getUserCart(userId), HttpStatus.OK);
    }

    @PutMapping("/private/order/cart/increase/{cartItemId}")
    public ResponseEntity<CartItem> increaseQuantity(@PathVariable Long cartItemId) throws Exception{
        CartItem cartItem = cartItemService.increaseQuantity(cartItemId);

        return new ResponseEntity<CartItem>(cartItem, HttpStatus.OK);
    }

    @PutMapping("/private/order/cart/decrease/{cartItemId}")
    public ResponseEntity<CartItem> decreaseQuantity(@PathVariable Long cartItemId) throws Exception{
        CartItem cartItem = cartItemService.decreaseQuantity(cartItemId);

        return new ResponseEntity<CartItem>(cartItem, HttpStatus.OK);
    }

    @DeleteMapping("/private/order/cart/remove/{cartItemId}")
    public ResponseEntity<String> removeCartItem(@RequestHeader("Authorization") String jwt, @PathVariable Long cartItemId){
        Long userId = JwtProvider.getUserIdFromJwtToken(jwt);

        String message = cartService.removeCartItem(cartItemId, userId);

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

    @PutMapping("/private/order/cart/clear")
    public ResponseEntity<String> clearCart(@RequestHeader("Authorization") String jwt){
        Long userId = JwtProvider.getUserIdFromJwtToken(jwt);

        String message = cartService.clearCart(userId);

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

    /////////////////////////////HTTP???????????????????/////////////////////////////////////////////////

    @GetMapping("/public/order/feign/{productId}")
    public ProductDTO getProductFeign(@PathVariable Long productId) {
        return productClient.getProduct(productId);
    }

    @GetMapping("/public/resttpl/{productId}")
    public ProductDTO getProduct(@PathVariable Long productId) {
        String url = "http://localhost:8082/public/product/" + productId;
        return restTemplate.getForObject(url, ProductDTO.class);
    }

    @GetMapping("/public/webclient/{productId}")
    public Mono<ProductDTO> getProductWebClient(@PathVariable Long productId) {
        return webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8082)
                        .path("/public/product/{productId}")
                        .build(productId))
                .retrieve()
                .bodyToMono(ProductDTO.class);
    }

    @PostMapping("/public/order")
    public ProductDTO creatProduct(@RequestBody ProductDTO productDTO, @RequestHeader("Authorization") String jwt) {
        String url = "http://localhost:8082/private/product";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwt);

        HttpEntity<ProductDTO> request = new HttpEntity<>(productDTO, headers);

        ResponseEntity<ProductDTO> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request,
                ProductDTO.class);
        ProductDTO responseBody = responseEntity.getBody();

        return responseBody;
    }

    @PostMapping("/private/testt")
    public ResponseEntity<String> test(@RequestBody String hello) {
        // String message = productService.deleteProduct(id);

        return new ResponseEntity<String>(hello, HttpStatus.OK);
    }

}
