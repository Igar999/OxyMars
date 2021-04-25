<?php
$DB_SERVER="localhost"; #la dirección del servidor
$DB_USER="Xigarcia353"; #el usuario para esa base de datos
$DB_PASS="94Kq8Btb"; #la clave para ese usuario
$DB_DATABASE="Xigarcia353_OxyMars"; #la base de datos a la que hay que conectarse
# Se establece la conexión:
$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);

#Comprobamos conexión
if (mysqli_connect_errno($con)) {
	echo 'Error de conexion: ' . mysqli_connect_error();
	exit();
}
$usuario = $_POST["usuario"];

# Ejecutar la sentencia SQL
$resultado = mysqli_query($con, "SELECT Usuario FROM Datos WHERE Usuario = '$usuario'");

# Comprobar si se ha ejecutado correctamente
if (!$resultado) {
	echo 'Ha ocurrido algún error: ' . mysqli_error($con);
}

$fila = mysqli_fetch_row($resultado);

if ($fila[0] == ""){
	echo 'no';
	exit;	
} else {
	echo 'si';
	exit;
}
?>