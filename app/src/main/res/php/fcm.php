<?php
$token = $_POST['token'];

$cabecera= array(
'Authorization: key=AAAAVCW9Gms:APA91bG46Bu3DuGummaz9N0j7hvzZsJVPUO8-FYK7INGRzlxXbqRlS6jA-gXffW0afTrai0nByXgXYYluEDnNm5Nid6qsTRzVswEt78s3ImVFnup-LK5gmWT0Lj-lv68HsGw9npD_4cI',
'Content-Type: application/json'
);

$msg= array(
'to'=> $token
);

$msgJSON= json_encode ($msg);

$ch = curl_init(); #inicializar el handler de curl
#indicar el destino de la petición, el servicio FCM de google
curl_setopt( $ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
#indicar que la conexión es de tipo POST
curl_setopt( $ch, CURLOPT_POST, true );
#agregar las cabeceras
curl_setopt( $ch, CURLOPT_HTTPHEADER, $cabecera);
#Indicar que se desea recibir la respuesta a la conexión en forma de string
curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
#agregar los datos de la petición en formato JSON
curl_setopt( $ch, CURLOPT_POSTFIELDS, $msgJSON );
#ejecutar la llamada
$resultado= curl_exec( $ch );
#cerrar el handler de curl
curl_close( $ch );

if (curl_errno($ch)) {
	print curl_error($ch);
}
echo $resultado;

?>