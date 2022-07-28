CREATE TABLE USERS
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    VARCHAR(20),
    pwd        varchaR(20),
    name       VARCHAR(20),
    created_at DATETIME DEFAULT NOW()
);

CREATE TABLE ORDERS
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    product_id  VARCHAR(20) NOT NULL,
    qty         INT      DEFAULT 0,
    unit_price  INT      DEFAULT 0,
    total_price INT      DEFAULT 0,
    user_id     VARCHAR(50) NOT NULL,
    order_id    VARCHAR(50) NOT NULL,
    created_at  DATETIME DEFAULT NOW()
);