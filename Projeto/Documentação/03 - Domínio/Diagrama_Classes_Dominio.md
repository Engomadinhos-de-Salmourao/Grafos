@startuml
top to bottom direction
skinparam classAttributeIconSize 0

class Usuario
class PreferenciaUsuario
class Roteiro
class Destino
interface Lugar
class Hotel
class Restaurante
class PontoTuristico
class ItemRoteiro

enum Role

Usuario "1" -- "1" PreferenciaUsuario : possui
Usuario "1" -- "0..*" Roteiro : possui
Usuario --> Role : possui

Roteiro "1" -- "1" Destino : pertence a
Roteiro "1" -- "1..*" ItemRoteiro : é composto por

Destino "1" -- "0..*" Lugar : possui

ItemRoteiro "1" -- "1" Lugar : referencia