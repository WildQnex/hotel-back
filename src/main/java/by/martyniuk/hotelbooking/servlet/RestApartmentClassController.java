package by.martyniuk.hotelbooking.servlet;

import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.service.ApartmentClassService;
import by.martyniuk.hotelbooking.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/apartmentClasses")
public class RestApartmentClassController {
    @Autowired
    private ApartmentClassService apartmentClassService;

    @GetMapping("/")
    public ResponseEntity<List<ApartmentClass>> getAllApartmentClasses() {
        return ResponseEntity.ok(apartmentClassService.findAllApartmentClasses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentClass> getApartmentClass(@PathVariable long id) {
        Optional<ApartmentClass> apartmentClass = apartmentClassService.findApartmentClassById(id);
        if (apartmentClass.isPresent()) {
            return ResponseEntity.ok(apartmentClass.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/")
    public ResponseEntity updateApartmentClass(@RequestBody ApartmentClass apartmentClass) {
        if (apartmentClassService.updateApartmentClass(apartmentClass)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity addApartmentClass(@RequestBody ApartmentClass apartmentClass) {
        if (apartmentClassService.addApartmentClass(apartmentClass)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteApartmentClass(@PathVariable long id) {
        if (apartmentClassService.deleteApartmentClass(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }
}
