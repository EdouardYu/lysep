CREATE TABLE IF NOT EXISTS "user" (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    firstname VARCHAR NOT NULL,
    lastname VARCHAR NOT NULL,
    username VARCHAR,
    email VARCHAR UNIQUE NOT NULL,
    password VARCHAR NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    phone VARCHAR NOT NULL,
    role VARCHAR NOT NULL,
    CONSTRAINT check_role CHECK (role IN (
        'STUDENT',
        'PROFESSOR',
        'SCHOOL_ADMINISTRATOR',
        'ASSOCIATION_MEMBER',
        'APPLICATION_ADMINISTRATOR'
    )),
    CONSTRAINT check_phone_format CHECK (phone ~ '^\(\+\d{1,3}\)\d{1,12}$')
);

CREATE TABLE IF NOT EXISTS validation (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    code CHARACTER(6) NOT NULL,
    expired_at TIMESTAMP NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    user_id INTEGER NOT NULL,
    CONSTRAINT validation_user_fk FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE TABLE IF NOT EXISTS jwt (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    value VARCHAR NOT NULL,
    expired_at TIMESTAMP NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    user_id INTEGER NOT NULL,
    CONSTRAINT jwt_user_fk FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE TABLE IF NOT EXISTS module (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    label VARCHAR UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS professor_module (
    professor_id INTEGER NOT NULL,
    module_id INTEGER NOT NULL,
    PRIMARY KEY (professor_id, module_id),
    CONSTRAINT professor_module_professor_fk FOREIGN KEY (professor_id) REFERENCES "user"(id),
    CONSTRAINT professor_module_fk FOREIGN KEY (module_id) REFERENCES module(id)
);

CREATE TABLE IF NOT EXISTS student_module (
    student_id INTEGER NOT NULL,
    module_id INTEGER NOT NULL,
    PRIMARY KEY (student_id, module_id),
    CONSTRAINT student_module_student_fk FOREIGN KEY (student_id) REFERENCES "user"(id),
    CONSTRAINT student_module_fk FOREIGN KEY (module_id) REFERENCES module(id)
);


CREATE TABLE IF NOT EXISTS event (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title VARCHAR NOT NULL,
    description TEXT NOT NULL,
    date TIMESTAMP NOT NULL,
    module_id INTEGER NOT NULL,
    created_by INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT event_module_fk FOREIGN KEY (module_id) REFERENCES module(id),
    CONSTRAINT event_organizer_fk FOREIGN KEY (created_by) REFERENCES "user"(id)
);

CREATE TABLE IF NOT EXISTS participant (
    event_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    PRIMARY KEY (event_id, user_id),
    CONSTRAINT participant_event_fk FOREIGN KEY (event_id) REFERENCES event(id),
    CONSTRAINT participant_user_fk FOREIGN KEY (user_id) REFERENCES "user"(id)
);

-- Répartition des étudiants (ID entre 2 et 31) et des professeurs (ID entre 32 et 41)
-- Répartition cohérente des étudiants et des professeurs

-- Insertions pour les modules liés à l'IA
INSERT INTO student_module (student_id, module_id) VALUES (2, 11), (2, 7), (3, 11), (3, 9), (4, 7), (4, 9), (5, 11), (5, 9);
INSERT INTO professor_module (professor_id, module_id) VALUES (32, 11), (33, 11), (34, 7), (35, 7), (36, 9), (37, 9);

-- Insertions pour les modules liés à la Vision par ordinateur
INSERT INTO student_module (student_id, module_id) VALUES (6, 8), (6, 10), (7, 8), (7, 13), (8, 8), (8, 13), (9, 10), (9, 13);
INSERT INTO professor_module (professor_id, module_id) VALUES (32, 8), (33, 8), (34, 10), (35, 10), (36, 13), (37, 13);

-- Insertions pour les modules liés à l'Électronique Analogique
INSERT INTO student_module (student_id, module_id) VALUES (10, 1), (10, 2), (11, 1), (11, 2), (12, 1), (12, 5), (13, 1), (13, 6);
INSERT INTO professor_module (professor_id, module_id) VALUES (32, 1), (33, 2), (34, 5), (35, 6);

-- Insertions pour les autres modules restants
INSERT INTO student_module (student_id, module_id) VALUES
                                                       (14, 3), (15, 4), (16, 12), (17, 14),
                                                       (18, 15), (19, 16), (20, 17),
                                                       (21, 3), (22, 4), (23, 12), (24, 14),
                                                       (25, 15), (26, 16), (27, 17),
                                                       (28, 3), (29, 4), (30, 12), (31, 14);

INSERT INTO professor_module (professor_id, module_id) VALUES
                                                           (38, 3), (39, 4), (40, 12), (41, 14),
                                                           (38, 15), (39, 16), (40, 17),
                                                           (41, 3), (38, 4), (39, 12), (40, 14),
                                                           (41, 15), (38, 16), (39, 17);

