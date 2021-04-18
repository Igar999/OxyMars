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
$contra = $_POST["contra"];
$foto = $_POST["foto"];

# Ejecutar la sentencia SQL
if ($usuario === null){
	echo 'Error: usuario vacio';
	exit;
}
$resultado = mysqli_query($con, "INSERT INTO Datos(Usuario, Contra, Foto, Oxigeno, OxiToque, OxiSegundo, DesbloqueadoToque, DesbloqueadoSegundo) VALUES ('$usuario','$contra','$foto',0,1,0,0,0)");

# Comprobar si se ha ejecutado correctamente
if (!$resultado) {
	echo 'Ha ocurrido algún error: ' . mysqli_error($con);
}
?>