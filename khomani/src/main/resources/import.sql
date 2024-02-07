INSERT INTO tb_user (first_name, last_name, email, id_number, validate, nuit, phone, license, address, img_url, alvaral, password) VALUES ('Cardoso', 'Mulhovo', 'cardoso@gmail.com','100108743210S', '2023-12-28','123456789' ,'+258841234567', 'Licensa singular', 'Av. Mao Tse Tung', 'bilhete de identidade img', 'Alvara singular','$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXBaOxJ9aORUYzfMQCbVBIhZ8tG');
INSERT INTO tb_user (first_name, last_name, email, id_number, validate, nuit, phone, license, address, img_url, alvaral, password) VALUES ('Januario', 'Chitsumba', 'chitsumba@gmail.com','101002341899J', '2023-12-29','098543187' ,'+258829885432', 'Licensa da empresa', 'Amahed Sekou Toure', 'bilhete de identidade', 'Alvara empresa', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXBaOxJ9aORUYzfMQCbVBIhZ8tG');

INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO tb_role (authority) VALUES ('ROLE_SINGULAR');
INSERT INTO tb_role (authority) VALUES ('ROLE_COMPANY');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1,1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (1,2);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2,1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2,3);