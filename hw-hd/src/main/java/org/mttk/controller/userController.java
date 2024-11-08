package org.mttk.controller;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.mttk.config.JwtProperties;
import org.mttk.pojo.dto.*;
import org.mttk.pojo.entity.Book;
import org.mttk.pojo.entity.User;
import org.mttk.pojo.vo.*;
import org.mttk.service.userService;
import org.mttk.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class userController {
    @Autowired
    private userService auserService;
    @Autowired
    private JwtProperties jwtProperties;
    @PostMapping("/login")
    public ResponseEntity<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("登录:{}", userLoginDTO);
        User user = auserService.login(userLoginDTO);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userid",user.getId().toString());
        String token = JwtUtil.createJWT(
                jwtProperties.getSecretKey(),
                jwtProperties.getTtl(),
                claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .token(token)
                .id(user.getId())
                .build();
        return ResponseEntity.ok(userLoginVO);
    }
    //book
    /*
    1.list
    2.buy
    3.addcart
     */
    @GetMapping("/book/list")
    public ResponseEntity<List<Book>> getBookList(HttpServletRequest request){
        List<Book> list = auserService.getBookList();
        return ResponseEntity.ok(list);
    }
    @PostMapping("/book/buy")
    public ResponseEntity<String> buyBook(HttpServletRequest request,@RequestBody BookBuyOrCartDTO bookBuyOrCartDTO){
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        log.info("jwt校验:{}", token);
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer userId = Integer.valueOf(claims.get("userid").toString());
        log.info("当前员工id：", userId);
        auserService.buyBook(userId,bookBuyOrCartDTO.getBookId(),bookBuyOrCartDTO.getQuantity());
        return ResponseEntity.ok("ok");
    }
    @PostMapping("/book/addcart")
    public ResponseEntity<String> addBook(HttpServletRequest request,@RequestBody BookBuyOrCartDTO bookBuyOrCartDTO){
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        log.info("jwt校验:{}", token);
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer userId = Integer.valueOf(claims.get("userid").toString());
        log.info("当前员工id：", userId);
        auserService.addBook(userId,bookBuyOrCartDTO.getBookId(),bookBuyOrCartDTO.getQuantity());
        return ResponseEntity.ok("ok");
    }
    //cart
    /*
    1. pay
    2. list
    3.update
    4.delete
     */
    @GetMapping("/cart/list")
    public ResponseEntity<List<CartListVO>> getCartList(HttpServletRequest request){
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        log.info("jwt校验:{}", token);
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer userId = Integer.valueOf(claims.get("userid").toString());
        log.info("当前员工id：", userId);
        List<CartListVO> cartList = auserService.getCartList(userId);
        return ResponseEntity.ok(cartList);
    }
    @GetMapping("/cart/pay/{cartId}")
    public ResponseEntity<String> payCart(HttpServletRequest request,@PathVariable Integer cartId){
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer userId = Integer.valueOf(claims.get("userid").toString());
        log.info("当前员工id：", userId);
        auserService.payCart(userId,cartId);
        return ResponseEntity.ok("ok");
    }
    @PostMapping("/cart/update")
    public ResponseEntity<String> updateCart(HttpServletRequest request,@RequestBody CartUpdateDTO cartUpdateDTO){
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer userId = Integer.valueOf(claims.get("userid").toString());
        log.info("当前员工id：", userId);
        auserService.updateCart(userId,cartUpdateDTO.getCartId(),cartUpdateDTO.getQuantity());
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/cart/delete/{cartId}")
    public ResponseEntity<String> deleteCart(HttpServletRequest request,@PathVariable Integer cartId){
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer userId = Integer.valueOf(claims.get("userid").toString());
        log.info("当前员工id：", userId);
        auserService.deleteCart(cartId);
        return ResponseEntity.ok("ok");
    }
    //order
    /*
    1.delete
    2.list
     */
    @GetMapping("/order/list")
    public ResponseEntity<List<OrderListVO>> getOrderList(HttpServletRequest request){
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer userId = Integer.valueOf(claims.get("userid").toString());
        log.info("当前员工id：", userId);
        List<OrderListVO> orderListVOS =  auserService.getOrderList(userId);
        return ResponseEntity.ok(orderListVOS);
    }
    @GetMapping("/order/delete/{orderId}")
    public ResponseEntity<String> deleteOrder(HttpServletRequest request,@PathVariable Integer orderId){
        auserService.deleteOrder(orderId);
        return ResponseEntity.ok("ok");
    }
    //self
    /*
    1.list
    2.addbalance
    3.update
     */
    @GetMapping("/self/list")
    public ResponseEntity<User> getSelfList(HttpServletRequest request){
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer userId = Integer.valueOf(claims.get("userid").toString());
        log.info("当前员工id：", userId);
        User user = auserService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
    @GetMapping("/self/add/{count}")
    public ResponseEntity<String> getSelfAdd(HttpServletRequest request, @PathVariable Long count){
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer userId = Integer.valueOf(claims.get("userid").toString());
        log.info("当前员工id：", userId);
        auserService.selfAdd(userId,count);
        return ResponseEntity.ok("ok");
    }
    @PostMapping("/self/update")
    public ResponseEntity<String> updateSelf(HttpServletRequest request,@RequestBody UserUpdateDTO updateDTO){
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer userId = Integer.valueOf(claims.get("userid").toString());
        log.info("当前员工id：", userId);
        String username = updateDTO.getUsername();
        String password = updateDTO.getPassword();
        String email = updateDTO.getEmail();
        auserService.updateSelf(userId,username,password,email);
        return ResponseEntity.ok("ok");
    }





























}
