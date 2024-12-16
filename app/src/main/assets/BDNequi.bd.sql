BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "bolsillo" (
	"ID"	INTEGER,
	"Nombre"	VARCHAR(255) NOT NULL UNIQUE,
	"Monto"	BIGINT NOT NULL,
	"FechaCreacion"	DATE NOT NULL,
	"CuentaID"	BIGINT NOT NULL,
	"Estado_idEstado"	INTEGER NOT NULL,
	PRIMARY KEY("ID"),
	FOREIGN KEY("CuentaID") REFERENCES "cuenta"("ID"),
	FOREIGN KEY("Estado_idEstado") REFERENCES "estado"("idEstado")
);
CREATE TABLE IF NOT EXISTS "ciudad" (
	"ID"	INTEGER,
	"Nombre"	VARCHAR(50) NOT NULL,
	"DepartamentoID"	INTEGER NOT NULL,
	PRIMARY KEY("ID"),
	FOREIGN KEY("DepartamentoID") REFERENCES "departamento"("ID")
);
CREATE TABLE IF NOT EXISTS "colchon" (
	"ID"	INTEGER,
	"Monto"	BIGINT,
	PRIMARY KEY("ID"),
	FOREIGN KEY("ID") REFERENCES "cuenta"("ID")
);
CREATE TABLE IF NOT EXISTS "cuenta" (
	"ID"	INTEGER,
	"Saldo"	BIGINT NOT NULL,
	"Contrasena"	VARCHAR(45) NOT NULL,
	PRIMARY KEY("ID")
);
CREATE TABLE IF NOT EXISTS "departamento" (
	"ID"	INTEGER,
	"Nombre"	VARCHAR(50) NOT NULL,
	"PaisID"	INTEGER NOT NULL,
	PRIMARY KEY("ID"),
	FOREIGN KEY("PaisID") REFERENCES "pais"("ID")
);
CREATE TABLE IF NOT EXISTS "envio" (
	"ID"	INTEGER,
	"CuentaRecibir"	BIGINT NOT NULL,
	"Monto"	BIGINT NOT NULL,
	"Mensaje"	VARCHAR(255),
	"Fecha"	DATE NOT NULL,
	"CuentaEnviador"	BIGINT NOT NULL,
	PRIMARY KEY("ID"),
	FOREIGN KEY("CuentaEnviador") REFERENCES "cuenta"("ID"),
	FOREIGN KEY("CuentaRecibir") REFERENCES "cuenta"("ID")
);
CREATE TABLE IF NOT EXISTS "estado" (
	"idEstado"	INTEGER,
	"Estado"	VARCHAR(45) NOT NULL,
	PRIMARY KEY("idEstado")
);
CREATE TABLE IF NOT EXISTS "mensaje" (
	"ID"	INTEGER,
	"Tipo"	VARCHAR(50) NOT NULL,
	"Mensaje"	VARCHAR(255) NOT NULL,
	"Fecha"	DATE NOT NULL,
	"UsuarioID"	INTEGER NOT NULL,
	"UsuarioID_Externo" INTEGER NOT NULL,
	"Monto" INTEGER NOT NULL,
	"Estado" INTEGER NOT NULL,
	PRIMARY KEY("ID"),
	FOREIGN KEY("UsuarioID") REFERENCES "usuario"("ID")
	FOREIGN KEY("UsuarioID_Externo") REFERENCES "usuario"("ID")
	FOREIGN KEY("Estado") REFERENCES "estado"("idEstado")
);
CREATE TABLE IF NOT EXISTS "meta" (
	"ID"	INTEGER,
	"Nombre"	VARCHAR(255) NOT NULL UNIQUE,
	"MontoAct"	DECIMAL(10, 2) NOT NULL,
	"MontoObj"	DECIMAL(10, 2) NOT NULL,
	"FechaCreacion"	DATE NOT NULL,
	"FechaObjetivo"	DATE NOT NULL,
	"CuentaID"	BIGINT NOT NULL,
	"Estado_idEstado"	INTEGER NOT NULL,
	PRIMARY KEY("ID"),
	FOREIGN KEY("CuentaID") REFERENCES "cuenta"("ID"),
	FOREIGN KEY("Estado_idEstado") REFERENCES "estado"("idEstado")
);
CREATE TABLE IF NOT EXISTS "pais" (
	"ID"	INTEGER,
	"Nombre"	VARCHAR(50) NOT NULL,
	PRIMARY KEY("ID")
);
CREATE TABLE IF NOT EXISTS "prestamo" (
	"ID"	INTEGER,
	"CuentaPedir"	BIGINT NOT NULL,
	"Monto"	BIGINT NOT NULL,
	"Fecha"	DATE NOT NULL,
	"CuentaActual"	BIGINT NOT NULL,
	"Estado_Id"	INTEGER NOT NULL,
	PRIMARY KEY("ID"),
	FOREIGN KEY("CuentaActual") REFERENCES "cuenta"("ID"),
	FOREIGN KEY("CuentaPedir") REFERENCES "cuenta"("ID"),
	FOREIGN KEY("Estado_Id") REFERENCES "estado"("idEstado")
);
CREATE TABLE IF NOT EXISTS "recarga" (
	"ID"	INTEGER,
	"Monto"	BIGINT NOT NULL,
	"Fecha"	DATE NOT NULL,
	"CuentaID"	BIGINT NOT NULL,
	"TipoRecarga_idTipoRecarga"	INTEGER NOT NULL,
	PRIMARY KEY("ID"),
	FOREIGN KEY("CuentaID") REFERENCES "cuenta"("ID"),
	FOREIGN KEY("TipoRecarga_idTipoRecarga") REFERENCES "tipoRecarga"("idTipoRecarga")
);
CREATE TABLE IF NOT EXISTS "retiro" (
	"ID"	INTEGER,
	"Metodo"	VARCHAR(50) NOT NULL,
	"Monto"	BIGINT NOT NULL,
	"Codigo"	VARCHAR(100) NOT NULL,
	"Fecha"	DATE NOT NULL,
	"CuentaRetiro"	BIGINT NOT NULL,
	PRIMARY KEY("ID"),
	FOREIGN KEY("CuentaRetiro") REFERENCES "cuenta"("ID"),
	FOREIGN KEY("Metodo") REFERENCES "tipoRetiro"("idTipoRetiro")
);
CREATE TABLE IF NOT EXISTS "tipoRecarga" (
	"idTipoRecarga"	INTEGER,
	"TipoRecarga"	VARCHAR(45) NOT NULL,
	PRIMARY KEY("idTipoRecarga")
);
CREATE TABLE IF NOT EXISTS "tipoRetiro" (
	"idTipoRetiro"	INTEGER,
	"TipoRetiro"	VARCHAR(45) NOT NULL,
	PRIMARY KEY("idTipoRetiro")
);
CREATE TABLE IF NOT EXISTS "usuario" (
	"ID"	INTEGER,
	"Nombre"	VARCHAR(50) NOT NULL,
	"FechaReg"	DATE NOT NULL,
	"Ocupacion"	VARCHAR(50),
	"CiudadID"	INTEGER NOT NULL,
	"Telefono"	BIGINT NOT NULL,
	PRIMARY KEY("ID"),
	FOREIGN KEY("CiudadID") REFERENCES "ciudad"("ID"),
	FOREIGN KEY("Telefono") REFERENCES "cuenta"("ID")
);

CREATE TABLE IF NOT EXISTS "regalo"(
	"id_regalo" INTEGER,
	"monto_regalo" BIGINT NOT NULL,
	"estado_regalo" INTEGER,
	"codigo_regalo" VARCHAR(10) UNIQUE,
	PRIMARY KEY("id_regalo")
);


INSERT INTO "bolsillo" VALUES (1,'Ahorro',15000,'2024-01-15',3142716310,1);
INSERT INTO "bolsillo" VALUES (2,'Viaje',300000,'2024-02-10',3142716310,2);
INSERT INTO "bolsillo" VALUES (3,'Emergencia',4500,'2024-03-25',3124330615,2);
INSERT INTO "bolsillo" VALUES (4,'Gastos Diarios',20000,'2024-08-25',3124330615,1);
INSERT INTO "bolsillo" VALUES (5,'Pasajes',120000,'2024-05-01',3202803027,1);
INSERT INTO "bolsillo" VALUES (6,'Entretenimiento',3500,'2024-09-01',3202803027,2);
INSERT INTO "ciudad" VALUES (1,'Medellin',1);
INSERT INTO "ciudad" VALUES (2,'Rionegro',1);
INSERT INTO "ciudad" VALUES (3,'Envigado',1);
INSERT INTO "ciudad" VALUES (4,'Bello',1);
INSERT INTO "ciudad" VALUES (5,'Bogota',2);
INSERT INTO "ciudad" VALUES (6,'Soacha',2);
INSERT INTO "ciudad" VALUES (7,'Chía',2);
INSERT INTO "ciudad" VALUES (8,'Zipaquira',2);
INSERT INTO "ciudad" VALUES (9,'Ciudad de Panama',3);
INSERT INTO "ciudad" VALUES (10,'Colón',3);
INSERT INTO "ciudad" VALUES (11,'San Miguelito',3);
INSERT INTO "ciudad" VALUES (12,'La Chorrera',3);
INSERT INTO "ciudad" VALUES (13,'Penonome',4);
INSERT INTO "ciudad" VALUES (14,'Aguadulce',4);
INSERT INTO "ciudad" VALUES (15,'Nata',4);
INSERT INTO "ciudad" VALUES (16,'La Pintada',4);
INSERT INTO "colchon" VALUES (3142716310,190000);
INSERT INTO "colchon" VALUES (3124330615,45650);
INSERT INTO "colchon" VALUES (3202803027,900000);
INSERT INTO "cuenta" VALUES (3142716310,2000000,'1234');
INSERT INTO "cuenta" VALUES (3124330615,345650,'5678');
INSERT INTO "cuenta" VALUES (3202803027,60500,'9876');
INSERT INTO "departamento" VALUES (1,'Antioquia',1);
INSERT INTO "departamento" VALUES (2,'Cundinamarca',1);
INSERT INTO "departamento" VALUES (3,'Panama',2);
INSERT INTO "departamento" VALUES (4,'Cocle',2);
INSERT INTO "envio" VALUES (1,3124330615,50000,'Transferencia para ahorro','2024-09-15',3142716310);
INSERT INTO "envio" VALUES (2,3202803027,200000,'Pago de servicio','2024-09-16',3142716310);
INSERT INTO "envio" VALUES (3,3142716310,10000,'Devolución de préstamo','2024-09-17',3124330615);
INSERT INTO "envio" VALUES (4,3202803027,30000,'Regalo de cumpleaños','2024-09-18',3124330615);
INSERT INTO "envio" VALUES (5,3142716310,150000,'Pago de renta','2024-09-19',3202803027);
INSERT INTO "envio" VALUES (6,3124330615,50000,'Transferencia para emergencias','2024-09-20',3202803027);
INSERT INTO "envio" VALUES (7,3202803027,75000,'Pago de suscripción','2024-09-21',3142716310);
INSERT INTO "envio" VALUES (8,3142716310,120000,'Reembolso de gastos','2024-09-22',3202803027);
INSERT INTO "envio" VALUES (9,3124330615,90000,'Transferencia para viaje','2024-09-23',3202803027);
INSERT INTO "estado" VALUES (1,'Activo');
INSERT INTO "estado" VALUES (2,'Inactivo');
INSERT INTO "estado" VALUES (3,'Pendiente');
INSERT INTO "estado" VALUES (4,'Aprobado');
INSERT INTO "estado" VALUES (5,'Rechazado');
INSERT INTO "estado" VALUES (6,'En proceso');
INSERT INTO "estado" VALUES (7,'Finalizada');
INSERT INTO "mensaje" VALUES (1,'repuesta Peticion','Su peticion ha sido aceptada.','2024-09-14',1,2,1000,1);
INSERT INTO "mensaje" VALUES (2,'peticion','Solicitud de prestamo.','2024-09-15',1,2,1000,1);
INSERT INTO "mensaje" VALUES (3,'repuesta Peticion','Su peticion ha sido rechazada.','2024-09-16',1,2,2000,1);
INSERT INTO "mensaje" VALUES (4,'peticion','Solicitud de prestamo.','2024-09-14',2,1,2000,1);
INSERT INTO "mensaje" VALUES (5,'repuesta Peticion','Su peticion ha sido rechazada.','2024-09-15',2,1,3000,1);
INSERT INTO "mensaje" VALUES (6,'peticion','Solicitud de prestamo.','2024-09-16',2,1,3000,1);
INSERT INTO "mensaje" VALUES (7,'repuesta Peticion','Su peticion ha sido aceptada.','2024-09-14',3,2,4000,1);
INSERT INTO "mensaje" VALUES (8,'peticion','Solicitud de prestamo.','2024-09-15',3,2,4000,1);
INSERT INTO "mensaje" VALUES (9,'repuesta Peticion de prestamo','Su peticion ha sido rechazada.','2024-09-16',3 , 2 , 5000,1);
INSERT INTO "meta" VALUES (1,'Meta Emergencias',135750,500000,'2024-07-01','2024-12-31',3124330615,6);
INSERT INTO "meta" VALUES (2,'Meta Viaje',105800,1000000,'2024-02-15','2024-11-30',3142716310,6);
INSERT INTO "meta" VALUES (3,'Meta Educación',298000,298000,'2024-03-01','2025-06-01',3202803027,7);
INSERT INTO "meta" VALUES (4,'Meta Inversiones',303000,303000,'2024-05-05','2025-04-15',3124330615,7);
INSERT INTO "meta" VALUES (5,'Meta Salud',154000,189000,'2024-06-10','2024-07-01',3142716310,3);
INSERT INTO "meta" VALUES (6,'Meta Compras Casa',350000,1000000,'2024-04-01','2025-12-30',3124330615,6);
INSERT INTO "meta" VALUES (7,'Meta Ahorro',900,198350,'2024-07-01','2025-09-01',3202803027,6);
INSERT INTO "meta" VALUES (8,'Meta Regalos',150000,150000,'2024-05-01','2024-08-15',3142716310,7);
INSERT INTO "meta" VALUES (9,'Meta Playa',100000,780000,'2024-09-10','2025-12-30',3202803027,6);
INSERT INTO "pais" VALUES (1,'Colombia');
INSERT INTO "pais" VALUES (2,'Panama');
INSERT INTO "prestamo" VALUES (1,3142716310,5000,'2024-01-05',3124330615,4);
INSERT INTO "prestamo" VALUES (2,3142716310,10000,'2024-02-10',3202803027,5);
INSERT INTO "prestamo" VALUES (3,3142716310,7500,'2024-02-11',3202803027,3);
INSERT INTO "prestamo" VALUES (4,3202803027,3000,'2024-04-20',3124330615,4);
INSERT INTO "prestamo" VALUES (5,3202803027,8500,'2024-09-25',3142716310,5);
INSERT INTO "prestamo" VALUES (6,3124330615,6000,'2024-06-30',3142716310,4);
INSERT INTO "prestamo" VALUES (7,3124330615,4000,'2024-07-15',3202803027,3);
INSERT INTO "prestamo" VALUES (8,3124330615,9200,'2024-08-05',3142716310,5);
INSERT INTO "recarga" VALUES (1,2300000,'2024-08-16',3142716310,1);
INSERT INTO "recarga" VALUES (2,1835000,'2024-09-01',3124330615,1);
INSERT INTO "recarga" VALUES (3,467000,'2024-05-29',3202803027,1);
INSERT INTO "recarga" VALUES (4,350000,'2024-07-27',3142716310,2);
INSERT INTO "recarga" VALUES (5,730000,'2024-08-02',3124330615,2);
INSERT INTO "recarga" VALUES (6,1450000,'2024-09-10',3202803027,2);
INSERT INTO "retiro" VALUES (1,'1',500000,'123456','2024-08-16',3142716310);
INSERT INTO "retiro" VALUES (2,'1',980000,'876345','2024-09-01',3124330615);
INSERT INTO "retiro" VALUES (3,'1',130000,'987217','2024-05-29',3202803027);
INSERT INTO "retiro" VALUES (4,'2',27000,'987213','2024-07-27',3142716310);
INSERT INTO "retiro" VALUES (5,'2',73000,'134553','2024-08-02',3124330615);
INSERT INTO "retiro" VALUES (6,'2',14500,'813123','2024-09-10',3202803027);
INSERT INTO "tipoRecarga" VALUES (1,'Efectivo');
INSERT INTO "tipoRecarga" VALUES (2,'Desde Otro Banco');
INSERT INTO "tipoRetiro" VALUES (1,'Cajero');
INSERT INTO "tipoRetiro" VALUES (2,'Punto Fisico');
INSERT INTO "usuario" VALUES (1,'Heider Letyton','2024-11-03','Ingeniero',1,3142716310);
INSERT INTO "usuario" VALUES (2,'Ana Gomez','2024-09-15','Docente',2,3124330615);
INSERT INTO "usuario" VALUES (3,'Carlos Lopez','2024-09-16','Arquitecto',3,3202803027);
INSERT INTO "regalo" VALUES(1,56000,1,"c1c1c1c1d1");
INSERT INTO "regalo" VALUES(2,100000,1,"c2c2c2c2d2");
INSERT INTO "regalo" VALUES(3,20000,1,"c3c3c3c3d3");
INSERT INTO "regalo" VALUES(4,135000,1,"c4c4c4c4d4");
INSERT INTO "regalo" VALUES(5,48000,1,"c5c5c5c5d5");
INSERT INTO "regalo" VALUES(6,12000,1,"c6c6c6c6d6");
INSERT INTO "regalo" VALUES(7,567500,1,"c7c7c7c7d7");
INSERT INTO "regalo" VALUES(8,187200,1,"c8c8c8c8d8");
INSERT INTO "regalo" VALUES(9,92300,1, "c9c9c9c9d9");
INSERT INTO "regalo" VALUES(10,1000000,1,"cocococada");
COMMIT;
