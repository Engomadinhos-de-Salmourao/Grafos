@startuml
top to bottom direction
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

enum Role

Usuario "1" -- "1" PreferenciaUsuario : define
Usuario "1" -- "0..*" Roteiro : cria
Usuario --> Role : possui

Roteiro "1" -- "1" Destino : é planejado para
Roteiro "1" -- "1..*" ItemRoteiro : é composto por

Destino "1" -- "0..*" Lugar : possui

ItemRoteiro "1" -- "1" Lugar : inclui

Lugar <|-- Hotel
Lugar <|-- Restaurante
Lugar <|-- PontoTuristico

@enduml
