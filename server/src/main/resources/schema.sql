CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS item_requests
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description TEXT                        NOT NULL,
    user_id     BIGINT                      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created     TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    available   BOOLEAN      NOT NULL DEFAULT TRUE,
    user_id     BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    request_id  BIGINT       REFERENCES item_requests (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id    BIGINT                      NOT NULL REFERENCES items (id) ON DELETE CASCADE,
    user_id    BIGINT                      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    status     VARCHAR(20)                 NOT NULL
);


CREATE TABLE IF NOT EXISTS comments
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text    TEXT                        NOT NULL,
    item_id BIGINT                      NOT NULL REFERENCES items (id) ON DELETE CASCADE,
    user_id BIGINT                      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);