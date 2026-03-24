# Diagrama Classes de Domínio
---
![](https://www.plantuml.com/plantuml/png/PP7DIWCn4CVlUOhGez1IhqMaeWSB7iIF1vYcqpgucocJ2OZuGZw7B-OGT-YkFIsP_FB_s5aN1T7QTw73lKRbA7HuqxWZmQJ4EPHtoaSGw6Fge9GRLQ5jLTmapi_qYN4PGhk9hwM240-dHy4z2kP4CBruOaKIFzrXKSeSO5jK86dy6p_K0yY0tDk3pXyR3bKWAxeFP-MNAcGwoLKtYltKvvOpVW27WBdsbg7348PSSNOvY_Dv-vodZgkumprbFCENYyN5pEiObeJ08GEkculDZrnAfH2SFDbvUuCiy3dG34PTZFlvZebxarGsON6_wHlNNSN3K2O4jvgcR__vbB4TfuZ_kLEFKQ1n5vUYd3h31gshBxjk6nmDnciSZ__jServL_lk5m00)
---
```plantuml
@startuml
left to right direction
skinparam classAttributeIconSize 0

class Usuario
class PreferenciaUsuario
class Roteiro
class Destino
abstract class Lugar
class Hotel
class Restaurante
class PontoTuristico
class ItemRoteiro
class Conexao

enum Role

Usuario "1" -- "1" PreferenciaUsuario : define
Usuario "1" -- "0..*" Roteiro : cria
Usuario --> Role : possui

Roteiro "1" -- "1" Destino : tem
Roteiro "1" -- "1..*" ItemRoteiro : é composto por
Usuario "1" -- "1..*" Destino: gerencia

Destino "1" -- "0..*" Lugar : possui
Lugar "1" -- "0..*" Conexao: possui

ItemRoteiro "1" -- "1" Lugar : inclui

Lugar <|-- Hotel
Lugar <|-- Restaurante
Lugar <|-- PontoTuristico

@enduml
```
