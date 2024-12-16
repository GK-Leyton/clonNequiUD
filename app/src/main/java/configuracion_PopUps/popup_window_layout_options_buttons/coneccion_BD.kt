package configuracion_PopUps.popup_window_layout_options_buttons

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.sql.SQLException

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Importa el archivo SQL para crear e inicializar la base de datos
        importSQLFromAssets(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Maneja la actualización de la base de datos
        // En este caso, simplemente elimina la tabla antigua y vuelve a crearla
        db.execSQL("DROP TABLE IF EXISTS cuenta") // Asegúrate de usar el nombre correcto de la tabla
        db.execSQL("DROP TABLE IF EXISTS usuario") // Asegúrate de usar el nombre correcto de la tabla
        onCreate(db)
    }

    /**
     * Importa el archivo SQL desde los assets para crear y poblar la base de datos.
     */
    private fun importSQLFromAssets(db: SQLiteDatabase) {
        try {
            context.assets.open("BDNequi.bd.sql").use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var sql: String? = null
                    reader.forEachLine {
                        sql = sql?.plus(it) ?: it
                        // Ejecutar cada línea de SQL
                        if (it.endsWith(";")) {
                            db.execSQL(sql)
                            sql = null
                        }
                    }
                    Log.d("DatabaseHelper", "SQL importado correctamente")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("DatabaseHelper", "Error al importar el SQL", e)
        }
    }



    /**
     * Consulta la base de datos para verificar la existencia de una cuenta por teléfono.
     * @param parametro El número de teléfono a buscar.
     * @return El número de coincidencias encontradas.
     */
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    fun ConsultaValidarExistenciaDelaCuentaPorTelefono(parametro: String): Int {
        // Abre la base de datos en modo solo lectura
        val db = this.readableDatabase

        // Consulta SQL para contar registros que cumplen con la condición
        val query = """
    SELECT COUNT(*)
    FROM cuenta
    INNER JOIN usuario ON cuenta.ID = usuario.Telefono
    WHERE cuenta.ID = ?    
    """.trimIndent()

        // Ejecuta la consulta y pasa los parámetros
        val cursor: Cursor = db.rawQuery(query, arrayOf(parametro))
        var count = 0

        // Procesa el cursor para obtener el conteo
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }

        // Cierra el cursor y la base de datos
        cursor.close()
        db.close()

        // Retorna el conteo obtenido
        return count
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    fun obtenerContrasenaPorID(parametro: String): String? {
        // Abre la base de datos en modo solo lectura
        val db = this.readableDatabase

        // Consulta SQL para obtener la contraseña de la cuenta basada en el ID
        val query = """
    SELECT cuenta.Contrasena
    FROM cuenta
    INNER JOIN usuario ON cuenta.ID = usuario.Telefono
    WHERE cuenta.ID = ?
    """.trimIndent()

        // Ejecuta la consulta y pasa el parámetro
        val cursor: Cursor = db.rawQuery(query, arrayOf(parametro))
        var contrasena: String? = null

        // Procesa el cursor para obtener la contraseña si existe
        if (cursor.moveToFirst()) {
            contrasena = cursor.getString(0)
        }

        // Cierra el cursor y la base de datos
        cursor.close()
        db.close()

        // Retorna la contraseña o null si no se encontró
        return contrasena
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    fun obtenerSaldoYNombrePorID(parametro: String): Map<String, String>? {
        // Abre la base de datos en modo solo lectura
        val db = this.readableDatabase

        // Consulta SQL para obtener el saldo y nombre del usuario basado en el ID de la cuenta
        val query = """
    SELECT cuenta.Saldo, usuario.Nombre
    FROM usuario
    INNER JOIN cuenta ON usuario.Telefono = cuenta.ID
    WHERE cuenta.ID = ?
    """.trimIndent()

        // Ejecuta la consulta y pasa el parámetro
        val cursor: Cursor = db.rawQuery(query, arrayOf(parametro))
        var resultado: Map<String, String>? = null

        // Procesa el cursor para obtener el saldo y nombre
        if (cursor.moveToFirst()) {
            val saldo = cursor.getString(0)
            val nombre = cursor.getString(1)

            // Crea un mapa con las llaves y valores obtenidos de la consulta
            resultado = mapOf(
                "Saldo" to saldo,
                "Nombre" to nombre
            )
        }

        // Cierra el cursor y la base de datos
        cursor.close()
        db.close()

        // Retorna el mapa con los resultados, o null si no se encontró
        return resultado
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    fun obtenerTotalPorID(idCuenta: String): Double? {
        // Abre la base de datos en modo solo lectura
        val db = this.readableDatabase

        // Consulta SQL para obtener el total basado en el ID de la cuenta
        val query = """
        SELECT 
            (cuenta.Saldo + IFNULL(SUM(bolsillo.Monto), 0) + IFNULL(SUM(colchon.Monto), 0) + IFNULL(SUM(meta.MontoAct), 0)) as total
        FROM usuario 
        INNER JOIN cuenta ON usuario.Telefono = cuenta.ID 
        LEFT JOIN bolsillo ON cuenta.ID = bolsillo.ID 
        LEFT JOIN colchon ON cuenta.ID = colchon.ID
        LEFT JOIN meta ON cuenta.ID = meta.CuentaID
        WHERE cuenta.ID = ?
        GROUP BY cuenta.ID;
    """.trimIndent()

        // Ejecuta la consulta y pasa el parámetro
        val cursor: Cursor = db.rawQuery(query, arrayOf(idCuenta))
        var total: Double? = null

        // Procesa el cursor para obtener el total
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }

        // Cierra el cursor y la base de datos
        cursor.close()
        db.close()

        return total
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    fun obtenerColchonPorID(id: String): Map<String, String>? {
        // Abre la base de datos en modo solo lectura
        val db = this.readableDatabase

        // Consulta SQL para obtener el registro de la tabla `colchon` basado en el ID
        val query = """
        SELECT ID, Monto FROM colchon
        WHERE ID = ?
    """.trimIndent()

        // Ejecuta la consulta y pasa el parámetro
        val cursor: Cursor = db.rawQuery(query, arrayOf(id))
        var resultado: Map<String, String>? = null

        // Procesa el cursor para obtener los datos
        if (cursor.moveToFirst()) {
            val colchonId = cursor.getString(cursor.getColumnIndex("ID"))
            val monto = cursor.getString(cursor.getColumnIndex("Monto"))

            // Crea un mapa con los resultados obtenidos
            resultado = mapOf(
                "ID" to colchonId,
                "Monto" to monto
            )
        }

        // Cierra el cursor y la base de datos
        cursor.close()
        db.close()

        // Retorna el mapa con el resultado, o null si no se encontró
        return resultado
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun obtenerSaldoDisponibleCuentaPorID(id: String): String? {
        // Abre la base de datos en modo solo lectura
        val db = this.readableDatabase

        // Consulta SQL para obtener el saldo basado en el ID de la cuenta
        val query = """
        SELECT Saldo
        FROM cuenta
        WHERE ID = ?
    """.trimIndent()

        // Ejecuta la consulta y pasa el parámetro
        val cursor: Cursor = db.rawQuery(query, arrayOf(id))
        var saldo: String? = null

        // Procesa el cursor para obtener el saldo
        if (cursor.moveToFirst()) {
            saldo = cursor.getString(cursor.getColumnIndex("Saldo"))
        }

        // Cierra el cursor y la base de datos
        cursor.close()
        db.close()

        // Retorna el saldo, o null si no se encontró
        return saldo
    }
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
fun actualizarMontoColchonSumar(id: Long, nuevoMonto: Long) {
    // Abre la base de datos en modo escritura
    val db = this.writableDatabase

    // Consulta SQL para actualizar el monto
    val query = """
        UPDATE colchon
        SET Monto = Monto + ?
        WHERE ID = ?
    """.trimIndent()

    // Ejecuta la consulta con los parámetros
    val args = arrayOf(nuevoMonto.toString(), id.toString())
    db.execSQL(query, args)

    // Cierra la base de datos
    db.close()
}
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
fun actualizarSaldoCuenta(idCuenta: Long, montoAdicional: Long) {
    // Abre la base de datos en modo escritura
    val db = this.writableDatabase

    // Consulta SQL para actualizar el saldo
    val query = """
        UPDATE cuenta
        SET Saldo = Saldo + ?
        WHERE ID = ?
    """.trimIndent()

    // Ejecuta la consulta con los parámetros
    val args = arrayOf(montoAdicional.toString(), idCuenta.toString())
    db.execSQL(query, args)

    // Cierra la base de datos
    db.close()
}

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun obtenerBolsillosConMontos(cuentaId: Long): List<Pair<String, String>> {
        val db = this.readableDatabase
        val listaBolsillos = mutableListOf<Pair<String, String>>()

        // Consulta SQL
        val query = """
        SELECT bolsillo.Nombre, bolsillo.Monto
        FROM bolsillo
        INNER JOIN cuenta ON bolsillo.CuentaID = cuenta.ID
        INNER JOIN usuario ON cuenta.ID = usuario.Telefono
        INNER JOIN estado ON bolsillo.Estado_idEstado = estado.idEstado
        WHERE cuenta.ID = ? AND estado.Estado = 'Activo'
    """.trimIndent()

        // Ejecutar consulta
        val cursor = db.rawQuery(query, arrayOf(cuentaId.toString()))

        // Procesar los resultados
        cursor.use {
            while (cursor.moveToNext()) {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))
                val monto = cursor.getDouble(cursor.getColumnIndexOrThrow("Monto"))

                // Convertir el monto a formato de dinero y agregarlo a la lista
                listaBolsillos.add(nombre to "$ ${String.format("%,.0f", monto)}")
            }
        }

        db.close()
        return listaBolsillos
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
fun actualizarBolsillo(nombreBolsillo: String, nuevoNombre: String?, nuevoMonto: String?) {
    val db = this.writableDatabase

    // Imprimir valores para depuración
    Log.d("DEBUG", "nombreBolsillo: $nombreBolsillo")
    Log.d("DEBUG", "nuevoNombre: $nuevoNombre")
    Log.d("DEBUG", "nuevoMonto: $nuevoMonto")
    var band = false

    // Comenzamos la transacción
    db.beginTransaction()
    try {
        // Actualizar nombre si es necesario
        if (!nuevoNombre.isNullOrEmpty()) {
            val updateNombreSql = """
                UPDATE bolsillo
                SET Nombre = ?
                WHERE Nombre = ?;
            """.trimIndent()

            Log.d("DEBUG", "Ejecutando consulta para actualizar nombre")
            db.execSQL(updateNombreSql, arrayOf(nuevoNombre, nombreBolsillo))
            band = true
        }

        // Actualizar monto si es necesario
        if (!nuevoMonto.isNullOrEmpty() && nuevoMonto != "0") {
            var nombreBolsillo2 = nombreBolsillo
            if(band == true){
                nombreBolsillo2 = nuevoNombre.toString()
            }
            val updateMontoSql = """
                UPDATE bolsillo
                SET Monto = Monto + CAST(? AS INTEGER)
                WHERE Nombre = ?;
            """.trimIndent()

            Log.d("DEBUG", "Ejecutando consulta para actualizar monto")
            db.execSQL(updateMontoSql, arrayOf(nuevoMonto, nombreBolsillo2))
        }

        // Confirmamos la transacción
        db.setTransactionSuccessful()
        Log.d("DEBUG", "Transacción exitosa")
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("DEBUG", "Error al actualizar el bolsillo: ${e.message}")
    } finally {
        db.endTransaction()
        Log.d("DEBUG", "Transacción finalizada")
    }

    db.close()
}

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
fun obtenerMetasEnProceso(telefonoUsuario: String): List<MetaItem> {
    val db = this.readableDatabase
    val listaMetas = mutableListOf<MetaItem>()

    // Consulta SQL
    val query = """
        SELECT meta.Nombre, meta.MontoAct, meta.MontoObj, meta.FechaCreacion
        FROM meta
        INNER JOIN cuenta ON meta.CuentaID = cuenta.ID
        INNER JOIN usuario ON cuenta.ID = usuario.Telefono
        INNER JOIN estado ON meta.Estado_idEstado = estado.idEstado
        WHERE cuenta.ID = ? AND estado.Estado = 'En proceso';
    """.trimIndent()

    // Ejecutar consulta
    val cursor = db.rawQuery(query, arrayOf(telefonoUsuario))

    try {
        if (cursor.moveToFirst()) {
            do {
                val nombreMeta = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))
                val montoAct = cursor.getInt(cursor.getColumnIndexOrThrow("MontoAct")).toString()
                val montoObj = cursor.getInt(cursor.getColumnIndexOrThrow("MontoObj")).toString()
                val fechaCreacion = cursor.getString(cursor.getColumnIndexOrThrow("FechaCreacion"))

                // Crear un objeto MetaItem y añadirlo a la lista
                val metaItem = MetaItem(nombreMeta, montoAct, montoObj, fechaCreacion)
                listaMetas.add(metaItem)
            } while (cursor.moveToNext())
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("DEBUG", "Error al obtener metas en proceso: ${e.message}")
    } finally {
        cursor.close()
        db.close()
    }

    return listaMetas
}

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
    fun actualizarMontoMeta(nombre: String, nuevoMonto: Long) {
        // Abre la base de datos en modo escritura
        val db = this.writableDatabase

        // Consulta SQL para actualizar el monto
        val query = """
        UPDATE meta
        SET MontoAct = MontoAct + ?
        WHERE Nombre = ?
    """.trimIndent()

        // Ejecuta la consulta con los parámetros
        val args = arrayOf(nuevoMonto.toString(), nombre)
        db.execSQL(query, args)

        // Cierra la base de datos
        db.close()
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun obtenerEnviosPorCuenta(cuentaId: String): List<Map<String, Any>> {
        val db = this.readableDatabase
        val resultList = mutableListOf<Map<String, Any>>()

        // Define the SQL query
        val query = """
        SELECT envio.ID, envio.Monto, envio.Mensaje, 
               CASE 
                   WHEN envio.CuentaEnviador = ? THEN 'Enviando'
                   ELSE 'Recibiendo'
               END AS Titulo
        FROM envio
        WHERE envio.CuentaRecibir = ? OR envio.CuentaEnviador = ?;
    """.trimIndent()

        // Execute the query with parameters
        val cursor: Cursor = db.rawQuery(query, arrayOf(cuentaId, cuentaId, cuentaId))

        // Process the cursor and populate the result list
        if (cursor.moveToFirst()) {
            do {
                val result = mapOf(
                    "ID" to cursor.getLong(cursor.getColumnIndex("ID")),
                    "Monto" to cursor.getString(cursor.getColumnIndex("Monto")),
                    "Mensaje" to cursor.getString(cursor.getColumnIndex("Mensaje")),
                    "Titulo" to cursor.getString(cursor.getColumnIndex("Titulo"))
                )
                resultList.add(result)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return resultList
    }
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun obtenerMovimientoPorId(envioId: String): Map<String, Any>? {
        val db = this.readableDatabase

        // Definir la consulta SQL
        val query = """
        SELECT envio.ID, envio.Mensaje, envio.Monto
        FROM envio
        WHERE envio.ID = ?;
    """.trimIndent()

        // Ejecutar la consulta con el parámetro
        val cursor: Cursor = db.rawQuery(query, arrayOf(envioId))

        // Verificar si hay un resultado
        var result: Map<String, Any>? = null
        if (cursor.moveToFirst()) {
            // Crear un mapa con los valores de la tupla
            result = mapOf(
                "ID" to cursor.getLong(cursor.getColumnIndex("ID")),
                "Mensaje" to cursor.getString(cursor.getColumnIndex("Mensaje")),
                "Monto" to cursor.getString(cursor.getColumnIndex("Monto"))
            )
        }

        cursor.close()
        db.close()

        return result
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun obtenerNombreUsuario_Telefono(cuentaId: String): Map<String, String>? {
        val db: SQLiteDatabase = this.readableDatabase
        var resultado: Map<String, String>? = null

        // Definir la consulta SQL
        val query = """
        SELECT usuario.Telefono, usuario.Nombre
        FROM cuenta INNER JOIN usuario
        ON cuenta.ID = usuario.Telefono
        WHERE cuenta.ID = ?;
    """.trimIndent()

        // Ejecutar la consulta con el parámetro
        val cursor: Cursor = db.rawQuery(query, arrayOf(cuentaId))

        // Procesar el cursor
        if (cursor.moveToFirst()) {
            resultado = mapOf(
                "Telefono" to cursor.getString(cursor.getColumnIndex("Telefono")),
                "Nombre" to cursor.getString(cursor.getColumnIndex("Nombre"))
            )
        }

        cursor.close()
        db.close()

        return resultado
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun obtenerNotificacionesPorCuenta(cuentaId: String , usuarioId: String): List<Map<String, String>> {
        val db: SQLiteDatabase = this.readableDatabase
        val resultList = mutableListOf<Map<String, String>>()

        // Definir la consulta SQL
        val query = """
        SELECT mensaje.Tipo, mensaje.Mensaje, usuario.Nombre, mensaje.UsuarioID_Externo , mensaje.Monto , mensaje.Estado , mensaje.ID 
        FROM mensaje
        INNER JOIN usuario ON mensaje.UsuarioID = usuario.ID
        INNER JOIN cuenta ON usuario.Telefono = cuenta.ID
        WHERE cuenta.ID = ? AND  mensaje.UsuarioID = ?;
    """.trimIndent()

        // Ejecutar la consulta con el parámetro
        val cursor: Cursor = db.rawQuery(query, arrayOf(cuentaId , usuarioId))

        // Procesar el cursor y llenar la lista
        if (cursor.moveToFirst()) {
            do {
                val result = mapOf(
                    "Tipo" to cursor.getString(cursor.getColumnIndex("Tipo")),
                    "Mensaje" to cursor.getString(cursor.getColumnIndex("Mensaje")),
                    "Nombre" to cursor.getString(cursor.getColumnIndex("Nombre")),
                    "CuentaEnviador" to cursor.getString(cursor.getColumnIndex("UsuarioID_Externo")),
                    "Monto" to cursor.getString(cursor.getColumnIndex("Monto")),
                    "Estado" to cursor.getString(cursor.getColumnIndex("Estado")),
                    "ID" to cursor.getString(cursor.getColumnIndex("ID"))

                )
                resultList.add(result)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return resultList
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun insertarMensaje(
        tipo: String,
        mensaje: String,
        fecha: String,
        usuarioId: String,
        usuarioIdExterno: String,
        monto: String,
        estado: String
    ): Long {
        val db: SQLiteDatabase = this.writableDatabase

        // Crear un ContentValues con los valores a insertar
        val values = ContentValues().apply {
            put("Tipo", tipo)
            put("Mensaje", mensaje)
            put("Fecha", fecha)
            put("UsuarioID", usuarioId)
            put("UsuarioID_Externo", usuarioIdExterno)
            put("Monto", monto)
            put("Estado", estado)
        }

        // Insertar el registro en la base de datos
        val newRowId: Long = try {
            db.insertOrThrow("mensaje", null, values)
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error al insertar mensaje", e)
            -1
        }

        // Cerrar la base de datos
        db.close()

        // Retornar el ID de la nueva fila insertada, o -1 si hubo un error
        return newRowId
    }


///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun obtenerUsuarioIdPorCuentaId(cuentaId: String): Int? {
        val db: SQLiteDatabase = this.readableDatabase
        var usuarioId: Int? = null

        // Definir la consulta SQL
        val query = """
        SELECT usuario.ID
        FROM usuario
        INNER JOIN cuenta ON usuario.Telefono = cuenta.ID
        WHERE cuenta.ID = ?;
    """.trimIndent()

        // Ejecutar la consulta con el parámetro
        val cursor: Cursor = db.rawQuery(query, arrayOf(cuentaId))

        // Procesar el cursor
        if (cursor.moveToFirst()) {
            // Obtener el ID del usuario
            usuarioId = cursor.getInt(cursor.getColumnIndex("ID"))
        }

        cursor.close()
        db.close()

        return usuarioId
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun actualizarEstadoDeUNMensaje(mensajeId: String, nuevoEstado: String): Int {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues().apply {
            put("Estado", nuevoEstado)
        }

        // Definir el criterio para la actualización (WHERE ID = ?)
        val selection = "ID = ?"
        val selectionArgs = arrayOf(mensajeId.toString())

        // Ejecutar la actualización
        val count = db.update(
            "mensaje", // Nombre de la tabla
            values,   // Los nuevos valores para actualizar
            selection, // La cláusula WHERE
            selectionArgs // Los argumentos de la cláusula WHERE
        )

        db.close()
        return count // Retorna el número de filas afectadas
    }


///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun obtenerMensajePorId(mensajeId: String): Map<String, Any?>? {
        val db: SQLiteDatabase = this.readableDatabase
        var resultado: Map<String, Any?>? = null

        // Definir la consulta SQL
        val query = """
        SELECT Mensaje, Monto, ID
        FROM mensaje
        WHERE ID = ?
    """.trimIndent()

        // Ejecutar la consulta con el parámetro
        val cursor: Cursor = db.rawQuery(query, arrayOf(mensajeId.toString()))

        // Procesar el cursor si hay resultados
        if (cursor.moveToFirst()) {
            val mensaje = cursor.getString(cursor.getColumnIndexOrThrow("Mensaje"))
            val monto = cursor.getDouble(cursor.getColumnIndexOrThrow("Monto"))
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))

            // Almacenar los resultados en un mapa
            resultado = mapOf(
                "Mensaje" to mensaje,
                "Monto" to monto,
                "ID" to id
            )
        }

        // Cerrar el cursor y la base de datos
        cursor.close()
        db.close()

        return resultado
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun actualizarSaldoPorTelefono(nuevoValor: String, usuarioID: String): Int {
        val db: SQLiteDatabase = this.writableDatabase

        // Primero, obtener el número de teléfono del usuario
        val telefonoQuery = """
        SELECT usuario.Telefono
        FROM usuario
        WHERE usuario.ID = ?
    """.trimIndent()

        // Preparar la consulta para obtener el teléfono
        val telefonoStatement = db.compileStatement(telefonoQuery)
        telefonoStatement.bindString(1, usuarioID)  // Asignar el ID del usuario

        // Ejecutar la consulta para obtener el teléfono
        val telefonoUsuario = telefonoStatement.simpleQueryForString()

        // Si no se encuentra un teléfono, retornar sin hacer la actualización
        if (telefonoUsuario.isNullOrEmpty()) {
            db.close()
            return -1  // O cualquier otro valor para indicar que no se encontró el usuario
        }

        // Luego, actualizar el saldo en la tabla cuenta
        val updateQuery = """
        UPDATE cuenta
        SET Saldo = Saldo + ?
        WHERE cuenta.ID = ?
    """.trimIndent()

        // Preparar la consulta para actualizar el saldo
        val updateStatement = db.compileStatement(updateQuery)
        updateStatement.bindString(1, nuevoValor)   // Asignar el nuevo valor de saldo
        updateStatement.bindString(2, telefonoUsuario)  // Asignar el número de teléfono como ID de cuenta

        // Ejecutar el update y obtener el número de filas afectadas
        val rowsAffected = updateStatement.executeUpdateDelete()

        // Cerrar la base de datos
        db.close()

        return rowsAffected
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun obtenerNombreUsuarioPorId(usuarioId: String): String? {
        val db = this.readableDatabase
        var nombreUsuario: String? = null

        // Define la consulta SQL
        val query = """
        SELECT usuario.Nombre
        FROM usuario
        WHERE usuario.ID = ?
    """.trimIndent()

        // Ejecutar la consulta con el parámetro
        val cursor: Cursor? = try {
            db.rawQuery(query, arrayOf(usuarioId))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        // Procesar el cursor
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    nombreUsuario = cursor.getString(cursor.getColumnIndex("Nombre"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor.close()
            }
        }

        db.close()

        return nombreUsuario
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


    fun obtenerNombreUsuarioPorTelefono(usuarioId: String): String? {
        val db = this.readableDatabase
        var nombreUsuario: String? = null

        // Define la consulta SQL
        val query = """
        SELECT usuario.Nombre
        FROM usuario
        WHERE usuario.Telefono = ?
    """.trimIndent()

        // Ejecutar la consulta con el parámetro
        val cursor: Cursor? = try {
            db.rawQuery(query, arrayOf(usuarioId))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        // Procesar el cursor
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    nombreUsuario = cursor.getString(cursor.getColumnIndex("Nombre"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor.close()
            }
        }

        db.close()

        return nombreUsuario
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


    fun insertarBolsillo(
        nombre: String,
        monto: String,
        fechaCreacion: String, // Usa el formato adecuado para tu base de datos (por ejemplo, YYYY-MM-DD)
        cuentaId: String,
        estadoId: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("Nombre", nombre)
            put("Monto", monto)
            put("FechaCreacion", fechaCreacion)
            put("CuentaID", cuentaId)
            put("Estado_idEstado", estadoId)
        }

        // Insertar el registro en la tabla 'bolsillo'
        val newRowId = db.insert("bolsillo", null, values)

        db.close()
        return newRowId
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun insertarMeta(
        nombre: String,
        montoAct: Double,
        montoObj: Double,
        fechaCreacion: String, // Formato: YYYY-MM-DD
        fechaObjetivo: String, // Formato: YYYY-MM-DD
        cuentaId: String,
        estadoId: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("Nombre", nombre)
            put("MontoAct", montoAct)
            put("MontoObj", montoObj)
            put("FechaCreacion", fechaCreacion)
            put("FechaObjetivo", fechaObjetivo)
            put("CuentaID", cuentaId)
            put("Estado_idEstado", estadoId)
        }

        var newRowId: Long = -1
        try {
            newRowId = db.insert("meta", null, values)
            if (newRowId == -1L) {
                Log.e("DatabaseInsert", "Error: La inserción falló.")
            } else {
                Log.d("DatabaseInsert", "Inserción exitosa con ID: $newRowId")
            }
        } catch (e: Exception) {
            Log.e("DatabaseInsert", "Excepción durante la inserción: ${e.message}")
        } finally {
            db.close()
        }

        return newRowId
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun insertarEnvio(
        cuentaRecibir: String,
        monto: String,
        mensaje: String,
        fecha: String,
        cuentaEnviador: String
    ): Boolean {
        // Obtiene una instancia de escritura en la base de datos
        val db = this.writableDatabase

        // Prepara el contenido para la inserción
        val contentValues = ContentValues().apply {
            put("CuentaRecibir", cuentaRecibir)
            put("Monto", monto)
            put("Mensaje", mensaje)
            put("Fecha", fecha)
            put("CuentaEnviador", cuentaEnviador)
        }

        // Intenta insertar los datos en la tabla
        return try {
            val result = db.insert("envio", null, contentValues)
            result != -1L  // Si el resultado es diferente de -1, significa que la inserción fue exitosa
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        } finally {
            db.close()  // Cierra la base de datos después de la operación
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun obtenerRegaloPorCodigo(codigoRegalo: String): Pair<String?, String?>? {
        val db = this.readableDatabase

        val query = "SELECT id_regalo, monto_regalo FROM regalo WHERE codigo_regalo = ? AND estado_regalo = 1"
        val cursor: Cursor = db.rawQuery(query, arrayOf(codigoRegalo))

        var result: Pair<String?, String?>? = null
        if (cursor.moveToFirst()) {
            val idRegalo = cursor.getString(cursor.getColumnIndexOrThrow("id_regalo"))
            val monto = cursor.getString(cursor.getColumnIndexOrThrow("monto_regalo"))
            result = Pair(idRegalo, monto)
        }

        cursor.close()
        db.close()  // Cierra la base de datos después de la operación
        return result
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun actualizarEstadoRegaloPorId(idRegalo: String, nuevoEstado: String): Boolean {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put("estado_regalo", nuevoEstado)
        }

        return try {
            // Ejecuta la consulta de actualización
            val rowsAffected = db.update(
                "regalo",
                values,
                "id_regalo = ?",
                arrayOf(idRegalo.toString())
            )
            rowsAffected > 0 // Retorna true si se actualizó al menos una fila
        } catch (e: SQLException) {
            e.printStackTrace()
            false // Si ocurre una excepción, la actualización falló
        } finally {
            db.close() // Cierra la base de datos después de la operación
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun actualizarContrasena(idCuenta: String, nuevaContrasena: String): Boolean {
        // Obtiene una instancia de escritura en la base de datos
        val db = this.writableDatabase

        // Prepara el contenido para la actualización
        val contentValues = ContentValues().apply {
            put("Contrasena", nuevaContrasena)  // Actualizamos el campo de la contraseña
        }

        // Define la condición de actualización
        val whereClause = "ID = ?"
        val whereArgs = arrayOf(idCuenta)

        // Intenta actualizar los datos en la tabla
        return try {
            val result = db.update("cuenta", contentValues, whereClause, whereArgs)
            result > 0  // Si el resultado es mayor a 0, significa que la actualización fue exitosa
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        } finally {
            db.close()  // Cierra la base de datos después de la operación
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun actualizarEstadoMeta(nombreMeta: String, nuevoEstado: String): Boolean {
        // Obtiene una instancia de escritura en la base de datos
        val db = this.writableDatabase

        // Prepara el contenido para la actualización
        val contentValues = ContentValues().apply {
            put("Estado_idEstado", nuevoEstado)  // Actualizamos el estado
        }

        // Define la condición de actualización
        val whereClause = "Nombre = ?"
        val whereArgs = arrayOf(nombreMeta)

        // Intenta actualizar los datos en la tabla
        return try {
            val result = db.update("meta", contentValues, whereClause, whereArgs)
            result > 0  // Si el resultado es mayor a 0, significa que la actualización fue exitosa
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        } finally {
            db.close()  // Cierra la base de datos después de la operación
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun insertarUsuario(
        nombre: String,
        fechaReg: String,
        ocupacion: String,
        ciudadID: String,
        telefono: String
    ): Boolean {
        // Obtiene una instancia de escritura en la base de datos
        val db = this.writableDatabase

        // Prepara el contenido para la inserción
        val contentValues = ContentValues().apply {
            put("Nombre", nombre)
            put("FechaReg", fechaReg)
            put("Ocupacion", ocupacion)
            put("CiudadID", ciudadID)
            put("Telefono", telefono)
        }

        // Intenta insertar los datos en la tabla
        return try {
            val result = db.insert("usuario", null, contentValues)
            result != -1L  // Si el resultado es diferente de -1, significa que la inserción fue exitosa
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun insertarCuenta(
        ID: String,
        Saldo: String,
        Contrasena: String
    ): Boolean {
        // Obtiene una instancia de escritura en la base de datos
        val db = this.writableDatabase

        // Prepara el contenido para la inserción
        val contentValues = ContentValues().apply {
            put("ID", ID)
            put("Saldo", Saldo)
            put("Contrasena", Contrasena)
        }

        // Intenta insertar los datos en la tabla
        return try {
            val result = db.insert("cuenta", null, contentValues)
            result != -1L  // Si el resultado es diferente de -1, significa que la inserción fue exitosa
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        } finally {
            db.close()  // Cierra la base de datos después de la operación
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    fun insertarColchon(
        id: String,
        monto: String
    ): Boolean {
        // Obtiene una instancia de escritura en la base de datos
        val db = this.writableDatabase

        // Prepara el contenido para la inserción
        val contentValues = ContentValues().apply {
            put("ID", id)
            put("Monto", monto)
        }

        // Intenta insertar los datos en la tabla
        return try {
            val result = db.insert("colchon", null, contentValues)
            result != -1L  // Si el resultado es diferente de -1, significa que la inserción fue exitosa
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        } finally {
            db.close()  // Cierra la base de datos después de la operación
        }
    }














    companion object {
        // Nombre del archivo de la base de datos
        private const val DATABASE_NAME = "my_database.db"
        // Versión de la base de datos
        private const val DATABASE_VERSION = 1
    }
}
