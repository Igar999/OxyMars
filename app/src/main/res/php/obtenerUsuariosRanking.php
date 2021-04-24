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
$resultado = mysqli_query($con, "SELECT Usuario, Oxigeno FROM Datos ORDER BY Oxigeno DESC");

# Comprobar si se ha ejecutado correctamente
if (!$resultado) {
	echo 'Ha ocurrido algún error: ' . mysqli_error($con);
	exit();
}

$res = array();
$num_filas = $resultado->num_rows;
while ($num_filas > 0){
	$fila = mysqli_fetch_row($resultado);
	$persona = array(
		'usuario' => $fila[0],
		'oxigeno' => $fila[1],
	);
    array_push($res, $persona);
	$num_filas = $num_filas -1;
};
echo json_encode($res);
?>
