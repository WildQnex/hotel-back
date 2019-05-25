package by.martyniuk.hotelbooking.servlet;

import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.service.ReservationService;
import by.martyniuk.hotelbooking.service.ReservationService;
import by.martyniuk.hotelbooking.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reservations")
public class RestReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.readAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable long id) {
        Optional<Reservation> user = reservationService.readReservation(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/")
    public ResponseEntity updateReservation(@RequestBody Reservation reservation) {
        if (reservationService.updateReservation(reservation)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity addReservation(@RequestBody Reservation reservation) {
        if (reservationService.bookApartment(reservation.getUser(), reservation.getApartment().getApartmentClass().getId(),
                reservation.getCheckInDate(), reservation.getCheckOutDate(), reservation.getPersonAmount())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservation(@PathVariable long id) {
        if (reservationService.deleteReservation(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }
}
