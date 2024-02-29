CREATE TABLE bike_to_service
(
    bike_to_service_id SERIAL       NOT NULL,
    serial             VARCHAR(20)  NOT NULL,
    brand              VARCHAR(32)  NOT NULL,
    model              VARCHAR(128) NOT NULL,
    production_year    INT          NOT NULL,
    PRIMARY KEY (bike_to_service_id),
    UNIQUE (serial)
);