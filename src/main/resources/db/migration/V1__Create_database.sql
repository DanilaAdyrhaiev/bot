CREATE TABLE users (
  id serial PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  chatId BIGINT NOT NULL,
  messageMenu int,
  selectedChannel BIGINT,
  coin BIGINT,
  status VARCHAR(255),
  usingPage VARCHAR(255),
  previousPage VARCHAR(255)
);

CREATE TABLE channels (
  id serial PRIMARY KEY,
  user_id BIGINT NOT NULL references users(id),
  link text,
  channel_name VARCHAR(255) NOT NULL,
  link_on_screenshot_1 VARCHAR(255),
  link_on_screenshot_2 VARCHAR(255),
  link_on_admin VARCHAR(255),
  category VARCHAR(255),
  directoryLink VARCHAR(255),
  rate int
);

CREATE TABLE notebook (
    id serial PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    coins_quantity INT NOT NULL,
    success BOOLEAN NOT NULL
);