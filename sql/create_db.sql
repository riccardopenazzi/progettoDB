CREATE DATABASE progetto_db;

USE progetto_db;

-- Creazione della tabella Utenti
CREATE TABLE Utenti (
    codUtente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cognome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL
);

-- Creazione della tabella Indirizzi
CREATE TABLE Indirizzi (
    via VARCHAR(255),
    numero INT,
    comune VARCHAR(255),
    PRIMARY KEY (via, numero, comune)
);


-- Creazione della tabella Carte_fedelta
CREATE TABLE Carte_fedelta (
    codCarta INT AUTO_INCREMENT PRIMARY KEY,
    punti INT DEFAULT 100
);


-- Creazione della tabella Coupon
CREATE TABLE Coupon (
    codCoupon INT AUTO_INCREMENT PRIMARY KEY,
    sconto DOUBLE NOT NULL,
    puntiRichiesti INT NOT NULL,
    disponibile BOOLEAN DEFAULT TRUE
);


CREATE TABLE Recensioni (
    codRecensione INT AUTO_INCREMENT PRIMARY KEY,
    stelle INT NOT NULL,
    commento TEXT,
    utente INT NOT NULL,
    FOREIGN KEY (utente) REFERENCES Utenti(codUtente)
);


-- Creazione della tabella Tempi
CREATE TABLE Tempi (
    orario DATETIME,
    PRIMARY KEY (orario)
);


-- Creazione della tabella Ordini
CREATE TABLE Ordini (
    codOrdine INT AUTO_INCREMENT PRIMARY KEY,
    totale DOUBLE NOT NULL,
    stato VARCHAR(50) DEFAULT 'In attesa',
    assegnata BOOLEAN DEFAULT FALSE,
    utente INT NOT NULL,
    orarioRitiro DATETIME NOT NULL,
    orarioEffettuazione DATETIME NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    FOREIGN KEY (utente) REFERENCES Utenti(codUtente),
    FOREIGN KEY (orarioRitiro) REFERENCES Tempi(orario),
    FOREIGN KEY (orarioEffettuazione) REFERENCES Tempi(orario)
);


-- Creazione della tabella Ingredienti
CREATE TABLE Ingredienti (
    nome VARCHAR(255) PRIMARY KEY,
    prezzo DOUBLE NOT NULL
);


-- Creazione della tabella Pizze
CREATE TABLE Pizze (
    nome VARCHAR(255) PRIMARY KEY,
    prezzoBase DOUBLE NOT NULL,
    presente BOOLEAN DEFAULT TRUE
);

-- Creazione della tabella Domiciliazioni
CREATE TABLE Domiciliazioni (
    utente INT NOT NULL,
    via VARCHAR(255) NOT NULL,
    numero INT NOT NULL,
    comune VARCHAR(255) NOT NULL,
    FOREIGN KEY (utente) REFERENCES Utenti(codUtente),
    FOREIGN KEY (via, numero, comune) REFERENCES Indirizzi(via, numero, comune)
);

-- Creazione della tabella Destinazioni
CREATE TABLE Destinazioni (
    ordine INT NOT NULL,
    via VARCHAR(255) NOT NULL,
    numero INT NOT NULL,
    comune VARCHAR(255) NOT NULL,
    FOREIGN KEY (ordine) REFERENCES Ordini(codOrdine),
    FOREIGN KEY (via, numero, comune) REFERENCES Indirizzi(via, numero, comune),
    UNIQUE KEY (ordine, via, numero, comune)
);

CREATE TABLE Assegnazioni (
    utente INT NOT NULL,
    ordine1 INT NOT NULL,
    ordine2 INT,
    ordine3 INT,
    orario DATETIME NOT NULL,
    contatore INT DEFAULT 1,
    orarioEffettivoOrdine1 DATETIME,
    orarioEffettivoOrdine2 DATETIME,
    orarioEffettivoOrdine3 DATETIME,
    PRIMARY KEY (utente, orario),
    FOREIGN KEY (utente) REFERENCES Utenti(codUtente),
    FOREIGN KEY (ordine1) REFERENCES Ordini(codOrdine),
    FOREIGN KEY (ordine2) REFERENCES Ordini(codOrdine),
    FOREIGN KEY (ordine3) REFERENCES Ordini(codOrdine),
    FOREIGN KEY (orario) REFERENCES Tempi(orario),
    FOREIGN KEY (orarioEffettivoOrdine1) REFERENCES Tempi(orario),
    FOREIGN KEY (orarioEffettivoOrdine2) REFERENCES Tempi(orario),
    FOREIGN KEY (orarioEffettivoOrdine3) REFERENCES Tempi(orario)
);

-- Creazione della tabella Appartenenze
CREATE TABLE Appartenenze (
    utente INT NOT NULL,
    cartaFedelta INT NOT NULL,
    FOREIGN KEY (utente) REFERENCES Utenti(codUtente),
    FOREIGN KEY (cartaFedelta) REFERENCES Carte_fedelta(codCarta),
    UNIQUE (cartaFedelta)
);

-- Creazione della tabella Utilizzi_coupon
CREATE TABLE Utilizzi_coupon (
    utente INT NOT NULL,
    coupon INT NOT NULL,
    FOREIGN KEY (utente) REFERENCES Utenti(codUtente),
    FOREIGN KEY (coupon) REFERENCES Coupon(codCoupon)
);

-- Creazione della tabella Farciture
CREATE TABLE Farciture (
    pizza VARCHAR(255) NOT NULL,
    ingrediente VARCHAR(255) NOT NULL,
    FOREIGN KEY (pizza) REFERENCES Pizze(nome),
    FOREIGN KEY (ingrediente) REFERENCES Ingredienti(nome)
);

-- Creazione della tabella Composizioni
CREATE TABLE Composizioni (
    ordine INT NOT NULL,
    pizza VARCHAR(255) NOT NULL,
    ingredienti_array VARCHAR(2048),
    FOREIGN KEY (ordine) REFERENCES Ordini(codOrdine),
    FOREIGN KEY (pizza) REFERENCES Pizze(nome)
);
