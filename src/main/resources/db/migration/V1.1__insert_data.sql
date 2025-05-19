INSERT INTO public.board_game(title, title_normalized, min_players, max_players, min_age, max_age, estimated_play_time, can_play_only_once, is_cooperative, is_extension, tutorial_url, version) 
VALUES 
('Stranger Things: Obrácený svět', lower(unaccent('Stranger Things: Obrácený svět')), 2, 4, 12, NULL, 60, FALSE, TRUE, FALSE, 'https://www.youtube.com/watch?v=GJxO3mwow5c', 0),
('Bandido', lower(unaccent('Bandido')), 1, 4, 6, NULL, 15, FALSE, TRUE, FALSE, 'https://www.youtube.com/watch?v=7t9IVz3ZUZE', 0),
('7 Divů světa: Duel', lower(unaccent('7 Divů světa: Duel')), 2, 2, 10, NULL, 30, FALSE, FALSE, FALSE, 'https://www.youtube.com/watch?v=SxQkMRUvCJ8', 0),
('7 Divů světa: Duel - Agora', lower(unaccent('7 Divů světa: Duel - Agora')), 2, 2, 10, NULL, 30, FALSE, FALSE, TRUE, 'https://www.youtube.com/watch?v=6RGu0WhPhMA', 0),
('7 Divů světa: Duel - Pantheon', lower(unaccent('7 Divů světa: Duel - Pantheon')), 2, 2, 10, NULL, 30, FALSE, FALSE, TRUE, 'https://www.youtube.com/watch?v=1wG34BYnd1U', 0),
('Podmořská města', lower(unaccent('Podmořská města')), 1, 4, 12, NULL, 40, FALSE, FALSE, FALSE, 'https://www.youtube.com/watch?v=UnderwaterCitiesTutorial', 0),
('Arnak', lower(unaccent('Arnak')), 1, 4, 12, NULL, 90, FALSE, FALSE, FALSE, 'https://www.youtube.com/watch?v=ArnakTutorial', 0),
('Jumanji', lower(unaccent('Jumanji')), 2, 4, 8, NULL, 45, FALSE, TRUE, FALSE, 'https://www.youtube.com/watch?v=JumanjiTutorial', 0),
('Bang', lower(unaccent('Bang')), 4, 7, 8, NULL, 30, FALSE, FALSE, FALSE, 'https://www.youtube.com/watch?v=BangTutorial', 0),
('Bang Duel', lower(unaccent('Bang Duel')), 2, 2, 8, NULL, 30, FALSE, FALSE, FALSE, 'https://www.youtube.com/watch?v=BangDuelTutorial', 0),
('Mars: Teraformace', lower(unaccent('Mars: Teraformace')), 1, 5, 12, NULL, 120, FALSE, FALSE, FALSE, 'https://www.youtube.com/watch?v=TerraformingMarsTutorial', 0),
('Memoir 44', lower(unaccent('Memoir 44')), 2, 2, 8, NULL, 60, FALSE, FALSE, FALSE, 'https://www.youtube.com/watch?v=Memoir44Tutorial', 0),
('Memoir 44: Pacific Theater', lower(unaccent('Memoir 44: Pacific Theater')), 2, 2, 8, NULL, 60, FALSE, FALSE, TRUE, 'https://www.youtube.com/watch?v=Memoir44PacificTutorial', 0);
