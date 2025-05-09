--DROP TABLE IF EXISTS public.flyway_schema_history;
--DROP TABLE IF EXISTS public.board_game;
--DROP TABLE IF EXISTS public.author;
--DROP TABLE IF EXISTS public.board_game_author;
--DROP TABLE IF EXISTS public.borrower;
--DROP TABLE IF EXISTS public.lend_log;

CREATE TABLE board_game (
    id BIGSERIAL PRIMARY KEY,
    version BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP,
    title VARCHAR(50) NOT NULL,
    min_players INTEGER,
    max_players INTEGER,
    estimated_play_time INTEGER
);
CREATE TABLE author (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP,
    name VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE board_game_author (
    board_game_id BIGINT REFERENCES board_game(id) ON DELETE CASCADE,
    author_id BIGINT REFERENCES author(id) ON DELETE CASCADE,
    PRIMARY KEY (board_game_id, author_id)
);
CREATE TABLE borrower (
    id BIGSERIAL PRIMARY KEY,
	created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50)
);
CREATE TABLE lend_log (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP,
    board_game_id BIGINT REFERENCES board_game(id) ON DELETE CASCADE,
    borrower_id BIGINT REFERENCES borrower(id) ON DELETE SET NULL,
    lend_date DATE NOT NULL DEFAULT NOW(),
    return_date DATE,
    notes VARCHAR(255)
);
