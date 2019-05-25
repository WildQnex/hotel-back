package by.martyniuk.hotelbooking.servlet;

import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.service.UserService;
import by.martyniuk.hotelbooking.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/")
    public ResponseEntity updateUser(@RequestBody User user) {
        if (Validator.validateUser(user) && userService.updateUser(user)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity addUser(@RequestBody User user) {
        System.out.println("Came id: " + user.getEmail());
        if (userService.addUser(user)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable long id) {
        System.out.println("Came id: " + id);
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }
}
