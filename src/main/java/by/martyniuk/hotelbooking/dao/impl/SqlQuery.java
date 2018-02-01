package by.martyniuk.hotelbooking.dao.impl;

class SqlQuery {

    static final String SQL_SELECT_ALL_APARTMENTS_CLASSES = "SELECT `id_apartment_class`, `type`, `rooms_amount`," +
            " `max_capacity`, `cost_per_night`, `cost_per_person`, `description`, `image_path` FROM `apartment_class`";

    static final String SQL_FIND_APARTMENT_CLASS_BY_ID = "SELECT `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `cost_per_night`, `cost_per_person`," +
            " `description`, `image_path` FROM `apartment_class` " +
            " WHERE `id_apartment_class` = ?";

    //-----------------

    static final String SQL_SELECT_ALL_APARTMENTS = "SELECT `id_apartment`, `number`, `floor`, `active`," +
            " `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `cost_per_night`, `cost_per_person`," +
            " `description`, `image_path` FROM `apartment` LEFT JOIN `apartment_class` " +
            " ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk`" +
            " WHERE `active` = 1";


    static final String SQL_FIND_APARTMENT_BY_ID = "SELECT `id_apartment`, `number`, `floor`, `active`," +
            " `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `cost_per_night`, `cost_per_person`," +
            " `description`, `image_path` FROM `apartment` LEFT JOIN `apartment_class` " +
            " ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk` WHERE `id_apartment` = ?" +
            " AND `active` = 1";

    static final String SQL_FIND_APARTMENT_BY_CLASS_ID = "SELECT `id_apartment`, `number`, `floor`, `active`, " +
            " `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `cost_per_night`, `cost_per_person`," +
            " `description`, `image_path` FROM `apartment` LEFT JOIN `apartment_class` " +
            " ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk` WHERE `apartment_class_id_fk` = ?" +
            " AND `active` = 1";

    static final String SQL_INSERT_APARTMENT = "INSERT INTO `apartment` (`number`,`floor`, `apartment_class_id_fk`, `active`) VALUES (?,?,?,1)";

    static final String SQL_UPDATE_APARTMENT = "UPDATE `apartment` SET `number` = ?, `floor` = ?, `apartment_class_id_fk` = ? WHERE `id_apartment` = ?;";

    static final String SQL_DELETE_APARTMENT = "UPDATE `apartment` SET `active` = 0 WHERE `id_apartment` = ?";

    //-----------------


    static final String SQL_RESERVE_APARTMENT = "INSERT INTO `reservation` (`check_in_date`, `check_out_date`, " +
            "`order_time`, `person_amount`, `cost_per_person`, `cost_per_night`, `total_cost`, `user_id_fk`, " +
            "`apartment_id_fk`, `status_id_fk`) VALUES (?,?,?,?,?,?,?,?,?,?)";

    static final String SQL_CHECK_AVAILABILITY = "SELECT `id_reservation` " +
            "FROM `reservation` " +
            "WHERE (NOT ((`check_in_date` > ? AND `check_in_date` >= ?) OR (check_out_date <= ? AND `check_out_date` < ?))) " +
            "AND (`apartment_id_fk` = ?) " +
            "AND ((`status_id_fk` = (SELECT `id_status` FROM `status` WHERE UPPER(`status`) LIKE UPPER(?)))" +
            "OR (`status_id_fk` = (SELECT `id_status` FROM `status` WHERE UPPER(`status`) LIKE UPPER(?))))";

    static final String SQL_SELECT_ALL_RESERVATIONS = "SELECT `id_reservation`, `check_in_date`, `check_out_date`, `order_time`," +
            " `person_amount`, `id_user`, `first_name`, `middle_name`, `last_name`, `balance`," +
            " `email`, `phone_number`, `password`, `role`,  `user`.`active` AS `user_active`, `apartment`.`active` AS `apartment_active`, `id_apartment`, `number`, `floor`, " +
            " `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `reservation`.`cost_per_night` AS `reservation_cost_per_night`, " +
            " `reservation`.`cost_per_person` AS `reservation_cost_per_person`, " +
            " `description`, `image_path`, `id_status`, `status`, `total_cost`, `apartment_class`.`cost_per_night` AS `apartment_cost_per_night`, " +
            " `apartment_class`.`cost_per_person` AS `apartment_cost_per_person` " +
            "FROM `reservation` " +
            "LEFT JOIN (`user` LEFT JOIN `role` ON `role`.`id_role` = `user`.`role_id_fk`) ON `user`.`id_user` = `reservation`.`user_id_fk` " +
            "LEFT JOIN (`apartment` LEFT JOIN `apartment_class` ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk`) " +
            "ON `apartment`.`id_apartment` = `reservation`.`apartment_id_fk` " +
            "LEFT JOIN `status` ON `status`.`id_status` = `reservation`.`status_id_fk`";

    static final String SQL_SELECT_ALL_RESERVATIONS_BY_STATUS = "SELECT `id_reservation`, `check_in_date`, `check_out_date`, `order_time`," +
            " `person_amount`, `id_user`, `first_name`, `middle_name`, `last_name`, `balance`," +
            " `email`, `phone_number`, `password`, `role`,  `user`.`active` AS `user_active`, `apartment`.`active` AS `apartment_active`, `id_apartment`, `number`, `floor`, " +
            " `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `reservation`.`cost_per_night` AS `reservation_cost_per_night`, " +
            " `reservation`.`cost_per_person` AS `reservation_cost_per_person`, " +
            " `description`, `image_path`, `id_status`, `status`, `total_cost`, `apartment_class`.`cost_per_night` AS `apartment_cost_per_night`, " +
            " `apartment_class`.`cost_per_person` AS `apartment_cost_per_person` " +
            "FROM `reservation` " +
            "LEFT JOIN (`user` LEFT JOIN `role` ON `role`.`id_role` = `user`.`role_id_fk`) ON `user`.`id_user` = `reservation`.`user_id_fk` " +
            "LEFT JOIN (`apartment` LEFT JOIN `apartment_class` ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk`) " +
            "ON `apartment`.`id_apartment` = `reservation`.`apartment_id_fk` " +
            "LEFT JOIN `status` ON `status`.`id_status` = `reservation`.`status_id_fk`" +
            "WHERE UPPER(`status`) LIKE UPPER(?)";

    static final String SQL_SELECT_ALL_RESERVATIONS_BY_USER_ID = "SELECT `id_reservation`, `check_in_date`, `check_out_date`, `order_time`," +
            " `person_amount`, `id_user`, `first_name`, `middle_name`, `last_name`, `balance`," +
            " `email`, `phone_number`, `password`, `role`,  `user`.`active` AS `user_active`, `apartment`.`active` AS `apartment_active`, `id_apartment`, `number`, `floor`," +
            " `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `reservation`.`cost_per_night` AS `reservation_cost_per_night`, " +
            " `reservation`.`cost_per_person` AS `reservation_cost_per_person`, " +
            " `description`, `image_path`, `id_status`, `status`, `total_cost`, `apartment_class`.`cost_per_night` AS `apartment_cost_per_night`, " +
            " `apartment_class`.`cost_per_person` AS `apartment_cost_per_person` " +
            "FROM `reservation` " +
            "LEFT JOIN (`user` LEFT JOIN `role` ON `role`.`id_role` = `user`.`role_id_fk`) ON `user`.`id_user` = `reservation`.`user_id_fk` " +
            "LEFT JOIN (`apartment` LEFT JOIN `apartment_class` ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk`) " +
            "ON `apartment`.`id_apartment` = `reservation`.`apartment_id_fk` " +
            "LEFT JOIN `status` ON `status`.`id_status` = `reservation`.`status_id_fk`" +
            "WHERE `id_user` = ?";

    static final String SQL_SELECT_RESERVATION_BY_ID = "SELECT `id_reservation`, `check_in_date`, `check_out_date`, `order_time`," +
            " `person_amount`, `id_user`, `first_name`, `middle_name`, `last_name`, `balance`," +
            " `email`, `phone_number`, `password`, `role`,  `user`.`active` AS `user_active`, `apartment`.`active` AS `apartment_active`, `id_apartment`, `number`, `floor`," +
            " `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `reservation`.`cost_per_night` AS `reservation_cost_per_night`, " +
            " `reservation`.`cost_per_person` AS `reservation_cost_per_person`, " +
            " `description`, `image_path`, `id_status`, `status`, `total_cost`, `apartment_class`.`cost_per_night` AS `apartment_cost_per_night`, " +
            " `apartment_class`.`cost_per_person` AS `apartment_cost_per_person` " +
            "FROM `reservation` " +
            "LEFT JOIN (`user` LEFT JOIN `role` ON `role`.`id_role` = `user`.`role_id_fk`) ON `user`.`id_user` = `reservation`.`user_id_fk` " +
            "LEFT JOIN (`apartment` LEFT JOIN `apartment_class` ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk`) " +
            "ON `apartment`.`id_apartment` = `reservation`.`apartment_id_fk` " +
            "LEFT JOIN `status` ON `status`.`id_status` = `reservation`.`status_id_fk`" +
            "WHERE `id_reservation` = ?";

    static final String SQL_UPDATE_RESERVATION_STATUS = "UPDATE `reservation` SET `apartment_id_fk` = ?, " +
            " `status_id_fk` = (SELECT `id_status` FROM `status` WHERE UPPER(`status`) LIKE UPPER(?)) " +
            " WHERE `id_reservation` = ?";

    //---------------


    static final String SQL_SELECT_ALL_USERS = "SELECT `id_user`, `first_name`, `middle_name`, `last_name`, `balance`, `email`, " +
            "`phone_number`, `password`, `role`, `active` FROM `user` LEFT JOIN `role` " +
            "ON `role`.`id_role` = `user`.`role_id_fk`";

    static final String SQL_FIND_USER_BY_MAIL = "SELECT `id_user`, `first_name`, `middle_name`, `last_name`, `balance`, `email`, " +
            "`phone_number`, `password`, `role`, `active` FROM `user` LEFT JOIN `role` " +
            "ON `role`.`id_role` = `user`.`role_id_fk` WHERE `user`.`email`	= ?  AND `active` = 1";

    static final String SQL_INSERT_USER = "INSERT INTO `user` (`first_name`, `middle_name`, `last_name`, " +
            "`balance`, `email`, `phone_number`, `password`, `role_id_fk`, `active`)  VALUES (?,?,?,?,?,?,?,(SELECT `id_role` FROM `role` WHERE UPPER(`role`) LIKE UPPER(?)),?)";

    static final String SQL_UPDATE_USER = "UPDATE `user` SET `first_name` = ?, `middle_name` = ?, `last_name` = ?, " +
            "`email` = ?, `phone_number` = ?, `password` = ?, `active` = ?, " +
            "`role_id_fk` = (SELECT `id_role` FROM `role` WHERE UPPER(`role`) LIKE UPPER(?)) WHERE `id_user` = ?";

    static final String SQL_UPDATE_USER_PASSWORD = "UPDATE `user` SET `password` = ? WHERE `id_user` = ?";


    static final String SQL_DEPOSIT_MONEY = "UPDATE `user` SET `balance` = (`balance` + ?) WHERE `id_user` = ?";

    static final String SQL_WITHDRAW_USER_MONEY = "UPDATE `user` SET `balance` = (`balance` - ?) " +
            "WHERE `id_user` = ? AND `balance` >= ?";
}
