-- schema_database.sql

-- 1. Creazione del Database
-- Se il database 'rubrica_db' esiste già, lo eliminiamo per ricrearlo pulito.
DROP DATABASE IF EXISTS rubrica_db;
CREATE DATABASE rubrica_db;

-- 2. Selezione del Database
USE rubrica_db;

-- 3. Creazione della Tabella 'utenti'
-- Questa tabella memorizza le informazioni sugli utenti che useranno la rubrica.
CREATE TABLE IF NOT EXISTS utenti (
    id INT AUTO_INCREMENT PRIMARY KEY,     -- ID unico dell'utente, autoincrementante e chiave primaria
    username VARCHAR(50) NOT NULL UNIQUE, -- Nome utente, obbligatorio e deve essere unico
    password VARCHAR(255) NOT NULL,       -- Password dell'utente (qui andrebbe l'hash in un'app reale)
    data_registrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Data e ora di registrazione, impostata automaticamente
);

-- 4. Creazione della Tabella 'persone'
-- Questa tabella memorizza i contatti della rubrica, con una relazione all'utente proprietario.
CREATE TABLE IF NOT EXISTS persone (
    id INT AUTO_INCREMENT PRIMARY KEY,     -- ID unico del contatto, autoincrementante e chiave primaria
    nome VARCHAR(100) NOT NULL,            -- Nome del contatto, obbligatorio
    cognome VARCHAR(100) NOT NULL,         -- Cognome del contatto, obbligatorio
    indirizzo VARCHAR(255),                -- Indirizzo del contatto, opzionale
    telefono VARCHAR(20) NOT NULL,         -- Numero di telefono, obbligatorio
    eta INT,                               -- Età del contatto, opzionale
    id_utente INT NOT NULL,                -- ID dell'utente proprietario del contatto (chiave esterna)

    -- Definizione della Chiave Esterna (Foreign Key)
    -- Questa relazione collega 'id_utente' in 'persone' con 'id' in 'utenti'.
    -- ON DELETE CASCADE: Se un utente viene eliminato, tutti i suoi contatti vengono automaticamente eliminati.
    -- ON UPDATE CASCADE: Se l'ID di un utente dovesse cambiare (molto raro per le PK), la chiave esterna si aggiorna.
    FOREIGN KEY (id_utente) REFERENCES utenti(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 5. Creazione di un Utente Dedicato per l'Applicazione (Raccomandato per la sicurezza)
-- Inserisci la password che hai deciso di usare nel file 'credenziali_database.properties'.
DROP USER IF EXISTS 'rubrica_user'@'localhost';
CREATE USER 'rubrica_user'@'localhost' IDENTIFIED BY 'password_sicura';

-- 6. Concessione dei Permessi all'Utente dell'Applicazione
GRANT ALL PRIVILEGES ON rubrica_db.* TO 'rubrica_user'@'localhost';

-- 7. Ricarica dei Privilegi
-- Questo comando è importante per rendere effettive le modifiche ai permessi.
FLUSH PRIVILEGES;