/* Used to create our messages in our POSTGRESQL tables with id being auto assigned and following infomration being created by the user with constratints*/
CREATE TABLE messages(
    id BIGSERIAL PRIMARY KEY,
    sender VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL DEFAULT NOW()
);