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

CREATE TABLE IF NOT EXISTS notification (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title VARCHAR NOT NULL,
    content TEXT NOT NULL,
    event_id INTEGER NOT NULL,
    CONSTRAINT notification_event_fk FOREIGN KEY (event_id) REFERENCES event(id)
);

CREATE TABLE IF NOT EXISTS alert (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title VARCHAR NOT NULL,
    content TEXT NOT NULL,
    event_id INTEGER NOT NULL,
    CONSTRAINT alert_event_fk FOREIGN KEY (event_id) REFERENCES event(id)
);

