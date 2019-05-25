package by.martyniuk.hotelbooking.servlet;

import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/apartments")
public class RestApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @GetMapping("/")
    public ResponseEntity<List<Apartment>> getAllApartments() {
        return ResponseEntity.ok(apartmentService.findAllApartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apartment> getApartment(@PathVariable long id) {
        Optional<Apartment> apartment = apartmentService.getApartment(id);
        if (apartment.isPresent()) {
            return ResponseEntity.ok(apartment.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/")
    public ResponseEntity updateApartment(@RequestBody Apartment apartment) {
        if (apartmentService.updateApartment(apartment)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity addApartment(@RequestBody Apartment apartment) {
        if (apartmentService.insertApartment(apartment)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteApartment(@PathVariable long id) {
        if (apartmentService.deleteApartment(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }
}
