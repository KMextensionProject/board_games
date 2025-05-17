-- drop indexes first (indexes are separate from tables)
DROP INDEX IF EXISTS public.idx_board_game_title;
DROP INDEX IF EXISTS public.idx_board_game_min_players;
DROP INDEX IF EXISTS public.idx_board_game_max_players;
DROP INDEX IF EXISTS public.idx_board_game_estimated_play_time;
DROP INDEX IF EXISTS public.idx_board_game_is_cooperative;
DROP INDEX IF EXISTS public.idx_board_game_one_time_play;
DROP INDEX IF EXISTS public.idx_board_game_min_age;
DROP INDEX IF EXISTS public.idx_board_game_max_age;
DROP INDEX IF EXISTS public.idx_board_game_description;

-- drop foreign key constraints and tables
DROP TABLE IF EXISTS public.board_game CASCADE;
DROP TABLE IF EXISTS public.author CASCADE;
DROP TABLE IF EXISTS public.board_game_author CASCADE;
DROP TABLE IF EXISTS public.borrower CASCADE;
DROP TABLE IF EXISTS public.lend_log CASCADE;

CREATE TABLE board_game (
    id BIGSERIAL PRIMARY KEY,
    version BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP,
    title VARCHAR(50) NOT NULL,
    min_players INTEGER,
    max_players INTEGER,
    estimated_play_time INTEGER,
    description VARCHAR(500),
    min_age INTEGER,
    max_age INTEGER,
    can_play_only_once BOOLEAN,
    is_cooperative BOOLEAN
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
CREATE INDEX idx_board_game_title ON board_game(title);
CREATE INDEX idx_board_game_min_players ON board_game(min_players);
CREATE INDEX idx_board_game_max_players ON board_game(max_players);
CREATE INDEX idx_board_game_estimated_play_time ON board_game(estimated_play_time);
CREATE INDEX idx_board_game_is_cooperative ON board_game(is_cooperative);
CREATE INDEX idx_board_game_one_time_play ON board_game(can_play_only_once);
CREATE INDEX idx_board_game_min_age ON board_game(min_age);
CREATE INDEX idx_board_game_max_age ON board_game(max_age);
CREATE INDEX idx_board_game_description ON board_game(description);
