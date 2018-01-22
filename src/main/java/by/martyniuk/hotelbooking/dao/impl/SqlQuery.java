package by.martyniuk.hotelbooking.dao.impl;

class SqlQuery {

    static final String SQL_SELECT_ALL_APARTMENTS_CLASSES = "SELECT `id_apartment_class`, `type`, `rooms_amount`," +
            " `max_capacity`, `cost_per_night`, `cost_per_person`, `animal_cost`, `image_path` FROM `hotel_booking`.`apartment_class`";

    static final String SQL_FIND_APARTMENT_CLASS_BY_ID = "SELECT `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `cost_per_night`, `cost_per_person`," +
            " `animal_cost`, `image_path` FROM `hotel_booking`.`apartment_class` " +
            " WHERE `id_apartment_class` = ?";

    //-----------------

    static final String SQL_SELECT_ALL_APARTMENTS = "SELECT `id_apartment`, `number`, `floor`, `animals_allowed`," +
            " `smoking_allowed`, `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `cost_per_night`, `cost_per_person`," +
            " `animal_cost`, `image_path` FROM `hotel_booking`.`apartment` LEFT JOIN `apartment_class` " +
            " ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk`";


    static final String SQL_FIND_APARTMENT_BY_ID = "SELECT `id_apartment`, `number`, `floor`, `animals_allowed`," +
            " `smoking_allowed`, `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `cost_per_night`, `cost_per_person`," +
            " `animal_cost`, `image_path` FROM `hotel_booking`.`apartment` LEFT JOIN `apartment_class` " +
            " ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk` WHERE `id_apartment` = ?";

    static final String SQL_FIND_APARTMENT_BY_CLASS_ID = "SELECT `id_apartment`, `number`, `floor`, `animals_allowed`," +
            " `smoking_allowed`, `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `cost_per_night`, `cost_per_person`," +
            " `animal_cost`, `image_path` FROM `hotel_booking`.`apartment` LEFT JOIN `apartment_class` " +
            " ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk` WHERE `apartment_class_id_fk` = ?";

    static final String SQL_INSERT_APARTMENT = "INSERT INTO apartment(`class`,`number`) VALUES(?,?)";

    //-----------------


    static final String SQL_RESERVE_APARTMENT = "INSERT INTO `hotel_booking`.`reservation` (`check_in_date`, `check_out_date`, " +
            "`order_time`, `person_amount`, `cost_per_person`, `cost_per_night`, `cost_animal`, `total_cost`, `user_id_fk`, " +
            "`apartment_id_fk`, `status_id_fk`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

    static final String SQL_CHECK_AVAILABILITY = "SELECT `id_reservation` " +
            "FROM `hotel_booking`.`reservation` " +
            "WHERE (NOT ((`check_in_date` > ? AND `check_in_date` >= ?) OR (check_out_date <= ? AND `check_out_date` < ?))) " +
            "AND (`apartment_id_fk` = ?) " +
            "AND ((`status_id_fk` = (SELECT `id_status` FROM `hotel_booking`.`status` WHERE UPPER(`status`) LIKE UPPER(?)))" +
            "OR (`status_id_fk` = (SELECT `id_status` FROM `hotel_booking`.`status` WHERE UPPER(`status`) LIKE UPPER(?))))";

    static final String SQL_SELECT_ALL_RESERVATIONS = "SELECT `id_reservation`, `check_in_date`, `check_out_date`, `order_time`," +
            " `person_amount`, `id_user`, `first_name`, `middle_name`, `last_name`, `balance`," +
            " `email`, `phone_number`, `password`, `role`, `active`, `id_apartment`, `number`, `floor`, `animals_allowed`," +
            " `smoking_allowed`, `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `reservation`.`cost_per_night` AS `reservation_cost_per_night`, " +
            " `reservation`.`cost_per_person` AS `reservation_cost_per_person`, `reservation`.`cost_animal` AS `reservation_animal_cost`, " +
            " `image_path`, `id_status`, `status`, `total_cost`, `apartment_class`.`cost_per_night` AS `apartment_cost_per_night`, " +
            " `apartment_class`.`cost_per_person` AS `apartment_cost_per_person`, `apartment_class`.`animal_cost` AS `apartment_animal_cost` " +
            "FROM `hotel_booking`.`reservation` " +
            "LEFT JOIN (`user` LEFT JOIN `role` ON `role`.`id_role` = `user`.`role_id_fk`) ON `user`.`id_user` = `reservation`.`user_id_fk` " +
            "LEFT JOIN (`apartment` LEFT JOIN `apartment_class` ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk`) " +
            "ON `apartment`.`id_apartment` = `reservation`.`apartment_id_fk` " +
            "LEFT JOIN `status` ON `status`.`id_status` = `reservation`.`status_id_fk`";

    static final String SQL_SELECT_ALL_RESERVATIONS_BY_STATUS = "SELECT `id_reservation`, `check_in_date`, `check_out_date`, `order_time`," +
            " `person_amount`, `id_user`, `first_name`, `middle_name`, `last_name`, `balance`," +
            " `email`, `phone_number`, `password`, `role`, `active`, `id_apartment`, `number`, `floor`, `animals_allowed`," +
            " `smoking_allowed`, `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `reservation`.`cost_per_night` AS `reservation_cost_per_night`, " +
            " `reservation`.`cost_per_person` AS `reservation_cost_per_person`, `reservation`.`cost_animal` AS `reservation_animal_cost`, " +
            " `image_path`, `id_status`, `status`, `total_cost`, `apartment_class`.`cost_per_night` AS `apartment_cost_per_night`, " +
            " `apartment_class`.`cost_per_person` AS `apartment_cost_per_person`, `apartment_class`.`animal_cost` AS `apartment_animal_cost` " +
            "FROM `hotel_booking`.`reservation` " +
            "LEFT JOIN (`user` LEFT JOIN `role` ON `role`.`id_role` = `user`.`role_id_fk`) ON `user`.`id_user` = `reservation`.`user_id_fk` " +
            "LEFT JOIN (`apartment` LEFT JOIN `apartment_class` ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk`) " +
            "ON `apartment`.`id_apartment` = `reservation`.`apartment_id_fk` " +
            "LEFT JOIN `status` ON `status`.`id_status` = `reservation`.`status_id_fk`" +
            "WHERE UPPER(`status`) LIKE UPPER(?)";

    static final String SQL_SELECT_ALL_RESERVATIONS_BY_USER_ID = "SELECT `id_reservation`, `check_in_date`, `check_out_date`, `order_time`," +
            " `person_amount`, `id_user`, `first_name`, `middle_name`, `last_name`, `balance`," +
            " `email`, `phone_number`, `password`, `role`, `active`, `id_apartment`, `number`, `floor`, `animals_allowed`," +
            " `smoking_allowed`, `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `reservation`.`cost_per_night` AS `reservation_cost_per_night`, " +
            " `reservation`.`cost_per_person` AS `reservation_cost_per_person`, `reservation`.`cost_animal` AS `reservation_animal_cost`, " +
            " `image_path`, `id_status`, `status`, `total_cost`, `apartment_class`.`cost_per_night` AS `apartment_cost_per_night`, " +
            " `apartment_class`.`cost_per_person` AS `apartment_cost_per_person`, `apartment_class`.`animal_cost` AS `apartment_animal_cost` " +
            "FROM `hotel_booking`.`reservation` " +
            "LEFT JOIN (`user` LEFT JOIN `role` ON `role`.`id_role` = `user`.`role_id_fk`) ON `user`.`id_user` = `reservation`.`user_id_fk` " +
            "LEFT JOIN (`apartment` LEFT JOIN `apartment_class` ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk`) " +
            "ON `apartment`.`id_apartment` = `reservation`.`apartment_id_fk` " +
            "LEFT JOIN `status` ON `status`.`id_status` = `reservation`.`status_id_fk`" +
            "WHERE `id_user` = ?";

    static final String SQL_SELECT_RESERVATION_BY_ID = "SELECT `id_reservation`, `check_in_date`, `check_out_date`, `order_time`," +
            " `person_amount`, `id_user`, `first_name`, `middle_name`, `last_name`, `balance`," +
            " `email`, `phone_number`, `password`, `role`, `active`, `id_apartment`, `number`, `floor`, `animals_allowed`," +
            " `smoking_allowed`, `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `reservation`.`cost_per_night` AS `reservation_cost_per_night`, " +
            " `reservation`.`cost_per_person` AS `reservation_cost_per_person`, `reservation`.`cost_animal` AS `reservation_animal_cost`, " +
            " `image_path`, `id_status`, `status`, `total_cost`, `apartment_class`.`cost_per_night` AS `apartment_cost_per_night`, " +
            " `apartment_class`.`cost_per_person` AS `apartment_cost_per_person`, `apartment_class`.`animal_cost` AS `apartment_animal_cost` " +
            "FROM `hotel_booking`.`reservation` " +
            "LEFT JOIN (`user` LEFT JOIN `role` ON `role`.`id_role` = `user`.`role_id_fk`) ON `user`.`id_user` = `reservation`.`user_id_fk` " +
            "LEFT JOIN (`apartment` LEFT JOIN `apartment_class` ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk`) " +
            "ON `apartment`.`id_apartment` = `reservation`.`apartment_id_fk` " +
            "LEFT JOIN `status` ON `status`.`id_status` = `reservation`.`status_id_fk`" +
            "WHERE `id_reservation` = ?";

    static final String SQL_UPDATE_RESERVATION = "UPDATE `hotel_booking`.`reservation` SET `check_in_date` = ?, `check_out_date` = ?," +
            " `order_time` = ?, `person_amount` = ?, `cost_per_person` = ?, `cost_per_night` = ?, `cost_animal` = ?, `total_cost` = ?," +
            " `user_id_fk` = ?, `apartment_id_fk` = ?, `status_id_fk` = (SELECT `id_status` FROM `status` WHERE UPPER(`status`) LIKE UPPER(?)) " +
            " WHERE `id_reservation` = ?";

    //---------------


    static final String SQL_SELECT_ALL_USERS = "SELECT `id_user`, `first_name`, `middle_name`, `last_name`, `balance`, `email`, " +
            "`phone_number`, `password`, `role`, `active` FROM `hotel_booking`.`user` LEFT JOIN `role` " +
            "ON `role`.`id_role` = `user`.`role_id_fk`";

    static final String SQL_FIND_USER_BY_MAIL = "SELECT `id_user`, `first_name`, `middle_name`, `last_name`, `balance`, `email`, " +
            "`phone_number`, `password`, `role`, `active` FROM `hotel_booking`.`user` LEFT JOIN `role` " +
            "ON `role`.`id_role` = `user`.`role_id_fk` WHERE `user`.`email`	= ?";

    static final String SQL_INSERT_USER = "INSERT INTO `hotel_booking`.`user` (`first_name`, `middle_name`, `last_name`, " +
            "`balance`, `email`, `phone_number`, `password`, `role_id_fk`, `active`)  VALUES (?,?,?,?,?,?,?,(SELECT `id_role` FROM `role` WHERE UPPER(`role`) LIKE UPPER(?)),?)";

    static final String SQL_UPDATE_USER = "UPDATE `hotel_booking`.`user` SET `first_name` = ?, `middle_name` = ?, `last_name` = ?, " +
            "`balance` = ?, `email` = ?, `phone_number` = ?, `password` = ?, `active` = ?, " +
            "`role_id_fk` = (SELECT `id_role` FROM `role` WHERE UPPER(`role`) LIKE UPPER(?)) WHERE `id_user` = ?";
}
