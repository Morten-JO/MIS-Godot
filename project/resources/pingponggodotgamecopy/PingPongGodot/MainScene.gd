extends Node2D

var udp_connection = PacketPeerUDP.new()
var tcp_connection = StreamPeerTCP.new()
var wrapped_tcp = null
var timeCounter = 0

const TIMEOUT_DURATION_CONNECT = 10

func _ready():
	tcp_connection.connect("localhost", 1234)
	
	#udp_connection.set_send_address("localhost", 1234)
	set_process(true)

func _process(delta):
	if tcp_connection.is_connected() && tcp_connection.get_available_bytes() > 0:
		print("len of received package: "+str(tcp_connection.get_string(tcp_connection.get_available_bytes()).length()))
		print("Received package with message: "+str(tcp_connection.get_string(tcp_connection.get_available_bytes()))+" \n")
	if tcp_connection.get_status() == 1:
		
		timeCounter = timeCounter + delta
		print("Error connecting")
	else:
		tcp_connection.put_var("garagamel!")
	if timeCounter > TIMEOUT_DURATION_CONNECT:
		print("Error connecting after 10 secs, closing")
		tcp_connection.disconnect()
		set_process(false)
	
	
	#udp_connection.put_var("Gargamel")
	
func onReceiveQueueStart(data):
	pass

func onReceiveQueueEnd(data):
	pass

func onReceiveRoomfound(data):
	pass

func onReceivedBroadcastMessageHello():
	pass
	