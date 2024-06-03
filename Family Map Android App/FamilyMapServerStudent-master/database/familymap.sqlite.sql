BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Persons" (
	"personID"	TEXT NOT NULL UNIQUE,
	"associatedUsername"	TEXT NOT NULL,
	"firstName"	TEXT NOT NULL,
	"lastName"	TEXT NOT NULL,
	"gender"	TEXT NOT NULL,
	"fatherID"	TEXT,
	"motherID"	TEXT,
	"spouseID"	TEXT,
	FOREIGN KEY("associatedUsername") REFERENCES "Users"("username"),
	PRIMARY KEY("personID")
);
CREATE TABLE IF NOT EXISTS "Users" (
	"username"	TEXT NOT NULL UNIQUE,
	"password"	TEXT NOT NULL,
	"email"	TEXT NOT NULL,
	"firstName"	TEXT NOT NULL,
	"lastName"	TEXT NOT NULL,
	"gender"	TEXT NOT NULL,
	"personID"	TEXT NOT NULL,
	FOREIGN KEY("personID") REFERENCES "Persons"("personID"),
	PRIMARY KEY("username")
);
CREATE TABLE IF NOT EXISTS "Events" (
	"eventID"	TEXT NOT NULL UNIQUE,
	"associatedUsername"	TEXT NOT NULL,
	"personID"	TEXT NOT NULL,
	"latitude"	REAL NOT NULL,
	"longitude"	REAL NOT NULL,
	"country"	TEXT NOT NULL,
	"city"	TEXT NOT NULL,
	"eventType"	TEXT NOT NULL,
	"year"	INTEGER NOT NULL,
	FOREIGN KEY("personID") REFERENCES "Persons"("personID"),
	FOREIGN KEY("associatedUsername") REFERENCES "Users"("username"),
	PRIMARY KEY("eventID")
);
CREATE TABLE IF NOT EXISTS "Authtokens" (
	"authtoken"	TEXT NOT NULL UNIQUE,
	"username"	TEXT NOT NULL,
	FOREIGN KEY("username") REFERENCES "Users"("username"),
	PRIMARY KEY("authtoken")
);
COMMIT;
