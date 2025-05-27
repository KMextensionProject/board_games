-- drop indexes first (indexes are separate from tables)
DROP INDEX IF EXISTS public.idx_board_game_title;
DROP INDEX IF EXISTS public.idx_board_game_min_players;
DROP INDEX IF EXISTS public.idx_board_game_max_players;
DROP INDEX IF EXISTS public.idx_board_game_estimated_play_time;
DROP INDEX IF EXISTS public.idx_board_game_min_age;
DROP INDEX IF EXISTS public.idx_board_game_max_age;
DROP INDEX IF EXISTS public.idx_board_game_description;
DROP INDEX IF EXISTS public.idx_board_game_title_normalized;
DROP INDEX IF EXISTS public.idx_board_game_created_at;
DROP INDEX IF EXISTS public.idx_board_game_modified_at;
DROP INDEX IF EXISTS public.idx_board_game_created_by;
DROP INDEX IF EXISTS public.idx_board_game_modified_by;
DROP INDEX IF EXISTS public.idx_author_name_normalized;
DROP INDEX IF EXISTS public.idx_tutorial_url;

-- drop foreign key constraints and tables
DROP TABLE IF EXISTS public.board_game CASCADE;
DROP TABLE IF EXISTS public.author CASCADE;
DROP TABLE IF EXISTS public.board_game_author CASCADE;
DROP TABLE IF EXISTS public.borrower CASCADE;
DROP TABLE IF EXISTS public.lend_log CASCADE;

CREATE TABLE board_game (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    title_normalized VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    min_players INTEGER,
    max_players INTEGER,
    min_age INTEGER,
    max_age INTEGER,
    estimated_play_time INTEGER,
    is_cooperative BOOLEAN,
    can_play_only_once BOOLEAN,
    is_extension BOOLEAN NOT NULL,
    tutorial_url VARCHAR(120),
    version BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP,
    created_by VARCHAR(50),
    modified_by VARCHAR(50)
);
CREATE TABLE author (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    name_normalized VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP,
    created_by VARCHAR(50),
    modified_by VARCHAR(50)
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
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50)
);
CREATE TABLE lend_log (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP,
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    board_game_id BIGINT REFERENCES board_game(id) ON DELETE CASCADE,
    borrower_id BIGINT REFERENCES borrower(id) ON DELETE SET NULL,
    lend_date DATE NOT NULL DEFAULT NOW(),
    return_date DATE,
    notes VARCHAR(255)
);
CREATE INDEX idx_board_game_title ON board_game(title);
CREATE INDEX idx_board_game_title_normalized ON board_game(title_normalized);
CREATE INDEX idx_board_game_min_players ON board_game(min_players);
CREATE INDEX idx_board_game_max_players ON board_game(max_players);
CREATE INDEX idx_board_game_estimated_play_time ON board_game(estimated_play_time);
CREATE INDEX idx_board_game_min_age ON board_game(min_age);
CREATE INDEX idx_board_game_max_age ON board_game(max_age);
CREATE INDEX idx_board_game_description ON board_game(description);
CREATE INDEX idx_board_game_created_at ON board_game(created_at);
CREATE INDEX idx_board_game_modified_at ON board_game(modified_at);
CREATE INDEX idx_board_game_created_by ON board_game(created_by);
CREATE INDEX idx_board_game_modified_by ON board_game(modified_by);
CREATE INDEX idx_author_name ON author(name);
CREATE INDEX idx_author_name_normalized ON author(name_normalized);
CREATE INDEX idx_tutorial_url ON board_game(tutorial_url);

CREATE EXTENSION IF NOT EXISTS unaccent;
